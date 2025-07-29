package com.winternari.sns_project.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User_Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;


}
