package com.sparta.team4.commerce_payment_system.domain.order.entity;

// import com.sparta.team4.commerce_payment_system.domain.product.Product;
import com.sparta.team4.commerce_payment_system.global.common.entity.BaseEntity;
import com.sparta.team4.commerce_payment_system.global.exception.CustomException;
import com.sparta.team4.commerce_payment_system.global.exception.ErrorCode;
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

    /*  product 연동 후 주석 삭제
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;
    */
    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(name = "order_price", nullable = false)
    private Integer orderPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Builder
    public OrderItem(String productName, Integer orderPrice, Integer quantity) { // 연동 후 Product product 추가
        if (orderPrice < 0 || quantity < 1) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        // this.product = product;
        this.productName = productName;
        this.orderPrice = orderPrice;
        this.quantity = quantity;
    }

    public int getSubtotal() {
        return this.orderPrice * this.quantity;
    }

    // 접근제어자로 Order 외부에서 직접 호출 차단
    void assignOrder(Order order) {
        this.order = order;
    }
}