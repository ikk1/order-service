package com.o2c.order.adapters.outbound.kafka.model;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreatedEvent(
        String orderId,
        String customerId,
        List<OrderItemEvent> items,
        BigDecimal totalAmount,
        String currency
) {}
