package com.sparta.team4.commerce_payment_system.domain.cart.repository;

import com.sparta.team4.commerce_payment_system.domain.cart.Cart;
import com.sparta.team4.commerce_payment_system.domain.cart.CartItem;
import com.sparta.team4.commerce_payment_system.domain.product.Product;
import java.util.Optional;

public interface CartItemRepositoryCustom {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    Optional<CartItem> findByIdAndCart_MemberId(Long cartItemId, Long memberId);
}