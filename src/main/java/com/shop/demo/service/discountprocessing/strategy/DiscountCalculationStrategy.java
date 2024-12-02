package com.shop.demo.service.discountprocessing.strategy;

import java.math.BigDecimal;

public interface DiscountCalculationStrategy {
    BigDecimal calculateDiscount(BigDecimal basePrice, BigDecimal value);
}
