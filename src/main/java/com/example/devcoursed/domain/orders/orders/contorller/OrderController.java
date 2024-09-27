package com.example.devcoursed.domain.orders.orders.contorller;

import com.example.devcoursed.domain.member.member.Exception.MemberException;
import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.member.member.service.MemberService;
import com.example.devcoursed.domain.orders.orders.dto.OrderDTO;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.orders.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberRepository memberRepository;

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO, @RequestParam Long memberId) {
        // memberId는 로그인된 사용자 정보를 받아와야 함
        // memberService로 멤버 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException);
        Orders newOrder = orderService.createOrder(orderDTO, member);
        return ResponseEntity.ok(new OrderDTO(newOrder));
    }

    // 주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long orderId) {
        OrderDTO orderDTO = orderService.read(orderId);
        return ResponseEntity.ok(orderDTO);
    }
    // 주문 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Page<OrderDTO.OrderListDTO>> getList(@Validated OrderDTO.PageRequestDTO pageRequestDTO){
        return ResponseEntity.ok(orderService.getList(pageRequestDTO));
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.delete(orderId);
        return ResponseEntity.noContent().build();
    }
}
