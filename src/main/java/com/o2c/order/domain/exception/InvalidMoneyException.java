package com.o2c.order.domain.exception;

import java.util.Map;

public final class InvalidMoneyException extends DomainException{

    public InvalidMoneyException(String message, Map<String, Object> details) {
        super("MONEY_INVALID", message, details);
    }

    public InvalidMoneyException(String message) {
        super("MONEY_INVALID", message, Map.of());
    }
}
