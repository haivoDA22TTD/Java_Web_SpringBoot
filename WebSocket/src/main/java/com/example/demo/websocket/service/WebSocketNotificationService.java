package com.example.demo.websocket.service;

import com.example.demo.dto.StudentResponse;
import com.example.demo.websocket.dto.StudentNotification;
import com.example.demo.websocket.dto.SystemNotification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * LESSON 10: WebSocket Notification Service
 * Service để gửi real-time notifications qua WebSocket
 */
@Service
public class WebSocketNotificationService {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    public WebSocketNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    /**
     * Gửi student notification tới tất cả users
     */
    public void sendStudentNotification(String action, StudentResponse student, String userId) {
        StudentNotification notification = new StudentNotification(action, student, userId);
        
        // Broadcast tới tất cả users subscribe /topic/students
        messagingTemplate.convertAndSend("/topic/students", notification);
        
        System.out.println("🔔 Student notification sent: " + action + " - " + 
                          (student != null ? student.getName() : "Unknown"));
    }
    
    /**
     * Gửi system notification tới tất cả users
     */
    public void sendSystemNotification(SystemNotification notification) {
        // Broadcast tới tất cả users subscribe /topic/system
        messagingTemplate.convertAndSend("/topic/system", notification);
        
        System.out.println("🔔 System notification sent: " + notification.getCategory() + 
                          " - " + notification.getContent());
    }
    
    /**
     * Gửi cache notification
     */
    public void sendCacheNotification(String cacheName, String action) {
        SystemNotification notification = new SystemNotification("CACHE", "INFO", 
                String.format("Cache '%s' %s", cacheName, action));
        sendSystemNotification(notification);
    }
    
    /**
     * Gửi queue notification
     */
    public void sendQueueNotification(String queueName, String action, Object data) {
        SystemNotification notification = new SystemNotification("QUEUE", "INFO", 
                String.format("Queue '%s' %s", queueName, action), data);
        sendSystemNotification(notification);
    }
    
    /**
     * Gửi private notification tới user cụ thể
     */
    public void sendPrivateNotification(String userId, String message) {
        SystemNotification notification = new SystemNotification("PRIVATE", "INFO", message);
        
        // Gửi tới user cụ thể subscribe /user/queue/notifications
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", notification);
        
        System.out.println("🔒 Private notification sent to " + userId + ": " + message);
    }
    
    /**
     * Gửi error notification
     */
    public void sendErrorNotification(String error) {
        SystemNotification notification = SystemNotification.systemError(error);
        sendSystemNotification(notification);
    }
    
    /**
     * Gửi user count update
     */
    public void sendUserCountUpdate(int userCount) {
        SystemNotification notification = new SystemNotification("SYSTEM", "INFO", 
                String.format("Active users: %d", userCount), userCount);
        sendSystemNotification(notification);
    }
}