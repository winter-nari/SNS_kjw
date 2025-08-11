package com.winternari.sns_project.global.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailService extends UserDetailsService {

    // Username으로 조회
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    // 추가: userId로 조회
    UserDetails loadUserById(String userId) throws UsernameNotFoundException;
}
