package com.sparta.team4.commerce_payment_system.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {


    // ===== 공통 =====
    // 400 — 검증 실패, 필수값 누락
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),

    // ====== 회원 =====
    // 401 — 로그인 실패 / 인증 실패
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),

    // 404 — 회원 없음
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),

    // 409 — 이메일 중복
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),



    // ===== 상품 =====
// 404 — 상품 없음
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),


    // ===== 주문 ===== (주문 담당자가 추가)

    OUT_OF_STOCK(HttpStatus.CONFLICT, "상품의 재고가 부족합니다."),

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 내역을 찾을 수 없습니다."),

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 주문에 접근할 권한이 없습니다."),

    CANCEL_NOT_ALLOWED(HttpStatus.CONFLICT, "이미 결제가 완료되었거나 취소된 주문은 처리할 수 없습니다.");







    // ===== 결제 ===== (결제 담당자가 추가)









    // ======= 필요한 생성자
    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
