package com.shop.demo.service.discountpolicy.exception;

import com.shop.demo.exception.AbstractShopDemoException;
import com.shop.demo.exception.ServiceErrorCode;
import org.springframework.http.HttpStatus;

public class DiscountPolicyNotFoundException extends AbstractShopDemoException {
    public DiscountPolicyNotFoundException(String message) {
        super(ServiceErrorCode.DISCOUNT_POLICY_NOT_FOUND, message);
    }

    @Override
    public HttpStatus getErrorStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
