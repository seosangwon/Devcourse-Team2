package com.example.devcoursed.domain.orders.orders.contorller;

import com.example.devcoursed.domain.orders.orders.dto.OrderDTO;
import com.example.devcoursed.domain.orders.orders.dto.OrderSummaryDTO;
import com.example.devcoursed.domain.orders.orders.service.OrderService;
import com.example.devcoursed.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Map<String, String> createOrder(@RequestBody OrderDTO orderDTO, @AuthenticationPrincipal SecurityUser user) {
        long memberId = user.getId();
        orderService.createOrder(orderDTO, memberId);

        return Map.of("success", "create");
    }
    // 주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long orderId) {
        OrderDTO orderDTO = orderService.read(orderId);
        return ResponseEntity.ok(orderDTO);
    }
    // 주문 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Page<OrderDTO.OrderListDTO>> getList(@Validated OrderDTO.PageRequestDTO pageRequestDTO
    ,@AuthenticationPrincipal SecurityUser user){
        long memberId = user.getId();
        return ResponseEntity.ok(orderService.getList(pageRequestDTO, memberId));
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    public Map<String, String> deleteOrder(@PathVariable Long orderId) {
        orderService.delete(orderId);
        return Map.of("success", "delete");
    }

    //주문 월별 그래프 조회
    @GetMapping("/monthly-summary")
    public ResponseEntity<List<OrderDTO.OrderListDTO>> getMonthlyOrderSummary(@AuthenticationPrincipal SecurityUser user) {
        long memberId = user.getId();
        return ResponseEntity.ok(orderService.getpList(memberId));
    }
}
