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
        ProductModel product = productService.getProduct(productId);
        Map<DiscountType, DiscountPolicyModel> applicableDiscounts = getApplicableDiscounts(product, quantity);

        BigDecimal totalPrice = calculateTotalPrice(product.getPrice(), quantity);
        BigDecimal totalDiscount = calculateTotalDiscount(totalPrice, applicableDiscounts);
        BigDecimal finalPrice = calculateFinalPrice(totalPrice, totalDiscount);

        return buildResult(totalPrice, totalDiscount, finalPrice);
    }

    private Map<DiscountType, DiscountPolicyModel> getApplicableDiscounts(ProductModel product, int quantity) {
        Map<DiscountType, DiscountPolicyModel> applicablePolicies = new HashMap<>();

        for (DiscountPolicyModel policy : product.getDiscountPolicies()) {
            if (isDiscountApplicable(policy, quantity)) {
                updateApplicablePolicies(applicablePolicies, policy);
            }
        }

        return applicablePolicies;
    }

    private boolean isDiscountApplicable(DiscountPolicyModel policy, int quantity) {
        return policy != null && 
               policy.getValue() != null &&
               quantity >= policy.getThreshold();
    }

    private void updateApplicablePolicies(
            Map<DiscountType, DiscountPolicyModel> applicablePolicies, 
            DiscountPolicyModel newPolicy) {
        applicablePolicies.merge(
                newPolicy.getType(),
                newPolicy,
                (existing, candidate) -> 
                    candidate.getThreshold() > existing.getThreshold() ? candidate : existing
        );
    }

    private BigDecimal calculateTotalPrice(BigDecimal unitPrice, int quantity) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    private BigDecimal calculateTotalDiscount(
            BigDecimal totalPrice,
            Map<DiscountType, DiscountPolicyModel> applicableDiscounts) {
        return applicableDiscounts.entrySet().stream()
                .map(entry -> calculateDiscountForPolicy(totalPrice, entry.getValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateDiscountForPolicy(BigDecimal totalPrice, DiscountPolicyModel policy) {
        var strategy = discountCalculationFactory.getStrategy(policy.getType());
        return strategy.calculateDiscount(totalPrice, policy.getValue());
    }

    private BigDecimal calculateFinalPrice(BigDecimal totalPrice, BigDecimal totalDiscount) {
        return totalPrice.subtract(totalDiscount)
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private DiscountProcessingResult buildResult(
            BigDecimal totalPrice,
            BigDecimal discount,
            BigDecimal finalPrice) {
        return DiscountProcessingResult.builder()
                .totalPrice(totalPrice)
                .discount(discount)
                .finalPrice(finalPrice)
                .build();
    }
}
