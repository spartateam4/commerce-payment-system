package com.sparta.team4.commerce_payment_system.domain.order.dto;

import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDetailResponse {
    private Long id;
    private String orderNumber;
    private Integer totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> orderItems;

    public OrderDetailResponse(Order order) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class OrderItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private Integer orderPrice;
        private Integer quantity;

        public OrderItemResponse(com.sparta.team4.commerce_payment_system.domain.order.entity.OrderItem item) {
            this.id = item.getId();
            this.productId = item.getProduct().getId();
            this.productName = item.getProductName();
            this.orderPrice = item.getOrderPrice();
            this.quantity = item.getQuantity();
        }
    }
}