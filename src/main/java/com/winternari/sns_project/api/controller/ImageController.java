package com.winternari.sns_project.api.controller;

import com.winternari.sns_project.domain.image.entity.Image;
import com.winternari.sns_project.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        Image image = imageService.uploadImage(file);
        return ResponseEntity.ok("Image uploaded successfully! ID = " + image.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable UUID id) {
        return imageService.getImage(id)
                .map(image -> {
                    ByteArrayResource resource = new ByteArrayResource(image.getData());
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(image.getFileType()))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFileName() + "\"")
                            .body(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
