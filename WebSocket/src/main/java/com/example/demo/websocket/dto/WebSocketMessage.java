package com.example.demo.websocket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * LESSON 10: WebSocket Message DTO
 * Base class cho tất cả WebSocket messages
 */
public class WebSocketMessage {
    
    private String type;
    private String content;
    private String sender;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    public WebSocketMessage() {
        this.timestamp = LocalDateTime.now();
    }
    
    public WebSocketMessage(String type, String content, String sender) {
        this();
        this.type = type;
        this.content = content;
        this.sender = sender;
    }
    
    // Getters and Setters
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getSender() {
        return sender;
    }
    
    public void setSender(String sender) {
        this.sender = sender;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}