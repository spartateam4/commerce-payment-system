package com.sparta.team4.commerce_payment_system.domain.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    @NotNull(message = "주문 상품 목록은 필수입니다.")
    private List<OrderItemRequest> orderItems;

    @Getter
    @NoArgsConstructor
    public static class OrderItemRequest {
        @NotNull(message = "상품 ID는 필수입니다.")
        private Long productId;

        @Min(value = 1, message = "주문 수량은 1개 이상이어야 합니다.")
        private Integer quantity;
    }
}