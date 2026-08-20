package com.sparta.team4.commerce_payment_system.domain.payment.service;

import com.sparta.team4.commerce_payment_system.domain.cart.service.CartService;
import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderItem;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import com.sparta.team4.commerce_payment_system.domain.order.repository.OrderRepository;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.CancelPaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.CreatePaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.GetPaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.PaymentRequest;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.PaymentStatus;
import com.sparta.team4.commerce_payment_system.domain.payment.repository.PaymentRepository;
import com.sparta.team4.commerce_payment_system.domain.product.service.ProductService;
import com.sparta.team4.commerce_payment_system.global.exception.CustomException;
import com.sparta.team4.commerce_payment_system.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CartService cartService;

    @Transactional
    public CreatePaymentResponse confirm(Long requestedByMemberId, PaymentRequest request) {
        // 요청 ID에 해당하는 주문데이터(행) 찾기
        Order targetOrder = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND)); // 404

        // 주문 소유자 확인
        if (!requestedByMemberId.equals(targetOrder.getMember().getId()))
            throw new CustomException(ErrorCode.ACCESS_DENIED); // 403

        // 찾은 주문데이터를 기반으로 임시 결제엔티티 생성
        Payment payment = new Payment(targetOrder, targetOrder.getTotalAmount());

        // 주문 상태가 "결제대기"가 아님
        if (targetOrder.getStatus() != OrderStatus.PAYMENT_PENDING)
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS); // 409

        // 결제 상태가 "대기"가 아님
        if (payment.getStatus() != PaymentStatus.PENDING)
            throw new CustomException(ErrorCode.INVALID_PAYMENT_STATUS); // 409

        // 요청금액과 주문금액 불일치 (금액 위조 방지)
        if (!request.amount().equals(targetOrder.getTotalAmount()))
            throw new CustomException(ErrorCode.PRICE_MISMATCH); // 400

        // 결과에 따라 분기
        switch (request.result()) {
            case "SUCCESS" -> {
                payment.complete(); // 결제: PENDING -> COMPLETED
                payment.getOrder().completeOrder(); // 주문: PAYMENT_PENDING -> COMPLETED
                cartService.deleteAllCartItems(requestedByMemberId); // 장바구니 비우기
            }
            case "FAIL" -> {
                payment.fail(); // 결제: PENDING -> FAILED
                payment.getOrder().cancelOrder(); // 주문: PAYMENT_PENDING -> CANCELLED
                restoreStockAtPayment(targetOrder); // 재고 복구
            }
            default -> throw new CustomException(ErrorCode.INVALID_PAYMENT_RESULT); // 400
        }

        // DB에 저장
        Payment savePayment = paymentRepository.save(payment);

        return CreatePaymentResponse.from(savePayment);
    }

    @Transactional(readOnly = true)
    public GetPaymentResponse get(Long requestedByMemberId, Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND)); // 404

        // 소유자 확인 (남의 결제 조회 차단)
        if (!requestedByMemberId.equals(payment.getOrder().getMember().getId()))
            throw new CustomException(ErrorCode.ACCESS_DENIED); // 403

        return GetPaymentResponse.from(payment);
    }

    @Transactional
    public CancelPaymentResponse cancel(Long requestedByMemberId, Long paymentId) {
        // 요청 ID에 해당하는 결제데이터(행) 찾기
        Payment targetPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND)); // 404

        // 주문 소유자 확인
        if (!requestedByMemberId.equals(targetPayment.getOrder().getMember().getId()))
            throw new CustomException(ErrorCode.ACCESS_DENIED); // 403

        // 결제 상태가 "완료"가 아님
        if (targetPayment.getStatus() != PaymentStatus.COMPLETED)
            throw new CustomException(ErrorCode.INVALID_PAYMENT_STATUS); // 409

        // 주문 상태가 "결제완료"가 아님
        if (targetPayment.getOrder().getStatus() != OrderStatus.COMPLETED)
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS); // 409

        targetPayment.cancel(); // 결제: COMPLETED -> CANCELLED
        targetPayment.getOrder().cancelOrder(); // 주문: COMPLETED -> CANCELLED
        restoreStockAtPayment(targetPayment.getOrder()); // 재고 복구

        return CancelPaymentResponse.from(targetPayment);
    }

    private void restoreStockAtPayment(Order order) {
        for (OrderItem orderItem : order.getOrderItems())
            productService.restoreStock(orderItem.getProductId(), orderItem.getQuantity());
    }
}
