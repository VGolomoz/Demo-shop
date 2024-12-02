package com.shop.demo.service.discountpolicy.exception;

import com.shop.demo.exception.AbstractShopDemoException;
import com.shop.demo.exception.ServiceErrorCode;
import org.springframework.http.HttpStatus;

public class DiscountPolicyAlreadyExistsException extends AbstractShopDemoException {
    public DiscountPolicyAlreadyExistsException(String message) {
        super(ServiceErrorCode.DISCOUNT_POLICY_ALREADY_EXISTS, message);
    }

    @Override
    public HttpStatus getErrorStatus() {
        return HttpStatus.NOT_ACCEPTABLE;
    }
}
