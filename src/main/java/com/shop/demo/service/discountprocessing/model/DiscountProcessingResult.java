package com.shop.demo.service.discountprocessing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@Schema(description = "Result of discount calculation")
public class DiscountProcessingResult {
    @Schema(description = "Total price before discount", 
            example = "100.00")
    BigDecimal totalPrice;

    @Schema(description = "Calculated discount amount", 
            example = "20.00")
    BigDecimal discount;

    @Schema(description = "Final price after applying discount", 
            example = "80.00")
    BigDecimal finalPrice;
}
