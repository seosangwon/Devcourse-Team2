package com.example.devcoursed.domain.orders.controller;

import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.member.member.service.MemberService;
import com.example.devcoursed.domain.orders.orderItem.dto.OrderItemDTO;
import com.example.devcoursed.domain.orders.orders.dto.OrderDTO;
import com.example.devcoursed.domain.orders.orders.dto.OrderListDTO;
import com.example.devcoursed.domain.orders.orders.dto.PageRequestDTO;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.orders.orders.service.OrderService;
import com.example.devcoursed.domain.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ProductService productService;

//    // 주문 생성 폼 페이지
//    @GetMapping("/create")
//    public String showCreateOrderForm(Model model) {
//        model.addAttribute("orderDTO", new OrderDTO());  // 빈 주문 DTO를 생성해 폼에 바인딩
//        model.addAttribute("products", productService.getAllProducts()); // 모든 상품 목록 추가
//        return "orderForm";  // 템플릿 이름
//    }

    //시큐리티
//    @PostMapping
//    public ResponseEntity<OrderDTO> createOrder(@ModelAttribute OrderDTO orderDTO, @AuthenticationPrincipal UserDetails userDetails) {
//        Long memberId = userDetails.getId(); // 로그인된 사용자 ID를 가져오는 방법을 사용
//        Member member = memberService.findById(memberId);
//        Orders newOrder = orderService.createOrder(orderDTO, member);
//        return ResponseEntity.ok(new OrderDTO(newOrder));
//    }

//    @PostMapping
//    public ResponseEntity<OrderDTO> createOrder(@ModelAttribute OrderDTO orderDTO, @RequestParam Long memberId) {
//        Member member = memberService.findById(memberId);  // memberService로 멤버 조회
//        Orders newOrder = orderService.createOrder(orderDTO, member);
//        return ResponseEntity.ok(new OrderDTO(newOrder));
//    }

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder( @RequestBody OrderDTO orderDTO, @RequestParam Long memberId) {
        // memberId는 로그인된 사용자 정보를 받아와야 함
        // memberService로 멤버 조회
        Optional<Member> member = memberRepository.findById(memberId);
        Orders newOrder = orderService.createOrder(orderDTO, member.get());
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
    public ResponseEntity<Page<OrderListDTO>> getList(@Validated PageRequestDTO pageRequestDTO){
        return ResponseEntity.ok(orderService.getList(pageRequestDTO));
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.delete(orderId);
        return ResponseEntity.noContent().build();
    }
}
