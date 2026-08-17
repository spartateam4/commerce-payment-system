package com.sparta.team4.commerce_payment_system.domain.cart.service;

import com.sparta.team4.commerce_payment_system.domain.cart.Cart;
import com.sparta.team4.commerce_payment_system.domain.cart.CartItem;
import com.sparta.team4.commerce_payment_system.domain.cart.dto.CartItemResponse;
import com.sparta.team4.commerce_payment_system.domain.cart.dto.CartResponse;
import com.sparta.team4.commerce_payment_system.domain.cart.repository.CartItemRepository;
import com.sparta.team4.commerce_payment_system.domain.cart.repository.CartRepository;
import com.sparta.team4.commerce_payment_system.domain.member.entity.Member;
import com.sparta.team4.commerce_payment_system.domain.member.repository.MemberRepository;
import com.sparta.team4.commerce_payment_system.domain.product.Product;
import com.sparta.team4.commerce_payment_system.domain.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // 장바구니 조회
    @Transactional
    public CartResponse getCart(Long memberId) {
        List<CartItem> items = cartItemRepository.findByCartMemberId(memberId);

        Long cartId = items.isEmpty() ? null : items.get(0).getCart().getId();

        List<CartItemResponse> itemResponses = items.stream()
                .map(this::toResponse)
                .toList();

        int totalAmount = itemResponses.stream()
                .mapToInt(CartItemResponse::total)
                .sum();

        return new CartResponse(cartId, itemResponses, totalAmount);
    }


    // 상품 담기
    @Transactional
    public CartItemResponse addItem(Long memberId, Long productId, int quantity) {

        // memberId로 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다"));

        // memberId로 장바구니 조회, 없으면 생성
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseGet(() -> cartRepository.save(new Cart(member)));

        // productId로 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다"));

        // 기존 장바구니 항목 확인
        Optional<CartItem> existing = cartItemRepository
                .findByCart_IdAndProduct_Id(cart.getId(), productId);

        // 기존 항목이 있으면 수량 추가, 없으면 새로 생성
        CartItem saved;
        if (existing.isPresent()) {
            int totalQuantity = existing.get().getQuantity() + quantity;

            if (totalQuantity > product.getStock()) {
                throw new RuntimeException("재고가 부족합니다");
            }

            existing.get().addQuantity(quantity);
            saved = existing.get();
            // ✅ save() 없어도 변경 감지로 DB 반영됨!
        } else {
            if (quantity > product.getStock()) {
                throw new RuntimeException("재고가 부족합니다");
            }

            CartItem newItem = new CartItem(cart, product, quantity);
            saved = cartItemRepository.save(newItem);
        }

        return toResponse(saved);
    }


    // 수량 변경
    @Transactional
    public CartItemResponse updateCartItem(Long memberId, Long itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .filter(ci -> ci.getCart().getMember().getId().equals(memberId))
                .orElseThrow(() -> new RuntimeException("장바구니 항목을 찾을 수 없습니다."));

        // quantity가 상품 재고를 초과하면 예외
        if (quantity > item.getProduct().getStock()) {
            throw new RuntimeException("재고가 부족합니다");
        }

        item.changeQuantity(quantity);
        return toResponse(item);
    }


    // 개별 삭제
    @Transactional
    public void deleteCartItem(Long memberId, Long itemId) {
        CartItem item = cartItemRepository.findById(itemId)
                .filter(ci -> ci.getCart().getMember().getId().equals(memberId))
                .orElseThrow(() -> new RuntimeException("장바구니 항목을 찾을 수 없습니다."));
        cartItemRepository.delete(item);
    }


    // 전체 비우기
    @Transactional
    public void deleteAllCartItems(Long memberId) {
        List<CartItem> items = cartItemRepository.findByCart_MemberId(memberId);
        cartItemRepository.deleteAll(items);
    }

    // 응답 변환
    private CartItemResponse toResponse(CartItem item) {
        int total = item.getProduct().getPrice() * item.getQuantity();
        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                total
        );
    }
}