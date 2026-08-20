package com.sparta.team4.commerce_payment_system.domain.cart.repository;

import com.sparta.team4.commerce_payment_system.domain.cart.entity.Cart;

import java.util.Optional;

public interface CartRepositoryCustom {
    Optional<Cart> findByMemberId(Long memberId);
}
