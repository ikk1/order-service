package com.o2c.order.adapters.inbound.rest.error;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        String correlationId,
        Instant timestamp,
        Map<String, Object> errors
) {}
