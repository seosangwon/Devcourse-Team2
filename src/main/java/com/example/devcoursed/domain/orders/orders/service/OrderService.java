package com.example.devcoursed.domain.orders.orders.service;

import com.example.devcoursed.domain.orders.exception.OrderNotFound;
import com.example.devcoursed.domain.orders.orderItem.entity.OrderItem;
import com.example.devcoursed.domain.orders.orderItem.request.OrderItemCreate;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.orders.orders.repository.OrderRepository;
import com.example.devcoursed.domain.orders.orders.request.OrdersRequest;
import com.example.devcoursed.domain.orders.orders.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void register(OrdersRequest ordersRequest){

    }


    public OrderResponse get(Long orderId){


        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(RuntimeException::new);

        return new OrderResponse(orders.getId(), orders.getOrderItems());
    }

    public void remove(Long id) {

        Orders orders = orderRepository.findById(id)
                .orElseThrow(OrderNotFound::new);

        orderRepository.delete(orders);
    }
}
