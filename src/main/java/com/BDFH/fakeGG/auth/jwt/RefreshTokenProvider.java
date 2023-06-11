package com.BDFH.fakeGG.auth.jwt;

import com.BDFH.fakeGG.auth.entity.RefreshToken;
import com.BDFH.fakeGG.auth.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;


@PropertySource("classpath:application-security.properties")
@Service
public class RefreshTokenProvider {
    private final RefreshTokenRepository refreshTokenRepository;
    private String secretKey; // 인코딩 해야하므로 final 쓰면 안됨
    private final String issuer;
    private long expirationTime;

    public RefreshTokenProvider(@Value("${jwt.secretKey}") String secretKey,
                         @Value("${jwt.expirationTime}") long expirationTime,
                         @Value("${jwt.issuer}") String issuer,
                                RefreshTokenRepository refreshTokenRepository) {
        this.secretKey = secretKey;
        this.expirationTime = expirationTime;
        this.issuer = issuer;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // secretkey를 base64로 인코딩
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * 새로운 refresh 토큰 발급
     * : 전달받은 email로 accessToken보다 만료기간이 2주인 refresh토큰을 생성
     *   이때 Refresh토큰은 db에 저장한다
     */
    @Transactional
    public String createRefreshToken(String email) {
        Date now = new Date();
        Date expTime = new Date(now.getTime() + expirationTime * 24 * 14);
        // 새로운 토큰 생성
        String newRefreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expTime)
                .setIssuer(issuer)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        // db에 저장된 refresh token을 새로운 refresh Token으로 업데이트
        Optional<RefreshToken> origin = refreshTokenRepository.findByMemberEmail(email);
        if (origin.isPresent()) {
            RefreshToken originToken = origin.get();
            originToken.update(newRefreshToken);
            refreshTokenRepository.save(originToken);
        } else {
            RefreshToken newToken = new RefreshToken(email, newRefreshToken);
            refreshTokenRepository.save(newToken);
        }

        // 토큰을 반환
        return newRefreshToken;
    }

    /**
     * 유효성 검사
     * : 전달받은 refreshToken의 유효성을 검증
     *   아니라면 error를 발생시켜 재로그인을 시킴
     */
    @Transactional
    public String validateRefreshToken(HttpServletRequest request){
        String refreshToken = getRefreshToken(request);
        // 1. 유효성 검사를 실시
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(refreshToken);

        // 만료 기간이 지났다면, 재로그인 시킴
        } catch (ExpiredJwtException expiredJwtException) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 기간이 만료되었습니다. 다시 로그인 하세요");
        // 토큰이 유효하지 않다면, 재로그인 시킴
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다. 다시 로그인 하세요");
        }

        // 2. db에 저장된 토큰과 일치하는지 확인 -> 일치하지 않는다면 error 발생
        RefreshToken currentToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("로그인에 실패하였습니다. 다시 로그인 하세요"));

        // 3. 모든 유효성 검사를 통과했다면 email을 리턴
        return currentToken.getMemberEmail();
    }


    /**
     *   request의 헤더에서 RefreshToken을 가져오는 메서드
     */
    public String getRefreshToken(HttpServletRequest request) {
        // HttpRequest에서는 헤더의 "ReAuthorization"에 Refresh토큰을 담아서 보냄
        String headerAuth = request.getHeader("ReAuthorization");
        // hasText로 headerAuth가 null인지 아닌지 확인 + headerAuth가 "Bearer"로 시작하는지 확인
        // 위의 조건을 만족한다면, substring을 사용하여 bearer이후의 문자열을 가져옴. 이것이 Refresh 토큰임
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }


}
