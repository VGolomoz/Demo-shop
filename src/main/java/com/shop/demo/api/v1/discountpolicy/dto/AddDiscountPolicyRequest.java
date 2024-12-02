package com.shop.demo.api.v1.discountpolicy.dto;

import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddDiscountPolicyRequest {
    @NotNull(message = "Discount type must not be null")
    private DiscountType type;

    @Min(value = 1, message = "Threshold must be greater than or equal to 1")
    private int threshold;

    @DecimalMin(value = "0.01", message = "Discount value must be greater than or equal to 0.01")
    @Digits(integer = 10, fraction = 2, message = "Discount value must have up to 2 decimal places and 10 integer digits")
    private BigDecimal value;
}
