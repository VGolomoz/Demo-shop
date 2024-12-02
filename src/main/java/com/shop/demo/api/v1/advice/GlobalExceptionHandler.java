package com.shop.demo.api.v1.advice;

import com.shop.demo.api.v1.advice.dto.ErrorResponse;
import com.shop.demo.exception.AbstractShopDemoException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

import static com.shop.demo.exception.ServiceErrorCode.FIELD_VALIDATION;

//@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {AbstractShopDemoException.class})
    public ResponseEntity<ErrorResponse> handleShopDemoException(AbstractShopDemoException ex) {
        return ResponseEntity
                .status(ex.getErrorStatus())
                .body(buildErrorResponse(ex));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var responseBody = ErrorResponse.builder()
                .errorCode(FIELD_VALIDATION.getCode())
                .errorMessage(ex.getBindingResult().getFieldErrors().stream()
                        .map(error -> String.format("%s:%s", error.getField(), error.getDefaultMessage()))
                        .collect(Collectors.joining(";")))
                .timestamp(Instant.now().toEpochMilli())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseBody);
    }

    private ErrorResponse buildErrorResponse(AbstractShopDemoException ex) {
        return ErrorResponse.builder()
                .errorCode(ex.getErrorCode().getCode())
                .errorMessage(ex.getMessage())
                .timestamp(Instant.now().toEpochMilli())
                .build();
    }
}
