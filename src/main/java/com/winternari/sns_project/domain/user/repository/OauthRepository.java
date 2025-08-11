package com.winternari.sns_project.domain.user.repository;


import com.winternari.sns_project.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OauthRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByProfile_Nickname(String nickname);

    @Query("SELECT COUNT(f) FROM FollowEntity f WHERE f.following.id = :userId")
    int countFollowers(@Param("userId") UUID userId);

    @Query("SELECT COUNT(f) FROM FollowEntity f WHERE f.follower.id = :userId")
    int countFollowing(@Param("userId") UUID userId);

}
