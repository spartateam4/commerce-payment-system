package com.sparta.team4.commerce_payment_system.domain.payment.entity;

public enum PaymentStatus {
    PENDING, // 대기
    COMPLETED, // 결제 성공
    FAILED, // 결제 실패
    CANCELLED // 결제 취소
}
