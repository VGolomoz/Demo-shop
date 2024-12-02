package com.shop.demo.service.product.model;

import com.shop.demo.service.discountpolicy.model.DiscountPolicyModel;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class ProductModel {
    private UUID id;
    private String name;
    private BigDecimal price;
    private List<DiscountPolicyModel> discountPolicies;
}
