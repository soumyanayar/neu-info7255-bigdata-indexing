package com.bigdata.medicalplanner.exceptions;

public class KeyAlreadyExistsException extends  Exception {
    public KeyAlreadyExistsException(String message) {
        super(message);
    }

    public KeyAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public KeyAlreadyExistsException() {
        super();
    }

    public KeyAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
