package com.shop.demo.service.discountpolicy;

import com.shop.demo.persistence.discountpolicy.entity.DiscountPolicyEntity;
import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import com.shop.demo.persistence.discountpolicy.repository.DiscountPolicyRepository;
import com.shop.demo.service.discountpolicy.exception.DiscountPolicyAlreadyExistsException;
import com.shop.demo.service.discountpolicy.exception.DiscountPolicyNotFoundException;
import com.shop.demo.service.discountpolicy.mapper.DiscountPolicyMapper;
import com.shop.demo.service.discountpolicy.model.DiscountPolicyModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiscountPolicyServiceImpl implements DiscountPolicyService {
    private final DiscountPolicyRepository discountPolicyRepository;
    private final DiscountPolicyMapper mapper;

    @Transactional
    @Override
    public DiscountPolicyModel addDiscountPolicy(DiscountType type, int threshold, BigDecimal value) {
        checkIfAlreadyExists(type, threshold, value);

        var entity = new DiscountPolicyEntity();
        entity.setType(type);
        entity.setThreshold(threshold);
        entity.setDiscountValue(value);
        entity = discountPolicyRepository.save(entity);
        return mapper.entityToModel(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public DiscountPolicyModel getDiscountPolicy(UUID id) {
        return discountPolicyRepository.findById(id)
                .map(mapper::entityToModel)
                .orElseThrow(() -> new DiscountPolicyNotFoundException("Policy with id=" + id + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<DiscountPolicyModel> getAllDiscountPolicies(Pageable pageable) {
        return discountPolicyRepository.findAll(pageable)
                .map(mapper::entityToModel)
                .toList();
    }

    @Transactional
    @Override
    public DiscountPolicyModel updateDiscountPolicy(UUID id, int threshold, BigDecimal value) {
        var entity = discountPolicyRepository.findById(id)
                .orElseThrow(() -> new DiscountPolicyNotFoundException("Policy with id=" + id + " not found"));
        checkIfAlreadyExists(entity.getType(), threshold, value);

        entity.setThreshold(threshold);
        entity.setDiscountValue(value);
        entity = discountPolicyRepository.save(entity);

        return mapper.entityToModel(entity);
    }

    @Transactional
    @Override
    public void deleteDiscountPolicy(UUID id) {
        discountPolicyRepository.deleteById(id);
    }

    private void checkIfAlreadyExists(DiscountType type, int threshold, BigDecimal value) {
        var similarEntity =
                discountPolicyRepository.findByTypeAndThresholdAndDiscountValue(type, threshold, value);

        if (similarEntity.isPresent()) {
            var msg = String.format("Policy with same type=%s, threshold=%s and value=%s already exists with id=%s",
                    type, threshold, value, similarEntity.get().getId());
            throw new DiscountPolicyAlreadyExistsException(msg);
        }
    }
}
