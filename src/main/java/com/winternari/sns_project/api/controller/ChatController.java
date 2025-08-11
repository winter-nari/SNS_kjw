package com.winternari.sns_project.api.controller;


import com.winternari.sns_project.domain.chat.dto.response.ChatMessageResponse;
import com.winternari.sns_project.domain.chat.dto.response.ChatRoomSummaryResponse;
import com.winternari.sns_project.domain.chat.entity.ChatMessages;
import com.winternari.sns_project.domain.chat.entity.ChatRoom;
import com.winternari.sns_project.domain.chat.entity.UserRoom;
import com.winternari.sns_project.domain.chat.repository.ChatMessagesRepository;
import com.winternari.sns_project.domain.chat.repository.ChatRoomRepository;
import com.winternari.sns_project.domain.chat.service.ChatService;
import com.winternari.sns_project.global.security.service.UserForSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessagesRepository chatMessagesRepository;

    @PostMapping
    public UUID createOrGetRoom(@RequestParam UUID friendId) {
        return chatRoomService.createOrGetRoom(friendId);
    }

    @GetMapping("/{roomId}/messages")
    public List<ChatMessageResponse> getMessages(@PathVariable UUID roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("채팅방이 존재하지 않습니다."));

        List<ChatMessages> messages = chatMessagesRepository.findByChatRoomOrderBySentAtAsc(room);

        return messages.stream().map(msg -> {
            ChatMessageResponse dto = new ChatMessageResponse();
            dto.setSender(msg.getSender());
            dto.setContent(msg.getContent());
            dto.setType(ChatMessageResponse.MessageType.CHAT);
            return dto;
        }).toList();
    }

    @GetMapping("/my")
    public List<ChatRoomSummaryResponse> getMyRooms(@AuthenticationPrincipal UserForSecurity user) {
        UUID myId = user.getId();
        List<ChatRoom> myRooms = chatRoomRepository.findAllByParticipantId(myId);

        return myRooms.stream().map(room -> {
            // 나를 제외한 상대방 가져오기
            UserRoom other = room.getParticipants().stream()
                    .filter(ur -> !ur.getUser().getId().equals(myId))
                    .findFirst()
                    .orElseThrow(); // 그룹 채팅이면 이 부분 수정 필요

            return new ChatRoomSummaryResponse(
                    room.getId(),
                    other.getUser().getProfile().getUsername(),
                    other.getUser().getEmail()
            );
        }).toList();
    }

    @PostMapping("/{roomId}/invite")
    public void inviteUser(@PathVariable UUID roomId, @RequestParam UUID newUserId) {
        chatRoomService.inviteUser(roomId, newUserId);
    }
}
