package com.shop.demo.api.v1.product.dto;

import com.shop.demo.api.v1.discountpolicy.dto.DiscountPolicyResponse;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class ProductResponse {
    private UUID id;
    private String name;
    private BigDecimal price;
    private List<DiscountPolicyResponse> discountPolicies;
}
