package com.winternari.sns_project.api.controller;

import com.winternari.sns_project.domain.user.dto.request.LoginRequest;
import com.winternari.sns_project.domain.user.dto.request.SignRequest;
import com.winternari.sns_project.domain.user.dto.response.LoginResponse;
import com.winternari.sns_project.domain.user.service.OauthService;
import com.winternari.sns_project.global.security.cookie.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;

    //로그인
    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> signin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws Exception {
        LoginResponse loginResponse = oauthService.signIn(loginRequest, response);
        return ResponseEntity.ok(loginResponse);
    }
    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignRequest signRequest) throws Exception {
        oauthService.signUp(signRequest);
        return ResponseEntity
                .ok()
                .build();
    }

    //토큰 재생성
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String refreshToken = CookieUtil.getCookieValue(request, "refreshToken");
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        LoginResponse loginResponse = oauthService.refreshToken(refreshToken, response);
        return ResponseEntity.ok(loginResponse);
    }
}
