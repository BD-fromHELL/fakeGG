package com.BDFH.fakeGG.exception;

public class NotFoundArticleException extends RuntimeException{

    public NotFoundArticleException() {
    }

    public NotFoundArticleException(String message) {
        super(message);
    }

    public NotFoundArticleException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundArticleException(Throwable cause) {
        super(cause);
    }

    public NotFoundArticleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
