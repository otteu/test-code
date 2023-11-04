package dev.example.api.controller.order;

import dev.example.api.controller.order.request.OrderCreateRequest;
import dev.example.api.service.order.OrderService;
import dev.example.api.service.order.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public OrderResponse createOrder(@RequestBody OrderCreateRequest request) {
        LocalDateTime registeredDateTime = LocalDateTime.now();

        // 1. 상품 번호
        return orderService.createOrder(request.toServiceRequest(), registeredDateTime);
    }
}
