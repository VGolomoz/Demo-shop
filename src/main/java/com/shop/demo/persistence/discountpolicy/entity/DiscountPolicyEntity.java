package com.shop.demo.persistence.discountpolicy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "discount_policies")
@Entity
@Data
public class DiscountPolicyEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private DiscountType type;

    private Integer threshold;

    @Column(name = "discount_value")
    private BigDecimal discountValue;
}
