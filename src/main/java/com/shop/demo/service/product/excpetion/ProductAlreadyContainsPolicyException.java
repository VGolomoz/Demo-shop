package com.shop.demo.service.product.excpetion;

import com.shop.demo.exception.AbstractShopDemoException;
import com.shop.demo.exception.ServiceErrorCode;
import org.springframework.http.HttpStatus;

public class ProductAlreadyContainsPolicyException extends AbstractShopDemoException {
    public ProductAlreadyContainsPolicyException(String message) {
        super(ServiceErrorCode.PRODUCT_ALREADY_CONTAINS_POLICY, message);
    }

    @Override
    public HttpStatus getErrorStatus() {
        return HttpStatus.NOT_ACCEPTABLE;
    }
}
