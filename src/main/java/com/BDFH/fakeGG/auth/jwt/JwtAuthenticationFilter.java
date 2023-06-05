package com.BDFH.fakeGG.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    /**
     *   request의 헤더에서 token을 가져오는 메서드
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
     * jwt토큰의 유효성을 검사하는 필터
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        // token이 존재하고, 유효성 검사를 통과한다면 -> authentication을 생성하고 contextHolder에 담는다
        if (token != null && tokenProvider.validateToken(token)) {
            Authentication authentication = tokenProvider.createAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);


    }
}
