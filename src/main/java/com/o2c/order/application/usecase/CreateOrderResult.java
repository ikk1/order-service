package com.o2c.order.application.usecase;

import com.o2c.order.domain.model.OrderStatus;

public record CreateOrderResult(
        String orderId,
        OrderStatus status,
        String correlationId
) {}
