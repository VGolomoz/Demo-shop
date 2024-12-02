package com.shop.demo.api.v1.discountpolicy.dto;

import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
public class DiscountPolicyResponse {
    private UUID id;
    private DiscountType type;
    private int threshold;
    private BigDecimal value;
}
