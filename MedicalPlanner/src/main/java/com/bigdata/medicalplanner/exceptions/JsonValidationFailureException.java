package com.bigdata.medicalplanner.exceptions;

public class JsonValidationFailureException extends Exception {
    public JsonValidationFailureException(String message) {
        super(message);
    }

    public JsonValidationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonValidationFailureException(Throwable cause) {
        super(cause);
    }

    public JsonValidationFailureException() {
        super();
    }

    public JsonValidationFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

