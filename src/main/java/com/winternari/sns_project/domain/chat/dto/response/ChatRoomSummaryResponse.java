package com.winternari.sns_project.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomSummaryResponse {
    private UUID roomId;
    private String otherUsername;
    private String otherEmail;

}
