package com.shop.demo.service.product;

import com.shop.demo.service.product.model.ProductModel;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductModel addDiscountPolicy(UUID productId, UUID discountPolicyId);

    ProductModel removeDiscountPolicy(UUID productId, UUID discountPolicyId);

    ProductModel getProduct(UUID id);

    List<ProductModel> getAllProducts(Pageable pageable);

    ProductModel createProduct(String name, BigDecimal price);

    ProductModel updateProduct(UUID id, String name, BigDecimal price);

    void deleteProduct(UUID id);
}
