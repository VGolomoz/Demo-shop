package com.shop.demo.api.v1.discountpolicy.controller;

import com.shop.demo.api.v1.discountpolicy.dto.AddDiscountPolicyRequest;
import com.shop.demo.api.v1.discountpolicy.dto.DiscountPolicyResponse;
import com.shop.demo.api.v1.discountpolicy.dto.UpdateDiscountPolicyRequest;
import com.shop.demo.service.discountpolicy.DiscountPolicyService;
import com.shop.demo.service.discountpolicy.mapper.DiscountPolicyMapper;
import com.shop.demo.service.discountpolicy.model.DiscountPolicyModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/discount-policies")
@RequiredArgsConstructor
public class DiscountPolicyController {
    private final DiscountPolicyService discountPolicyService;
    private final DiscountPolicyMapper mapper;

    @GetMapping("/{discountPolicyId}")
    public ResponseEntity<DiscountPolicyResponse> getDiscountPolicy(@PathVariable UUID discountPolicyId) {
        var result = discountPolicyService.getDiscountPolicy(discountPolicyId);
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DiscountPolicyResponse>> getAllDiscountPolicies(@RequestParam int pageNumber,
                                                                               @RequestParam int pageSize) {
        List<DiscountPolicyModel> result = discountPolicyService.getAllDiscountPolicies(PageRequest.of(pageNumber, pageSize));
        List<DiscountPolicyResponse> response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<DiscountPolicyResponse> addDiscountPolicy(@RequestBody @Valid AddDiscountPolicyRequest request) {
        var result = discountPolicyService.addDiscountPolicy(request.getType(), request.getThreshold(), request.getValue());
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{discountPolicyId}")
    public ResponseEntity<DiscountPolicyResponse> updateDiscountPolicy(@PathVariable UUID discountPolicyId,
                                                                       @RequestBody @Valid UpdateDiscountPolicyRequest request) {
        var result = discountPolicyService.updateDiscountPolicy(discountPolicyId, request.getThreshold(), request.getValue());
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{discountPolicyId}")
    public ResponseEntity<Void> deleteDiscountPolicy(@PathVariable UUID discountPolicyId) {
        discountPolicyService.deleteDiscountPolicy(discountPolicyId);
        return ResponseEntity.ok().build();
    }
}
