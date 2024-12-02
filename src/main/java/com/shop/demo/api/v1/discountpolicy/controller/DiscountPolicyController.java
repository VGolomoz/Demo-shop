package com.shop.demo.api.v1.discountpolicy.controller;

import com.shop.demo.api.v1.discountpolicy.api.DiscountPolicyApi;
import com.shop.demo.api.v1.discountpolicy.dto.AddDiscountPolicyRequest;
import com.shop.demo.api.v1.discountpolicy.dto.DiscountPolicyResponse;
import com.shop.demo.api.v1.discountpolicy.dto.UpdateDiscountPolicyRequest;
import com.shop.demo.service.discountpolicy.DiscountPolicyService;
import com.shop.demo.service.discountpolicy.mapper.DiscountPolicyMapper;
import com.shop.demo.service.discountpolicy.model.DiscountPolicyModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DiscountPolicyController implements DiscountPolicyApi {
    private final DiscountPolicyService discountPolicyService;
    private final DiscountPolicyMapper mapper;

    @Override
    public ResponseEntity<DiscountPolicyResponse> getDiscountPolicy(UUID discountPolicyId) {
        var result = discountPolicyService.getDiscountPolicy(discountPolicyId);
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<DiscountPolicyResponse>> getAllDiscountPolicies(int pageNumber, int pageSize) {
        var result = discountPolicyService.getAllDiscountPolicies(PageRequest.of(pageNumber, pageSize));
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<DiscountPolicyResponse> addDiscountPolicy(AddDiscountPolicyRequest request) {
        var result = discountPolicyService.addDiscountPolicy(request.getType(), request.getThreshold(), request.getValue());
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<DiscountPolicyResponse> updateDiscountPolicy(UUID discountPolicyId, UpdateDiscountPolicyRequest request) {
        var result = discountPolicyService.updateDiscountPolicy(discountPolicyId, request.getThreshold(), request.getValue());
        var response = mapper.modelToResponse(result);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteDiscountPolicy(UUID discountPolicyId) {
        discountPolicyService.deleteDiscountPolicy(discountPolicyId);
        return ResponseEntity.ok().build();
    }
}
