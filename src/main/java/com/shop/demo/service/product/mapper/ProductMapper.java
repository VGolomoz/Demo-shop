package com.shop.demo.service.product.mapper;

import com.shop.demo.api.v1.product.dto.ProductResponse;
import com.shop.demo.persistence.product.entity.ProductEntity;
import com.shop.demo.service.discountpolicy.mapper.DiscountPolicyMapper;
import com.shop.demo.service.product.model.ProductModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final DiscountPolicyMapper discountPolicyMapper;

    public ProductModel entityToModel(ProductEntity entity) {
        var discountPolicies = entity.getDiscountPolicies().stream()
                .map(discountPolicyMapper::entityToModel)
                .toList();

        return ProductModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .discountPolicies(discountPolicies)
                .build();
    }

    public List<ProductResponse> modelToResponse(List<ProductModel> models) {
        return models.stream().map(this::modelToResponse).toList();
    }

    public ProductResponse modelToResponse(ProductModel model) {
        var discountPolicies = model.getDiscountPolicies().stream()
                .map(discountPolicyMapper::modelToResponse)
                .toList();

        return ProductResponse.builder()
                .id(model.getId())
                .name(model.getName())
                .price(model.getPrice())
                .discountPolicies(discountPolicies)
                .build();
    }

}
