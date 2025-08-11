package com.winternari.sns_project.domain.user.service.impl;


import com.winternari.sns_project.domain.user.dto.response.MemberResponse;
import com.winternari.sns_project.domain.user.entity.UserEntity;
import com.winternari.sns_project.domain.user.repository.OauthRepository;
import com.winternari.sns_project.domain.user.service.MemberService;
import com.winternari.sns_project.global.security.service.UserForSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final OauthRepository oauthRepository;

    @Override
    public MemberResponse getMember() {

        // 현재 인증된 UserDetails 꺼내기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserForSecurity)) {
            throw new RuntimeException("인증된 사용자가 없습니다.");
        }

        UserForSecurity userDetails = (UserForSecurity) principal;
        UUID userId = userDetails.getUser().getId();

        UserEntity user = oauthRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int followerCount = oauthRepository.countFollowers(userId);
        int followingCount = oauthRepository.countFollowing(userId);

        return MemberResponse.builder()
                .id(user.getProfile().getId())
                .email(user.getEmail())
                .username(user.getProfile().getUsername())
                .phone(user.getProfile().getPhone())
                .bio(user.getProfile().getBio())
                .location(user.getProfile().getLocation())
                .isVerified(user.getProfile().isVerified())
                .isBanned(user.getProfile().isBanned())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }
}
