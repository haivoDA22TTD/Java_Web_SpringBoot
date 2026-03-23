package com.example.demo.websocket.dto;

import com.example.demo.dto.StudentResponse;

/**
 * LESSON 10: Student Notification DTO
 * Notification message khi có student events
 */
public class StudentNotification extends WebSocketMessage {
    
    private String action; // CREATE, UPDATE, DELETE
    private StudentResponse student;
    private String userId;
    
    public StudentNotification() {
        super();
        setType("STUDENT_NOTIFICATION");
    }
    
    public StudentNotification(String action, StudentResponse student, String userId) {
        this();
        this.action = action;
        this.student = student;
        this.userId = userId;
        setContent(String.format("Student %s: %s", action.toLowerCase(), 
                student != null ? student.getName() : "Unknown"));
        setSender("SYSTEM");
    }
    
    // Getters and Setters
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public StudentResponse getStudent() {
        return student;
    }
    
    public void setStudent(StudentResponse student) {
        this.student = student;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
}