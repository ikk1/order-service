package com.o2c.order.domain.exception;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class DomainException extends RuntimeException {

    private final String code;
    private final Map<String, Object> details;

    protected DomainException(String code, String message) {
        this(code, message, Map.of());
    }

    protected DomainException(String code, String message, Map<String, Object> details) {
        super(message);
        this.code = Objects.requireNonNull(code, "code");
        this.details = Collections.unmodifiableMap(Objects.requireNonNull(details, "details"));
    }

    public String code() {
        return code;
    }

    public Map<String, Object> details() {
        return details;
    }
}
