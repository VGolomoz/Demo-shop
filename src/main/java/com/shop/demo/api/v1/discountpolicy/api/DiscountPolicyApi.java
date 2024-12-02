package com.shop.demo.api.v1.discountpolicy.api;

import com.shop.demo.api.v1.advice.dto.ErrorResponse;
import com.shop.demo.api.v1.discountpolicy.dto.AddDiscountPolicyRequest;
import com.shop.demo.api.v1.discountpolicy.dto.DiscountPolicyResponse;
import com.shop.demo.api.v1.discountpolicy.dto.UpdateDiscountPolicyRequest;
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

@Tag(name = "Discount Policies", description = "API for managing discount policies")
@RequestMapping("/v1/discount-policies")
public interface DiscountPolicyApi {

    @Operation(
            summary = "Get discount policy by ID",
            description = "Retrieves a discount policy by its unique identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Discount policy found successfully",
                    content = @Content(schema = @Schema(implementation = DiscountPolicyResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Discount policy not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{discountPolicyId}")
    ResponseEntity<DiscountPolicyResponse> getDiscountPolicy(
            @Parameter(description = "ID of the discount policy to retrieve", required = true)
            @PathVariable UUID discountPolicyId);

    @Operation(
            summary = "Get all discount policies",
            description = "Retrieves a paginated list of all discount policies"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Discount policies retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DiscountPolicyResponse.class))
            )
    })
    @GetMapping
    ResponseEntity<List<DiscountPolicyResponse>> getAllDiscountPolicies(
            @Parameter(description = "Page number (zero-based)", required = true, example = "0")
            @RequestParam int pageNumber,
            @Parameter(description = "Number of items per page", required = true, example = "10")
            @RequestParam int pageSize);

    @Operation(
            summary = "Create new discount policy",
            description = "Creates a new discount policy with the provided details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Discount policy created successfully",
                    content = @Content(schema = @Schema(implementation = DiscountPolicyResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    ResponseEntity<DiscountPolicyResponse> addDiscountPolicy(
            @Parameter(description = "Discount policy creation details", required = true)
            @RequestBody @Valid AddDiscountPolicyRequest request);

    @Operation(
            summary = "Update discount policy",
            description = "Updates an existing discount policy with the provided details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Discount policy updated successfully",
                    content = @Content(schema = @Schema(implementation = DiscountPolicyResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Discount policy not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PutMapping("/{discountPolicyId}")
    ResponseEntity<DiscountPolicyResponse> updateDiscountPolicy(
            @Parameter(description = "ID of the discount policy to update", required = true)
            @PathVariable UUID discountPolicyId,
            @Parameter(description = "Discount policy update details", required = true)
            @RequestBody @Valid UpdateDiscountPolicyRequest request);

    @Operation(
            summary = "Delete discount policy",
            description = "Deletes an existing discount policy"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Discount policy deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Discount policy not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/{discountPolicyId}")
    ResponseEntity<Void> deleteDiscountPolicy(
            @Parameter(description = "ID of the discount policy to delete", required = true)
            @PathVariable UUID discountPolicyId);
}
