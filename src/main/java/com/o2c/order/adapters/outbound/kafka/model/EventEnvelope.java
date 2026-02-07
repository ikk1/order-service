package com.o2c.order.adapters.outbound.kafka.model;

import java.time.Instant;
import java.util.Map;

public record EventEnvelope<T>(
        String eventId,
       String eventType,
       String aggregateId,
       Instant occurredAt,
       String correlationId,
       String causationId,
       String producer,
       Map<String, String> headers,
       T payload) {}
