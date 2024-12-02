package com.shop.demo.service.discountprocessing;

import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import com.shop.demo.service.discountpolicy.model.DiscountPolicyModel;
import com.shop.demo.service.discountprocessing.factory.DiscountCalculationFactory;
import com.shop.demo.service.discountprocessing.model.DiscountProcessingResult;
import com.shop.demo.service.product.ProductService;
import com.shop.demo.service.product.model.ProductModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiscountProcessingServiceImpl implements DiscountProcessingService {
    private final ProductService productService;
    private final DiscountCalculationFactory discountCalculationFactory;

    @Override
    public DiscountProcessingResult calculate(UUID productId, int quantity) {
        var product = productService.getProduct(productId);
        var applicableDiscounts = getApplicableDiscounts(product, quantity);

        var totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        var totalDiscount = calculateTotalDiscount(totalPrice, applicableDiscounts);
        var finalPrice = calculateFinalPrice(totalPrice, totalDiscount);

        return DiscountProcessingResult.builder()
                .totalPrice(totalPrice)
                .discount(totalDiscount)
                .finalPrice(finalPrice)
                .build();
    }

    private Map<DiscountType, DiscountPolicyModel> getApplicableDiscounts(ProductModel product, int quantity) {
        var applicableDiscounts = new HashMap<DiscountType, DiscountPolicyModel>();

        for (DiscountPolicyModel policy : product.getDiscountPolicies()) {
            if (isDiscountApplicable(policy, quantity)) {
                updateApplicableDiscounts(applicableDiscounts, policy);
            }
        }

        return applicableDiscounts;
    }

    private boolean isDiscountApplicable(DiscountPolicyModel policy, int quantity) {
        return policy != null &&
                policy.getValue() != null &&
                quantity >= policy.getThreshold();
    }

    private void updateApplicableDiscounts(Map<DiscountType, DiscountPolicyModel> applicableDiscounts,
                                           DiscountPolicyModel newPolicy) {
        applicableDiscounts.merge(
                newPolicy.getType(),
                newPolicy,
                (existing, candidate) -> candidate.getThreshold() > existing.getThreshold() ? candidate : existing
        );
    }

    private BigDecimal calculateTotalDiscount(BigDecimal totalPrice,
                                              Map<DiscountType, DiscountPolicyModel> applicableDiscounts) {

        return applicableDiscounts.values().stream()
                .map(discountPolicy -> calculateDiscountByPolicy(totalPrice, discountPolicy))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateDiscountByPolicy(BigDecimal totalPrice, DiscountPolicyModel policy) {
        var strategy = discountCalculationFactory.getStrategy(policy.getType());
        return strategy.calculateDiscount(totalPrice, policy.getValue());
    }

    private BigDecimal calculateFinalPrice(BigDecimal totalPrice, BigDecimal totalDiscount) {
        var finalPrice = totalPrice.subtract(totalDiscount);
        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return finalPrice.setScale(2, RoundingMode.HALF_EVEN);
    }
}
