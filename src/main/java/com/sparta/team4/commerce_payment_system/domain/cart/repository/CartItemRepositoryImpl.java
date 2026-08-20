package com.sparta.team4.commerce_payment_system.domain.cart.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.team4.commerce_payment_system.domain.cart.entity.Cart;
import com.sparta.team4.commerce_payment_system.domain.cart.entity.CartItem;
import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.sparta.team4.commerce_payment_system.domain.cart.entity.QCartItem.cartItem; // QClass import 필요

@Repository
@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CartItem> findByCartAndProduct(Cart cart, Product product) {
        CartItem result = queryFactory
                .selectFrom(cartItem)
                .where(
                        cartItem.cart.eq(cart),
                        cartItem.product.eq(product)
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId) {
        CartItem result = queryFactory
                .selectFrom(cartItem)
                .where(
                        cartItem.cart.id.eq(cartId),
                        cartItem.product.id.eq(productId)
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public List<CartItem> findByCartMemberId(Long memberId) {
        return queryFactory
                .selectFrom(cartItem)
                .where(cartItem.cart.member.id.eq(memberId))
                .fetch();
    }
}