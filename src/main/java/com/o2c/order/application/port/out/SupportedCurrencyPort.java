package com.o2c.order.application.port.out;

public interface SupportedCurrencyPort {
    boolean isSupported(String currency);
}
