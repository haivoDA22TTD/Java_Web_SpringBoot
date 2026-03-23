package com.example.demo.controller;

import com.example.demo.websocket.dto.SystemNotification;
import com.example.demo.websocket.listener.WebSocketEventListener;
import com.example.demo.websocket.service.WebSocketNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * LESSON 10: WebSocket Management REST Controller
 * REST API cho WebSocket management và monitoring
 */
@RestController
@RequestMapping("/api/websocket")
@CrossOrigin(origins = "*")
public class WebSocketController {
    
    private final WebSocketNotificationService notificationService;
    private final WebSocketEventListener eventListener;
    
    public WebSocketController(WebSocketNotificationService notificationService,
                             WebSocketEventListener eventListener) {
        this.notificationService = notificationService;
        this.eventListener = eventListener;
    }
    
    /**
     * GET /api/websocket/status - Get WebSocket status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getWebSocketStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("activeUsers", eventListener.getActiveUsersCount());
        status.put("sessionUsers", eventListener.getSessionUsers().size());
        status.put("timestamp", System.currentTimeMillis());
        status.put("status", "ACTIVE");
        
        return ResponseEntity.ok(status);
    }
    
    /**
     * POST /api/websocket/broadcast - Send broadcast message
     */
    @PostMapping("/broadcast")
    public ResponseEntity<Map<String, String>> sendBroadcastMessage(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String category = request.getOrDefault("category", "SYSTEM");
        String level = request.getOrDefault("level", "INFO");
        
        SystemNotification notification = new SystemNotification(category, level, message);
        notificationService.sendSystemNotification(notification);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Broadcast message sent successfully");
        response.put("content", message);
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST /api/websocket/notify/{userId} - Send private notification
     */
    @PostMapping("/notify/{userId}")
    public ResponseEntity<Map<String, String>> sendPrivateNotification(
            @PathVariable String userId,
            @RequestBody Map<String, String> request) {
        
        String message = request.get("message");
        notificationService.sendPrivateNotification(userId, message);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Private notification sent successfully");
        response.put("recipient", userId);
        response.put("content", message);
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST /api/websocket/test/cache - Test cache notification
     */
    @PostMapping("/test/cache")
    public ResponseEntity<Map<String, String>> testCacheNotification() {
        notificationService.sendCacheNotification("test-cache", "cleared for testing");
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cache notification test sent");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST /api/websocket/test/queue - Test queue notification
     */
    @PostMapping("/test/queue")
    public ResponseEntity<Map<String, String>> testQueueNotification() {
        notificationService.sendQueueNotification("test-queue", "processed 5 messages", 5);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Queue notification test sent");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST /api/websocket/test/error - Test error notification
     */
    @PostMapping("/test/error")
    public ResponseEntity<Map<String, String>> testErrorNotification() {
        notificationService.sendErrorNotification("This is a test error notification");
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Error notification test sent");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/websocket/users - Get active users info
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getActiveUsers() {
        Map<String, Object> users = new HashMap<>();
        users.put("count", eventListener.getActiveUsersCount());
        users.put("sessions", eventListener.getSessionUsers());
        users.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(users);
    }
}