package com.BDFH.fakeGG.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    /**
     *   request의 헤더에서 AccessToken을 가져오는 메서드
     */
    private String getToken(HttpServletRequest request) {
        // HttpRequest에서는 헤더의 "Authorization"에 토큰을 담아서 보냄
        String headerAuth = request.getHeader("Authorization");
        // hasText로 headerAuth가 null인지 아닌지 확인 + headerAuth가 "Bearer"로 시작하는지 확인
        // 위의 조건을 만족한다면, substring을 사용하여 bearer이후의 문자열을 가져옴. 이것이 토큰임
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

    /**
     *   request의 헤더에서 RefreshToken을 가져오는 메서드
     */
    private String getRefreshToken(HttpServletRequest request) {
        // HttpRequest에서는 헤더의 "ReAuthorization"에 Refresh토큰을 담아서 보냄
        String headerAuth = request.getHeader("ReAuthorization");
        // hasText로 headerAuth가 null인지 아닌지 확인 + headerAuth가 "Bearer"로 시작하는지 확인
        // 위의 조건을 만족한다면, substring을 사용하여 bearer이후의 문자열을 가져옴. 이것이 Refresh 토큰임
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }



    /**
     * jwt토큰의 유효성을 검사하는 필터
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String AccessToken = getToken(request);
        String RefreshToken = getRefreshToken(request);
        // 유효성 검사
        String TokenValidation = tokenProvider.validateToken(AccessToken);
        // 토큰이 유효하다면 토큰을 사용하여 authentication을 생성
        if (AccessToken != null && TokenValidation == "valid") {
            System.out.println("유저가 인증되었습니다");
            // token으로 authentication을 생성하고 contextHolder에 담는다
            Authentication authentication = tokenProvider.createAuthentication(AccessToken);
            // SecurityContextHolder에 tokenProvider에서 만든 authentication를 인증 객체로 설정함
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 토큰이 만료되었다면 새로운 토큰을 생성하여 authentication을 생성
        else if (AccessToken != null && TokenValidation != null) {
            // refresh토큰을 확인하여 토큰을 재발급
            refreshTokenProvider.validateRefreshToken(RefreshToken);

            // refreshToken 유효성 검사를 통과했다면, 기존 token의 email로 새로운 토큰 생성
            // 만료된 토큰은 토큰 유효성 검사에서 email을 return받기 때문에 TokenValidation을 사용하면 된다
            AccessToken = tokenProvider.createToken(TokenValidation);

            // 재발급된 accessToken을 Authorization에 다시 넣어줌
            response.addHeader("Authorization","Bearer " + AccessToken);

            // token으로 authentication을 생성하고 contextHolder에 담는다
            Authentication authentication = tokenProvider.createAuthentication(AccessToken);
            // SecurityContextHolder에 tokenProvider에서 만든 authentication를 인증 객체로 설정함
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
