package com.bigdata.medicalplanner.exceptions;

public class InvalidPayloadException extends Exception{
    public InvalidPayloadException(String message) {
        super(message);
    }

    public InvalidPayloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPayloadException(Throwable cause) {
        super(cause);
    }

    public InvalidPayloadException() {
        super();
    }

    public InvalidPayloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

