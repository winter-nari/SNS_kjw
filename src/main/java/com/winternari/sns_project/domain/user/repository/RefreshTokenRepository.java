package com.winternari.sns_project.domain.user.repository;

import com.winternari.sns_project.domain.user.dto.response.RefreshToken;
import com.winternari.sns_project.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByUser(UserEntity user);

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserId(UUID userId);
}
