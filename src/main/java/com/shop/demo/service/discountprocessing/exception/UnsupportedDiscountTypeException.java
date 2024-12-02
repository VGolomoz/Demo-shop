package com.shop.demo.service.discountprocessing.exception;

import com.shop.demo.exception.AbstractShopDemoException;
import com.shop.demo.exception.ServiceErrorCode;

public class UnsupportedDiscountTypeException extends AbstractShopDemoException {
    public UnsupportedDiscountTypeException(String message) {
        super(ServiceErrorCode.UNSUPPORTED_DISCOUNT_TYPE, message);
    }
}
