package com.shop.demo.api.v1.product.api;

import com.shop.demo.api.v1.advice.dto.ErrorResponse;
import com.shop.demo.api.v1.product.dto.CreateProductRequest;
import com.shop.demo.api.v1.product.dto.ProductResponse;
import com.shop.demo.api.v1.product.dto.UpdateProductRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Products", description = "API for managing products")
@RequestMapping("/v1/products")
public interface ProductApi {

    @Operation(
        summary = "Get product by ID",
        description = "Retrieves a product by its unique identifier"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product found successfully",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{productId}")
    ResponseEntity<ProductResponse> getProduct(
            @Parameter(description = "ID of the product to retrieve", required = true)
            @PathVariable UUID productId);

    @Operation(
        summary = "Get all products",
        description = "Retrieves a paginated list of all products"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Products retrieved successfully",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        )
    })
    @GetMapping
    ResponseEntity<List<ProductResponse>> getAllProducts(
            @Parameter(description = "Page number (zero-based)", required = true, example = "0")
            @RequestParam int pageNumber,
            @Parameter(description = "Number of items per page", required = true, example = "10")
            @RequestParam int pageSize);

    @Operation(
        summary = "Create new product",
        description = "Creates a new product with the provided details"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product created successfully",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<ProductResponse> createProduct(
            @Parameter(description = "Product creation details", required = true)
            @RequestBody @Valid CreateProductRequest request);

    @Operation(
        summary = "Update product",
        description = "Updates an existing product with the provided details"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product updated successfully",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{productId}")
    ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable UUID productId,
            @Parameter(description = "Product update details", required = true)
            @RequestBody @Valid UpdateProductRequest request);

    @Operation(
        summary = "Delete product",
        description = "Deletes an existing product"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Product deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{productId}")
    ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to delete", required = true)
            @PathVariable UUID productId);

    @Operation(
        summary = "Add discount policy to product",
        description = "Adds a discount policy to an existing product"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Discount policy added successfully",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product or discount policy not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Product already contains this discount policy",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping("/{productId}/discount-policies/{discountPolicyId}")
    ResponseEntity<ProductResponse> addDiscountPolicy(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable UUID productId,
            @Parameter(description = "ID of the discount policy to add", required = true)
            @PathVariable UUID discountPolicyId);

    @Operation(
        summary = "Remove discount policy from product",
        description = "Removes a discount policy from an existing product"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Discount policy removed successfully",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product or discount policy not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{productId}/discount-policies/{discountPolicyId}")
    ResponseEntity<ProductResponse> removeDiscountPolicy(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable UUID productId,
            @Parameter(description = "ID of the discount policy to remove", required = true)
            @PathVariable UUID discountPolicyId);
}
