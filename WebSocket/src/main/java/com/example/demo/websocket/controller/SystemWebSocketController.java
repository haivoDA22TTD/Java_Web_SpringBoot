package com.example.demo.websocket.controller;

import com.example.demo.websocket.dto.SystemNotification;
import com.example.demo.websocket.listener.WebSocketEventListener;
import com.example.demo.websocket.service.WebSocketNotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * LESSON 10: System WebSocket Controller
 * Xử lý system monitoring và real-time updates
 */
@Controller
public class SystemWebSocketController {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketEventListener eventListener;
    private final WebSocketNotificationService notificationService;
    
    public SystemWebSocketController(SimpMessagingTemplate messagingTemplate,
                                   WebSocketEventListener eventListener,
                                   WebSocketNotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.eventListener = eventListener;
        this.notificationService = notificationService;
    }
    
    /**
     * Get system status
     * Client gửi tới: /app/system/status
     * Response tới: /topic/system
     */
    @MessageMapping("/system/status")
    @SendTo("/topic/system")
    public SystemNotification getSystemStatus() {
        int activeUsers = eventListener.getActiveUsersCount();
        
        Map<String, Object> systemData = new HashMap<>();
        systemData.put("activeUsers", activeUsers);
        systemData.put("timestamp", System.currentTimeMillis());
        systemData.put("status", "ONLINE");
        
        return new SystemNotification("SYSTEM", "INFO", 
                String.format("System Status: %d active users", activeUsers), systemData);
    }
    
    /**
     * Ping system
     * Client gửi tới: /app/system/ping
     * Response tới: /topic/system
     */
    @MessageMapping("/system/ping")
    @SendTo("/topic/system")
    public SystemNotification pingSystem() {
        return new SystemNotification("SYSTEM", "INFO", "Pong! System is alive", 
                System.currentTimeMillis());
    }
    
    /**
     * Request user count update
     * Client gửi tới: /app/system/usercount
     */
    @MessageMapping("/system/usercount")
    public void requestUserCount() {
        int activeUsers = eventListener.getActiveUsersCount();
        notificationService.sendUserCountUpdate(activeUsers);
    }
}