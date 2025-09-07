package com.example.chatti.service;

import com.example.chatti.controller.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class HeartbeatService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private WebSocketController webSocketController;

    public static final String ONLINE_USERS_KEY = "online_users";
    public static final String USER_SESSIONS_KEY = "user_sessions";
    public static final String HEARTBEAT_KEY_PREFIX = "user_heartbeat:";

    @Scheduled(fixedRate = 5000)
    public void checkForStaleUsers() {
        System.out.println("Running stale user check...");
        Set<String> onlineUsers = redisTemplate.opsForSet().members(ONLINE_USERS_KEY);
        if (onlineUsers == null || onlineUsers.isEmpty()) {
            return;
        }

        for (String username : onlineUsers) {
            Boolean hasHeartbeat = redisTemplate.hasKey(HEARTBEAT_KEY_PREFIX + username);

            if (hasHeartbeat == null || !hasHeartbeat) {
                System.out.println("Stale user detected (no heartbeat): " + username + ". Disconnecting.");
                String sessionId = (String) redisTemplate.opsForHash().get(USER_SESSIONS_KEY, username);
                if (sessionId != null) {
                    webSocketController.handleUserDisconnect(sessionId);
                } else {
                    redisTemplate.opsForSet().remove(ONLINE_USERS_KEY, username);
                }
            }
        }
    }
}