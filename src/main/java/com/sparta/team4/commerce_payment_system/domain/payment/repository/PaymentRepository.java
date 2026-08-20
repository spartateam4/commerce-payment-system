package com.sparta.team4.commerce_payment_system.domain.payment.repository;

import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("""
            SELECT DISTINCT o FROM Order o JOIN FETCH o.member LEFT JOIN FETCH o.orderItems oi
            LEFT JOIN FETCH oi.product WHERE o.id = :orderId""")
    Optional<Order> findOrderByIdWithPaymentDetails(@Param("orderId") Long orderId);

    @Query("SELECT p FROM Payment p JOIN FETCH p.order o JOIN FETCH o.member WHERE p.id = :paymentId")
    Optional<Payment> findByIdWithOrderAndMember(@Param("paymentId") Long paymentId);
}
