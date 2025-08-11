package com.winternari.sns_project.domain.user.repository;

import com.winternari.sns_project.domain.user.entity.User_Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface UserProfileRepository extends JpaRepository<User_Profile, UUID> {

}
