package com.shop.demo.api.v1.discountpolicy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.demo.api.v1.advice.GlobalExceptionHandler;
import com.shop.demo.api.v1.discountpolicy.dto.AddDiscountPolicyRequest;
import com.shop.demo.api.v1.discountpolicy.dto.DiscountPolicyResponse;
import com.shop.demo.api.v1.discountpolicy.dto.UpdateDiscountPolicyRequest;
import com.shop.demo.persistence.discountpolicy.entity.DiscountType;
import com.shop.demo.service.discountpolicy.DiscountPolicyService;
import com.shop.demo.service.discountpolicy.exception.DiscountPolicyNotFoundException;
import com.shop.demo.service.discountpolicy.mapper.DiscountPolicyMapper;
import com.shop.demo.service.discountpolicy.model.DiscountPolicyModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
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
class DiscountPolicyControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private DiscountPolicyService discountPolicyService;

    @Mock
    private DiscountPolicyMapper discountPolicyMapper;

    private UUID validPolicyId;
    private DiscountPolicyModel policyModel;
    private DiscountPolicyResponse policyResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(new DiscountPolicyController(discountPolicyService, discountPolicyMapper))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        validPolicyId = UUID.randomUUID();
        
        policyModel = DiscountPolicyModel.builder()
                .id(validPolicyId)
                .type(DiscountType.PERCENTAGE)
                .threshold(5)
                .value(new BigDecimal("10.0"))
                .build();

        policyResponse = DiscountPolicyResponse.builder()
                .id(validPolicyId)
                .type(DiscountType.PERCENTAGE)
                .threshold(5)
                .value(new BigDecimal("10.0"))
                .build();
    }

    @Test
    void getDiscountPolicy_WithValidId_ShouldReturnPolicy() throws Exception {
        when(discountPolicyService.getDiscountPolicy(validPolicyId)).thenReturn(policyModel);
        when(discountPolicyMapper.modelToResponse(policyModel)).thenReturn(policyResponse);

        mockMvc.perform(get("/v1/discount-policies/{id}", validPolicyId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validPolicyId.toString()))
                .andExpect(jsonPath("$.type").value("PERCENTAGE"))
                .andExpect(jsonPath("$.threshold").value(5))
                .andExpect(jsonPath("$.value").value("10.0"));
    }

    @Test
    void getDiscountPolicy_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        when(discountPolicyService.getDiscountPolicy(validPolicyId))
                .thenThrow(new DiscountPolicyNotFoundException("Policy not found"));

        mockMvc.perform(get("/v1/discount-policies/{id}", validPolicyId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllDiscountPolicies_ShouldReturnPagedPolicies() throws Exception {
        List<DiscountPolicyModel> policies = List.of(policyModel);
        List<DiscountPolicyResponse> responses = List.of(policyResponse);

        when(discountPolicyService.getAllDiscountPolicies(any(PageRequest.class))).thenReturn(policies);
        when(discountPolicyMapper.modelToResponse(policies)).thenReturn(responses);

        mockMvc.perform(get("/v1/discount-policies")
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(validPolicyId.toString()))
                .andExpect(jsonPath("$[0].type").value("PERCENTAGE"))
                .andExpect(jsonPath("$[0].threshold").value(5))
                .andExpect(jsonPath("$[0].value").value("10.0"));
    }

    @Test
    void addDiscountPolicy_WithValidPercentageRequest_ShouldReturnCreatedPolicy() throws Exception {
        AddDiscountPolicyRequest request = new AddDiscountPolicyRequest();
        request.setType(DiscountType.PERCENTAGE);
        request.setThreshold(5);
        request.setValue(new BigDecimal("11.99"));

        DiscountPolicyModel model = DiscountPolicyModel.builder()
                .id(validPolicyId)
                .type(DiscountType.PERCENTAGE)
                .threshold(5)
                .value(new BigDecimal("11.99"))
                .build();

        DiscountPolicyResponse response = DiscountPolicyResponse.builder()
                .id(validPolicyId)
                .type(DiscountType.PERCENTAGE)
                .threshold(5)
                .value(new BigDecimal("11.99"))
                .build();

        when(discountPolicyService.addDiscountPolicy(eq(DiscountType.PERCENTAGE), eq(5), any(BigDecimal.class)))
                .thenReturn(model);
        when(discountPolicyMapper.modelToResponse(model)).thenReturn(response);

        mockMvc.perform(post("/v1/discount-policies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validPolicyId.toString()))
                .andExpect(jsonPath("$.type").value("PERCENTAGE"))
                .andExpect(jsonPath("$.value").value("11.99"));
    }

    @Test
    void addDiscountPolicy_WithValidAmountRequest_ShouldReturnCreatedPolicy() throws Exception {
        AddDiscountPolicyRequest request = new AddDiscountPolicyRequest();
        request.setType(DiscountType.AMOUNT);
        request.setThreshold(3);
        request.setValue(new BigDecimal("5.99"));

        DiscountPolicyModel amountModel = DiscountPolicyModel.builder()
                .id(validPolicyId)
                .type(DiscountType.AMOUNT)
                .threshold(3)
                .value(new BigDecimal("5.99"))
                .build();

        DiscountPolicyResponse amountResponse = DiscountPolicyResponse.builder()
                .id(validPolicyId)
                .type(DiscountType.AMOUNT)
                .threshold(3)
                .value(new BigDecimal("5.99"))
                .build();

        when(discountPolicyService.addDiscountPolicy(eq(DiscountType.AMOUNT), eq(3), any(BigDecimal.class)))
                .thenReturn(amountModel);
        when(discountPolicyMapper.modelToResponse(amountModel)).thenReturn(amountResponse);

        mockMvc.perform(post("/v1/discount-policies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("AMOUNT"))
                .andExpect(jsonPath("$.value").value("5.99"));
    }

    @Test
    void addDiscountPolicy_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        AddDiscountPolicyRequest request = new AddDiscountPolicyRequest();
        request.setType(null);
        request.setThreshold(0);
        request.setValue(BigDecimal.ZERO);

        mockMvc.perform(post("/v1/discount-policies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDiscountPolicy_WithValidRequest_ShouldReturnUpdatedPolicy() throws Exception {
        UpdateDiscountPolicyRequest request = new UpdateDiscountPolicyRequest();
        request.setThreshold(10);
        request.setValue(new BigDecimal("15.99"));

        DiscountPolicyModel updatedModel = DiscountPolicyModel.builder()
                .id(validPolicyId)
                .type(DiscountType.PERCENTAGE)
                .threshold(10)
                .value(new BigDecimal("15.99"))
                .build();

        DiscountPolicyResponse updatedResponse = DiscountPolicyResponse.builder()
                .id(validPolicyId)
                .type(DiscountType.PERCENTAGE)
                .threshold(10)
                .value(new BigDecimal("15.99"))
                .build();

        when(discountPolicyService.updateDiscountPolicy(eq(validPolicyId), eq(10), any(BigDecimal.class)))
                .thenReturn(updatedModel);
        when(discountPolicyMapper.modelToResponse(updatedModel)).thenReturn(updatedResponse);

        mockMvc.perform(put("/v1/discount-policies/{id}", validPolicyId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validPolicyId.toString()))
                .andExpect(jsonPath("$.value").value("15.99"));
    }

    @Test
    void updateDiscountPolicy_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        UpdateDiscountPolicyRequest request = new UpdateDiscountPolicyRequest();
        request.setThreshold(10);
        request.setValue(new BigDecimal("15.99"));

        when(discountPolicyService.updateDiscountPolicy(eq(validPolicyId), eq(10), any(BigDecimal.class)))
                .thenThrow(new DiscountPolicyNotFoundException("Policy not found"));

        mockMvc.perform(put("/v1/discount-policies/{id}", validPolicyId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateDiscountPolicy_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        UpdateDiscountPolicyRequest request = new UpdateDiscountPolicyRequest();
        request.setThreshold(0);
        request.setValue(BigDecimal.ZERO);

        mockMvc.perform(put("/v1/discount-policies/{id}", validPolicyId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteDiscountPolicy_WithValidId_ShouldReturnOk() throws Exception {
        doNothing().when(discountPolicyService).deleteDiscountPolicy(validPolicyId);

        mockMvc.perform(delete("/v1/discount-policies/{id}", validPolicyId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(discountPolicyService).deleteDiscountPolicy(validPolicyId);
    }

    @Test
    void deleteDiscountPolicy_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        doThrow(new DiscountPolicyNotFoundException("Policy not found"))
                .when(discountPolicyService).deleteDiscountPolicy(validPolicyId);

        mockMvc.perform(delete("/v1/discount-policies/{id}", validPolicyId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
