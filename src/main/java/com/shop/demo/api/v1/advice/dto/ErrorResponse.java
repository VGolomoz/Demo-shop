package com.shop.demo.api.v1.advice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponse {
    private final String errorCode;
    private final String errorMessage;
    private final long timestamp;
}
