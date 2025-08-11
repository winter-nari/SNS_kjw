package com.winternari.sns_project.domain.chat.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_chat_room")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name; // 그룹이면 이름, 1:1이면 null

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserRoom> participants = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessages> messages;

    // 양방향 연관관계 편의 메서드
    public void addParticipant(UserRoom userRoom) {
        participants.add(userRoom);
        userRoom.setChatRoom(this);
    }

    public void removeParticipant(UserRoom userRoom) {
        participants.remove(userRoom);
        userRoom.setChatRoom(null);
    }
}

