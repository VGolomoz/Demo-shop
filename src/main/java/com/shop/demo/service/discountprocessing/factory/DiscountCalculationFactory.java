package com.shop.demo.service.discountprocessing.factory;

import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import com.shop.demo.service.discountprocessing.exception.UnsupportedDiscountTypeException;
import com.shop.demo.service.discountprocessing.strategy.AmountDiscountStrategy;
import com.shop.demo.service.discountprocessing.strategy.DiscountCalculationStrategy;
import com.shop.demo.service.discountprocessing.strategy.PercentageDiscountStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DiscountCalculationFactory {
    private final AmountDiscountStrategy amountDiscountStrategy;
    private final PercentageDiscountStrategy percentageDiscountStrategy;
    
    private final Map<DiscountType, DiscountCalculationStrategy> strategies = new EnumMap<>(DiscountType.class);

    public DiscountCalculationStrategy getStrategy(DiscountType discountType) {
        if (strategies.isEmpty()) {
            initializeStrategies();
        }
        
        DiscountCalculationStrategy strategy = strategies.get(discountType);
        if (strategy == null) {
            throw new UnsupportedDiscountTypeException("Unsupported discount type: " + discountType);
        }
        return strategy;
    }

    private void initializeStrategies() {
        strategies.put(DiscountType.AMOUNT, amountDiscountStrategy);
        strategies.put(DiscountType.PERCENTAGE, percentageDiscountStrategy);
    }
}
