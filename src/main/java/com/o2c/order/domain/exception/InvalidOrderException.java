package com.o2c.order.domain.exception;

import java.util.Map;

public final class InvalidOrderException extends DomainException {

    public InvalidOrderException(String message, Map<String, Object> details) {
        super("ORDER_INVALID", message, details);
    }

    public InvalidOrderException(String message) {
        super("ORDER_INVALID", message, Map.of());
    }
}
