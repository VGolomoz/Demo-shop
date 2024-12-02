package com.shop.demo.service.product;

import com.shop.demo.persistence.discountpolicy.entity.DiscountPolicyEntity;
import com.shop.demo.persistence.discountpolicy.repository.DiscountPolicyRepository;
import com.shop.demo.persistence.product.entity.ProductEntity;
import com.shop.demo.persistence.product.repository.ProductRepository;
import com.shop.demo.service.discountpolicy.exception.DiscountPolicyNotFoundException;
import com.shop.demo.service.product.excpetion.ProductAlreadyContainsPolicyException;
import com.shop.demo.service.product.excpetion.ProductNotFoundException;
import com.shop.demo.service.product.mapper.ProductMapper;
import com.shop.demo.service.product.model.ProductModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    private final DiscountPolicyRepository discountPolicyRepository;

    @Transactional
    @Override
    public ProductModel addDiscountPolicy(UUID productId, UUID discountPolicyId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id=" + productId + " not found"));

        var discountPolicy = discountPolicyRepository.findById(discountPolicyId)
                .orElseThrow(() -> new DiscountPolicyNotFoundException("Policy with id=" + discountPolicyId + " not found"));

        checkIfHasSamePolicy(product, discountPolicy);

        product.getDiscountPolicies().add(discountPolicy);
        product = productRepository.save(product);
        return mapper.entityToModel(product);
    }

    @Transactional
    @Override
    public ProductModel removeDiscountPolicy(UUID productId, UUID discountPolicyId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id=" + productId + " not found"));

        product.getDiscountPolicies().removeIf(policy -> policy.getId().equals(discountPolicyId));

        product = productRepository.save(product);
        return mapper.entityToModel(product);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductModel getProduct(UUID id) {
        return productRepository.findById(id)
                .map(mapper::entityToModel)
                .orElseThrow(() -> new ProductNotFoundException("Product with id=" + id + " not found"));
    }

    @Transactional
    @Override
    public ProductModel createProduct(String name, BigDecimal price) {
        var entity = new ProductEntity();
        entity.setName(name);
        entity.setPrice(price);

        entity = productRepository.save(entity);
        return mapper.entityToModel(entity);
    }

    @Transactional
    @Override
    public ProductModel updateProduct(UUID id, String name, BigDecimal price) {
        var entity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id=" + id + " not found"));
        entity.setName(name);
        entity.setPrice(price);

        entity = productRepository.save(entity);
        return mapper.entityToModel(entity);
    }

    @Transactional
    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    private void checkIfHasSamePolicy(ProductEntity product, DiscountPolicyEntity discountPolicy) {
        var contains = product.getDiscountPolicies().stream()
                .anyMatch(policy ->
                        policy.getType().equals(discountPolicy.getType()) &&
                                policy.getThreshold().equals(discountPolicy.getThreshold()) &&
                                policy.getDiscountValue().equals(discountPolicy.getDiscountValue()));

        if (contains) {
            var msg = String.format("The product already contains a discount policy with the same type=%s, threshold=%s, " +
                    "and value=%s.", discountPolicy.getType(), discountPolicy.getThreshold(), discountPolicy.getDiscountValue());
            throw new ProductAlreadyContainsPolicyException(msg);
        }
    }
}
