package com.sparta.team4.commerce_payment_system.payment;

import com.sparta.team4.commerce_payment_system.domain.member.entity.Member;
import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import com.sparta.team4.commerce_payment_system.domain.order.repository.OrderRepository;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.CreatePaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.PaymentRequest;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.PaymentStatus;
import com.sparta.team4.commerce_payment_system.domain.payment.repository.PaymentRepository;
import com.sparta.team4.commerce_payment_system.domain.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    PaymentService paymentService;

    private Member member;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        member = mock(Member.class);

        orders = List.of(
                new Order(member, "191", 4000, OrderStatus.PAYMENT_PENDING),
                new Order(member, "192", 5500, OrderStatus.PAYMENT_PENDING),
                new Order(member, "193", 14300, OrderStatus.COMPLETED),
                new Order(member, "194", 23300, OrderStatus.PAYMENT_PENDING),
                new Order(member, "195", 7100, OrderStatus.CANCELLED),
                new Order(member, "196", 12000, OrderStatus.PAYMENT_PENDING),
                new Order(member, "197", 11700, OrderStatus.COMPLETED),
                new Order(member, "198", 8900, OrderStatus.COMPLETED),
                new Order(member, "199", 15000, OrderStatus.PAYMENT_PENDING)
        );
    }

    /**
     * 정상 흐름
     */

    @Test
    @DisplayName("결제 성공: 결제·주문 상태 COMPLETED")
    void completed_pay() {
        // Given
        Order order = orders.get(3);

        when(member.getId()).thenReturn(1L);
        when(orderRepository.findById(4L)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PaymentRequest request = new PaymentRequest(4L, "SUCCESS", 23300);

        // When
        CreatePaymentResponse response = paymentService.confirm(1L, request);

        // Then
        assertEquals(PaymentStatus.COMPLETED, response.pay_status());
        assertEquals(OrderStatus.COMPLETED, response.order_status());
    }

    @Test
    @DisplayName("결제 실패: 결제 FAILED, 주문 CANCELLED")
    void failed_pay() {
        // Given
        // When
        // Then
    }

    @Test
    @DisplayName("완료된 결제 취소 성공")
    void cancelled_pay() {
        // Given
        // When
        // Then
    }

    /**
     * 예외 흐름
      */

    @Test
    @DisplayName("주문 소유자 불일치: ACCESS_DENIED")
    void access_denied_403() {
        // Given
        // When
        // Then
    }

    @Test
    @DisplayName("결제 금액 불일치: PRICE_MISMATCH")
    void price_mismatch_400() {
        // Given
        // When
        // Then
    }

    @Test
    @DisplayName("존재하지 않는 주문·결제")
    void payment_or_order_not_found_404() {
        // Given
        // When
        // Then
    }

    @Test
    @DisplayName("잘못된 상태에서 결제 실패")
    void illegal_status_pay_409() {
        // Given
        // When
        // Then
    }

    @Test
    @DisplayName("잘못된 상태에서 결제 취소 실패")
    void illegal_status_cancel_409() {
        // Given
        // When
        // Then
    }
}