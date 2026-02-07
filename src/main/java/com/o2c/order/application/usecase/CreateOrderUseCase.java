package com.o2c.order.application.usecase;

public interface CreateOrderUseCase {
    CreateOrderResult execute(CreateOrderCommand command);
}
