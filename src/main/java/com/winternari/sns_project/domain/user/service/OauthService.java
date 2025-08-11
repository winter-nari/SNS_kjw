package com.winternari.sns_project.domain.user.service;

import com.winternari.sns_project.domain.user.dto.request.LoginRequest;
import com.winternari.sns_project.domain.user.dto.request.SignRequest;
import com.winternari.sns_project.domain.user.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface OauthService {
    public boolean signUp(SignRequest signRequest) throws Exception;
    public LoginResponse signIn(LoginRequest loginRequest, HttpServletResponse response) throws Exception;
    LoginResponse refreshToken(String refreshToken, HttpServletResponse response) throws Exception;
}
