package com.sparta.team4.commerce_payment_system.domain.cart.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.team4.commerce_payment_system.domain.cart.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.sparta.team4.commerce_payment_system.domain.cart.entity.QCart.cart;
import static com.sparta.team4.commerce_payment_system.domain.cart.entity.QCartItem.cartItem;
import static com.sparta.team4.commerce_payment_system.domain.product.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Cart> findByMemberId(Long memberId) {
        Cart result = queryFactory
                .selectFrom(cart)
                .leftJoin(cart.items, cartItem)
                .fetchJoin()
                .leftJoin(cartItem.product, product)
                .fetchJoin()
                .where(cart.member.id.eq(memberId))
                .fetchOne();
        return Optional.ofNullable(result);
    }
}