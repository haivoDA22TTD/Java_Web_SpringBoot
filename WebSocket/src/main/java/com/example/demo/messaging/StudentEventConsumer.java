package com.example.demo.messaging;

import com.example.demo.messaging.dto.StudentEventMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * LESSON 9: Student Event Consumer
 * Consumer để xử lý student events từ RabbitMQ
 */
@Service
public class StudentEventConsumer {
    
    /**
     * Consumer cho student created events
     */
    @RabbitListener(queues = "student.created")
    public void handleStudentCreated(StudentEventMessage message) {
        System.out.println("📥 Received STUDENT_CREATED event:");
        System.out.println("   Student ID: " + message.getStudentId());
        System.out.println("   Student Name: " + message.getStudentName());
        System.out.println("   Student Email: " + message.getStudentEmail());
        System.out.println("   Student Age: " + message.getStudentAge());
        System.out.println("   Action By: " + message.getUserId());
        System.out.println("   Timestamp: " + message.getTimestamp());
        
        // Business logic cho student created event
        processStudentCreatedEvent(message);
    }
    
    /**
     * Consumer cho student updated events
     */
    @RabbitListener(queues = "student.updated")
    public void handleStudentUpdated(StudentEventMessage message) {
        System.out.println("📥 Received STUDENT_UPDATED event:");
        System.out.println("   Student ID: " + message.getStudentId());
        System.out.println("   Student Name: " + message.getStudentName());
        System.out.println("   Action By: " + message.getUserId());
        System.out.println("   Timestamp: " + message.getTimestamp());
        
        // Business logic cho student updated event
        processStudentUpdatedEvent(message);
    }
    
    /**
     * Consumer cho student deleted events
     */
    @RabbitListener(queues = "student.deleted")
    public void handleStudentDeleted(StudentEventMessage message) {
        System.out.println("📥 Received STUDENT_DELETED event:");
        System.out.println("   Student ID: " + message.getStudentId());
        System.out.println("   Student Name: " + message.getStudentName());
        System.out.println("   Action By: " + message.getUserId());
        System.out.println("   Timestamp: " + message.getTimestamp());
        
        // Business logic cho student deleted event
        processStudentDeletedEvent(message);
    }
    
    /**
     * Xử lý student created event
     */
    private void processStudentCreatedEvent(StudentEventMessage message) {
        try {
            // Log to audit system
            System.out.println("🔍 Processing student created event...");
            
            // Update search index
            updateSearchIndex(message);
            
            // Send notification to administrators
            notifyAdministrators("New student registered: " + message.getStudentName());
            
            // Update statistics cache
            invalidateStatisticsCache();
            
            System.out.println("✅ Student created event processed successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Error processing student created event: " + e.getMessage());
            // Handle error (retry, dead letter queue, etc.)
        }
    }
    
    /**
     * Xử lý student updated event
     */
    private void processStudentUpdatedEvent(StudentEventMessage message) {
        try {
            System.out.println("🔍 Processing student updated event...");
            
            // Update search index
            updateSearchIndex(message);
            
            // Invalidate related caches
            invalidateStudentCache(message.getStudentId());
            
            // Log change for audit
            logAuditTrail(message, "STUDENT_UPDATED");
            
            System.out.println("✅ Student updated event processed successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Error processing student updated event: " + e.getMessage());
        }
    }
    
    /**
     * Xử lý student deleted event
     */
    private void processStudentDeletedEvent(StudentEventMessage message) {
        try {
            System.out.println("🔍 Processing student deleted event...");
            
            // Remove from search index
            removeFromSearchIndex(message.getStudentId());
            
            // Clean up related data
            cleanupRelatedData(message.getStudentId());
            
            // Update statistics
            invalidateStatisticsCache();
            
            // Log deletion for audit
            logAuditTrail(message, "STUDENT_DELETED");
            
            System.out.println("✅ Student deleted event processed successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Error processing student deleted event: " + e.getMessage());
        }
    }
    
    // Helper methods
    
    private void updateSearchIndex(StudentEventMessage message) {
        // Simulate search index update
        System.out.println("🔍 Updating search index for student: " + message.getStudentId());
        // In real implementation: update Elasticsearch, Solr, etc.
    }
    
    private void removeFromSearchIndex(String studentId) {
        // Simulate search index removal
        System.out.println("🗑️ Removing from search index: " + studentId);
        // In real implementation: remove from Elasticsearch, Solr, etc.
    }
    
    private void notifyAdministrators(String message) {
        // Simulate admin notification
        System.out.println("📢 Admin notification: " + message);
        // In real implementation: send email, Slack notification, etc.
    }
    
    private void invalidateStatisticsCache() {
        // Simulate cache invalidation
        System.out.println("🧹 Invalidating statistics cache");
        // In real implementation: clear Redis cache keys
    }
    
    private void invalidateStudentCache(String studentId) {
        // Simulate cache invalidation
        System.out.println("🧹 Invalidating cache for student: " + studentId);
        // In real implementation: clear specific Redis cache keys
    }
    
    private void cleanupRelatedData(String studentId) {
        // Simulate cleanup
        System.out.println("🧹 Cleaning up related data for student: " + studentId);
        // In real implementation: cleanup files, references, etc.
    }
    
    private void logAuditTrail(StudentEventMessage message, String action) {
        // Simulate audit logging
        System.out.println("📝 Audit log: " + action + " for student " + message.getStudentId() + " by " + message.getUserId());
        // In real implementation: write to audit database, log files, etc.
    }
}