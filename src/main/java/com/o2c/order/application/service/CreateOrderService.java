package com.o2c.order.application.service;

import com.o2c.order.application.exception.BusinessException;
import com.o2c.order.application.port.out.PublishOrderEventPort;
import com.o2c.order.application.port.out.SkuCatalogPort;
import com.o2c.order.application.port.out.SupportedCurrencyPort;
import com.o2c.order.application.usecase.CreateOrderCommand;
import com.o2c.order.application.usecase.CreateOrderResult;
import com.o2c.order.application.usecase.CreateOrderUseCase;
import com.o2c.order.domain.model.Money;
import com.o2c.order.domain.model.Order;
import com.o2c.order.domain.model.OrderItem;
import com.o2c.order.domain.model.OrderStatus;
import com.o2c.order.domain.service.OrderDomainService;

import java.util.Set;
import java.util.stream.Collectors;

public class CreateOrderService implements CreateOrderUseCase {

    private final PublishOrderEventPort publishPort;
    private final SupportedCurrencyPort supportedCurrencyPort;
    private final SkuCatalogPort skuCatalogPort;
    private final OrderDomainService domainService;

    public CreateOrderService(
            PublishOrderEventPort publishPort,
            SupportedCurrencyPort supportedCurrencyPort,
            SkuCatalogPort skuCatalogPort,
            OrderDomainService domainService
    ) {
        this.publishPort = publishPort;
        this.supportedCurrencyPort = supportedCurrencyPort;
        this.skuCatalogPort = skuCatalogPort;
        this.domainService = domainService;
    }

    @Override
    public CreateOrderResult execute(CreateOrderCommand command) {

        String currency = normalizeUpper(command.currency());

        if (!supportedCurrencyPort.isSupported(currency)) {
            throw new BusinessException(
                    "CURRENCY_NOT_SUPPORTED",
                    "Currency is not supported: " + currency
            );
        }

        Set<String> skus = command.items().stream()
                .map(i -> normalizeUpper(i.sku()))
                .collect(Collectors.toSet());

        var invalidSkus = skuCatalogPort.findInvalidSkus(skus);
        if (!invalidSkus.isEmpty()) {
            throw new BusinessException(
                    "SKU_INVALID",
                    "Invalid SKUs: " + invalidSkus
            );
        }

        var items = command.items().stream()
                .map(i -> new OrderItem(i.sku(), i.quantity()))
                .toList();

        Money total = new Money(command.totalAmount(), currency);

        Order order = domainService.createNew(command.customerId(), items, total);

        publishPort.publishOrderCreated(order, command.correlationId());

        return new CreateOrderResult(order.id(), OrderStatus.PENDING, command.correlationId());
    }

    private static String normalizeUpper(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }
}
