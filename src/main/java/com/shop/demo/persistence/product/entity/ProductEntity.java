package com.shop.demo.persistence.product.entity;

import com.shop.demo.persistence.discountpolicy.entity.DiscountPolicyEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name = "products")
@Entity
@Data
public class ProductEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private BigDecimal price;

    @ManyToMany
    @JoinTable(
            name = "product_discount_policy",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_policy_id")
    )
    private Set<DiscountPolicyEntity> discountPolicies = new HashSet<>();
}
