package com.winternari.sns_project.domain.image.service.impl;

import com.winternari.sns_project.domain.image.entity.Image;
import com.winternari.sns_project.domain.image.repository.ImageRepository;
import com.winternari.sns_project.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public Image uploadImage(MultipartFile file) throws IOException {
        Image image = Image.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .data(file.getBytes())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        return imageRepository.save(image);
    }

    @Override
    public Optional<Image> getImage(UUID id) {
        return imageRepository.findById(id);
    }

}
