package com.sparta.team4.commerce_payment_system.domain.order.entity;

import com.sparta.team4.commerce_payment_system.domain.member.entity.Member;
import com.sparta.team4.commerce_payment_system.global.common.entity.BaseEntity;
import com.sparta.team4.commerce_payment_system.global.exception.CustomException;
import com.sparta.team4.commerce_payment_system.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "order_number", unique = true, nullable = false, length = 20)
    private String orderNumber;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Order(Member member, String orderNumber, Integer totalAmount, OrderStatus status) {
        if (totalAmount < 0) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        this.member = member;
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        // 초기 상태 (null로 들어올 경우)
        this.status = status != null ? status : OrderStatus.PAYMENT_PENDING;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }

    public void cancelOrder() {
        changeStatus(OrderStatus.CANCELLED);
    }

    public void completeOrder() {
        changeStatus(OrderStatus.COMPLETED);
    }

    // 상태 전이 규칙
    private void changeStatus(OrderStatus newStatus) {
        if (!this.status.canTransitTo(newStatus)) {
            // 추후 ErrorCode에 INVALID_ORDER_STATUS 추가
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        this.status = newStatus;
    }
}