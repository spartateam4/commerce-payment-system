package com.sparta.team4.commerce_payment_system.domain.payment.dto;

import java.time.LocalDateTime;

public record PaymentResponse(Long paymentId, Long orderId, Integer stock, Integer amount,
                              String pay_status, String order_status, LocalDateTime paid_at) {
}
