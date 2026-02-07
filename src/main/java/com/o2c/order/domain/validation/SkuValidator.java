package com.o2c.order.domain.validation;

import com.o2c.order.domain.exception.InvalidOrderItemException;

import java.util.Locale;
import java.util.regex.Pattern;

public final class SkuValidator {
    private SkuValidator() {
    }

    // A-Z 0-9 - _ . (2..64)
    private static final Pattern SKU_PATTERN = Pattern.compile("^[A-Z0-9][A-Z0-9._-]{0,62}[A-Z0-9]$");

    public static String normalizeAndValidate(String rawSku) {
        if (rawSku == null || rawSku.isBlank()) {
            throw new InvalidOrderItemException("sku must be provided");
        }

        String sku = rawSku.trim().toUpperCase(Locale.ROOT);

        if (sku.length() < 2 || sku.length() > 64) {
            throw new InvalidOrderItemException("sku length must be between 2 and 64",
                    java.util.Map.of("sku", sku, "length", sku.length()));
        }

        if (!SKU_PATTERN.matcher(sku).matches()) {
            throw new InvalidOrderItemException("sku has invalid format",
                    java.util.Map.of("sku", sku, "pattern", SKU_PATTERN.pattern()));
        }

        return sku;
    }
}
