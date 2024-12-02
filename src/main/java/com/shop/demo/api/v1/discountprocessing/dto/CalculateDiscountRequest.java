package com.shop.demo.api.v1.discountprocessing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Request for calculating product discount")
public class CalculateDiscountRequest {
    @Schema(description = "Product ID for which to calculate discount", 
           example = "123e4567-e89b-12d3-a456-426614174000",
           required = true)
    @NotNull(message = "Product ID cannot be null")
    private UUID productId;

    @Schema(description = "Quantity of products to calculate discount for", 
           example = "5",
           minimum = "1",
           required = true)
    @Positive(message = "Quantity must be greater than zero")
    private int quantity;
}
