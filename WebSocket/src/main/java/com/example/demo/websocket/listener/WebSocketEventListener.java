package com.example.demo.websocket.listener;

import com.example.demo.websocket.dto.WebSocketMessage;
import com.example.demo.websocket.service.WebSocketNotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * LESSON 10: WebSocket Event Listener
 * Lắng nghe WebSocket connection events
 */
@Component
public class WebSocketEventListener {
    
    private final SimpMessageSendingOperations messagingTemplate;
    private final WebSocketNotificationService notificationService;
    
    // Track active users
    private final AtomicInteger activeUsers = new AtomicInteger(0);
    private final ConcurrentHashMap<String, String> sessionUsers = new ConcurrentHashMap<>();
    
    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate,
                                WebSocketNotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }
    
    /**
     * Xử lý khi user connect
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        
        // Increment active users count
        int currentUsers = activeUsers.incrementAndGet();
        
        System.out.println("🔗 New WebSocket connection: " + sessionId);
        System.out.println("👥 Active users: " + currentUsers);
        
        // Send user count update
        notificationService.sendUserCountUpdate(currentUsers);
        
        // Send welcome message
        WebSocketMessage welcomeMessage = new WebSocketMessage("SYSTEM_INFO", 
                "Welcome to Student Management System!", "SYSTEM");
        messagingTemplate.convertAndSend("/topic/system", welcomeMessage);
    }
    
    /**
     * Xử lý khi user disconnect
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        
        // Get user info from session
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String room = (String) headerAccessor.getSessionAttributes().get("room");
        
        // Remove from session tracking
        sessionUsers.remove(sessionId);
        
        // Decrement active users count
        int currentUsers = activeUsers.decrementAndGet();
        
        System.out.println("❌ WebSocket connection closed: " + sessionId);
        if (username != null) {
            System.out.println("👤 User disconnected: " + username);
        }
        System.out.println("👥 Active users: " + currentUsers);
        
        // Send user count update
        notificationService.sendUserCountUpdate(currentUsers);
        
        // Send user leave notification to room
        if (username != null && room != null) {
            WebSocketMessage leaveMessage = new WebSocketMessage("USER_LEAVE", 
                    username + " left the room", "SYSTEM");
            messagingTemplate.convertAndSend("/topic/chat/" + room, leaveMessage);
        }
    }
    
    /**
     * Get current active users count
     */
    public int getActiveUsersCount() {
        return activeUsers.get();
    }
    
    /**
     * Get session users map
     */
    public ConcurrentHashMap<String, String> getSessionUsers() {
        return new ConcurrentHashMap<>(sessionUsers);
    }
}