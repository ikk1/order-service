package com.o2c.order.adapters.outbound.catalog;

import com.o2c.order.application.port.out.SkuCatalogPort;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InMemorySkuCatalogAdapter implements SkuCatalogPort {

    // TODO: MVP: catálogo fake. Depois troca por DB/API/cache.
    private final Set<String> activeSkus = Set.of("SKU-1", "SKU-9", "SKU-10");

    @Override
    public Set<String> findInvalidSkus(Set<String> skus) {
        if (skus == null || skus.isEmpty()) return Set.of();

        return skus.stream()
                .map(s -> s == null ? "" : s.trim().toUpperCase())
                .filter(s -> s.isBlank() || !activeSkus.contains(s))
                .collect(Collectors.toSet());
    }
}
