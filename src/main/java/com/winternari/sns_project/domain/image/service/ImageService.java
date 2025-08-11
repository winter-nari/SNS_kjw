package com.winternari.sns_project.domain.image.service;

import com.winternari.sns_project.domain.image.entity.Image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface ImageService {

    Image uploadImage(MultipartFile file) throws IOException;

    Optional<Image> getImage(UUID id);
}
