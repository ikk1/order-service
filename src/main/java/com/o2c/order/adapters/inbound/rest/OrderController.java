package com.o2c.order.adapters.inbound.rest;

import com.o2c.order.adapters.inbound.rest.dto.CreateOrderRequest;
import com.o2c.order.adapters.inbound.rest.dto.CreateOrderResponse;
import com.o2c.order.adapters.inbound.rest.mapper.OrderApiMapper;
import com.o2c.order.application.usecase.CreateOrderUseCase;
import com.o2c.order.shared.CorrelationId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUseCase useCase;

    public OrderController(CreateOrderUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> create(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestHeader(value = CorrelationId.HEADER, required = false) String correlationId
    ) {
        String cid = CorrelationId.getOrCreate(correlationId);
        var result = useCase.execute(OrderApiMapper.toCommand(request, cid));

        return ResponseEntity.accepted().body(new CreateOrderResponse(
                result.orderId(),
                result.status(),
                result.correlationId()
        ));
    }
}
