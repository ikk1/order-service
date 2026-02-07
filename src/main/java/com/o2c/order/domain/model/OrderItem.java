package com.o2c.order.domain.model;

import com.o2c.order.domain.exception.InvalidOrderItemException;
import com.o2c.order.domain.validation.SkuValidator;

public record OrderItem(String sku, int quantity) {

    public OrderItem {
        sku = SkuValidator.normalizeAndValidate(sku);

        if (quantity <= 0) {
            throw new InvalidOrderItemException("quantity must be greater than zero",
                    java.util.Map.of("quantity", quantity));
        }
    }
}
