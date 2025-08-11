package com.winternari.sns_project.global.security.service.impl;

import com.winternari.sns_project.domain.user.entity.UserEntity;
import com.winternari.sns_project.domain.user.repository.OauthRepository;
import com.winternari.sns_project.global.security.service.CustomUserDetailService;
import com.winternari.sns_project.global.security.service.UserForSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailServiceImpl implements CustomUserDetailService {

    private final OauthRepository oauthRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = oauthRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return UserForSecurity.builder()
                .user(user)
                .build();
    }

    @Override
    public UserDetails loadUserById(String userId) throws UsernameNotFoundException {
        // userId 타입이 Long이면 아래처럼, UUID면 UUID.fromString(userId)로 변경
        UserEntity user = oauthRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
        return UserForSecurity.builder()
                .user(user)
                .build();
    }
}
