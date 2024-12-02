package com.shop.demo.service.product;

import com.shop.demo.service.product.model.ProductModel;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductService {
    ProductModel addDiscountPolicy(UUID productId, UUID discountPolicyId);

    ProductModel removeDiscountPolicy(UUID productId, UUID discountPolicyId);

    ProductModel getProduct(UUID id);

    ProductModel createProduct(String name, BigDecimal price);

    ProductModel updateProduct(UUID id, String name, BigDecimal price);

    void deleteProduct(UUID id);
}
