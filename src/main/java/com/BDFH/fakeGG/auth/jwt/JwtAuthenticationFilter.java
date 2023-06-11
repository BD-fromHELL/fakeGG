package com.BDFH.fakeGG.auth.jwt;

import com.BDFH.fakeGG.exception.TokenExpiredException;
import com.BDFH.fakeGG.exception.TokenInvalidException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;

/**
 * GenericFilter vs OncePerRequestFilter
 * 스프링부트는 bean 등록된 것 중 filter가 있다면 자동으로 등록하고 실행시켜줌
 * 따라서 securityConfig에서 filter를 add해주면, 총 두 번 등록되기 떄문에 중복 실행되는 경우가 발생할 수 있음
 * 따라서 딱 한번 실행되도록 보장해주는 OncePerRequestFilter를 사용해야함
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;


    /**
     * jwt토큰의 유효성을 검사하는 필터
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String AccessToken = tokenProvider.getAccessToken(request);
        if (AccessToken != null) {
            // 유효성 검사, error가 발생하지 않는다면 토큰이 유효하다는 뜻이다
            tokenProvider.validateToken(AccessToken);
            // 토큰이 유효하다면 토큰을 사용하여 authentication을 생성

            System.out.println("유저가 인증되었습니다");
            // token으로 authentication을 생성하고 contextHolder에 담는다
            Authentication authentication = tokenProvider.createAuthentication(AccessToken);
            // SecurityContextHolder에 tokenProvider에서 만든 authentication를 인증 객체로 설정함
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}




        // 다른 방법 : 토큰이 만료되었다면 새로운 토큰을 생성하여 authentication을 생성
//        String RefreshToken = getRefreshToken(request);
//        else if (AccessToken != null && TokenValidation != null) {
//            // refresh토큰을 확인하여 토큰을 재발급
//            refreshTokenProvider.validateRefreshToken(RefreshToken);
//
//            // refreshToken 유효성 검사를 통과했다면, 기존 token의 email로 새로운 토큰 생성
//            // 만료된 토큰은 토큰 유효성 검사에서 email을 return받기 때문에 TokenValidation을 사용하면 된다
//            AccessToken = tokenProvider.createToken(TokenValidation);
//
//            // 재발급된 accessToken을 response헤더에 Authorization필드로 넣어줌
//            response.addHeader("Authorization","Bearer " + AccessToken);
//
//            // token으로 authentication을 생성하고 contextHolder에 담는다
//            Authentication authentication = tokenProvider.createAuthentication(AccessToken);
//            // SecurityContextHolder에 tokenProvider에서 만든 authentication를 인증 객체로 설정함
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//    /**
//     *   request의 헤더에서 RefreshToken을 가져오는 메서드
//     */
//    private String getRefreshToken(HttpServletRequest request) {
//        // HttpRequest에서는 헤더의 "ReAuthorization"에 Refresh토큰을 담아서 보냄
//        String headerAuth = request.getHeader("ReAuthorization");
//        // hasText로 headerAuth가 null인지 아닌지 확인 + headerAuth가 "Bearer"로 시작하는지 확인
//        // 위의 조건을 만족한다면, substring을 사용하여 bearer이후의 문자열을 가져옴. 이것이 Refresh 토큰임
//        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
//            return headerAuth.substring(7, headerAuth.length());
//        }
//        return null;
//    }
