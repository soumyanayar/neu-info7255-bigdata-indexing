package com.bigdata.medicalplanner.exceptions;

public class InvalidEtagException extends  Exception {
    public InvalidEtagException(String message) {
        super(message);
    }

    public InvalidEtagException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEtagException(Throwable cause) {
        super(cause);
    }

    public InvalidEtagException() {
        super();
    }

    public InvalidEtagException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
