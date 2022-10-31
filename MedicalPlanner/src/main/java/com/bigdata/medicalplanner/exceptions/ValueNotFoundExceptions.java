package com.bigdata.medicalplanner.exceptions;

public class ValueNotFoundExceptions extends Exception{
    public ValueNotFoundExceptions(String message) {
        super(message);
    }

    public ValueNotFoundExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueNotFoundExceptions(Throwable cause) {
        super(cause);
    }

    public ValueNotFoundExceptions() {
        super();
    }

    public ValueNotFoundExceptions(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
