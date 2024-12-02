package com.shop.demo.service.product.excpetion;

import com.shop.demo.exception.AbstractShopDemoException;
import com.shop.demo.exception.ServiceErrorCode;
import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends AbstractShopDemoException {
    public ProductNotFoundException(String message) {
        super(ServiceErrorCode.PRODUCT_NOT_FOUND, message);
    }

    @Override
    public HttpStatus getErrorStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
