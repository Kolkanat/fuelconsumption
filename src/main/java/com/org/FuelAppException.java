package com.org;

public class FuelAppException extends Exception {
    public FuelAppException() {
        super();
    }

    public FuelAppException(String message) {
        super(message);
    }

    public FuelAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public FuelAppException(Throwable cause) {
        super(cause);
    }

    public FuelAppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
