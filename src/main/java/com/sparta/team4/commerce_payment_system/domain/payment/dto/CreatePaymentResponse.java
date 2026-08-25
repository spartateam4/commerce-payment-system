package com.sparta.team4.commerce_payment_system.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

public record CreatePaymentResponse(Long paymentId, Long orderId,
                                    List<ProductStock> stock,
                                    Integer amount,
                                    @JsonProperty("pay_status") PaymentStatus paymentStatus,
                                    @JsonProperty("order_status") OrderStatus orderStatus,
                                    @JsonProperty("paid_at") LocalDateTime paidAt) {
    public static CreatePaymentResponse from(Payment payment) {

        List<ProductStock> stocks = payment.getOrder().getOrderItems().stream()
                .map(item -> new ProductStock(
                        item.getProduct().getId(),
                        item.getProduct().getStockQuantity()))
                .toList();

        return new CreatePaymentResponse(payment.getId(), payment.getOrder().getId(),
                stocks, payment.getAmount(), payment.getStatus(),
                payment.getOrder().getStatus(), payment.getPaidAt());
    }

    public record ProductStock(Long productId, Integer stock) {}
}
