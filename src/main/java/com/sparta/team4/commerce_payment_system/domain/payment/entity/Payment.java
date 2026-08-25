package com.sparta.team4.commerce_payment_system.domain.payment.entity;

import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import com.sparta.team4.commerce_payment_system.global.common.entity.BaseEntity;
import com.sparta.team4.commerce_payment_system.global.exception.CustomException;
import com.sparta.team4.commerce_payment_system.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public Payment(Order order, Integer amount) {
        this.order = order;
        this.amount = amount;
        this.status = PaymentStatus.PENDING; // 일단 "결제대기" 상태로 생성
    }

    public void complete() {
        changeStatus(PaymentStatus.COMPLETED);
        this.paidAt = LocalDateTime.now();
    }

    public void fail() {
        changeStatus(PaymentStatus.FAILED);
    }

    public void cancel() {
        changeStatus(PaymentStatus.CANCELLED);
    }

    private void changeStatus(PaymentStatus newStatus) {
        if (!this.status.canTransitTo(newStatus))
            throw new CustomException(ErrorCode.INVALID_PAYMENT_STATUS);

        this.status = newStatus;
    }
}
