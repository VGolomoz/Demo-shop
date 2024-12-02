package com.shop.demo.exception;

import org.springframework.http.HttpStatus;

public abstract class AbstractShopDemoException extends RuntimeException {
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private final ServiceErrorCode errorCode;

    public AbstractShopDemoException(ServiceErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public AbstractShopDemoException(ServiceErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public HttpStatus getErrorStatus() {
        return DEFAULT_STATUS;
    }

    public ServiceErrorCode getErrorCode() {
        return this.errorCode;
    }

    public String toString() {
        ServiceErrorCode errorCode = this.getErrorCode();
        return "ShopDemoException ( errorCode: " + errorCode + ")";
    }
}
