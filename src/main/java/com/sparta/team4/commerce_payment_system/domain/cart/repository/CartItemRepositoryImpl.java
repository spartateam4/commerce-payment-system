package com.sparta.team4.commerce_payment_system.domain.cart.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.team4.commerce_payment_system.domain.cart.Cart;
import com.sparta.team4.commerce_payment_system.domain.cart.CartItem;
import com.sparta.team4.commerce_payment_system.domain.product.Product;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

import static com.sparta.team4.commerce_payment_system.domain.cart.QCartItem.cartItem; // QClass import 필요

@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepositoryCustom {

    private final JPAQueryFactory queryFactory; // QueryDSL 전용 도구 주입

    @Override
    public Optional<CartItem> findByCartAndProduct(Cart cart, Product product) {
        // 💡 힌트: queryFactory.selectFrom(cartItem).where(...).fetchOne();
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
    public Optional<CartItem> findByIdAndCart_MemberId(Long cartItemId, Long memberId) {
        // 💡 힌트: queryFactory를 활용해 id와 memberId 조건 걸기
        CartItem result = queryFactory
                .selectFrom(cartItem)
                .where(
                        cartItem.id.eq(cartItemId),
                        cartItem.cart.member.id.eq(memberId)
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }
}