package com.shop.demo.api.v1.product.controller;

import com.shop.demo.api.v1.product.dto.CreateProductRequest;
import com.shop.demo.api.v1.product.dto.ProductResponse;
import com.shop.demo.api.v1.product.dto.UpdateProductRequest;
import com.shop.demo.service.product.ProductService;
import com.shop.demo.service.product.mapper.ProductMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper mapper;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID productId) {
        var response = mapper.modelToResponse(productService.getProduct(productId));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid CreateProductRequest request) {
        var result = productService.createProduct(request.getName(), request.getPrice());
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID productId,
                                                         @RequestBody @Valid UpdateProductRequest request) {
        var result = productService.updateProduct(productId, request.getName(), request.getPrice());
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/discount-policies/{discountPolicyId}")
    public ResponseEntity<ProductResponse> addDiscountPolicy(@PathVariable UUID productId,
                                                             @PathVariable UUID discountPolicyId) {
        var result = productService.addDiscountPolicy(productId, discountPolicyId);
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}/discount-policies/{discountPolicyId}")
    public ResponseEntity<ProductResponse> removeDiscountPolicy(@PathVariable UUID productId,
                                                                @PathVariable UUID discountPolicyId) {
        var result = productService.removeDiscountPolicy(productId, discountPolicyId);
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }
}
