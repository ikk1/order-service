package com.o2c.order.adapters.inbound.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OrderItemDto(@NotBlank String sku,
                           @Min(1) int quantity) {
}
