package com.o2c.order.shared;

public final class CorrelationId {
    private CorrelationId() {
    }

    public static final String HEADER = "X-Correlation-Id";

    public static String getOrCreate(String value) {
        if (value == null || value.isBlank()) {
            return java.util.UUID.randomUUID().toString();
        }
        return value;
    }
}
