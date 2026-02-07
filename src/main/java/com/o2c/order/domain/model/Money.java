package com.o2c.order.domain.model;

import com.o2c.order.domain.exception.InvalidMoneyException;
import com.o2c.order.domain.validation.CurrencyValidator;

import java.math.BigDecimal;
import java.util.Objects;

public record Money(BigDecimal amount, String currency) {

    public Money {
        Objects.requireNonNull(amount, "amount");

        if (amount.signum() <= 0) {
            throw new InvalidMoneyException("amount must be greater than zero",
                    java.util.Map.of("amount", amount));
        }

        currency = CurrencyValidator.normalizeAndValidate(currency);
    }
}
