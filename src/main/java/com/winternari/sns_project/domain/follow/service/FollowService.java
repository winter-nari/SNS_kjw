package com.winternari.sns_project.domain.follow.service;

import com.winternari.sns_project.domain.follow.dto.response.FollowResponse;
import com.winternari.sns_project.domain.user.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface FollowService {

    public void follow(String toNickname);
    public void unfollow(String toNickname);
    public List<FollowResponse> getFollowingList();
    public List<FollowResponse> getFollowerList();
    public boolean isFollowing(String toNickname);
}
