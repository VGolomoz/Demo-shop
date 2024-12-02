package com.shop.demo.api.v1.discountpolicy.dto;

import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Schema(description = "Response containing discount policy details")
public class DiscountPolicyResponse {
    @Schema(description = "Unique identifier of the discount policy",
            example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Type of discount",
            example = "PERCENTAGE",
            allowableValues = {"AMOUNT", "PERCENTAGE"})
    private DiscountType type;

    @Schema(description = "Minimum quantity threshold for the discount to apply",
            example = "5")
    private int threshold;

    @Schema(description = "Discount value (amount or percentage depending on type)",
            example = "10.00")
    private BigDecimal value;
}
