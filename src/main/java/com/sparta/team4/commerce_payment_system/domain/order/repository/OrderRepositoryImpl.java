package com.sparta.team4.commerce_payment_system.domain.order.repository; //QueryDSL 구현체

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sparta.team4.commerce_payment_system.domain.order.entity.QOrder.order;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> findMyOrders(Long memberId, Pageable pageable) {
        // 1. 주문 목록 조회 (페이징 + 최신순 정렬)
        List<Order> content = queryFactory
                .selectFrom(order)
                .where(order.member.id.eq(memberId))
                .orderBy(order.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2. 전체 카운트 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(order.member.id.eq(memberId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}