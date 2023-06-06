package com.BDFH.fakeGG.auth.jwt;

import com.BDFH.fakeGG.auth.MemberDetails;
import com.BDFH.fakeGG.entity.Member;
import com.BDFH.fakeGG.exception.NotExistMemberException;
import com.BDFH.fakeGG.repository.MemberRepository;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@PropertySource("classpath:application-security.properties")
@Service
public class TokenProvider {
    private String secretKey;
    private final long expirationTime;
    private final String issuer;
    private final MemberRepository memberRepository;
    private final RefreshTokenProvider refreshTokenProvider;


    public TokenProvider(@Value("${jwt.secretKey}") String secretKey,
                         @Value("${jwt.expirationTime}") long expirationTime,
                         @Value("${jwt.issuer}") String issuer,
                         MemberRepository memberRepository,
                         RefreshTokenProvider refreshTokenProvider) {

        this.secretKey = secretKey;
        this.expirationTime = 1000*60;
        this.issuer = issuer;
        this.memberRepository = memberRepository;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    // secretkey를 base64로 인코딩
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    /**
     * memberEmail을 전달받아 access토큰을 생성
     * 토큰에 들어갈 정보 : email, 현재날짜, 만료날짜
     */
    public String createToken(String email){
        Date now = new Date();
        Date expTime = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expTime)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }


    /**
     * jwt 토큰 유효성 검사
     * 클라이언트에서 전달받은 jwt토큰의 유효성을 검증하여,
     * 유효하다면 true, 아니라면 false를 리턴해줌
     */
    public String validateToken(String token){
        // 유효성 검사
        try {
            Jwts.parserBuilder()
                    // token을 복호화할 비밀키 설정
                    .setSigningKey(secretKey)
                    .build()
                    // 비밀키를 사용하여 복호화 시행. 여기서 만료 여부까지 확인할 수 있다
                    .parseClaimsJws(token);
            return "valid";
        // 토큰이 만료된 경우라면 email을 return
        } catch (ExpiredJwtException expiredException) {
            Claims claims = expiredException.getClaims();
            String email = claims.getSubject();
            System.out.println("email = " + email);
            return email;
        } catch (JwtException e) {
            return null;
        }
    }

    public boolean validateExpTime(Claims claims, String refreshToken) {
        Date expTime = claims.getExpiration();
        // 만료 기간이 지났다면 refresh 토큰을 검사하여 토큰 재발급
        if (expTime.before(new Date())) {
            return false;
        }
        return true;
    }

    /**
     * 토큰 정보 추출 : 전달받은 토큰에 들어있는 claims의 정보를 가져옴
     */
    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                // 전달받은 토큰이 유효한지, 서명이 올바른지 검사함
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 멤버 email 추출 : 전달받은 토큰으로 멤버의 email을 찾는다
     */
    public String getMemberEmail(String token){
        Claims claims = getClaims(token);
        return claims.get("email", String.class);
    }

    /**
     * 인증된 멤버 객체 생성 : 전달받은 토큰으로 Authentication 객체를 생성함
     */
    public Authentication createAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority(("ROLE")));

        // email, authorities로 UserDetails 객체 생성
//        UserDetails userDetails = new User(claims.getSubject(), "", authorities);

        // email에 해당하는 member를 찾은 후, 그 member로 MemberDeatils를 생성
        String email = claims.getSubject();
        Member member = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new NotExistMemberException("회원이 존재하지 않습니다"));
        MemberDetails memberDetails = new MemberDetails(member);

        // Authentication 생성 : Rest API서버에서는, id와 pw대신에 token과 memberDetails로 authentication을 생성함
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, token, authorities);

        return authentication;
    }


}
