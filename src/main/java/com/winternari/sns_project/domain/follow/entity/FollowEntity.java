package com.winternari.sns_project.domain.follow.entity;


import com.winternari.sns_project.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "follows", uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"follower_id", "following_id"})
//})
@Table(name = "follows")
public class FollowEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private UserEntity follower;  // 내가 팔로우 당한거

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private UserEntity following;  // 내가 팔로우 한거

    @CreatedDate
    private LocalDateTime createdAt;
}
