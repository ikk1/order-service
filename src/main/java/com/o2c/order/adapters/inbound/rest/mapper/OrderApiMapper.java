package com.o2c.order.adapters.inbound.rest.mapper;

import com.o2c.order.adapters.inbound.rest.dto.CreateOrderRequest;
import com.o2c.order.application.usecase.CreateOrderCommand;

public final class OrderApiMapper {
    private OrderApiMapper() {
    }

    public static CreateOrderCommand toCommand(CreateOrderRequest req, String correlationId) {
        var items = req.items().stream()
                .map(i -> new CreateOrderCommand.Item(i.sku(), i.quantity()))
                .toList();

        return new CreateOrderCommand(
                req.customerId(),
                items,
                req.totalAmount(),
                req.currency(),
                correlationId
        );
    }
}
