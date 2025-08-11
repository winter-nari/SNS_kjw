package com.winternari.sns_project.domain.board.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardWithImageResponse {
    private UUID id;
    private String name;
    private String description;
    private String imageUrl;

}
