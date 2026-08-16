package com.sparta.team4.commerce_payment_system.domain.cart.repository;

import com.sparta.team4.commerce_payment_system.domain.cart.Cart;

import java.util.Optional;

public interface CartRepositoryCustom {
    Optional<Cart> findByMemberId(Long memberId);
    // 추가로 필요한 복잡한 쿼리들...
}
