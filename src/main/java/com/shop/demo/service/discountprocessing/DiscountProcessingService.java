package com.shop.demo.service.discountprocessing;

import com.shop.demo.service.discountprocessing.model.DiscountProcessingResult;

import java.util.UUID;

public interface DiscountProcessingService {
    DiscountProcessingResult calculate(UUID productId, int quantity);
}
