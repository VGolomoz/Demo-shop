package com.shop.demo.api.v1.advice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "Error response details")
public class ErrorResponse {
    @Schema(description = "Error code identifying the error type", 
            example = "product_not_found")
    private final String errorCode;

    @Schema(description = "Detailed error message", 
            example = "Product with ID '123e4567-e89b-12d3-a456-426614174000' not found")
    private final String errorMessage;

    @Schema(description = "Timestamp when the error occurred", 
            example = "1648656000000")
    private final long timestamp;
}
