package com.BDFH.fakeGG.auth.jwt;

import com.BDFH.fakeGG.exception.NotSignedUpMemberException;
import com.BDFH.fakeGG.exception.TokenExpiredException;
import com.BDFH.fakeGG.exception.TokenInvalidException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    /**
     * Filter단에서 발생하는 exception을 처리하는 필터
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException e) {
            logger.error("액세스 토큰이 만료되었습니다");
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, 401, "4000");
        } catch (TokenInvalidException e) {
            logger.error("액세스 토큰이 유효하지 않습니다");
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, 401, "4001");
        } catch (NotSignedUpMemberException e) {
            logger.error("회원가입이 필요한 사용자입니다");
            setErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, 403, "4002");
        }

    }

    /**
     * Error를 Json을 바꿔서 클라이언트에 전달
     */
    private void setErrorResponse(HttpServletResponse response,
                                  int status,
                                  int errorCode,
                                  String message) {
        // 직렬화 하기위한 object mapper
        ObjectMapper objectMapper = new ObjectMapper();
        // response의 status, contentType, errorCode, message 등을 설정
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        // response를 설정
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Data
    public static class ErrorResponse {
        private final int code;
        private final String message;
    }

}
