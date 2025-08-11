package com.winternari.sns_project.domain.follow.dto.response;


import com.winternari.sns_project.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
public class FollowResponse {
    private UUID id;
    private String email;
    private String nickname;

    public FollowResponse(UUID id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public static FollowResponse of(UserEntity user) {
        return new FollowResponse(
                user.getId(),
                user.getEmail(),
                user.getProfile().getNickname()
        );
    }
}
