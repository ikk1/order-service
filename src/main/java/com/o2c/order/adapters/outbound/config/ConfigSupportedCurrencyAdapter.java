package com.o2c.order.adapters.outbound.config;

import com.o2c.order.application.port.out.SupportedCurrencyPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ConfigSupportedCurrencyAdapter implements SupportedCurrencyPort {

    private final Set<String> supported;

    public ConfigSupportedCurrencyAdapter(@Value("${order.supported-currencies}") Set<String> supported) {
        this.supported = supported.stream()
                .map(s -> s.trim().toUpperCase())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isSupported(String currency) {
        return supported.contains(currency == null ? null : currency.trim().toUpperCase());
    }
}
