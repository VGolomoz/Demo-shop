package com.shop.demo.api.v1.discountprocessing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.demo.api.v1.advice.GlobalExceptionHandler;
import com.shop.demo.api.v1.discountprocessing.dto.CalculateDiscountRequest;
import com.shop.demo.service.discountprocessing.DiscountProcessingService;
import com.shop.demo.service.discountprocessing.model.DiscountProcessingResult;
import com.shop.demo.service.product.excpetion.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DiscountProcessingControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private DiscountProcessingService discountProcessingService;

    private UUID validProductId;
    private CalculateDiscountRequest validRequest;
    private DiscountProcessingResult successResult;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(new DiscountProcessingController(discountProcessingService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        validProductId = UUID.randomUUID();
        
        validRequest = new CalculateDiscountRequest();
        validRequest.setProductId(validProductId);
        validRequest.setQuantity(5);

        successResult = DiscountProcessingResult.builder()
                .totalPrice(new BigDecimal("99.99"))
                .discount(new BigDecimal("19.99"))
                .finalPrice(new BigDecimal("80.0"))
                .build();
    }

    @Test
    void calculate_WithValidRequest_ShouldReturnDiscountResult() throws Exception {
        when(discountProcessingService.calculate(eq(validProductId), eq(5)))
                .thenReturn(successResult);

        mockMvc.perform(post("/v1/discount-processing/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value("99.99"))
                .andExpect(jsonPath("$.discount").value("19.99"))
                .andExpect(jsonPath("$.finalPrice").value("80.0"));
    }

    @Test
    void calculate_WithNoApplicableDiscounts_ShouldReturnZeroDiscount() throws Exception {
        DiscountProcessingResult noDiscountResult = DiscountProcessingResult.builder()
                .totalPrice(new BigDecimal("99.99"))
                .discount(BigDecimal.ZERO)
                .finalPrice(new BigDecimal("99.99"))
                .build();

        when(discountProcessingService.calculate(eq(validProductId), eq(5)))
                .thenReturn(noDiscountResult);

        mockMvc.perform(post("/v1/discount-processing/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value("99.99"))
                .andExpect(jsonPath("$.discount").value("0"))
                .andExpect(jsonPath("$.finalPrice").value("99.99"));
    }

    @Test
    void calculate_WithNullProductId_ShouldReturnBadRequest() throws Exception {
        CalculateDiscountRequest invalidRequest = new CalculateDiscountRequest();
        invalidRequest.setQuantity(5);

        mockMvc.perform(post("/v1/discount-processing/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculate_WithZeroQuantity_ShouldReturnBadRequest() throws Exception {
        CalculateDiscountRequest invalidRequest = new CalculateDiscountRequest();
        invalidRequest.setProductId(validProductId);
        invalidRequest.setQuantity(0);

        mockMvc.perform(post("/v1/discount-processing/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculate_WithNegativeQuantity_ShouldReturnBadRequest() throws Exception {
        CalculateDiscountRequest invalidRequest = new CalculateDiscountRequest();
        invalidRequest.setProductId(validProductId);
        invalidRequest.setQuantity(-1);

        mockMvc.perform(post("/v1/discount-processing/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculate_WithNonExistentProduct_ShouldReturnNotFound() throws Exception {
        when(discountProcessingService.calculate(eq(validProductId), eq(5)))
                .thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(post("/v1/discount-processing/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound());
    }
}
