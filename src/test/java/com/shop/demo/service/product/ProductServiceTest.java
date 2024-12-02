package com.shop.demo.service.product;

import com.shop.demo.persistence.discountpolicy.entity.DiscountPolicyEntity;
import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import com.shop.demo.persistence.discountpolicy.repository.DiscountPolicyRepository;
import com.shop.demo.persistence.product.entity.ProductEntity;
import com.shop.demo.persistence.product.repository.ProductRepository;
import com.shop.demo.service.discountpolicy.exception.DiscountPolicyNotFoundException;
import com.shop.demo.service.product.excpetion.ProductAlreadyContainsPolicyException;
import com.shop.demo.service.product.excpetion.ProductNotFoundException;
import com.shop.demo.service.product.mapper.ProductMapper;
import com.shop.demo.service.product.model.ProductModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DiscountPolicyRepository discountPolicyRepository;

    @Mock
    private ProductMapper productMapper;

    private ProductService productService;

    private UUID validProductId;
    private UUID validPolicyId;
    private ProductEntity validProductEntity;
    private DiscountPolicyEntity validPolicyEntity;
    private ProductModel validProductModel;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository, productMapper, discountPolicyRepository);

        validProductId = UUID.randomUUID();
        validPolicyId = UUID.randomUUID();

        validPolicyEntity = new DiscountPolicyEntity();
        validPolicyEntity.setId(validPolicyId);
        validPolicyEntity.setType(DiscountType.PERCENTAGE);
        validPolicyEntity.setThreshold(5);
        validPolicyEntity.setDiscountValue(new BigDecimal("10.99"));

        validProductEntity = new ProductEntity();
        validProductEntity.setId(validProductId);
        validProductEntity.setName("Test Product");
        validProductEntity.setPrice(new BigDecimal("99.99"));
        validProductEntity.setDiscountPolicies(new HashSet<>());

        validProductModel = ProductModel.builder()
                .id(validProductId)
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .discountPolicies(List.of())
                .build();
    }

    @Test
    void createProduct_WithValidData_ShouldReturnCreatedProduct() {
        when(productRepository.save(any(ProductEntity.class))).thenReturn(validProductEntity);
        when(productMapper.entityToModel(validProductEntity)).thenReturn(validProductModel);

        ProductModel result = productService.createProduct("Test Product", new BigDecimal("99.99"));

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(0, new BigDecimal("99.99").compareTo(result.getPrice()));
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    void getProduct_WithExistingId_ShouldReturnProduct() {
        when(productRepository.findById(validProductId)).thenReturn(Optional.of(validProductEntity));
        when(productMapper.entityToModel(validProductEntity)).thenReturn(validProductModel);

        ProductModel result = productService.getProduct(validProductId);

        assertNotNull(result);
        assertEquals(validProductId, result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals(0, new BigDecimal("99.99").compareTo(result.getPrice()));
    }

    @Test
    void getProduct_WithNonExistentId_ShouldThrowException() {
        when(productRepository.findById(validProductId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                productService.getProduct(validProductId));
    }

    @Test
    void getAllProducts_WithEmptyRepository_ShouldReturnEmptyList() {
        Page<ProductEntity> emptyPage = new PageImpl<>(List.of());
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(emptyPage);

        List<ProductModel> result = productService.getAllProducts(PageRequest.of(0, 10));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllProducts_WithOneProduct_ShouldReturnListWithOneElement() {
        Page<ProductEntity> page = new PageImpl<>(List.of(validProductEntity));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(productMapper.entityToModel(validProductEntity)).thenReturn(validProductModel);

        List<ProductModel> result = productService.getAllProducts(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(validProductId, result.get(0).getId());
    }

    @Test
    void getAllProducts_WithMultipleProducts_ShouldReturnAllProducts() {
        ProductEntity secondEntity = new ProductEntity();
        secondEntity.setId(UUID.randomUUID());
        secondEntity.setName("Second Product");
        secondEntity.setPrice(new BigDecimal("149.99"));
        secondEntity.setDiscountPolicies(new HashSet<>());

        ProductModel secondModel = ProductModel.builder()
                .id(secondEntity.getId())
                .name("Second Product")
                .price(new BigDecimal("149.99"))
                .discountPolicies(List.of())
                .build();

        Page<ProductEntity> page = new PageImpl<>(List.of(validProductEntity, secondEntity));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(productMapper.entityToModel(validProductEntity)).thenReturn(validProductModel);
        when(productMapper.entityToModel(secondEntity)).thenReturn(secondModel);

        List<ProductModel> result = productService.getAllProducts(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getAllProducts_ShouldRespectPagination() {
        PageRequest pageRequest = PageRequest.of(1, 5);
        Page<ProductEntity> page = new PageImpl<>(List.of(validProductEntity));
        when(productRepository.findAll(pageRequest)).thenReturn(page);
        when(productMapper.entityToModel(validProductEntity)).thenReturn(validProductModel);

        List<ProductModel> result = productService.getAllProducts(pageRequest);

        assertNotNull(result);
        verify(productRepository).findAll(pageRequest);
    }

    @Test
    void updateProduct_WithValidData_ShouldReturnUpdatedProduct() {
        when(productRepository.findById(validProductId)).thenReturn(Optional.of(validProductEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(validProductEntity);
        when(productMapper.entityToModel(validProductEntity)).thenReturn(validProductModel);

        ProductModel result = productService.updateProduct(validProductId, "Updated Product", new BigDecimal("149.99"));

        assertNotNull(result);
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    void updateProduct_WithNonExistentId_ShouldThrowException() {
        when(productRepository.findById(validProductId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                productService.updateProduct(validProductId, "Updated Product", new BigDecimal("149.99")));
        verify(productRepository, never()).save(any());
    }

    @Test
    void addDiscountPolicy_WithValidData_ShouldReturnUpdatedProduct() {
        when(productRepository.findById(validProductId)).thenReturn(Optional.of(validProductEntity));
        when(discountPolicyRepository.findById(validPolicyId)).thenReturn(Optional.of(validPolicyEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(validProductEntity);
        when(productMapper.entityToModel(validProductEntity)).thenReturn(validProductModel);

        ProductModel result = productService.addDiscountPolicy(validProductId, validPolicyId);

        assertNotNull(result);
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    void addDiscountPolicy_WithNonExistentProduct_ShouldThrowException() {
        when(productRepository.findById(validProductId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                productService.addDiscountPolicy(validProductId, validPolicyId));
        verify(productRepository, never()).save(any());
    }

    @Test
    void addDiscountPolicy_WithNonExistentPolicy_ShouldThrowException() {
        when(productRepository.findById(validProductId)).thenReturn(Optional.of(validProductEntity));
        when(discountPolicyRepository.findById(validPolicyId)).thenReturn(Optional.empty());

        assertThrows(DiscountPolicyNotFoundException.class, () ->
                productService.addDiscountPolicy(validProductId, validPolicyId));
        verify(productRepository, never()).save(any());
    }

    @Test
    void addDiscountPolicy_WithExistingPolicy_ShouldThrowException() {
        validProductEntity.getDiscountPolicies().add(validPolicyEntity);

        when(productRepository.findById(validProductId)).thenReturn(Optional.of(validProductEntity));
        when(discountPolicyRepository.findById(validPolicyId)).thenReturn(Optional.of(validPolicyEntity));

        assertThrows(ProductAlreadyContainsPolicyException.class, () ->
                productService.addDiscountPolicy(validProductId, validPolicyId));
        verify(productRepository, never()).save(any());
    }

    @Test
    void removeDiscountPolicy_WithValidData_ShouldReturnUpdatedProduct() {
        validProductEntity.getDiscountPolicies().add(validPolicyEntity);

        when(productRepository.findById(validProductId)).thenReturn(Optional.of(validProductEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(validProductEntity);
        when(productMapper.entityToModel(validProductEntity)).thenReturn(validProductModel);

        ProductModel result = productService.removeDiscountPolicy(validProductId, validPolicyId);

        assertNotNull(result);
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    void removeDiscountPolicy_WithNonExistentProduct_ShouldThrowException() {
        when(productRepository.findById(validProductId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                productService.removeDiscountPolicy(validProductId, validPolicyId));
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_WithExistingId_ShouldDeleteSuccessfully() {
        doNothing().when(productRepository).deleteById(validProductId);

        assertDoesNotThrow(() -> productService.deleteProduct(validProductId));
        verify(productRepository).deleteById(validProductId);
    }
}
