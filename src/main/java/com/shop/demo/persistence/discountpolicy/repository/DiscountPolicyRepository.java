package com.shop.demo.persistence.discountpolicy.repository;

import com.shop.demo.persistence.discountpolicy.entity.DiscountPolicyEntity;
import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface DiscountPolicyRepository extends JpaRepository<DiscountPolicyEntity, UUID> {
    Optional<DiscountPolicyEntity> findByTypeAndThresholdAndDiscountValue(DiscountType type,
                                                                          Integer threshold,
                                                                          BigDecimal discountValue);
}
