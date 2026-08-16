package com.sparta.team4.commerce_payment_system.domain.cart.service;

import com.sparta.team4.commerce_payment_system.domain.cart.CartItem;
import com.sparta.team4.commerce_payment_system.domain.cart.dto.CartItemResponse;
import com.sparta.team4.commerce_payment_system.domain.cart.dto.CartResponse;
import com.sparta.team4.commerce_payment_system.domain.cart.repository.CartItemRepository;
import com.sparta.team4.commerce_payment_system.domain.cart.repository.CartRepository;
import com.sparta.team4.commerce_payment_system.domain.member.repository.MemberRepository;
import com.sparta.team4.commerce_payment_system.domain.product.Product;
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
    public CartItemResponse addItem(Long memberId, Long cartId, CartItem cartItem) {

        // 상품 재고 확인
        Product product = cartItem.getProduct();
        int requestQuantity = cartItem.getQuantity();

        Optional<CartItem> existing = cartItemRepository
                .findByCart_IdAndProduct_Id(cartId, cartItem.getProduct().getId());


        // TODO: 합산될 수량이 재고를 초과하는지 확인
        // existing이 있으면: existing의 quantity + requestQuantity 와 비교
        // existing이 없으면: requestQuantity와 비교
        CartItem saved;
        if (existing.isPresent()) {
            int totalQuantity = existing.get().getQuantity() + requestQuantity;

            if(totalQuantity > product.getStock()) {
                throw new RuntimeException("재고가 부족합니다");
            }

            CartItem found = existing.get();
            found.addQuantity(cartItem.getQuantity());
            saved = found;
        } else {
            if (requestQuantity > product.getStock()) {
                throw new RuntimeException("재고가 부족합니다");
            }
            saved = cartItemRepository.save(cartItem);
        }
        return toResponse(saved);
    }


    // 수량 변경
    @Transactional
    public CartItemResponse updateQuantity(Long memberId, Long itemId, int quantity) {
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
    public void removeItem(Long memberId, Long itemId) {
        CartItem item = cartItemRepository.findById(itemId)
                .filter(ci -> ci.getCart().getMember().getId().equals(memberId))
                .orElseThrow(() -> new RuntimeException("장바구니 항목을 찾을 수 없습니다."));
        cartItemRepository.delete(item);
    }


    // 전체 비우기
    @Transactional
    public void clearCart(Long memberId) {
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