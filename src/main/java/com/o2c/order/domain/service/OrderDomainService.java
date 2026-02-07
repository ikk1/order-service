package com.o2c.order.domain.service;

import com.o2c.order.domain.exception.InvalidOrderException;
import com.o2c.order.domain.model.Money;
import com.o2c.order.domain.model.Order;
import com.o2c.order.domain.model.OrderItem;
import com.o2c.order.domain.model.OrderStatus;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OrderDomainService {

    private final Clock clock;

    public OrderDomainService() {
        this(Clock.systemUTC());
    }

    public OrderDomainService(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public Order createNew(String customerId, List<OrderItem> items, Money total) {
        if (customerId == null || customerId.isBlank()) {
            throw new InvalidOrderException("customerId must be provided");
        }
        if (items == null || items.isEmpty()) {
            throw new InvalidOrderException("items must not be empty");
        }

        String id = UUID.randomUUID().toString();
        Instant now = Instant.now(clock);

        return new Order(id, customerId, items, total, now, OrderStatus.PENDING);
    }
}
