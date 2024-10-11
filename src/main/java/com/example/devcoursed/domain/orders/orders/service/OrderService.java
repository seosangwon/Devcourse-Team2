package com.example.devcoursed.domain.orders.orders.service;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.exception.MemberException;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.orders.exception.OrderException;
import com.example.devcoursed.domain.orders.orderItem.entity.OrderItem;
import com.example.devcoursed.domain.orders.orderItem.repository.OrderItemRepository;
import com.example.devcoursed.domain.orders.orders.dto.OrderDTO;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.orders.orders.repository.OrderRepository;
import com.example.devcoursed.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Log4j2
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Orders createOrder(OrderDTO orderDTO, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException);

        memberRepository.save(member);
        Orders orders = orderDTO.toEntity(member, productRepository); // DTO에서 엔티티로 변환
        orderRepository.save(orders);

        return orders;
    }
    public OrderDTO read(Long orderId) {
        Orders orders = orderRepository.findById(orderId).
                orElseThrow(OrderException.NOT_FOUND::get);

        return new OrderDTO(orders);
    }
    @Transactional
    public void delete(Long orderId) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(OrderException.NOT_FOUND::get);
        try {
            orderRepository.delete(orders);
        } catch (Exception e) {
            throw OrderException.NOT_REMOVED.get();
        }
    }
    public Page<OrderDTO.OrderListDTO> getList(OrderDTO.PageRequestDTO pageRequestDTO, Long memberId) {
        try {
            Sort sort = Sort.by("id").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException);
            Page<Orders> ordersPage = orderRepository.findByMember(member,pageable);
            return ordersPage.map(OrderDTO.OrderListDTO::new);
        } catch (Exception e) {
            throw OrderException.NOT_REMOVED.get();
        }

    }
    public List<Map<String, Object>> getMonthlyOrderSummary(long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found")); // 예외 처리

        List<Object[]> results = orderRepository.getMonthlyTotalPrice(member);

        // 결과를 Map 형태로 변환
        return results.stream()
                .map(result -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderMonth", result[0]); // 월
                    map.put("totalPrice", result[1]); // 총 금액
                    return map;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Map<String, Double>> getMonthlyAveragePrices() {
        List<OrderItem> orderItems = orderItemRepository.findAll();

        // 월별 및 상품별 평균 단가 계산
        return orderItems.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getOrders().getCreatedAt().getMonth().name(), // 월 이름
                        Collectors.groupingBy(
                                item -> item.getProduct().getName(), // 상품 이름
                                Collectors.averagingDouble(item -> (double) item.getPrice() / item.getQuantity()) // 평균 단가 계산
                        )
                ));
    }

//    public List<OrderSummaryDTO> getMonthlyOrderSummary(long memberId, String month) {
//
//        return orderRepository.getOrdersSummary(month);
//    }

//    public List<OrderDTO.OrderListDTO> getpList(long memberId) {
//        List<Orders> ordersList = orderRepository.findAll(orderRepository.findById(memberId).get().getMember());
//        List<OrderDTO.OrderListDTO> orderDTOS = new ArrayList<>();
//        for (Orders order : ordersList) {
//            orderDTOS.add(new OrderDTO.OrderListDTO(order));
//        }
//        return orderDTOS;
//    }
}