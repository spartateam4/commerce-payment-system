package com.sparta.team4.commerce_payment_system.domain.cart.dto;

import java.util.List;

public record CartResponse (
        Long cartId,
        List<CartItemResponse> items,    // ← items 배열!
        int totalAmount
){
}
