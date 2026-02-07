package com.o2c.order.domain.model;

import com.o2c.order.domain.exception.InvalidOrderException;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class Order {

    private final String id;
    private final String customerId;
    private final List<OrderItem> items;
    private final Money total;
    private final Instant createdAt;

    private OrderStatus status;

    public Order(String id,
                 String customerId,
                 List<OrderItem> items,
                 Money total,
                 Instant createdAt,
                 OrderStatus status) {

        this.id = Objects.requireNonNull(id, "id");
        this.customerId = Objects.requireNonNull(customerId, "customerId");
        this.items = List.copyOf(Objects.requireNonNull(items, "items"));
        this.total = Objects.requireNonNull(total, "total");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.status = Objects.requireNonNull(status, "status");

        validate();
    }

    private void validate() {
        if (id.isBlank()) {
            throw new InvalidOrderException("id must be provided");
        }
        if (customerId.isBlank()) {
            throw new InvalidOrderException("customerId must be provided");
        }
        if (items.isEmpty()) {
            throw new InvalidOrderException("items must not be empty");
        }
        long distinctSkuCount = items.stream().map(OrderItem::sku).distinct().count();
        if (distinctSkuCount != items.size()) {
            throw new InvalidOrderException(
                    "items must not contain duplicate SKUs",
                    java.util.Map.of("distinctSkuCount", distinctSkuCount, "itemsCount", items.size())
            );
        }
    }

    public String id() {
        return id;
    }

    public String customerId() {
        return customerId;
    }

    public List<OrderItem> items() {
        return items;
    }

    public Money total() {
        return total;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public OrderStatus status() {
        return status;
    }

    public void markCompleted() {
        if (status == OrderStatus.CANCELLED) {
            throw new InvalidOrderException("cannot complete a cancelled order",
                    java.util.Map.of("orderId", id, "status", status));
        }
        status = OrderStatus.COMPLETED;
    }

    public void cancel(String reason) {
        if (status == OrderStatus.COMPLETED) {
            throw new InvalidOrderException("cannot cancel a completed order",
                    java.util.Map.of("orderId", id, "status", status, "reason", reason));
        }

        if(status == OrderStatus.SHIPPED){
            throw new InvalidOrderException("cannot cancel a shipped order",
                    java.util.Map.of("orderId", id, "status", status, "reason", reason));
        }

        if(status == OrderStatus.DELIVERED){
            throw new InvalidOrderException("cannot cancel a delivered order",
                    java.util.Map.of("orderId", id, "status", status, "reason", reason));
        }

        status = OrderStatus.CANCELLED;
    }
}
