package com.o2c.order.application.exception;

import java.util.Map;

public class ValidationException extends RuntimeException {

    private final Map<String, Object> errors;

    public ValidationException(String message, Map<String, Object> errors) {
        super(message);
        this.errors = errors;
    }

    public Map<String, Object> errors() {
        return errors;
    }
}