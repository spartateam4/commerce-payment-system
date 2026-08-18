package com.sparta.team4.commerce_payment_system.domain.cart.repository;

import com.sparta.team4.commerce_payment_system.domain.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // TODO: CartItem과 Product를 cart_id, product_id로 찾기
    // 힌트: findBy + 엔티티.필드 + And + 엔티티.필드
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    // TODO: Cart의 Member ID로 모든 CartItem 찾기
    List<CartItem> findByCartMemberId(Long memberId);
}