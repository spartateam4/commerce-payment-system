package com.sparta.team4.commerce_payment_system.domain.cart.entity;

import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import com.sparta.team4.commerce_payment_system.global.common.entity.BaseEntity;
import com.sparta.team4.commerce_payment_system.global.exception.CustomException;
import com.sparta.team4.commerce_payment_system.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "cartItems",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"cart_id", "product_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public CartItem(Cart cart, Product product, int quantity) {
        if (quantity < 1) {
            throw new CustomException(ErrorCode.INVALID_QUANTITY);
        }
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return product.getId();
    }

    // 동일 상품 중복 담기 시 기존 CartItem의 수량 증가
    public void addQuantity(int quantity) {
        if (quantity < 1) {
            throw new CustomException(ErrorCode.INVALID_QUANTITY);
        }
        this.quantity += quantity;
    }

    // 장바구니 상품 수량 변경
    public void changeQuantity(int quantity) {
        if (quantity < 1) {
            throw new CustomException(ErrorCode.INVALID_QUANTITY);
        }
        this.quantity = quantity;
    }
}