package com.BDFH.fakeGG.exception;

public class NotSignedUpMemberException extends RuntimeException {
    public NotSignedUpMemberException() {
        super();
    }

    public NotSignedUpMemberException(String message) {
        super(message);
    }

    public NotSignedUpMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSignedUpMemberException(Throwable cause) {
        super(cause);
    }

    protected NotSignedUpMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
