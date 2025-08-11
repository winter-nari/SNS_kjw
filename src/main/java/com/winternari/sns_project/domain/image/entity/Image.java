package com.winternari.sns_project.domain.image.entity;

import com.winternari.sns_project.domain.board.entity.Board;
import com.winternari.sns_project.domain.user.entity.User_Profile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/*
    이미지를 DB의 BLOB 컬럼(LONGBLOB)에 저장
 */

@Entity
@Table(name = "tb_image")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String fileName;    // "abc123.png"
    private String filePath;    // "/uploads/abc123.png"
    private String fileType;    // "image/png"
    private Long fileSize;      // 바이트 단위

    @Lob
    @Column(name = "data", columnDefinition = "LONGBLOB")
    private byte[] data;          // data 저장

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User_Profile user_profile;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
