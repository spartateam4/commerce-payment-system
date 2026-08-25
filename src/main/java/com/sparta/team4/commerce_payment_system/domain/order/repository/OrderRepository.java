package com.sparta.team4.commerce_payment_system.domain.order.repository;

import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// JpaRepository & OrderRepositoryCustom을 다중 상속
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    // 상세 조회 시 Fetch Join -> (N+1 문제 방지)
    @Query("select o from Order o join fetch o.member join fetch o.orderItems where o.id = :orderId")
    Optional<Order> findByIdWithItems(@Param("orderId") Long orderId);
}