package com.shop.demo.service.discountpolicy;

import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import com.shop.demo.service.discountpolicy.model.DiscountPolicyModel;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface DiscountPolicyService {
    DiscountPolicyModel addDiscountPolicy(DiscountType type, int threshold, BigDecimal value);

    DiscountPolicyModel getDiscountPolicy(UUID id);

    List<DiscountPolicyModel> getAllDiscountPolicies(Pageable pageable);

    DiscountPolicyModel updateDiscountPolicy(UUID id, int threshold, BigDecimal value);

    void deleteDiscountPolicy(UUID id);
}
