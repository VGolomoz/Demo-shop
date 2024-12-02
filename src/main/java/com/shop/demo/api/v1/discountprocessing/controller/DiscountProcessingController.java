package com.shop.demo.api.v1.discountprocessing.controller;

import com.shop.demo.api.v1.discountprocessing.api.DiscountProcessingApi;
import com.shop.demo.api.v1.discountprocessing.dto.CalculateDiscountRequest;
import com.shop.demo.service.discountprocessing.DiscountProcessingService;
import com.shop.demo.service.discountprocessing.model.DiscountProcessingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiscountProcessingController implements DiscountProcessingApi {
    private final DiscountProcessingService discountProcessingService;

    @Override
    public ResponseEntity<DiscountProcessingResult> calculate(CalculateDiscountRequest request) {
        return ResponseEntity.ok(discountProcessingService.calculate(request.getProductId(), request.getQuantity()));
    }
}
