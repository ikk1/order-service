package com.o2c.order.adapters.inbound.rest.dto;

import com.o2c.order.domain.model.OrderStatus;

public record CreateOrderResponse(
        String orderId,
        OrderStatus status,
        String correlationId
) {}
