package com.shop.demo.api.v1.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request for updating an existing product")
public class UpdateProductRequest {
    @Schema(
        description = "Updated name of the product",
        example = "Wireless Gaming Mouse",
        maxLength = 255,
        required = true
    )
    @NotBlank(message = "Name must not be blank")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Schema(
        description = "Updated price of the product",
        example = "39.99",
        minimum = "0.01",
        required = true
    )
    @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than or equal 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have up to 2 decimal places and 10 integer digits")
    private BigDecimal price;
}
