package com.winternari.sns_project.domain.user.entity;


import com.winternari.sns_project.domain.follow.entity.FollowEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_entity")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id") // user_entity 테이블에 profile_id 외래키 생성
    private User_Profile profile;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<FollowEntity> following; // 내가 팔로우 한 사람들

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    private List<FollowEntity> followers; // 나를 팔로우한 사람들
}
