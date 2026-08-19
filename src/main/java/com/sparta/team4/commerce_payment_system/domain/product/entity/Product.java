package com.sparta.team4.commerce_payment_system.domain.product.entity;

import com.sparta.team4.commerce_payment_system.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder
    private Product(
            String name,
            String category,
            int price,
            int stockQuantity,
            String description
    ) {
        if (price < 0) {
            throw new IllegalArgumentException("상품 가격은 0 이상이어야 합니다.");
        }

        if (stockQuantity < 0) {
            throw new IllegalArgumentException("상품 재고는 0 이상이어야 합니다.");
        }

        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }

    // 주문 생성 시 재고 선차감
    public void removeStock(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("차감 수량은 1개 이상이어야 합니다.");
        }

        if (this.stockQuantity < quantity) {
            throw new IllegalStateException("상품 재고가 부족합니다.");
        }

        this.stockQuantity -= quantity;
    }

    // 주문 취소 또는 결제 실패/취소 시 재고 복구
    public void addStock(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("복구 수량은 1개 이상이어야 합니다.");
        }

        this.stockQuantity += quantity;
    }
}