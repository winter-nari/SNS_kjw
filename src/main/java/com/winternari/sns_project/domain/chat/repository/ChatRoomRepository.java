package com.winternari.sns_project.domain.chat.repository;

import com.winternari.sns_project.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
    @Query("""
    SELECT r FROM ChatRoom r
    WHERE EXISTS (
        SELECT ur1 FROM UserRoom ur1
        WHERE ur1.chatRoom = r AND ur1.user.id = :userAId
    )
    AND EXISTS (
        SELECT ur2 FROM UserRoom ur2
        WHERE ur2.chatRoom = r AND ur2.user.id = :userBId
    )
""")
    ChatRoom findRoomByParticipants(@Param("userAId") UUID userAId, @Param("userBId") UUID userBId);

    @Query("""
    SELECT r FROM ChatRoom r
    JOIN r.participants p
    WHERE p.user.id = :userId
""")
    List<ChatRoom> findAllByParticipantId(@Param("userId") UUID userId);

}
