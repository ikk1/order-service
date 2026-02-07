package com.o2c.order.domain.validation;

import com.o2c.order.domain.exception.InvalidMoneyException;

import java.util.Locale;
import java.util.regex.Pattern;

public final class CurrencyValidator {
    private CurrencyValidator() {
    }

    private static final Pattern ISO_4217 = Pattern.compile("^[A-Z]{3}$");

    public static String normalizeAndValidate(String rawCurrency) {
        if (rawCurrency == null || rawCurrency.isBlank()) {
            throw new InvalidMoneyException("currency must be provided");
        }

        String cur = rawCurrency.trim().toUpperCase(Locale.ROOT);

        if (!ISO_4217.matcher(cur).matches()) {
            throw new InvalidMoneyException("currency must be a 3-letter ISO code",
                    java.util.Map.of("currency", cur, "pattern", ISO_4217.pattern()));
        }

        return cur;
    }
}
