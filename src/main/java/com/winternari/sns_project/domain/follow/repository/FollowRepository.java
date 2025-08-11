package com.winternari.sns_project.domain.follow.repository;

import com.winternari.sns_project.domain.follow.entity.FollowEntity;
import com.winternari.sns_project.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, UUID> {

    Optional<FollowEntity> findByFollowerAndFollowing(UserEntity follower, UserEntity following);

    List<FollowEntity> findByFollower(UserEntity follower); // 내가 팔로우한 유저들
    List<FollowEntity> findByFollowing(UserEntity following); // 나를 팔로우한 유저들

    boolean existsByFollowerAndFollowing(UserEntity follower, UserEntity following);

    void deleteByFollowerAndFollowing(UserEntity follower, UserEntity following);
}

