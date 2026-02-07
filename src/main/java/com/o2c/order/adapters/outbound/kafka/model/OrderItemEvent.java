package com.o2c.order.adapters.outbound.kafka.model;

public record OrderItemEvent(
        String sku,
        int quantity
) {}
