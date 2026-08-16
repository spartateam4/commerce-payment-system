package com.sparta.team4.commerce_payment_system.domain.cart.repository;

import com.sparta.team4.commerce_payment_system.domain.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long>, CartItemRepositoryCustom {
    // 기본 CRUD 기능(JpaRepository) + QueryDSL 기능(CartItemRepositoryCustom)이 하나로 합쳐진다
}