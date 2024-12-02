package com.shop.demo.service.discountprocessing.strategy;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PercentageDiscountStrategy implements DiscountCalculationStrategy {
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    @Override
    public BigDecimal calculateDiscount(BigDecimal basePrice, BigDecimal percentageValue) {
        if (basePrice == null || percentageValue == null || 
            percentageValue.compareTo(BigDecimal.ZERO) < 0 || 
            percentageValue.compareTo(HUNDRED) > 0) {
            return BigDecimal.ZERO;
        }
        
        return basePrice.multiply(percentageValue)
                .divide(HUNDRED, 2, RoundingMode.HALF_EVEN);
    }
}
