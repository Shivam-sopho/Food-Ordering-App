package org.example.foodorderingsystem.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class SessionService {
    private final Map<Long, Set<String>> userSessions = new HashMap<>();

    public void registerSession(Long userId, String sessionId) {
        userSessions.computeIfAbsent(userId, k -> new HashSet<>()).add(sessionId);
    }

    public Set<String> getSessions(Long userId) {
        return userSessions.getOrDefault(userId, new HashSet<>());
    }

    public void removeSession(Long userId, String sessionId) {
        Set<String> sessions = userSessions.get(userId);
        if (sessions != null) {
            sessions.remove(sessionId);
            if (sessions.isEmpty()) {
                userSessions.remove(userId);
            }
        }
    }

    public void removeAllSessions(Long userId) {
        userSessions.remove(userId);
    }
}
