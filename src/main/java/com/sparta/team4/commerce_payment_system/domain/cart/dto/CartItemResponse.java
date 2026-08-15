package com.sparta.team4.commerce_payment_system.domain.cart.dto;

public record CartItemResponse (
        Long cartItemId,                 // ← 상품별 고유 ID
        Long productId,
        String productName,
        int price,
        int quantity,
        int total
){
}
