package com.shop.demo.api.v1.discountprocessing.controller;

import com.shop.demo.service.discountprocessing.DiscountProcessingService;
import com.shop.demo.service.discountprocessing.model.DiscountProcessingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/discount-processing")
@RequiredArgsConstructor
public class DiscountProcessingController {
    private final DiscountProcessingService discountProcessingService;

    @GetMapping("/calculate")
    public ResponseEntity<DiscountProcessingResult> calculate(@RequestParam UUID productId,
                                                              @RequestParam int quantity) {
        return ResponseEntity.ok(discountProcessingService.calculate(productId, quantity));
    }
}
