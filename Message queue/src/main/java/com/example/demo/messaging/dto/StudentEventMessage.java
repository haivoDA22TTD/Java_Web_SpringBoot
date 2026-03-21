package com.example.demo.messaging.dto;

import java.time.Instant;

/**
 * LESSON 9: Student Event Message DTO
 * Message DTO cho student events
 */
public class StudentEventMessage {
    
    private String eventType; // CREATED, UPDATED, DELETED
    private String studentId;
    private String studentName;
    private String studentEmail;
    private Integer studentAge;
    private String timestamp;
    private String userId; // Who performed the action
    
    // Constructors
    public StudentEventMessage() {
        this.timestamp = Instant.now().toString();
    }
    
    public StudentEventMessage(String eventType, String studentId, String studentName, 
                              String studentEmail, Integer studentAge, String userId) {
        this();
        this.eventType = eventType;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentAge = studentAge;
        this.userId = userId;
    }
    
    // Getters and Setters
    public String getEventType() {
        return eventType;
    }
    
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getStudentEmail() {
        return studentEmail;
    }
    
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }
    
    public Integer getStudentAge() {
        return studentAge;
    }
    
    public void setStudentAge(Integer studentAge) {
        this.studentAge = studentAge;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    @Override
    public String toString() {
        return "StudentEventMessage{" +
                "eventType='" + eventType + '\'' +
                ", studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", studentEmail='" + studentEmail + '\'' +
                ", studentAge=" + studentAge +
                ", timestamp='" + timestamp + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}