package com.winternari.sns_project.domain.follow.service.impl;

import com.winternari.sns_project.domain.follow.dto.response.FollowResponse;
import com.winternari.sns_project.domain.follow.entity.FollowEntity;
import com.winternari.sns_project.domain.follow.repository.FollowRepository;
import com.winternari.sns_project.domain.follow.service.FollowService;
import com.winternari.sns_project.domain.user.entity.UserEntity;
import com.winternari.sns_project.domain.user.repository.OauthRepository;
import com.winternari.sns_project.global.security.service.UserForSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final OauthRepository oauthRepository;

    private UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserForSecurity userDetails = (UserForSecurity) authentication.getPrincipal();
        return oauthRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("현재 사용자 없음"));
    }

    @Override
    public void follow(String toNickname) {
        UserEntity fromUser = getCurrentUser();
        UserEntity toUser = oauthRepository.findByProfile_Nickname(toNickname)
                .orElseThrow(() -> new RuntimeException("팔로우 대상 없음"));

        if (followRepository.existsByFollowerAndFollowing(fromUser, toUser)) {
            throw new IllegalStateException("이미 팔로우함");
        }

        FollowEntity follow = new FollowEntity();
        follow.setFollower(fromUser);
        follow.setFollowing(toUser);
        follow.setCreatedAt(LocalDateTime.now());

        followRepository.save(follow);
    }

    @Override
    public void unfollow(String toNickname) {
        UserEntity fromUser = getCurrentUser();
        UserEntity toUser = oauthRepository.findByProfile_Nickname(toNickname)
                .orElseThrow(() -> new RuntimeException("언팔 대상 없음"));

        followRepository.deleteByFollowerAndFollowing(fromUser, toUser);
    }

    @Override
    public List<FollowResponse> getFollowingList() {
        UserEntity currentUser = getCurrentUser();
        return followRepository.findByFollower(currentUser).stream()
                .map(f -> FollowResponse.of(f.getFollowing()))
                .collect(Collectors.toList());
    }

    @Override
    public List<FollowResponse> getFollowerList() {
        UserEntity currentUser = getCurrentUser();
        return followRepository.findByFollowing(currentUser).stream()
                .map(f -> FollowResponse.of(f.getFollower()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFollowing(String toNickname) {
        UserEntity fromUser = getCurrentUser();
        UserEntity toUser = oauthRepository.findByProfile_Nickname(toNickname)
                .orElseThrow(() -> new RuntimeException("확인 대상 없음"));
        return followRepository.existsByFollowerAndFollowing(fromUser, toUser);
    }
}

