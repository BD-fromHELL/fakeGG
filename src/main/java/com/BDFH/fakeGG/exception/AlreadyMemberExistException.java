package com.BDFH.fakeGG.exception;

public class AlreadyMemberExistException extends RuntimeException{
    public AlreadyMemberExistException() {
        super();
    }

    public AlreadyMemberExistException(String message) {
        super(message);
    }

    public AlreadyMemberExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyMemberExistException(Throwable cause) {
        super(cause);
    }

    protected AlreadyMemberExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
