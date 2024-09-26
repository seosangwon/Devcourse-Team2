package com.example.devcoursed.domain.orders.orders.contorller;

import com.example.devcoursed.domain.orders.orders.request.OrdersRequest;
import com.example.devcoursed.domain.orders.orders.response.OrderResponse;
import com.example.devcoursed.domain.orders.orders.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public void createOrder(@RequestBody @Valid OrdersRequest ordersRequest){
        orderService.register(ordersRequest);
    }

    @GetMapping("/orders/{id}")
    public OrderResponse get(@PathVariable Long id) {
        return orderService.get(id);
    }

    @DeleteMapping("/orders/{id}")
    public void delete(@PathVariable Long id) {
        orderService.remove(id);
    }
}
