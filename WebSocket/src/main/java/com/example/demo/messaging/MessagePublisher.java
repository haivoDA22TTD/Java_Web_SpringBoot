package com.example.demo.messaging;

import com.example.demo.config.RabbitConfig;
import com.example.demo.dto.StudentResponse;
import com.example.demo.messaging.dto.AuditLogMessage;
import com.example.demo.messaging.dto.EmailNotificationMessage;
import com.example.demo.messaging.dto.StudentEventMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * LESSON 9: Message Publisher Service
 * Service để publish messages tới RabbitMQ queues
 */
@Service
public class MessagePublisher {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * Publish student created event
     */
    public void publishStudentCreated(StudentResponse student, String userId) {
        StudentEventMessage message = new StudentEventMessage(
            "CREATED",
            student.getId(),
            student.getName(),
            student.getEmail(),
            student.getAge(),
            userId != null ? userId : "system"
        );
        
        rabbitTemplate.convertAndSend(
            RabbitConfig.STUDENT_EXCHANGE,
            RabbitConfig.STUDENT_CREATED_ROUTING_KEY,
            message
        );
        
        System.out.println("📤 Published STUDENT_CREATED event for: " + student.getName());
    }
    
    /**
     * Publish student updated event
     */
    public void publishStudentUpdated(StudentResponse student, String userId) {
        StudentEventMessage message = new StudentEventMessage(
            "UPDATED",
            student.getId(),
            student.getName(),
            student.getEmail(),
            student.getAge(),
            userId != null ? userId : "system"
        );
        
        rabbitTemplate.convertAndSend(
            RabbitConfig.STUDENT_EXCHANGE,
            RabbitConfig.STUDENT_UPDATED_ROUTING_KEY,
            message
        );
        
        System.out.println("📤 Published STUDENT_UPDATED event for: " + student.getName());
    }
    
    /**
     * Publish student deleted event
     */
    public void publishStudentDeleted(String studentId, String studentName, String userId) {
        StudentEventMessage message = new StudentEventMessage(
            "DELETED",
            studentId,
            studentName,
            null, // Email not needed for delete
            null, // Age not needed for delete
            userId != null ? userId : "system"
        );
        
        rabbitTemplate.convertAndSend(
            RabbitConfig.STUDENT_EXCHANGE,
            RabbitConfig.STUDENT_DELETED_ROUTING_KEY,
            message
        );
        
        System.out.println("📤 Published STUDENT_DELETED event for: " + studentName);
    }
    
    /**
     * Publish email notification
     */
    public void publishEmailNotification(String to, String subject, String body) {
        EmailNotificationMessage message = new EmailNotificationMessage(to, subject, body);
        
        rabbitTemplate.convertAndSend(
            RabbitConfig.NOTIFICATION_EXCHANGE,
            RabbitConfig.EMAIL_SEND_ROUTING_KEY,
            message
        );
        
        System.out.println("📧 Published EMAIL_NOTIFICATION to: " + to);
    }
    
    /**
     * Publish email notification with template
     */
    public void publishEmailNotification(String to, String subject, String body, 
                                       String template, Map<String, Object> variables) {
        EmailNotificationMessage message = new EmailNotificationMessage(
            to, subject, body, template, variables
        );
        
        rabbitTemplate.convertAndSend(
            RabbitConfig.NOTIFICATION_EXCHANGE,
            RabbitConfig.EMAIL_SEND_ROUTING_KEY,
            message
        );
        
        System.out.println("📧 Published EMAIL_NOTIFICATION (template: " + template + ") to: " + to);
    }
    
    /**
     * Publish audit log
     */
    public void publishAuditLog(String action, String entityType, String entityId, String userId) {
        AuditLogMessage message = new AuditLogMessage(action, entityType, entityId, userId);
        
        rabbitTemplate.convertAndSend(
            RabbitConfig.STUDENT_EXCHANGE,
            RabbitConfig.AUDIT_LOG_ROUTING_KEY,
            message
        );
        
        System.out.println("📝 Published AUDIT_LOG: " + action + " on " + entityType + " by " + userId);
    }
    
    /**
     * Publish audit log with details
     */
    public void publishAuditLog(String action, String entityType, String entityId, 
                               String userId, Map<String, Object> details) {
        AuditLogMessage message = new AuditLogMessage(action, entityType, entityId, userId, details);
        
        rabbitTemplate.convertAndSend(
            RabbitConfig.STUDENT_EXCHANGE,
            RabbitConfig.AUDIT_LOG_ROUTING_KEY,
            message
        );
        
        System.out.println("📝 Published AUDIT_LOG: " + action + " on " + entityType + " by " + userId);
    }
    
    /**
     * Send test message to any queue
     */
    public void sendTestMessage(String queueName, Object message) {
        rabbitTemplate.convertAndSend(queueName, message);
        System.out.println("🧪 Sent test message to queue: " + queueName);
    }
    
    /**
     * Send welcome email for new student
     */
    public void sendWelcomeEmail(StudentResponse student) {
        String subject = "Welcome to Student Management System";
        String body = String.format(
            "Hello %s,\n\n" +
            "Welcome to our Student Management System!\n\n" +
            "Your account has been created successfully with the following details:\n" +
            "- Name: %s\n" +
            "- Email: %s\n" +
            "- Age: %d\n\n" +
            "You can now access all student services.\n\n" +
            "Best regards,\n" +
            "Student Management Team",
            student.getName(),
            student.getName(),
            student.getEmail(),
            student.getAge()
        );
        
        publishEmailNotification(student.getEmail(), subject, body);
    }
    
    /**
     * Send student update notification
     */
    public void sendUpdateNotification(StudentResponse student) {
        String subject = "Student Information Updated";
        String body = String.format(
            "Hello %s,\n\n" +
            "Your student information has been updated successfully.\n\n" +
            "Updated details:\n" +
            "- Name: %s\n" +
            "- Email: %s\n" +
            "- Age: %d\n\n" +
            "If you did not make this change, please contact support.\n\n" +
            "Best regards,\n" +
            "Student Management Team",
            student.getName(),
            student.getName(),
            student.getEmail(),
            student.getAge()
        );
        
        publishEmailNotification(student.getEmail(), subject, body);
    }
}