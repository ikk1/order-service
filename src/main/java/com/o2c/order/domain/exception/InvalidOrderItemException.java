package com.o2c.order.domain.exception;

import java.util.Map;

public class InvalidOrderItemException extends DomainException {

    public InvalidOrderItemException(String message) {
        super("ORDER_ITEM_INVALID", message, Map.of());
    }

    public InvalidOrderItemException(String message, Map<String, Object> details) {
        super("ORDER_ITEM_INVALID", message, details);
    }
}
