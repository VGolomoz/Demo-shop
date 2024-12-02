package com.shop.demo.api.v1.product.controller;

import com.shop.demo.api.v1.product.api.ProductApi;
import com.shop.demo.api.v1.product.dto.CreateProductRequest;
import com.shop.demo.api.v1.product.dto.ProductResponse;
import com.shop.demo.api.v1.product.dto.UpdateProductRequest;
import com.shop.demo.service.product.ProductService;
import com.shop.demo.service.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {
    private final ProductService productService;
    private final ProductMapper mapper;

    @Override
    public ResponseEntity<ProductResponse> getProduct(UUID productId) {
        var response = mapper.modelToResponse(productService.getProduct(productId));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getAllProducts(int pageNumber, int pageSize) {
        var result = productService.getAllProducts(PageRequest.of(pageNumber, pageSize));
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProductResponse> createProduct(CreateProductRequest request) {
        var result = productService.createProduct(request.getName(), request.getPrice());
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProductResponse> updateProduct(UUID productId, UpdateProductRequest request) {
        var result = productService.updateProduct(productId, request.getName(), request.getPrice());
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(UUID productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ProductResponse> addDiscountPolicy(UUID productId, UUID discountPolicyId) {
        var result = productService.addDiscountPolicy(productId, discountPolicyId);
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProductResponse> removeDiscountPolicy(UUID productId, UUID discountPolicyId) {
        var result = productService.removeDiscountPolicy(productId, discountPolicyId);
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }
}
