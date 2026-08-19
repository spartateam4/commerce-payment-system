package com.sparta.team4.commerce_payment_system.domain.order.service;

import com.sparta.team4.commerce_payment_system.domain.member.entity.Member;
import com.sparta.team4.commerce_payment_system.domain.order.dto.OrderCreateResponse;
import com.sparta.team4.commerce_payment_system.domain.order.dto.OrderDetailResponse;
import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderItem;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import com.sparta.team4.commerce_payment_system.domain.order.repository.OrderRepository;
import com.sparta.team4.commerce_payment_system.global.exception.CustomException;
import com.sparta.team4.commerce_payment_system.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;


   //  1. 신규 주문 생성 (Facade에서 엔티티를 저장)

    @Transactional
    public Order createOrder(Member member, List<OrderItem> orderItems, int totalAmount) {
        Order order = Order.builder()
                .member(member)
                .orderNumber(generateOrderNumber())
                .totalAmount(totalAmount)
                .status(OrderStatus.PAYMENT_PENDING)
                .build();

        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
        }

        return orderRepository.save(order);
    }


    //  2. 내 주문 목록 조회 (QueryDSL 적용)

    public Page<OrderCreateResponse> getMyOrders(Long memberId, Pageable pageable) {
        Page<Order> orders = orderRepository.findMyOrders(memberId, pageable);
        return orders.map(OrderCreateResponse::new);
    }


    //  3. 주문 상세 내역 조회

    public OrderDetailResponse getOrderDetail(Long memberId, Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 소유권 검증
        if (!order.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        return new OrderDetailResponse(order);
    }


    //  4. 결제 전 주문 취소 (주문 상태 변경)


    @Transactional
    public Order cancelOrder(Long memberId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        // 엔티티 내부에서 상태 변경 (PAYMENT_PENDING 내부적으로 동작)
        order.cancelOrder();

        return order;
    }

    private String generateOrderNumber() {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return datePrefix + "-" + randomSuffix;
    }
}