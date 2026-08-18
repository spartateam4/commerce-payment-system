package com.sparta.team4.commerce_payment_system.domain.order.entity;

public enum OrderStatus {
    PAYMENT_PENDING, // 결제 대기
    COMPLETED,       // 주문 완료 (결제 성공)
    CANCELLED        // 주문 취소
}