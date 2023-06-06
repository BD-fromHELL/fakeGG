package com.BDFH.fakeGG.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;


@PropertySource("classpath:application-security.properties")
@Service
public class RefreshTokenProvider {
    private String secretKey;
    private long expirationTime;
    private final String issuer;

    public RefreshTokenProvider(@Value("${jwt.secretKey}") String secretKey,
                         @Value("${jwt.expirationTime}") long expirationTime,
                         @Value("${jwt.issuer}") String issuer) {
        this.secretKey = secretKey;
        this.expirationTime = expirationTime;
        this.issuer = issuer;
    }

    // secretkey를 base64로 인코딩
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * 새로운 토큰 발급
     * : 전달받은 email로 accessToken보다 만료기간이 2주인 refresh토큰을 생성
     *   이때 Refresh토큰은 db에 저장한다
     */
    public String createRefreshToken(String email) {
        Date now = new Date();
        Date expTime = new Date(now.getTime() + expirationTime * 24 * 14);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expTime)
                .setIssuer(issuer)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    /**
     * 유효성 검사 & access토큰 반환
     * : 전달받은 refreshToken의 유효성을 검증하여 유효하다면 access토큰을 반환
     *   아니라면 error를 발생시켜 재로그인을 시킴
     */
    public void validateRefreshToken(String refreshToken){
        // 유효성 검사에 실패했다면 예외 발생
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(refreshToken);
        } catch (Exception e) {
            throw new IllegalAccessError("로그인에 실패하였습니다. 다시 로그인 하세요.");
        }

        // 만료 기간이 지났다면, 재로그인 시킴
        Claims claims = getClaims(refreshToken);
        Date expTime = claims.getExpiration();

        if (expTime.before(new Date())) {
            throw new IllegalAccessError("로그인 기간이 만료되었습니다. 다시 로그인 하세요");
        }
    }


    /**
     * 사용자 정보 추출
     */
    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                // 전달받은 토큰이 유효한지, 서명이 올바른지 검사함
                .parseClaimsJws(token)
                .getBody();
    }


}
