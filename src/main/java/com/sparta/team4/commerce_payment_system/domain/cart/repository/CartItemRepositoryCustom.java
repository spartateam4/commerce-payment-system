package com.sparta.team4.commerce_payment_system.domain.cart.repository;

import com.sparta.team4.commerce_payment_system.domain.cart.entity.Cart;
import com.sparta.team4.commerce_payment_system.domain.cart.entity.CartItem;
import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;

import java.util.List;
import java.util.Optional;

public interface CartItemRepositoryCustom {

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    List<CartItem> findByCartMemberId(Long memberId);
}