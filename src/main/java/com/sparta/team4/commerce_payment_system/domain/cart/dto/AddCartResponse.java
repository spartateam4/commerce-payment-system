package com.sparta.team4.commerce_payment_system.domain.cart.dto;

public record AddCartResponse(
        Long cartItemId,
        Long productId,
        String productName,
        int price,
        int quantity

) {

}
