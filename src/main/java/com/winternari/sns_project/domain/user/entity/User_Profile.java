package com.winternari.sns_project.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.winternari.sns_project.domain.follow.entity.FollowEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User_Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nickname; // 유저 아이디
    private String username; // 유저 실제 이름
    private String phone;
    private String bio;
    private String location;

    private boolean isVerified;
    private boolean isBanned;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    private UserEntity user;

}
