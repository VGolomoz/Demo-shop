package com.shop.demo.service.discountprocessing.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class DiscountProcessingResult {
    private BigDecimal totalPrice;
    private BigDecimal discount;
    private BigDecimal finalPrice;
}
