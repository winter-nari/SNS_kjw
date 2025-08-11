package com.winternari.sns_project.domain.websocket.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserSessionService {
    private final Set<String> usernames = Collections.synchronizedSet(new HashSet<>());

    public boolean addUser(String username) {
        return usernames.add(username); // 이미 있으면 false
    }

    public void removeUser(String username) {
        usernames.remove(username);
    }
}