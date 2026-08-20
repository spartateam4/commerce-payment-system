package com.sparta.team4.commerce_payment_system.domain.order.facade;

import com.sparta.team4.commerce_payment_system.domain.cart.CartItem;
import com.sparta.team4.commerce_payment_system.domain.cart.service.CartService;
import com.sparta.team4.commerce_payment_system.domain.member.entity.Member;
import com.sparta.team4.commerce_payment_system.domain.member.repository.MemberRepository;
import com.sparta.team4.commerce_payment_system.domain.order.dto.OrderCancelRequest;
import com.sparta.team4.commerce_payment_system.domain.order.dto.OrderCancelResponse;
import com.sparta.team4.commerce_payment_system.domain.order.dto.OrderCreateRequest;
import com.sparta.team4.commerce_payment_system.domain.order.dto.OrderCreateResponse;
import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderItem;
import com.sparta.team4.commerce_payment_system.domain.order.service.OrderService;
import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import com.sparta.team4.commerce_payment_system.domain.product.service.ProductService;
import com.sparta.team4.commerce_payment_system.global.exception.CustomException;
import com.sparta.team4.commerce_payment_system.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderFacade {

    private final OrderService orderService;
    private final MemberRepository memberRepository;

    // TODO: 윤지님(장바구니), 준호님(상품), 예림님(결제) 기능 서비스 추가 (주석 지우고 사용)

    private final CartService cartService;
    private final ProductService productService;
    // private final PaymentService paymentService;

    @Transactional
    public OrderCreateResponse createOrder(Long memberId, OrderCreateRequest request) {
        // 1. 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        int totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        // TODO: 윤지님(장바구니) + 준호님(상품) 연동 시 주석 해제 및 수정

        // 2. 장바구니 아이템 조회 (요청이 비어있으면 전체 장바구니 조회)
        List<Long> cartItemIds = (request.getCartItemIds() != null) ? request.getCartItemIds() : List.of();
        List<CartItem> cartItems = cartItemIds.isEmpty()
                ? cartService.findCartEntities(memberId)
                : cartService.findCartEntitiesByIds(memberId, cartItemIds);

        if (cartItems.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST); // 추후 CART_EMPTY 에러코드로 변경
        }

        // 3. 재고 선차감 및 OrderItem 스냅샷 구성
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new CustomException(ErrorCode.OUT_OF_STOCK);
            }
            product.removeStock(cartItem.getQuantity());

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .productName(product.getName())
                    .orderPrice(product.getPrice()) // 현재가(price)를 스냅샷으로 저장
                    .quantity(cartItem.getQuantity())
                    .build();

            orderItems.add(orderItem);
            totalAmount += orderItem.getSubtotal(); // 내부 메서드 사용
        }


        // 4.  주문 로직 (DB 저장)
        Order order = orderService.createOrder(member, orderItems, totalAmount);

        // TODO: 예림님(결제) + 윤지님(장바구니 비우기) 연동 시 주석 해제

        /*
        // 5. 결제 정보(대기) 기록
        paymentService.createPayment(order, totalAmount);
        */

        // 6. 주문이 완료된 장바구니 아이템만 삭제
        List<Long> orderedItemIds = cartItems.stream().map(CartItem::getId).toList();
        cartService.clearCartItems(orderedItemIds, memberId);
        return new OrderCreateResponse(order);
    }

    @Transactional
    public OrderCancelResponse cancelOrder(Long memberId, Long orderId, OrderCancelRequest request) {
        if (!"CANCEL".equals(request.getAction())) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        Order order = orderService.cancelOrder(memberId, orderId);

        // TODO: 준호님 재고 복구, 예림님 결제 상태 업데이트 연동 후 주석 해제 하고 사용

        for (OrderItem orderItem : order.getOrderItems()) {
            productService.restoreStock(orderItem.getProductId(), orderItem.getQuantity());
        }
        /*
        paymentService.failPaymentAndOrder(orderId); // 또는 결제 상태 FAILED 변경
        */
        return new OrderCancelResponse(order, "FAILED");
    }
}