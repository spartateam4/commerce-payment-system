package com.sparta.team4.commerce_payment_system.domain.member.dto;

import com.sparta.team4.commerce_payment_system.domain.member.entity.Member;
import java.time.LocalDateTime;

public record MemberResponse(
        Long id, String email, String name, String phone, LocalDateTime createdAt
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(), member.getEmail(), member.getName(),
                member.getPhone(), member.getCreatedAt());
    }
}