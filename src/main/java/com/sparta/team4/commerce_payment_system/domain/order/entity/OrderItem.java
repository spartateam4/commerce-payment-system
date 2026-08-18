package com.sparta.team4.commerce_payment_system.domain.order.entity;

import com.sparta.team4.commerce_payment_system.domain.product.Product;
import com.sparta.team4.commerce_payment_system.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;
    @Column(name = "order_price", nullable = false)
    private Integer orderPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Builder
    public OrderItem(Product product, String productName, Integer orderPrice, Integer quantity) {
        this.product = product;
        this.productName = productName;
        this.orderPrice = orderPrice;
        this.quantity = quantity;
    }

    public void assignOrder(Order order) {
        this.order = order;
    }
}