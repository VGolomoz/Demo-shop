package com.shop.demo.api.v1.discountpolicy.dto;

import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request for creating a new discount policy")
public class AddDiscountPolicyRequest {
    @Schema(
        description = "Type of discount to apply",
        example = "PERCENTAGE",
        allowableValues = {"AMOUNT", "PERCENTAGE"},
        required = true
    )
    @NotNull(message = "Discount type must not be null")
    private DiscountType type;

    @Schema(
        description = "Minimum quantity threshold for the discount to apply",
        example = "5",
        minimum = "1",
        required = true
    )
    @Min(value = 1, message = "Threshold must be greater than or equal to 1")
    private int threshold;

    @Schema(
        description = "Value of the discount (amount or percentage depending on type)",
        example = "10.00",
        minimum = "0.01",
        required = true
    )
    @DecimalMin(value = "0.01", message = "Discount value must be greater than or equal to 0.01")
    @Digits(integer = 10, fraction = 2, message = "Discount value must have up to 2 decimal places and 10 integer digits")
    private BigDecimal value;
}
