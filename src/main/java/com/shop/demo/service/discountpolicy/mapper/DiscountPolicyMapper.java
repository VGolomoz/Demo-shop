package com.shop.demo.service.discountpolicy.mapper;

import com.shop.demo.api.v1.discountpolicy.dto.DiscountPolicyResponse;
import com.shop.demo.persistence.discountpolicy.entity.DiscountPolicyEntity;
import com.shop.demo.service.discountpolicy.model.DiscountPolicyModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscountPolicyMapper {
    public DiscountPolicyModel entityToModel(DiscountPolicyEntity entity) {
        return DiscountPolicyModel.builder()
                .id(entity.getId())
                .type(entity.getType())
                .threshold(entity.getThreshold())
                .value(entity.getDiscountValue())
                .build();
    }

    public List<DiscountPolicyResponse> modelToResponse(List<DiscountPolicyModel> models) {
        return models.stream().map(this::modelToResponse).toList();
    }

    public DiscountPolicyResponse modelToResponse(DiscountPolicyModel model) {
        return DiscountPolicyResponse.builder()
                .id(model.getId())
                .type(model.getType())
                .threshold(model.getThreshold())
                .value(model.getValue())
                .build();
    }
}
