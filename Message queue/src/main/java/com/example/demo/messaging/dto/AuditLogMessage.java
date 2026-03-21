package com.example.demo.messaging.dto;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * LESSON 9: Audit Log Message DTO
 * Message DTO cho audit logging
 */
public class AuditLogMessage {
    
    private String action;      // CREATE, UPDATE, DELETE, LOGIN, etc.
    private String entityType;  // STUDENT, USER, etc.
    private String entityId;
    private String userId;      // Who performed the action
    private String timestamp;
    private Map<String, Object> details;
    private String ipAddress;
    private String userAgent;
    
    // Constructors
    public AuditLogMessage() {
        this.timestamp = Instant.now().toString();
        this.details = new HashMap<>();
    }
    
    public AuditLogMessage(String action, String entityType, String entityId, String userId) {
        this();
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.userId = userId;
    }
    
    public AuditLogMessage(String action, String entityType, String entityId, 
                          String userId, Map<String, Object> details) {
        this();
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.userId = userId;
        this.details = details != null ? details : new HashMap<>();
    }
    
    // Getters and Setters
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public String getEntityId() {
        return entityId;
    }
    
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public Map<String, Object> getDetails() {
        return details;
    }
    
    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    @Override
    public String toString() {
        return "AuditLogMessage{" +
                "action='" + action + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId='" + entityId + '\'' +
                ", userId='" + userId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", details=" + details +
                ", ipAddress='" + ipAddress + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
}