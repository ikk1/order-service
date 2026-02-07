package com.o2c.order.application.usecase;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderCommand(
        String customerId,
        List<Item> items,
        BigDecimal totalAmount,
        String currency,
        String correlationId
) {
    public record Item(String sku, int quantity) {
    }
}
