package com.shop.demo.service.discountprocessing.strategy;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class AmountDiscountStrategy implements DiscountCalculationStrategy {
    @Override
    public BigDecimal calculateDiscount(BigDecimal basePrice, BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return value;
    }
}
