package com.x2bee.common.base.exception;

@SuppressWarnings("serial")
public class RestApiException extends UserDefinedException {

    public RestApiException() {
        super();
    }

    public RestApiException(String message) {
        super(message);
    }

    public RestApiException(Throwable t) {
        super(t);
    }

    public RestApiException(String message, Throwable t) {
        super(message, t);
    }
}
