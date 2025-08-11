package com.winternari.sns_project.api.controller;


import com.winternari.sns_project.domain.user.dto.response.MemberResponse;
import com.winternari.sns_project.domain.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberResponse> getMyInfo() {
        MemberResponse memberResponse = memberService.getMember();
        return ResponseEntity.ok(memberResponse);
    }
}
