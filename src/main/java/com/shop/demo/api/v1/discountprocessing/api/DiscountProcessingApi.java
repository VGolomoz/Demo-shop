package com.shop.demo.api.v1.discountprocessing.api;

import com.shop.demo.api.v1.advice.dto.ErrorResponse;
import com.shop.demo.api.v1.discountprocessing.dto.CalculateDiscountRequest;
import com.shop.demo.service.discountprocessing.model.DiscountProcessingResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Discount Processing", description = "API for processing product discounts")
@RequestMapping("/v1/discount-processing")
public interface DiscountProcessingApi {

    @Operation(
        summary = "Calculate discount for product",
        description = "Calculates the applicable discount for a product based on quantity and available discount policies"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Discount calculated successfully",
            content = @Content(schema = @Schema(implementation = DiscountProcessingResult.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input parameters",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping("/calculate")
    ResponseEntity<DiscountProcessingResult> calculate(
            @Parameter(description = "Discount calculation request", required = true)
            @Valid @RequestBody CalculateDiscountRequest request);
}
