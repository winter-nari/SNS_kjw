package com.winternari.sns_project.domain.user.dto.response;


import com.winternari.sns_project.domain.user.entity.UserEntity;
import com.winternari.sns_project.domain.user.entity.User_Profile;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class MemberResponse {
    private UUID id;
    private String email;
    private String username;
    private String phone;
    private String bio;
    private String location;
    private boolean isVerified;
    private boolean isBanned;

    private int followerCount; // 팔로워 수
    private int followingCount; // 팔로잉 수



    public static MemberResponse fromEntity(UserEntity userEntity) {
        User_Profile profile = userEntity.getProfile();

        return MemberResponse.builder()
                .id(profile.getId())
                .email(userEntity.getEmail()) // email은 UserEntity에서
                .username(profile.getUsername()) // 이름은 User_Profile에서
                .phone(profile.getPhone())
                .bio(profile.getBio())
                .location(profile.getLocation())
                .isVerified(profile.isVerified())
                .isBanned(profile.isBanned())
                .followerCount(userEntity.getFollowers() != null ? userEntity.getFollowers().size() : 0)
                .followingCount(userEntity.getFollowing() != null ? userEntity.getFollowing().size() : 0)
                .build();
    }
}

