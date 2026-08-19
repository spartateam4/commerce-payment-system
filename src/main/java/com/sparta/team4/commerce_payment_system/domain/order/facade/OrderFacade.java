package com.sparta.team4.commerce_payment_system.domain.order.facade;

import com.sparta.team4.commerce_payment_system.domain.member.entity.Member;
import com.sparta.team4.commerce_payment_system.domain.member.repository.MemberRepository;
import com.sparta.team4.commerce_payment_system.domain.order.dto.OrderCancelRequest;
import com.sparta.team4.commerce_payment_system.domain.order.dto.OrderCancelResponse;
import com.sparta.team4.commerce_payment_system.domain.order.dto.OrderCreateRequest;
import com.sparta.team4.commerce_payment_system.domain.order.dto.OrderCreateResponse;
import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderItem;
import com.sparta.team4.commerce_payment_system.domain.order.service.OrderService;
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

    // TODO: 준호님, 예림님 기능 서비스 추가  (주석 지우고 사용)
    // private final ProductService productService;
    // private final PaymentService paymentService;

    @Transactional
    public OrderCreateResponse createOrder(Long memberId, OrderCreateRequest request) {
        // 1. 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        int totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        // 2. 상품 조회 및 재고 선차감, OrderItem 구성
        for (OrderCreateRequest.OrderItemRequest itemReq : request.getOrderItems()) {

            // TODO: 준호님 Product 연동 시 주석 해제 및 수정

            /*
            Product product = productService.findProductEntity(itemReq.getProductId());

            if (product.getStock() < itemReq.getQuantity()) {
                throw new CustomException(ErrorCode.OUT_OF_STOCK);
            }
            product.removeStock(itemReq.getQuantity());

            OrderItem orderItem = OrderItem.builder()
                    // .product(product)
                    .productName(product.getName())
                    .orderPrice(product.getTotalPrice())
                    .quantity(itemReq.getQuantity())
                    .build();

            orderItems.add(orderItem);
            totalAmount += orderItem.getSubtotal(); // OrderItem 내부 메서드 사용
            */
        }

        // 3. 주문  로직
        Order order = orderService.createOrder(member, orderItems, totalAmount);

        // 4. 결제 정보(대기) 미리 기록

        // TODO: 예림님의 Payment 로직 연동 시 주석 해제

        /*
        paymentService.createPayment(order, totalAmount);
        */

        return new OrderCreateResponse(order);
    }

    @Transactional
    public OrderCancelResponse cancelOrder(Long memberId, Long orderId, OrderCancelRequest request) {
        if (!"CANCEL".equals(request.getAction())) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        // 1. 주문 취소 (상태 변경) 로직
        Order order = orderService.cancelOrder(memberId, orderId);

        // 2. 타 기능(재고 복구, 결제 실패 처리) 트랜잭션

        // TODO: 준호님 재고 복구, 예림님 결제 상태 업데이트 연동 후 주석 해제 하고 사용

        // 준호님 상품 (재고 복구 로직)

        /*
        for (OrderItem orderItem : order.getOrderItems()) {
            // product.addStock(orderItem.getQuantity()); // 기존의 로직을 productService.restoreStock() 형태로 변경
            productService.restoreStock(orderItem.getProductId(), orderItem.getQuantity());
        }
        */

        // 예림님 결제 (결제 실패 처리 로직)

        /*
        paymentService.failPaymentAndOrder(orderId); // 또는 결제 상태 FAILED 변경
        */

        return new OrderCancelResponse(order, "FAILED"); // Payment 연동 후 실제 상태 반영
    }
}