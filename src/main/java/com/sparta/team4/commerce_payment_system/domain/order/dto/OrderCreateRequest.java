package com.sparta.team4.commerce_payment_system.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    // 장바구니 아이템 ID 목록 (값이 없거나 비어있으면 전체 장바구니 주문)
    private List<Long> cartItemIds;

}