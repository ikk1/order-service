package com.o2c.order.adapters.outbound.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2c.order.adapters.outbound.kafka.model.EventEnvelope;
import com.o2c.order.adapters.outbound.kafka.model.OrderCreatedEvent;
import com.o2c.order.adapters.outbound.kafka.model.OrderItemEvent;
import com.o2c.order.application.port.out.PublishOrderEventPort;
import com.o2c.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaOrderEventProducer implements PublishOrderEventPort {

    private static final String TOPIC = "o2c.order.v1";

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publishOrderCreated(Order order, String correlationId) {
        try {
            var payload = new OrderCreatedEvent(
                    order.id(),
                    order.customerId(),
                    order.items().stream()
                            .map(i -> new OrderItemEvent(i.sku(), i.quantity()))
                            .toList(),
                    order.total().amount(),
                    order.total().currency()
            );

            var envelope = new EventEnvelope<>(
                    UUID.randomUUID().toString(),
                    "OrderCreated",
                    order.id(),
                    Instant.now(),
                    correlationId,
                    null,
                    "order-service",
                    Map.of(),
                    payload
            );

            byte[] json = objectMapper.writeValueAsBytes(envelope);

            kafkaTemplate.send(TOPIC, order.id(), json);

        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize OrderCreated event", e);
        }
    }
}
