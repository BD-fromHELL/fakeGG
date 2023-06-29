package com.BDFH.fakeGG.controller;

import com.BDFH.fakeGG.auth.oauth.OAuth2SuccessHandler;
import com.BDFH.fakeGG.exception.NotSignedUpMemberException;
import com.BDFH.fakeGG.exception.TokenExpiredException;
import com.BDFH.fakeGG.exception.TokenInvalidException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RestController
public class ExceptionController {
    /**
     *
     */

    // 로그 출력
    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    /**
     * 만료된 토큰 : 4000
     */
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleExpiredToken(TokenExpiredException e) {
        ErrorResponse errorResponse = new ErrorResponse(401, "4000");
        logger.info("만료된 토큰입니다");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * 유효하지 않은 토큰 : 4001
     */
    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(TokenInvalidException e) {
        ErrorResponse errorResponse = new ErrorResponse(401, "4001");
        logger.info("유효하지 않은 토큰입니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * 회원 가입이 필요한 사용자 : 4002
     */
    @ExceptionHandler(NotSignedUpMemberException.class)
    public ResponseEntity<ErrorResponse> NotSignedUpMember(NotSignedUpMemberException e) {
        ErrorResponse errorResponse = new ErrorResponse(403, "4002");
        logger.info("회원가입이 필요한 사용자입니다");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }



    /**
     * 에러 메시지 dto
     */
    @Data
    private static class ErrorResponse {
        private int errorCode;
        private String message;

        public ErrorResponse(int errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }
    }
}
