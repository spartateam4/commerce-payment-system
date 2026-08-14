package com.sparta.team4.commerce_payment_system.domain.payment.entity;

import com.sparta.team4.commerce_payment_system.domain.order.Order;
import com.sparta.team4.commerce_payment_system.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
//@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Order가 아직 JPA 엔티티가 아님
    //@OneToOne(fetch = FetchType.LAZY, optional = false)
    //@JoinColumn(name = "order_id", nullable = false, unique = true)
    //private Order order;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @LastModifiedDate
    //@Column(nullable = false, name = "paid_at")
    private LocalDateTime paidAt;

    public Payment(Order order, Integer amount) {
        //this.order = order;
        this.amount = amount;
        status = PaymentStatus.COMPLETED;
        paidAt = LocalDateTime.now();
    }
}
