package com.sparta.team4.commerce_payment_system.domain.order;

import com.sparta.team4.commerce_payment_system.domain.member.entity.Member;
import com.sparta.team4.commerce_payment_system.global.common.entity.BaseEntity;
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
        this.member = member;
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }

    public void cancelOrder() {
        this.status = OrderStatus.CANCELLED;
    }

    public void completeOrder() {
        this.status = OrderStatus.COMPLETED;
    }
}