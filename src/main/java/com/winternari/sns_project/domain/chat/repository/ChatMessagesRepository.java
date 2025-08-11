package com.winternari.sns_project.domain.chat.repository;

import com.winternari.sns_project.domain.chat.entity.ChatMessages;
import com.winternari.sns_project.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessagesRepository extends JpaRepository<ChatMessages, UUID> {
    List<ChatMessages> findByChatRoomOrderBySentAtAsc(ChatRoom chatRoom);

}
