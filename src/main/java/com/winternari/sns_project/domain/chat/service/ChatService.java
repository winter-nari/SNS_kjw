package com.winternari.sns_project.domain.chat.service;

import java.util.UUID;

public interface ChatService {
    public UUID createOrGetRoom(UUID friendId);
    public void inviteUser(UUID roomId, UUID newUserId);
}
