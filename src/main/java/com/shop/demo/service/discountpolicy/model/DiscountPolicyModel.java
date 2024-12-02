package com.shop.demo.service.discountpolicy.model;

import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
public class DiscountPolicyModel {
    private UUID id;
    private DiscountType type;
    private int threshold;
    private BigDecimal value;
}
