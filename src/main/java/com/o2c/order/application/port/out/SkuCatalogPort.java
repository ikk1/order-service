package com.o2c.order.application.port.out;

import java.util.Set;

public interface SkuCatalogPort {
    /**
     * Returns the subset of SKUs that are invalid/nonexistent/inactive.
     * This avoids N calls per item.
     */
    Set<String> findInvalidSkus(Set<String> skus);
}
