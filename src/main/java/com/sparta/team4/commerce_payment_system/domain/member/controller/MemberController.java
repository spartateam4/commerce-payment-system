package com.sparta.team4.commerce_payment_system.domain.member.controller;

import com.sparta.team4.commerce_payment_system.domain.member.dto.MemberResponse;
import com.sparta.team4.commerce_payment_system.domain.member.service.MemberService;
import com.sparta.team4.commerce_payment_system.global.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMe(
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        MemberResponse response = memberService.getMe(principal.getMemberId());
        return ResponseEntity.ok(response);
    }
}