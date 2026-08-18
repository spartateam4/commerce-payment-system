package com.sparta.team4.commerce_payment_system.domain.order.service; // ProductRepository, PaymentRepository 수정 전

import com.sparta.team4.commerce_payment_system.domain.member.entity.Member;
import com.sparta.team4.commerce_payment_system.domain.member.repository.MemberRepository;
import com.sparta.team4.commerce_payment_system.domain.order.dto.*;
import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderItem;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import com.sparta.team4.commerce_payment_system.domain.order.repository.OrderRepository;
import com.sparta.team4.commerce_payment_system.domain.payment.Payment;
// import com.sparta.team4.commerce_payment_system.domain.payment.PaymentRepository;
// import com.sparta.team4.commerce_payment_system.domain.payment.PaymentStatus;
import com.sparta.team4.commerce_payment_system.domain.product.Product;
// import com.sparta.team4.commerce_payment_system.domain.product.ProductRepository;
import com.sparta.team4.commerce_payment_system.global.exception.CustomException;
import com.sparta.team4.commerce_payment_system.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    // TODO: 준호님, 예림님 기능 레포지토리 주입 필요 (주석 지우고 사용)
    // private final ProductRepository productRepository;
    // private final PaymentRepository paymentRepository;

    /*
     * 1. 신규 주문 생성 시 (재고 선차감)
     */

    @Transactional
    public OrderCreateResponse createOrder(Long memberId, OrderCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)); // ErrorCode에 맞춰 수정 필요

        int totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderCreateRequest.OrderItemRequest itemReq : request.getOrderItems()) {

            // TODO: 준호님 ProductRepository 연동 시 주석 지우기

            /*
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

            if (product.getStock() < itemReq.getQuantity()) {
                throw new CustomException(ErrorCode.OUT_OF_STOCK);
            }

            product.removeStock(itemReq.getQuantity());

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .productName(product.getName())
                    .orderPrice(product.getTotalPrice())
                    .quantity(itemReq.getQuantity())
                    .build();

            orderItems.add(orderItem);
            totalAmount += (product.getTotalPrice() * itemReq.getQuantity());
            */
        }

        Order order = Order.builder()
                .member(member)
                .orderNumber(generateOrderNumber())
                .totalAmount(totalAmount)
                .status(OrderStatus.PAYMENT_PENDING)
                .build();

        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
        }

        orderRepository.save(order);

        // TODO: 예림님의 Payment 로직 연동 시 주석 해제

        /*
        Payment payment = Payment.builder()
                .order(order)
                .amount(totalAmount)
                .status(PaymentStatus.PENDING)
                .build();
        paymentRepository.save(payment);
        */

        return new OrderCreateResponse(order);
    }

    /*
     * 2. 내 주문 목록 조회 (QueryDSL 적용)
     */

    public Page<OrderCreateResponse> getMyOrders(Long memberId, Pageable pageable) {

        Page<Order> orders = orderRepository.findMyOrders(memberId, pageable);
        return orders.map(OrderCreateResponse::new);
    }

    /*
     * 3. 주문 상세 내역 조회
     */

    public OrderDetailResponse getOrderDetail(Long memberId, Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND)); // ErrorCode 확인 필요

        // 소유권 검증
        if (!order.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED); // ErrorCode 확인 필요
        }

        return new OrderDetailResponse(order);
    }

    /*
     * 4. 결제 전 주문 취소
     */

    @Transactional
    public OrderCancelResponse cancelOrder(Long memberId, Long orderId, OrderCancelRequest request) {
        if (!"CANCEL".equals(request.getAction())) {
            throw new CustomException(ErrorCode.INVALID_REQUEST); // ErrorCode 확인 필요
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        if (order.getStatus() != OrderStatus.PAYMENT_PENDING) {
            throw new CustomException(ErrorCode.CANCEL_NOT_ALLOWED); // ErrorCode 확인 필요
        }

        order.cancelOrder();

        // TODO: 준호님 재고 복구, 예림님 결제 상태 업데이트 연동 후 주석 해제 하고 사용

        /*
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            product.addStock(orderItem.getQuantity());
        }

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
        payment.updateStatus(PaymentStatus.FAILED);
        */

        // 임시 리턴 (Payment 구현 후 수정)
        return new OrderCancelResponse(order, "FAILED");
    }

    private String generateOrderNumber() {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return datePrefix + "-" + randomSuffix;
    }
}