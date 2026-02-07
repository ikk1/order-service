package com.o2c.order.config;

import com.o2c.order.application.port.out.PublishOrderEventPort;
import com.o2c.order.application.port.out.SkuCatalogPort;
import com.o2c.order.application.port.out.SupportedCurrencyPort;
import com.o2c.order.application.service.CreateOrderService;
import com.o2c.order.application.usecase.CreateOrderUseCase;
import com.o2c.order.domain.service.OrderDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationWiring {

    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainService();
    }

    @Bean
    public CreateOrderUseCase createOrderUseCase(
            PublishOrderEventPort publishPort,
            SupportedCurrencyPort supportedCurrencyPort,
            SkuCatalogPort skuCatalogPort,
            OrderDomainService domainService
    ) {
        return new CreateOrderService(publishPort, supportedCurrencyPort, skuCatalogPort, domainService);
    }
}
