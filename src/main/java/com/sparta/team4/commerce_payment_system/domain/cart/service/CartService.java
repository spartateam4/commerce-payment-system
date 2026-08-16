package com.sparta.team4.commerce_payment_system.domain.cart.service;

import com.sparta.team4.commerce_payment_system.domain.cart.CartItem;
import com.sparta.team4.commerce_payment_system.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long addItem(CartItem cartItem) {
    }




}
