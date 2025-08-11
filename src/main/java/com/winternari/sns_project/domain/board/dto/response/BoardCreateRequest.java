package com.winternari.sns_project.domain.board.dto.response;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BoardCreateRequest {
    private String name;
    private String description;
    private MultipartFile image;
}
