package com.shop.demo.service.discountpolicy;

import com.shop.demo.persistence.discountpolicy.entity.DiscountPolicyEntity;
import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import com.shop.demo.persistence.discountpolicy.repository.DiscountPolicyRepository;
import com.shop.demo.service.discountpolicy.exception.DiscountPolicyAlreadyExistsException;
import com.shop.demo.service.discountpolicy.exception.DiscountPolicyNotFoundException;
import com.shop.demo.service.discountpolicy.mapper.DiscountPolicyMapper;
import com.shop.demo.service.discountpolicy.model.DiscountPolicyModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountPolicyServiceTest {

    @Mock
    private DiscountPolicyRepository discountPolicyRepository;

    @Mock
    private DiscountPolicyMapper discountPolicyMapper;

    private DiscountPolicyService discountPolicyService;

    private UUID validId;
    private DiscountPolicyEntity validEntity;
    private DiscountPolicyModel validModel;

    @BeforeEach
    void setUp() {
        discountPolicyService = new DiscountPolicyServiceImpl(discountPolicyRepository, discountPolicyMapper);
        
        validId = UUID.randomUUID();
        validEntity = new DiscountPolicyEntity();
        validEntity.setId(validId);
        validEntity.setType(DiscountType.PERCENTAGE);
        validEntity.setThreshold(5);
        validEntity.setDiscountValue(new BigDecimal("10.99"));

        validModel = DiscountPolicyModel.builder()
                .id(validId)
                .type(DiscountType.PERCENTAGE)
                .threshold(5)
                .value(new BigDecimal("10.99"))
                .build();
    }

    @Test
    void addDiscountPolicy_WithValidPercentageDiscount_ShouldReturnSavedPolicy() {
        when(discountPolicyRepository.findByTypeAndThresholdAndDiscountValue(
                DiscountType.PERCENTAGE, 5, new BigDecimal("10.99")))
                .thenReturn(Optional.empty());
        when(discountPolicyRepository.save(any(DiscountPolicyEntity.class))).thenReturn(validEntity);
        when(discountPolicyMapper.entityToModel(validEntity)).thenReturn(validModel);

        DiscountPolicyModel result = discountPolicyService.addDiscountPolicy(
                DiscountType.PERCENTAGE, 5, new BigDecimal("10.99"));

        assertNotNull(result);
        assertEquals(DiscountType.PERCENTAGE, result.getType());
        assertEquals(5, result.getThreshold());
        assertEquals(0, new BigDecimal("10.99").compareTo(result.getValue()));
        verify(discountPolicyRepository).save(any(DiscountPolicyEntity.class));
    }

    @Test
    void addDiscountPolicy_WithValidAmountDiscount_ShouldReturnSavedPolicy() {
        DiscountPolicyEntity amountEntity = new DiscountPolicyEntity();
        amountEntity.setId(validId);
        amountEntity.setType(DiscountType.AMOUNT);
        amountEntity.setThreshold(3);
        amountEntity.setDiscountValue(new BigDecimal("5.99"));

        DiscountPolicyModel amountModel = DiscountPolicyModel.builder()
                .id(validId)
                .type(DiscountType.AMOUNT)
                .threshold(3)
                .value(new BigDecimal("5.99"))
                .build();

        when(discountPolicyRepository.findByTypeAndThresholdAndDiscountValue(
                DiscountType.AMOUNT, 3, new BigDecimal("5.99")))
                .thenReturn(Optional.empty());
        when(discountPolicyRepository.save(any(DiscountPolicyEntity.class))).thenReturn(amountEntity);
        when(discountPolicyMapper.entityToModel(amountEntity)).thenReturn(amountModel);

        DiscountPolicyModel result = discountPolicyService.addDiscountPolicy(
                DiscountType.AMOUNT, 3, new BigDecimal("5.99"));

        assertNotNull(result);
        assertEquals(DiscountType.AMOUNT, result.getType());
        assertEquals(3, result.getThreshold());
        assertEquals(0, new BigDecimal("5.99").compareTo(result.getValue()));
        verify(discountPolicyRepository).save(any(DiscountPolicyEntity.class));
    }

    @Test
    void addDiscountPolicy_WithExistingPolicy_ShouldThrowException() {
        when(discountPolicyRepository.findByTypeAndThresholdAndDiscountValue(
                DiscountType.PERCENTAGE, 5, new BigDecimal("10.99")))
                .thenReturn(Optional.of(validEntity));

        assertThrows(DiscountPolicyAlreadyExistsException.class, () ->
                discountPolicyService.addDiscountPolicy(
                        DiscountType.PERCENTAGE, 5, new BigDecimal("10.99")));
        verify(discountPolicyRepository, never()).save(any());
    }

    @Test
    void getDiscountPolicy_WithExistingId_ShouldReturnPolicy() {
        when(discountPolicyRepository.findById(validId)).thenReturn(Optional.of(validEntity));
        when(discountPolicyMapper.entityToModel(validEntity)).thenReturn(validModel);

        DiscountPolicyModel result = discountPolicyService.getDiscountPolicy(validId);

        assertNotNull(result);
        assertEquals(validId, result.getId());
        assertEquals(DiscountType.PERCENTAGE, result.getType());
        assertEquals(5, result.getThreshold());
        assertEquals(0, new BigDecimal("10.99").compareTo(result.getValue()));
    }

    @Test
    void getDiscountPolicy_WithNonExistentId_ShouldThrowException() {
        when(discountPolicyRepository.findById(validId)).thenReturn(Optional.empty());

        assertThrows(DiscountPolicyNotFoundException.class, () ->
                discountPolicyService.getDiscountPolicy(validId));
    }

    @Test
    void getAllDiscountPolicies_WithEmptyRepository_ShouldReturnEmptyList() {
        Page<DiscountPolicyEntity> emptyPage = new PageImpl<>(List.of());
        when(discountPolicyRepository.findAll(any(PageRequest.class))).thenReturn(emptyPage);

        List<DiscountPolicyModel> result = discountPolicyService.getAllDiscountPolicies(PageRequest.of(0, 10));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllDiscountPolicies_WithOnePolicy_ShouldReturnListWithOneElement() {
        Page<DiscountPolicyEntity> page = new PageImpl<>(List.of(validEntity));
        when(discountPolicyRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(discountPolicyMapper.entityToModel(validEntity)).thenReturn(validModel);

        List<DiscountPolicyModel> result = discountPolicyService.getAllDiscountPolicies(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(validId, result.get(0).getId());
    }

    @Test
    void getAllDiscountPolicies_WithMultiplePolicies_ShouldReturnAllPolicies() {
        DiscountPolicyEntity secondEntity = new DiscountPolicyEntity();
        secondEntity.setId(UUID.randomUUID());
        secondEntity.setType(DiscountType.AMOUNT);
        secondEntity.setThreshold(3);
        secondEntity.setDiscountValue(new BigDecimal("5.99"));

        DiscountPolicyModel secondModel = DiscountPolicyModel.builder()
                .id(secondEntity.getId())
                .type(DiscountType.AMOUNT)
                .threshold(3)
                .value(new BigDecimal("5.99"))
                .build();

        Page<DiscountPolicyEntity> page = new PageImpl<>(List.of(validEntity, secondEntity));
        when(discountPolicyRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(discountPolicyMapper.entityToModel(validEntity)).thenReturn(validModel);
        when(discountPolicyMapper.entityToModel(secondEntity)).thenReturn(secondModel);

        List<DiscountPolicyModel> result = discountPolicyService.getAllDiscountPolicies(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getAllDiscountPolicies_ShouldRespectPagination() {
        PageRequest pageRequest = PageRequest.of(1, 5);
        Page<DiscountPolicyEntity> page = new PageImpl<>(List.of(validEntity));
        when(discountPolicyRepository.findAll(pageRequest)).thenReturn(page);
        when(discountPolicyMapper.entityToModel(validEntity)).thenReturn(validModel);

        List<DiscountPolicyModel> result = discountPolicyService.getAllDiscountPolicies(pageRequest);

        assertNotNull(result);
        verify(discountPolicyRepository).findAll(pageRequest);
    }

    @Test
    void updateDiscountPolicy_WithValidData_ShouldReturnUpdatedPolicy() {
        when(discountPolicyRepository.findById(validId)).thenReturn(Optional.of(validEntity));
        when(discountPolicyRepository.findByTypeAndThresholdAndDiscountValue(
                DiscountType.PERCENTAGE, 10, new BigDecimal("15.99")))
                .thenReturn(Optional.empty());
        when(discountPolicyRepository.save(any(DiscountPolicyEntity.class))).thenReturn(validEntity);
        when(discountPolicyMapper.entityToModel(validEntity)).thenReturn(validModel);

        DiscountPolicyModel result = discountPolicyService.updateDiscountPolicy(
                validId, 10, new BigDecimal("15.99"));

        assertNotNull(result);
        verify(discountPolicyRepository).save(any(DiscountPolicyEntity.class));
    }

    @Test
    void updateDiscountPolicy_WithExistingPolicy_ShouldThrowException() {
        DiscountPolicyEntity existingEntity = new DiscountPolicyEntity();
        existingEntity.setId(UUID.randomUUID());
        existingEntity.setType(DiscountType.PERCENTAGE);
        existingEntity.setThreshold(10);
        existingEntity.setDiscountValue(new BigDecimal("15.99"));

        when(discountPolicyRepository.findById(validId)).thenReturn(Optional.of(validEntity));
        when(discountPolicyRepository.findByTypeAndThresholdAndDiscountValue(
                DiscountType.PERCENTAGE, 10, new BigDecimal("15.99")))
                .thenReturn(Optional.of(existingEntity));

        assertThrows(DiscountPolicyAlreadyExistsException.class, () ->
                discountPolicyService.updateDiscountPolicy(validId, 10, new BigDecimal("15.99")));
        verify(discountPolicyRepository, never()).save(any());
    }

    @Test
    void updateDiscountPolicy_WithNonExistentId_ShouldThrowException() {
        when(discountPolicyRepository.findById(validId)).thenReturn(Optional.empty());

        assertThrows(DiscountPolicyNotFoundException.class, () ->
                discountPolicyService.updateDiscountPolicy(validId, 10, new BigDecimal("15.99")));
        verify(discountPolicyRepository, never()).save(any());
    }

    @Test
    void deleteDiscountPolicy_WithExistingId_ShouldDeleteSuccessfully() {
        doNothing().when(discountPolicyRepository).deleteById(validId);

        assertDoesNotThrow(() -> discountPolicyService.deleteDiscountPolicy(validId));
        verify(discountPolicyRepository).deleteById(validId);
    }
}
