package com.hcframe.base.module.data.exception;

public class BaseMapperException  extends RuntimeException{
    private static final long serialVersionUID = -3691286451456700443L;
    public BaseMapperException() {
    }

    public BaseMapperException(String message) {
        super(message);
    }

    public BaseMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseMapperException(Throwable cause) {
        super(cause);
    }

    public BaseMapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
