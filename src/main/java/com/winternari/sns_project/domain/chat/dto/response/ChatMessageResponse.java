package com.winternari.sns_project.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // → @Getter, @Setter, @ToString, @EqualsAndHashCode 포함
@Builder // 선택사항: 빌더 패턴으로 객체 생성
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {

    private MessageType type;
    private String content;
    private String sender;
    private Long receiverId;

    public enum MessageType {
        CHAT, JOIN, ERROR, LEAVE
    }
}

