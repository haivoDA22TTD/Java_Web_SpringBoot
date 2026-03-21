package com.example.demo.messaging;

import com.example.demo.messaging.dto.AuditLogMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * LESSON 9: Audit Log Consumer
 * Consumer để xử lý audit logs từ RabbitMQ
 */
@Service
public class AuditLogConsumer {
    
    /**
     * Consumer cho audit log messages
     */
    @RabbitListener(queues = "audit.log")
    public void handleAuditLog(AuditLogMessage message) {
        System.out.println("📝 Received AUDIT_LOG:");
        System.out.println("   Action: " + message.getAction());
        System.out.println("   Entity Type: " + message.getEntityType());
        System.out.println("   Entity ID: " + message.getEntityId());
        System.out.println("   User ID: " + message.getUserId());
        System.out.println("   Timestamp: " + message.getTimestamp());
        
        // Process audit log
        processAuditLog(message);
    }
    
    /**
     * Xử lý audit log message
     */
    private void processAuditLog(AuditLogMessage message) {
        try {
            System.out.println("📊 Processing audit log...");
            
            // Validate audit message
            if (!isValidAuditMessage(message)) {
                System.err.println("❌ Invalid audit message: " + message);
                return;
            }
            
            // Write to audit database
            writeToAuditDatabase(message);
            
            // Write to audit log file
            writeToAuditLogFile(message);
            
            // Check for security alerts
            checkSecurityAlerts(message);
            
            // Update audit statistics
            updateAuditStatistics(message);
            
            System.out.println("✅ Audit log processed successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Error processing audit log: " + e.getMessage());
            handleAuditError(message, e);
        }
    }
    
    /**
     * Validate audit message
     */
    private boolean isValidAuditMessage(AuditLogMessage message) {
        if (message.getAction() == null || message.getAction().trim().isEmpty()) {
            System.err.println("❌ Audit validation failed: 'action' field is empty");
            return false;
        }
        
        if (message.getEntityType() == null || message.getEntityType().trim().isEmpty()) {
            System.err.println("❌ Audit validation failed: 'entityType' field is empty");
            return false;
        }
        
        if (message.getEntityId() == null || message.getEntityId().trim().isEmpty()) {
            System.err.println("❌ Audit validation failed: 'entityId' field is empty");
            return false;
        }
        
        if (message.getUserId() == null || message.getUserId().trim().isEmpty()) {
            System.err.println("❌ Audit validation failed: 'userId' field is empty");
            return false;
        }
        
        return true;
    }
    
    /**
     * Write audit log to database
     */
    private void writeToAuditDatabase(AuditLogMessage message) {
        System.out.println("💾 Writing audit log to database...");
        
        // In production: save to audit database table
        /*
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(message.getAction());
        auditLog.setEntityType(message.getEntityType());
        auditLog.setEntityId(message.getEntityId());
        auditLog.setUserId(message.getUserId());
        auditLog.setTimestamp(message.getTimestamp());
        auditLog.setDetails(message.getDetails());
        auditLog.setIpAddress(message.getIpAddress());
        auditLog.setUserAgent(message.getUserAgent());
        
        auditLogRepository.save(auditLog);
        */
        
        // Simulate database write
        System.out.println("💾 Audit log saved to database:");
        System.out.println("   ID: " + generateAuditId());
        System.out.println("   Action: " + message.getAction());
        System.out.println("   Entity: " + message.getEntityType() + ":" + message.getEntityId());
        System.out.println("   User: " + message.getUserId());
        System.out.println("   Time: " + message.getTimestamp());
    }
    
    /**
     * Write audit log to file
     */
    private void writeToAuditLogFile(AuditLogMessage message) {
        System.out.println("📄 Writing audit log to file...");
        
        String logEntry = formatAuditLogEntry(message);
        
        // In production: write to actual log file
        /*
        try (FileWriter writer = new FileWriter("audit.log", true)) {
            writer.write(logEntry + "\n");
        } catch (IOException e) {
            System.err.println("Failed to write audit log to file: " + e.getMessage());
        }
        */
        
        // Simulate file write
        System.out.println("📄 Audit log entry:");
        System.out.println(logEntry);
    }
    
    /**
     * Format audit log entry
     */
    private String formatAuditLogEntry(AuditLogMessage message) {
        return String.format(
            "[%s] USER=%s ACTION=%s ENTITY=%s:%s%s",
            message.getTimestamp(), // Already a String, no need to format
            message.getUserId(),
            message.getAction(),
            message.getEntityType(),
            message.getEntityId(),
            message.getDetails() != null ? " DETAILS=" + message.getDetails() : ""
        );
    }
    
    /**
     * Check for security alerts
     */
    private void checkSecurityAlerts(AuditLogMessage message) {
        System.out.println("🔍 Checking for security alerts...");
        
        // Check for suspicious activities
        if (isSuspiciousActivity(message)) {
            triggerSecurityAlert(message);
        }
        
        // Check for high-risk actions
        if (isHighRiskAction(message)) {
            notifySecurityTeam(message);
        }
        
        // Check for unusual patterns
        if (isUnusualPattern(message)) {
            flagForReview(message);
        }
    }
    
    /**
     * Check if activity is suspicious
     */
    private boolean isSuspiciousActivity(AuditLogMessage message) {
        // Example suspicious activity checks
        String action = message.getAction().toUpperCase();
        
        // Multiple delete operations
        if (action.contains("DELETE") || action.contains("BULK_DELETE")) {
            System.out.println("⚠️ Suspicious activity detected: " + action);
            return true;
        }
        
        // Admin privilege escalation
        if (action.contains("ADMIN") || action.contains("PRIVILEGE")) {
            System.out.println("⚠️ Privilege escalation detected: " + action);
            return true;
        }
        
        // Data export operations
        if (action.contains("EXPORT") || action.contains("DOWNLOAD_ALL")) {
            System.out.println("⚠️ Data export detected: " + action);
            return true;
        }
        
        return false;
    }
    
    /**
     * Check if action is high-risk
     */
    private boolean isHighRiskAction(AuditLogMessage message) {
        String action = message.getAction().toUpperCase();
        
        return action.contains("DELETE") || 
               action.contains("ADMIN") || 
               action.contains("SECURITY") ||
               action.contains("CONFIG");
    }
    
    /**
     * Check for unusual patterns
     */
    private boolean isUnusualPattern(AuditLogMessage message) {
        // In production: implement pattern detection
        // - Unusual time of access
        // - High frequency of operations
        // - Access from unusual locations
        // - Unusual user behavior patterns
        
        return false; // Simplified for demo
    }
    
    /**
     * Trigger security alert
     */
    private void triggerSecurityAlert(AuditLogMessage message) {
        System.out.println("🚨 SECURITY ALERT TRIGGERED:");
        System.out.println("   Action: " + message.getAction());
        System.out.println("   User: " + message.getUserId());
        System.out.println("   Entity: " + message.getEntityType() + ":" + message.getEntityId());
        System.out.println("   Time: " + message.getTimestamp());
        
        // In production:
        // - Send alert to security team
        // - Log to security incident system
        // - Potentially block user account
        // - Send SMS/email alerts
    }
    
    /**
     * Notify security team
     */
    private void notifySecurityTeam(AuditLogMessage message) {
        System.out.println("📢 Notifying security team about high-risk action:");
        System.out.println("   Action: " + message.getAction());
        System.out.println("   User: " + message.getUserId());
        
        // In production: send notification to security team
    }
    
    /**
     * Flag for review
     */
    private void flagForReview(AuditLogMessage message) {
        System.out.println("🏁 Flagging for manual review:");
        System.out.println("   Action: " + message.getAction());
        System.out.println("   User: " + message.getUserId());
        
        // In production: add to review queue
    }
    
    /**
     * Update audit statistics
     */
    private void updateAuditStatistics(AuditLogMessage message) {
        System.out.println("📊 Updating audit statistics...");
        
        // In production: update metrics
        // - Action counts by type
        // - User activity metrics
        // - Entity modification counts
        // - Time-based statistics
        
        System.out.println("📊 Statistics updated for action: " + message.getAction());
    }
    
    /**
     * Handle audit processing errors
     */
    private void handleAuditError(AuditLogMessage message, Exception error) {
        System.err.println("📝 Audit processing failed:");
        System.err.println("   Message: " + message);
        System.err.println("   Error: " + error.getMessage());
        
        // In production:
        // - Log error to monitoring system
        // - Retry mechanism
        // - Dead letter queue
        // - Alert administrators
        
        // Critical: audit logs should never be lost
        writeToBackupAuditLog(message, error);
    }
    
    /**
     * Write to backup audit log
     */
    private void writeToBackupAuditLog(AuditLogMessage message, Exception error) {
        System.out.println("💾 Writing to backup audit log...");
        
        // In production: write to backup system
        // - Separate database
        // - File system backup
        // - External audit service
        
        System.out.println("💾 Backup audit log entry created");
    }
    
    /**
     * Generate unique audit ID
     */
    private String generateAuditId() {
        return "AUDIT_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    /**
     * Get audit statistics
     */
    public Map<String, Object> getAuditStatistics() {
        // In production: return real statistics from database
        return Map.of(
            "totalAuditLogs", 1250,
            "todayLogs", 45,
            "securityAlerts", 3,
            "highRiskActions", 12,
            "uniqueUsers", 28
        );
    }
}