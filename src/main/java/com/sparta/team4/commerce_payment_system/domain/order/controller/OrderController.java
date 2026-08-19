package com.sparta.team4.commerce_payment_system.domain.order.controller;

import com.sparta.team4.commerce_payment_system.domain.order.dto.*;
import com.sparta.team4.commerce_payment_system.domain.order.facade.OrderFacade;
import com.sparta.team4.commerce_payment_system.domain.order.service.OrderService;
import com.sparta.team4.commerce_payment_system.global.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderFacade orderFacade;

    // 1. 신규 주문 생성
    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(
            @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
            @Valid @RequestBody OrderCreateRequest request
    ) {
        // 복합 트랜잭션 -> Facade 호출
        OrderCreateResponse response = orderFacade.createOrder(userPrincipal.getMemberId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. 내 주문 목록 페이징 조회
    @GetMapping
    public ResponseEntity<Page<OrderCreateResponse>> getMyOrders(
            @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        // 단일 조회 로직 -> Service 직접 호출
        Page<OrderCreateResponse> response = orderService.getMyOrders(userPrincipal.getMemberId(), pageable);
        return ResponseEntity.ok(response);
    }

    // 3. 주문 상세 내역 조회
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(
            @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
            @PathVariable("id") Long orderId
    ) {
        // 단일 조회 로직 -> Service 직접 호출
        OrderDetailResponse response = orderService.getOrderDetail(userPrincipal.getMemberId(), orderId);
        return ResponseEntity.ok(response);
    }

    // 4. 결제 전 주문 취소
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderCancelResponse> cancelOrder(
            @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
            @PathVariable("id") Long orderId,
            @Valid @RequestBody OrderCancelRequest request
    ) {
        // 복합 트랜잭션 -> Facade 호출
        OrderCancelResponse response = orderFacade.cancelOrder(userPrincipal.getMemberId(), orderId, request);
        return ResponseEntity.ok(response);
    }
}