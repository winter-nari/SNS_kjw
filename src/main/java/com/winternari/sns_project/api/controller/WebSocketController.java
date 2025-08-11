package com.winternari.sns_project.api.controller;

import com.winternari.sns_project.domain.chat.dto.response.ChatMessageResponse;
import com.winternari.sns_project.domain.chat.entity.ChatMessages;
import com.winternari.sns_project.domain.chat.repository.ChatMessagesRepository;
import com.winternari.sns_project.domain.chat.repository.ChatRoomRepository;
import com.winternari.sns_project.domain.websocket.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class WebSocketController {
    private final UserSessionService userSessionService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessagesRepository chatMessagesRepository;

    // 공용 채팅
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageResponse sendPublicMessage(@Payload ChatMessageResponse chatMessageResponse) {
        return chatMessageResponse;
    }

    // private (방별)
    @MessageMapping("/chat.sendMessage/{roomId}")
    public void sendPrivateMessage(@DestinationVariable UUID roomId,
                                   @Payload ChatMessageResponse chatMessageResponse,
                                   Principal principal) {
        String username = principal.getName(); // 스프링 시큐리티 인증 정보에서 로그인한 사용자 ID/이름 추출

        // DB 저장 시 sender 대신 username
        ChatMessages entity = ChatMessages.builder()
                .content(chatMessageResponse.getContent())
                .sender(username)  // 클라이언트가 보낸 sender 무시하고 서버가 인증된 사용자 정보 사용
                .chatRoom(chatRoomRepository.findById(roomId).orElseThrow())
                .sentAt(LocalDateTime.now())
                .build();
        chatMessagesRepository.save(entity);

        // 클라이언트 전송 시도 (sender는 서버 저장된 값 사용)
        chatMessageResponse.setSender(username);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, chatMessageResponse);
    }



    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageResponse addUser(@Payload ChatMessageResponse chatMessageResponse,
                                       SimpMessageHeaderAccessor headerAccessor) {

        String username = chatMessageResponse.getSender();

        // 닉네임 중복 확인
        if (!userSessionService.addUser(username)) {
            ChatMessageResponse errorMessage = new ChatMessageResponse();
            errorMessage.setType(ChatMessageResponse.MessageType.ERROR);
            errorMessage.setContent("같은 이름이 이미 등록되어 있습니다.");
            return errorMessage;
        }

        // 세션에 유저 이름 저장
        headerAccessor.getSessionAttributes().put("username", username);
        chatMessageResponse.setType(ChatMessageResponse.MessageType.JOIN);
        return chatMessageResponse;
    }
}
