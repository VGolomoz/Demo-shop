package com.shop.demo.service.discountprocessing;

import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import com.shop.demo.service.discountpolicy.model.DiscountPolicyModel;
import com.shop.demo.service.discountprocessing.factory.DiscountCalculationFactory;
import com.shop.demo.service.discountprocessing.model.DiscountProcessingResult;
import com.shop.demo.service.discountprocessing.strategy.DiscountCalculationStrategy;
import com.shop.demo.service.product.ProductService;
import com.shop.demo.service.product.excpetion.ProductNotFoundException;
import com.shop.demo.service.product.model.ProductModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountProcessingServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private DiscountCalculationFactory discountCalculationFactory;

    @Mock
    private DiscountCalculationStrategy percentageStrategy;

    @Mock
    private DiscountCalculationStrategy amountStrategy;

    private DiscountProcessingService discountProcessingService;

    private UUID validProductId;
    private ProductModel productWithoutDiscounts;
    private ProductModel productWithOnePercentageDiscount;
    private ProductModel productWithOneAmountDiscount;
    private ProductModel productWithMultipleDiscounts;
    private ProductModel productWithMultiplePercentageDiscounts;
    private ProductModel productWithLargeDiscounts;

    @BeforeEach
    void setUp() {
        discountProcessingService = new DiscountProcessingServiceImpl(productService, discountCalculationFactory);

        validProductId = UUID.randomUUID();

        productWithoutDiscounts = ProductModel.builder()
                .id(validProductId)
                .name("Product without discounts")
                .price(new BigDecimal("100.00"))
                .discountPolicies(List.of())
                .build();

        var percentageDiscount = DiscountPolicyModel.builder()
                .id(UUID.randomUUID())
                .type(DiscountType.PERCENTAGE)
                .threshold(5)
                .value(new BigDecimal("10.00"))
                .build();
        productWithOnePercentageDiscount = ProductModel.builder()
                .id(validProductId)
                .name("Product with percentage discount")
                .price(new BigDecimal("100.00"))
                .discountPolicies(List.of(percentageDiscount))
                .build();

        var amountDiscount = DiscountPolicyModel.builder()
                .id(UUID.randomUUID())
                .type(DiscountType.AMOUNT)
                .threshold(3)
                .value(new BigDecimal("5.00"))
                .build();
        productWithOneAmountDiscount = ProductModel.builder()
                .id(validProductId)
                .name("Product with amount discount")
                .price(new BigDecimal("100.00"))
                .discountPolicies(List.of(amountDiscount))
                .build();

        productWithMultipleDiscounts = ProductModel.builder()
                .id(validProductId)
                .name("Product with multiple discounts")
                .price(new BigDecimal("100.00"))
                .discountPolicies(List.of(percentageDiscount, amountDiscount))
                .build();

        var smallPercentageDiscount = DiscountPolicyModel.builder()
                .id(UUID.randomUUID())
                .type(DiscountType.PERCENTAGE)
                .threshold(3)
                .value(new BigDecimal("5.00"))
                .build();
        var largePercentageDiscount = DiscountPolicyModel.builder()
                .id(UUID.randomUUID())
                .type(DiscountType.PERCENTAGE)
                .threshold(10)
                .value(new BigDecimal("15.00"))
                .build();
        productWithMultiplePercentageDiscounts = ProductModel.builder()
                .id(validProductId)
                .name("Product with multiple percentage discounts")
                .price(new BigDecimal("100.00"))
                .discountPolicies(List.of(smallPercentageDiscount, largePercentageDiscount))
                .build();

        var largePercentageDiscountPolicy = DiscountPolicyModel.builder()
                .id(UUID.randomUUID())
                .type(DiscountType.PERCENTAGE)
                .threshold(1)
                .value(new BigDecimal("60.00"))
                .build();
        var largeAmountDiscountPolicy = DiscountPolicyModel.builder()
                .id(UUID.randomUUID())
                .type(DiscountType.AMOUNT)
                .threshold(1)
                .value(new BigDecimal("50.00"))
                .build();
        productWithLargeDiscounts = ProductModel.builder()
                .id(validProductId)
                .name("Product with large discounts")
                .price(new BigDecimal("100.00"))
                .discountPolicies(List.of(largePercentageDiscountPolicy, largeAmountDiscountPolicy))
                .build();
    }

    @Test
    void calculate_WithNoDiscounts_ShouldReturnOriginalPrice() {
        when(productService.getProduct(validProductId)).thenReturn(productWithoutDiscounts);

        DiscountProcessingResult result = discountProcessingService.calculate(validProductId, 5);

        assertNotNull(result);
        assertEquals(0, new BigDecimal("500.00").compareTo(result.getTotalPrice()));
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getDiscount()));
        assertEquals(0, new BigDecimal("500.00").compareTo(result.getFinalPrice()));
    }

    @Test
    void calculate_WithOnePercentageDiscount_ShouldApplyDiscount() {
        when(productService.getProduct(validProductId)).thenReturn(productWithOnePercentageDiscount);
        when(discountCalculationFactory.getStrategy(DiscountType.PERCENTAGE)).thenReturn(percentageStrategy);
        when(percentageStrategy.calculateDiscount(new BigDecimal("500.00"), new BigDecimal("10.00")))
                .thenReturn(new BigDecimal("50.00"));

        DiscountProcessingResult result = discountProcessingService.calculate(validProductId, 5);

        assertNotNull(result);
        assertEquals(0, new BigDecimal("500.00").compareTo(result.getTotalPrice()));
        assertEquals(0, new BigDecimal("50.00").compareTo(result.getDiscount()));
        assertEquals(0, new BigDecimal("450.00").compareTo(result.getFinalPrice()));
    }

    @Test
    void calculate_WithOneAmountDiscount_ShouldApplyDiscount() {
        when(productService.getProduct(validProductId)).thenReturn(productWithOneAmountDiscount);
        when(discountCalculationFactory.getStrategy(DiscountType.AMOUNT)).thenReturn(amountStrategy);
        when(amountStrategy.calculateDiscount(new BigDecimal("500.00"), new BigDecimal("5.00")))
                .thenReturn(new BigDecimal("5.00"));

        DiscountProcessingResult result = discountProcessingService.calculate(validProductId, 5);

        assertNotNull(result);
        assertEquals(0, new BigDecimal("500.00").compareTo(result.getTotalPrice()));
        assertEquals(0, new BigDecimal("5.00").compareTo(result.getDiscount()));
        assertEquals(0, new BigDecimal("495.00").compareTo(result.getFinalPrice()));
    }

    @Test
    void calculate_WithMultipleDiscounts_ShouldApplyBothDiscounts() {
        when(productService.getProduct(validProductId)).thenReturn(productWithMultipleDiscounts);
        when(discountCalculationFactory.getStrategy(DiscountType.PERCENTAGE)).thenReturn(percentageStrategy);
        when(discountCalculationFactory.getStrategy(DiscountType.AMOUNT)).thenReturn(amountStrategy);
        when(percentageStrategy.calculateDiscount(new BigDecimal("500.00"), new BigDecimal("10.00")))
                .thenReturn(new BigDecimal("50.00"));
        when(amountStrategy.calculateDiscount(new BigDecimal("500.00"), new BigDecimal("5.00")))
                .thenReturn(new BigDecimal("5.00"));

        DiscountProcessingResult result = discountProcessingService.calculate(validProductId, 5);

        assertNotNull(result);
        assertEquals(0, new BigDecimal("500.00").compareTo(result.getTotalPrice()));
        assertEquals(0, new BigDecimal("55.00").compareTo(result.getDiscount()));
        assertEquals(0, new BigDecimal("445.00").compareTo(result.getFinalPrice()));
    }

    @Test
    void calculate_WithMultiplePercentageDiscounts_ShouldApplyHighestThresholdDiscount() {
        when(productService.getProduct(validProductId)).thenReturn(productWithMultiplePercentageDiscounts);
        when(discountCalculationFactory.getStrategy(DiscountType.PERCENTAGE)).thenReturn(percentageStrategy);
        when(percentageStrategy.calculateDiscount(new BigDecimal("1000.00"), new BigDecimal("15.00")))
                .thenReturn(new BigDecimal("150.00"));

        DiscountProcessingResult result = discountProcessingService.calculate(validProductId, 10);

        assertNotNull(result);
        assertEquals(0, new BigDecimal("1000.00").compareTo(result.getTotalPrice()));
        assertEquals(0, new BigDecimal("150.00").compareTo(result.getDiscount()));
        assertEquals(0, new BigDecimal("850.00").compareTo(result.getFinalPrice()));
        verify(percentageStrategy, never()).calculateDiscount(any(), eq(new BigDecimal("5.00")));
    }

    @Test
    void calculate_WithQuantityBelowThreshold_ShouldNotApplyDiscount() {
        when(productService.getProduct(validProductId)).thenReturn(productWithOnePercentageDiscount);

        DiscountProcessingResult result = discountProcessingService.calculate(validProductId, 3);

        assertNotNull(result);
        assertEquals(0, new BigDecimal("300.00").compareTo(result.getTotalPrice()));
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getDiscount()));
        assertEquals(0, new BigDecimal("300.00").compareTo(result.getFinalPrice()));
        verify(discountCalculationFactory, never()).getStrategy(any());
    }

    @Test
    void calculate_WithNonExistentProduct_ShouldThrowException() {
        when(productService.getProduct(validProductId)).thenThrow(new ProductNotFoundException("Product not found"));

        assertThrows(ProductNotFoundException.class, () ->
                discountProcessingService.calculate(validProductId, 5));
    }

    @Test
    void calculate_WithDiscountsExceedingTotalPrice_ShouldReturnZeroFinalPrice() {
        when(productService.getProduct(validProductId)).thenReturn(productWithLargeDiscounts);
        when(discountCalculationFactory.getStrategy(DiscountType.PERCENTAGE)).thenReturn(percentageStrategy);
        when(discountCalculationFactory.getStrategy(DiscountType.AMOUNT)).thenReturn(amountStrategy);
        when(percentageStrategy.calculateDiscount(new BigDecimal("100.00"), new BigDecimal("60.00")))
                .thenReturn(new BigDecimal("60.00"));
        when(amountStrategy.calculateDiscount(new BigDecimal("100.00"), new BigDecimal("50.00")))
                .thenReturn(new BigDecimal("50.00"));

        DiscountProcessingResult result = discountProcessingService.calculate(validProductId, 1);

        assertNotNull(result);
        assertEquals(0, new BigDecimal("100.00").compareTo(result.getTotalPrice()));
        assertEquals(0, new BigDecimal("110.00").compareTo(result.getDiscount()));
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getFinalPrice()));
    }
}
