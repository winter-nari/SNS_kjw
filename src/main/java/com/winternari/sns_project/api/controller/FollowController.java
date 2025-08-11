package com.winternari.sns_project.api.controller;


import com.winternari.sns_project.domain.follow.dto.response.FollowResponse;
import com.winternari.sns_project.domain.follow.service.FollowService;
import com.winternari.sns_project.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우 요청 (로그인한 사용자가 toNickname 팔로우)
    @PostMapping("/follow/{toNickname}")
    public ResponseEntity<ApiResponse<Void>> follow(@PathVariable String toNickname) {
        followService.follow(toNickname);
        return ResponseEntity.ok(new ApiResponse<>(true, "팔로우 성공", null));
    }

    // 언팔로우 요청
    @DeleteMapping("/unfollow/{toNickname}")
    public ResponseEntity<ApiResponse<Void>> unfollow(@PathVariable String toNickname) {
        followService.unfollow(toNickname);
        return ResponseEntity.ok(new ApiResponse<>(true, "언팔로우 성공", null));
    }

    // 내가 팔로우한 사람 목록 조회
    @GetMapping("/following")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> getFollowingList() {
        List<FollowResponse> followingList = followService.getFollowingList();
        return ResponseEntity.ok(new ApiResponse<>(true, "팔로잉 목록 조회 성공", followingList));
    }

    // 나를 팔로우한 사람 목록 조회
    @GetMapping("/followers")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> getFollowerList() {
        List<FollowResponse> followerList = followService.getFollowerList();
        return ResponseEntity.ok(new ApiResponse<>(true, "팔로워 목록 조회 성공", followerList));
    }

    // 특정 유저 팔로우 중인지 확인
    @GetMapping("/check/{toNickname}")
    public ResponseEntity<ApiResponse<Boolean>> checkFollowing(@PathVariable String toNickname) {
        boolean isFollowing = followService.isFollowing(toNickname);
        return ResponseEntity.ok(new ApiResponse<>(true, "팔로우 여부 확인", isFollowing));
    }
}


