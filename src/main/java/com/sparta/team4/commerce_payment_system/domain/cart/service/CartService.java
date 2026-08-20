package com.sparta.team4.commerce_payment_system.domain.cart.service;

import com.sparta.team4.commerce_payment_system.domain.cart.Cart;
import com.sparta.team4.commerce_payment_system.domain.cart.CartItem;
import com.sparta.team4.commerce_payment_system.domain.cart.dto.CartItemResponse;
import com.sparta.team4.commerce_payment_system.domain.cart.dto.CartResponse;
import com.sparta.team4.commerce_payment_system.domain.cart.repository.CartItemRepository;
import com.sparta.team4.commerce_payment_system.domain.cart.repository.CartRepository;
import com.sparta.team4.commerce_payment_system.domain.member.entity.Member;
import com.sparta.team4.commerce_payment_system.domain.member.repository.MemberRepository;
import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import com.sparta.team4.commerce_payment_system.domain.product.repository.ProductRepository;
import com.sparta.team4.commerce_payment_system.global.exception.CustomException;
import com.sparta.team4.commerce_payment_system.global.exception.ErrorCode;

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

        // 수량 검증 (가장 먼저 체크)
        if (quantity <= 0) {
            throw new CustomException(ErrorCode.INVALID_QUANTITY);
        }

        // memberId로 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // memberId로 장바구니 조회, 없으면 생성
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseGet(() -> cartRepository.save(new Cart(member)));

        // productId로 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 기존 장바구니 항목 확인
        Optional<CartItem> existing = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId);

        CartItem saved;

        if (existing.isPresent()) {
            // 기존 항목이 있으면 수량 추가
            int totalQuantity = existing.get().getQuantity() + quantity;

            if (totalQuantity > product.getStockQuantity()) {
                throw new CustomException(ErrorCode.OUT_OF_STOCK);
            }

            existing.get().addQuantity(quantity);
            saved = existing.get();

        } else {
            // 새로운 항목 생성
            if (quantity > product.getStockQuantity()) {
                throw new CustomException(ErrorCode.OUT_OF_STOCK);
            }

            CartItem newItem = new CartItem(cart, product, quantity);
            saved = cartItemRepository.save(newItem);
        }

        return toResponse(saved);
    }


    // 수량 변경
    @Transactional
    public CartItemResponse updateCartItem(Long memberId, Long itemId, int quantity) {

        // 수량 검증 (가장 먼저 체크)
        if (quantity <= 0) {
            throw new CustomException(ErrorCode.INVALID_QUANTITY);
        }

        CartItem item = cartItemRepository.findById(itemId)
                .filter(ci -> ci.getCart().getMember().getId().equals(memberId))
                .orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

        // 상품 재고 확인
        if (quantity > item.getProduct().getStockQuantity()) {
            throw new CustomException(ErrorCode.OUT_OF_STOCK);
        }

        item.changeQuantity(quantity);
        return toResponse(item);
    }


    // 개별 삭제
    @Transactional
    public void deleteCartItem(Long memberId, Long itemId) {
        CartItem item = cartItemRepository.findById(itemId)
                .filter(ci -> ci.getCart().getMember().getId().equals(memberId))
                .orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

        cartItemRepository.delete(item);
    }


    // 전체 비우기
    @Transactional
    public void deleteAllCartItems(Long memberId) {
        List<CartItem> items = cartItemRepository.findByCartMemberId(memberId);
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
    // Facade에서 전체 장바구니 엔티티를 가져올 때 사용
    public List<CartItem> findCartEntities(Long memberId) {
        return cartItemRepository.findByCartMemberId(memberId);
    }
    // Facade에서 선택된 장바구니 엔티티만 가져올 때 사용
    public List<CartItem> findCartEntitiesByIds(Long memberId, List<Long> cartItemIds) {
        return cartItemRepository.findByCartMemberId(memberId).stream()
                .filter(item -> cartItemIds.contains(item.getId()))
                .toList();
    }
    // Facade에서 주문이 끝난 장바구니 아이템들을 비울 때 사용
    @Transactional
    public void clearCartItems(List<Long> cartItemIds, Long memberId) {
        List<CartItem> items = cartItemRepository.findByCartMemberId(memberId).stream()
                .filter(item -> cartItemIds.contains(item.getId()))
                .toList();
        cartItemRepository.deleteAll(items);
    }
}