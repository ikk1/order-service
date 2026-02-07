package com.o2c.order.adapters.outbound.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2c.order.adapters.outbound.kafka.model.EventEnvelope;
import com.o2c.order.adapters.outbound.kafka.model.OrderCreatedEvent;
import com.o2c.order.adapters.outbound.kafka.model.OrderItemEvent;
import com.o2c.order.application.port.out.PublishOrderEventPort;
import com.o2c.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderEventProducer implements PublishOrderEventPort {

    private static final String EVENT_TYPE = "OrderCreated";

    @Value("${o2c.kafka.topics.order-events:o2c.order.v1}")
    private String TOPIC;


    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publishOrderCreated(Order order, String correlationId) {
        String eventId = UUID.randomUUID().toString();
        long startNs = System.nanoTime();

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
                    eventId,
                    EVENT_TYPE,
                    order.id(),
                    Instant.now(),
                    correlationId,
                    null,
                    "order-service",
                    Map.of(),
                    payload
            );

            byte[] json = objectMapper.writeValueAsBytes(envelope);

            ProducerRecord<String, byte[]> record = new ProducerRecord<>(TOPIC, order.id(), json);

            // Headers (para tracing/idempotência no consumer)
            addHeader(record, "eventId", eventId);
            addHeader(record, "correlationId", correlationId);
            addHeader(record, "eventType", EVENT_TYPE);
            addHeader(record, "aggregateId", order.id());
            addHeader(record, "producer", "order-service");

            CompletableFuture<SendResult<String, byte[]>> future = kafkaTemplate.send(record);

            future.whenComplete((result, ex) -> {
                long durationMs = (System.nanoTime() - startNs) / 1_000_000;

                if (ex != null) {
                    log.error(
                            "kafka_publish_failed eventType={} topic={} orderId={} eventId={} correlationId={} durationMs={} error={}",
                            EVENT_TYPE, TOPIC, order.id(), eventId, correlationId, durationMs, ex, ex
                    );
                    return;
                }

                var meta = result.getRecordMetadata();
                log.info(
                        "kafka_publish_ok eventType={} topic={} partition={} offset={} orderId={} eventId={} correlationId={} durationMs={}",
                        EVENT_TYPE, meta.topic(), meta.partition(), meta.offset(),
                        order.id(), eventId, correlationId, durationMs
                );
            });

        } catch (Exception e) {
            log.error(
                    "kafka_publish_failed eventType={} topic={} orderId={} eventId={} correlationId={} error={}",
                    EVENT_TYPE, TOPIC, order.id(), eventId, correlationId, e, e
            );
            throw new RuntimeException("Failed to publish OrderCreated event", e);
        }
    }

    private static void addHeader(ProducerRecord<String, byte[]> record, String key, String value) {
        if (value == null) return;
        record.headers().add(key, value.getBytes(StandardCharsets.UTF_8));
    }
}
