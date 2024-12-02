package com.shop.demo.api.v1.product.dto;

import com.shop.demo.api.v1.discountpolicy.dto.DiscountPolicyResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Schema(description = "Response containing product details")
public class ProductResponse {
    @Schema(description = "Unique identifier of the product",
            example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Name of the product",
            example = "Wireless Gaming Mouse")
    private String name;

    @Schema(description = "Price of the product",
            example = "29.99")
    private BigDecimal price;

    @Schema(description = "List of discount policies applied to the product")
    private List<DiscountPolicyResponse> discountPolicies;
}
