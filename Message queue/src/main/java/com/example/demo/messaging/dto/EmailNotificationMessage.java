package com.example.demo.messaging.dto;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * LESSON 9: Email Notification Message DTO
 * Message DTO cho email notifications
 */
public class EmailNotificationMessage {
    
    private String to;
    private String subject;
    private String body;
    private String template;
    private Map<String, Object> variables;
    private String timestamp;
    
    // Constructors
    public EmailNotificationMessage() {
        this.timestamp = Instant.now().toString();
        this.variables = new HashMap<>();
    }
    
    public EmailNotificationMessage(String to, String subject, String body) {
        this();
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.template = "default";
    }
    
    public EmailNotificationMessage(String to, String subject, String body, 
                                   String template, Map<String, Object> variables) {
        this();
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.template = template;
        this.variables = variables != null ? variables : new HashMap<>();
    }
    
    // Getters and Setters
    public String getTo() {
        return to;
    }
    
    public void setTo(String to) {
        this.to = to;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public String getTemplate() {
        return template;
    }
    
    public void setTemplate(String template) {
        this.template = template;
    }
    
    public Map<String, Object> getVariables() {
        return variables;
    }
    
    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "EmailNotificationMessage{" +
                "to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", template='" + template + '\'' +
                ", variables=" + variables +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}