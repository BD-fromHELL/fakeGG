package com.BDFH.fakeGG.controller;

import com.BDFH.fakeGG.exception.TokenInvalidException;
import lombok.Data;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionController {

    @ExceptionHandler(TokenInvalidException.class)
    public ErrorResponse handleInvalidToken(TokenInvalidException e) {
        ErrorResponse errorResponse = new ErrorResponse(403, "please login again");
        return errorResponse;
    }


    /**
     * 에러 메시지 dto
     */
    @Data
    private static class ErrorResponse{
        private int errorCode;
        private String message;

        public ErrorResponse(int errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }
    }
}
