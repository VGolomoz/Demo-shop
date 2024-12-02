package com.shop.demo.exception;

import lombok.Getter;

@Getter
public enum ServiceErrorCode {

    PRODUCT_NOT_FOUND("product_not_found"),
    DISCOUNT_POLICY_NOT_FOUND("discount_policy_not_found"),
    DISCOUNT_POLICY_ALREADY_EXISTS("discount_policy_already_exists"),
    PRODUCT_ALREADY_CONTAINS_POLICY("product_already_contains_discount_policy"),
    UNSUPPORTED_DISCOUNT_TYPE("unsupported_discount_type"),
    FIELD_VALIDATION("field_validation");

    private final String code;

    ServiceErrorCode(String code) {
        this.code = code;
    }
}
