package com.o2c.order.application.port.out;

import com.o2c.order.domain.model.Order;

public interface PublishOrderEventPort {
    void publishOrderCreated(Order order, String correlationId);
}
