package com.example.demo.websocket.dto;

/**
 * LESSON 10: System Notification DTO
 * Notification cho system events (cache clear, queue status, etc.)
 */
public class SystemNotification extends WebSocketMessage {
    
    private String category; // CACHE, QUEUE, SYSTEM, ERROR
    private String level;    // INFO, WARNING, ERROR
    private Object data;     // Additional data
    
    public SystemNotification() {
        super();
        setType("SYSTEM_NOTIFICATION");
        setSender("SYSTEM");
    }
    
    public SystemNotification(String category, String level, String content) {
        this();
        this.category = category;
        this.level = level;
        setContent(content);
    }
    
    public SystemNotification(String category, String level, String content, Object data) {
        this(category, level, content);
        this.data = data;
    }
    
    // Static factory methods
    public static SystemNotification cacheCleared(String cacheName) {
        return new SystemNotification("CACHE", "INFO", 
                String.format("Cache '%s' has been cleared", cacheName));
    }
    
    public static SystemNotification queueMessage(String queueName, int messageCount) {
        return new SystemNotification("QUEUE", "INFO", 
                String.format("Queue '%s' has %d messages", queueName, messageCount), 
                messageCount);
    }
    
    public static SystemNotification systemError(String error) {
        return new SystemNotification("SYSTEM", "ERROR", 
                String.format("System error: %s", error));
    }
    
    // Getters and Setters
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getLevel() {
        return level;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
}