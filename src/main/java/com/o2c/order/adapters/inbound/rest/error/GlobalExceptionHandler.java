package com.o2c.order.adapters.inbound.rest.error;


import com.o2c.order.application.exception.BusinessException;
import com.o2c.order.shared.CorrelationId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleBeanValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, Object> errors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return build(HttpStatus.BAD_REQUEST, "ValidationError", "Request validation failed",
                req, errors, ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, Object> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(v ->
                errors.put(v.getPropertyPath().toString(), v.getMessage())
        );
        return build(HttpStatus.BAD_REQUEST, "ValidationError", "Constraint violation",
                req, errors, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "MalformedJson", "Malformed JSON request",
                req, Map.of(), ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessException ex, HttpServletRequest req) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.code(), "Business rule violation",
                req, Map.of(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "InternalError", "Unexpected error",
                req, Map.of(), ex.getMessage());
    }

    private ResponseEntity<ApiError> build(HttpStatus status,
                                           String type,
                                           String title,
                                           HttpServletRequest req,
                                           Map<String, Object> errors,
                                           String detail) {

        String correlationId = CorrelationId.getOrCreate(req.getHeader(CorrelationId.HEADER));

        ApiError body = new ApiError(
                "https://errors.o2c.dev/" + type,
                title,
                status.value(),
                detail,
                req.getRequestURI(),
                correlationId,
                Instant.now(),
                errors
        );
        return ResponseEntity.status(status).body(body);
    }
}