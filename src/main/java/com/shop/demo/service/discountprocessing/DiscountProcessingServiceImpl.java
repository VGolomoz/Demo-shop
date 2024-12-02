package com.shop.demo.service.discountprocessing;

import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import com.shop.demo.service.discountpolicy.model.DiscountPolicyModel;
import com.shop.demo.service.discountprocessing.exception.UnsupportedDiscountTypeException;
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

    @Override
    public DiscountProcessingResult calculate(UUID productId, int quantity) {
        var product = productService.getProduct(productId);
        var discountPolicies = getApplicableDiscounts(product, quantity);

        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        BigDecimal discount = calculateDiscount(totalPrice, discountPolicies);
        BigDecimal finalPrice = totalPrice.subtract(discount).setScale(2, RoundingMode.HALF_EVEN);

        return DiscountProcessingResult.builder()
                .totalPrice(totalPrice)
                .discount(discount)
                .finalPrice(finalPrice)
                .build();
    }

    private Map<DiscountType, DiscountPolicyModel> getApplicableDiscounts(ProductModel product, int quantity) {
        Map<DiscountType, DiscountPolicyModel> applicablePolicies = new HashMap<>();

        for (DiscountPolicyModel policy : product.getDiscountPolicies()) {
            if (quantity >= policy.getThreshold()) {
                applicablePolicies.merge(
                        policy.getType(),
                        policy,
                        (existing, candidate) -> candidate.getThreshold() > existing.getThreshold() ? candidate : existing
                );
            }
        }

        return applicablePolicies;
    }

    private BigDecimal calculateDiscount(BigDecimal totalPrice, Map<DiscountType, DiscountPolicyModel> policies) {
        BigDecimal discount = BigDecimal.ZERO;

        for (DiscountPolicyModel policy : policies.values()) {
            switch (policy.getType()) {
                case AMOUNT -> discount = discount.add(policy.getValue());
                case PERCENTAGE -> {
                    BigDecimal discountValue = totalPrice.multiply(policy.getValue())
                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN);
                    discount = discount.add(discountValue);
                }
                default -> throw new UnsupportedDiscountTypeException("Unsupported discount type: " + policy.getType());
            }
        }
        return discount;
    }
}
