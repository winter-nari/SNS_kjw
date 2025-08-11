package com.winternari.sns_project.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignRequest {
    private UUID id;

    private String email;
    private String password;
    private String nickname;
    private String username;
    private String phone;
    private String bio;
    private String location;
}
