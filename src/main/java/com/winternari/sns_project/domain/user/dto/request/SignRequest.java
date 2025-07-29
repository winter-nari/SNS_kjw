package com.winternari.sns_project.domain.user.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class SignRequest {
    private UUID Id;
    private String email;
    private String password;
    private String username;
    private String phone;
    private int age;
    private String sex;
    private String image;
}
