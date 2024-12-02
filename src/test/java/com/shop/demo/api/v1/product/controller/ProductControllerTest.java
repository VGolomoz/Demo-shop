package com.shop.demo.api.v1.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.demo.api.v1.advice.GlobalExceptionHandler;
import com.shop.demo.api.v1.product.dto.CreateProductRequest;
import com.shop.demo.api.v1.product.dto.ProductResponse;
import com.shop.demo.api.v1.product.dto.UpdateProductRequest;
import com.shop.demo.service.product.ProductService;
import com.shop.demo.service.product.excpetion.ProductAlreadyContainsPolicyException;
import com.shop.demo.service.product.excpetion.ProductNotFoundException;
import com.shop.demo.service.product.mapper.ProductMapper;
import com.shop.demo.service.product.model.ProductModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    private UUID validProductId;
    private ProductModel productModel;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ProductController(productService, productMapper))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        validProductId = UUID.randomUUID();
        
        productModel = ProductModel.builder()
                .id(validProductId)
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .discountPolicies(List.of())
                .build();

        productResponse = ProductResponse.builder()
                .id(validProductId)
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .discountPolicies(List.of())
                .build();
    }

    @Test
    void getProduct_WithValidId_ShouldReturnProduct() throws Exception {
        when(productService.getProduct(validProductId)).thenReturn(productModel);
        when(productMapper.modelToResponse(productModel)).thenReturn(productResponse);

        mockMvc.perform(get("/v1/products/{id}", validProductId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validProductId.toString()))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value("99.99"));
    }

    @Test
    void getProduct_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        when(productService.getProduct(validProductId))
                .thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/v1/products/{id}", validProductId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllProducts_ShouldReturnPagedProducts() throws Exception {
        List<ProductModel> products = List.of(productModel);
        List<ProductResponse> responses = List.of(productResponse);

        when(productService.getAllProducts(any())).thenReturn(products);
        when(productMapper.modelToResponse(products)).thenReturn(responses);

        mockMvc.perform(get("/v1/products")
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(validProductId.toString()))
                .andExpect(jsonPath("$[0].name").value("Test Product"))
                .andExpect(jsonPath("$[0].price").value("99.99"));
    }

    @Test
    void createProduct_WithValidRequest_ShouldReturnCreatedProduct() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("New Product");
        request.setPrice(new BigDecimal("149.99"));

        when(productService.createProduct(eq("New Product"), any(BigDecimal.class)))
                .thenReturn(productModel);
        when(productMapper.modelToResponse(productModel)).thenReturn(productResponse);

        mockMvc.perform(post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validProductId.toString()));
    }

    @Test
    void createProduct_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("");
        request.setPrice(BigDecimal.ZERO);

        mockMvc.perform(post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProduct_WithValidRequest_ShouldReturnUpdatedProduct() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Updated Product");
        request.setPrice(new BigDecimal("199.99"));

        when(productService.updateProduct(eq(validProductId), eq("Updated Product"), any(BigDecimal.class)))
                .thenReturn(productModel);
        when(productMapper.modelToResponse(productModel)).thenReturn(productResponse);

        mockMvc.perform(put("/v1/products/{id}", validProductId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validProductId.toString()));
    }

    @Test
    void updateProduct_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Updated Product");
        request.setPrice(new BigDecimal("199.99"));

        when(productService.updateProduct(eq(validProductId), any(), any()))
                .thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(put("/v1/products/{id}", validProductId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_WithValidId_ShouldReturnOk() throws Exception {
        doNothing().when(productService).deleteProduct(validProductId);

        mockMvc.perform(delete("/v1/products/{id}", validProductId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService).deleteProduct(validProductId);
    }

    @Test
    void deleteProduct_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        doThrow(new ProductNotFoundException("Product not found"))
                .when(productService).deleteProduct(validProductId);

        mockMvc.perform(delete("/v1/products/{id}", validProductId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void addDiscountPolicy_WithValidIds_ShouldReturnUpdatedProduct() throws Exception {
        UUID discountPolicyId = UUID.randomUUID();

        when(productService.addDiscountPolicy(validProductId, discountPolicyId))
                .thenReturn(productModel);
        when(productMapper.modelToResponse(productModel)).thenReturn(productResponse);

        mockMvc.perform(post("/v1/products/{productId}/discount-policies/{policyId}", 
                validProductId, discountPolicyId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validProductId.toString()));
    }

    @Test
    void addDiscountPolicy_WithExistingPolicy_ShouldReturnNotAcceptable() throws Exception {
        UUID discountPolicyId = UUID.randomUUID();

        when(productService.addDiscountPolicy(validProductId, discountPolicyId))
                .thenThrow(new ProductAlreadyContainsPolicyException("Policy already exists"));

        mockMvc.perform(post("/v1/products/{productId}/discount-policies/{policyId}", 
                validProductId, discountPolicyId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void removeDiscountPolicy_WithValidIds_ShouldReturnUpdatedProduct() throws Exception {
        UUID discountPolicyId = UUID.randomUUID();

        when(productService.removeDiscountPolicy(validProductId, discountPolicyId))
                .thenReturn(productModel);
        when(productMapper.modelToResponse(productModel)).thenReturn(productResponse);

        mockMvc.perform(delete("/v1/products/{productId}/discount-policies/{policyId}", 
                validProductId, discountPolicyId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validProductId.toString()));
    }

    @Test
    void removeDiscountPolicy_WithNonExistentProduct_ShouldReturnNotFound() throws Exception {
        UUID discountPolicyId = UUID.randomUUID();

        when(productService.removeDiscountPolicy(validProductId, discountPolicyId))
                .thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(delete("/v1/products/{productId}/discount-policies/{policyId}", 
                validProductId, discountPolicyId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
