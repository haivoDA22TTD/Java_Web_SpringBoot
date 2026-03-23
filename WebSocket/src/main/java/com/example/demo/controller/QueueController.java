package com.example.demo.controller;

import com.example.demo.messaging.MessagePublisher;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * LESSON 9: Queue Management Controller
 * REST API cho RabbitMQ queue management và monitoring
 */
@RestController
@RequestMapping("/api/queues")
@CrossOrigin(origins = "*")
public class QueueController {
    
    @Autowired
    private MessagePublisher messagePublisher;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private AmqpAdmin amqpAdmin;
    
    /**
     * GET /api/queues/info - Get queue information
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getQueueInfo() {
        Map<String, Object> queueInfo = new HashMap<>();
        
        try {
            // Get queue information
            Map<String, Object> queues = new HashMap<>();
            
            // Student event queues
            queues.put("student.created", getQueueStats("student.created"));
            queues.put("student.updated", getQueueStats("student.updated"));
            queues.put("student.deleted", getQueueStats("student.deleted"));
            
            // Notification queues
            queues.put("email.notification", getQueueStats("email.notification"));
            
            // Audit queues
            queues.put("audit.log", getQueueStats("audit.log"));
            
            queueInfo.put("queues", queues);
            queueInfo.put("exchanges", getExchangeInfo());
            queueInfo.put("connections", getConnectionInfo());
            queueInfo.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(queueInfo);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to get queue information");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * GET /api/queues/stats - Get queue statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getQueueStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // Overall statistics
            stats.put("totalQueues", 5);
            stats.put("totalExchanges", 2);
            stats.put("activeConnections", 1);
            
            // Message counts
            Map<String, Object> messageCounts = new HashMap<>();
            messageCounts.put("student.created", getMessageCount("student.created"));
            messageCounts.put("student.updated", getMessageCount("student.updated"));
            messageCounts.put("student.deleted", getMessageCount("student.deleted"));
            messageCounts.put("email.notification", getMessageCount("email.notification"));
            messageCounts.put("audit.log", getMessageCount("audit.log"));
            
            stats.put("messageCounts", messageCounts);
            stats.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to get queue statistics");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * POST /api/queues/test/student-created - Test student created message
     */
    @PostMapping("/test/student-created")
    public ResponseEntity<Map<String, String>> testStudentCreatedMessage(@RequestBody Map<String, Object> testData) {
        try {
            String studentId = (String) testData.getOrDefault("studentId", "test-student-123");
            String studentName = (String) testData.getOrDefault("studentName", "Test Student");
            String studentEmail = (String) testData.getOrDefault("studentEmail", "test@example.com");
            Integer studentAge = (Integer) testData.getOrDefault("studentAge", 25);
            String userId = (String) testData.getOrDefault("userId", "test-user");
            
            // Create test student response
            com.example.demo.dto.StudentResponse testStudent = new com.example.demo.dto.StudentResponse();
            testStudent.setId(studentId);
            testStudent.setName(studentName);
            testStudent.setEmail(studentEmail);
            testStudent.setAge(studentAge);
            
            // Publish test message
            messagePublisher.publishStudentCreated(testStudent, userId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Test student created message sent successfully");
            response.put("studentId", studentId);
            response.put("queue", "student.created");
            response.put("timestamp", String.valueOf(System.currentTimeMillis()));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to send test message");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * POST /api/queues/test/email - Test email notification
     */
    @PostMapping("/test/email")
    public ResponseEntity<Map<String, String>> testEmailNotification(@RequestBody Map<String, String> emailData) {
        try {
            String to = emailData.getOrDefault("to", "test@example.com");
            String subject = emailData.getOrDefault("subject", "Test Email Notification");
            String body = emailData.getOrDefault("body", "This is a test email notification from the queue system.");
            
            // Publish test email
            messagePublisher.publishEmailNotification(to, subject, body);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Test email notification sent successfully");
            response.put("to", to);
            response.put("subject", subject);
            response.put("queue", "email.notification");
            response.put("timestamp", String.valueOf(System.currentTimeMillis()));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to send test email");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * POST /api/queues/test/audit - Test audit log
     */
    @PostMapping("/test/audit")
    public ResponseEntity<Map<String, String>> testAuditLog(@RequestBody Map<String, String> auditData) {
        try {
            String action = auditData.getOrDefault("action", "TEST_ACTION");
            String entityType = auditData.getOrDefault("entityType", "TestEntity");
            String entityId = auditData.getOrDefault("entityId", "test-entity-123");
            String userId = auditData.getOrDefault("userId", "test-user");
            
            // Publish test audit log
            messagePublisher.publishAuditLog(action, entityType, entityId, userId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Test audit log sent successfully");
            response.put("action", action);
            response.put("entityType", entityType);
            response.put("entityId", entityId);
            response.put("userId", userId);
            response.put("queue", "audit.log");
            response.put("timestamp", String.valueOf(System.currentTimeMillis()));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to send test audit log");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * POST /api/queues/test/custom - Send custom test message
     */
    @PostMapping("/test/custom")
    public ResponseEntity<Map<String, String>> sendCustomTestMessage(@RequestBody Map<String, Object> messageData) {
        try {
            String queueName = (String) messageData.getOrDefault("queue", "student.created");
            Object message = messageData.getOrDefault("message", "Test message");
            
            // Send custom test message
            messagePublisher.sendTestMessage(queueName, message);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Custom test message sent successfully");
            response.put("queue", queueName);
            response.put("timestamp", String.valueOf(System.currentTimeMillis()));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to send custom test message");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * GET /api/queues/health - Check queue health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> checkQueueHealth() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            // Check RabbitMQ connection
            boolean rabbitMQHealthy = checkRabbitMQConnection();
            
            // Check individual queues
            Map<String, Boolean> queueHealth = new HashMap<>();
            queueHealth.put("student.created", checkQueueHealth("student.created"));
            queueHealth.put("student.updated", checkQueueHealth("student.updated"));
            queueHealth.put("student.deleted", checkQueueHealth("student.deleted"));
            queueHealth.put("email.notification", checkQueueHealth("email.notification"));
            queueHealth.put("audit.log", checkQueueHealth("audit.log"));
            
            health.put("rabbitMQConnection", rabbitMQHealthy);
            health.put("queues", queueHealth);
            health.put("overallHealth", rabbitMQHealthy && queueHealth.values().stream().allMatch(Boolean::booleanValue));
            health.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(health);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Health check failed");
            error.put("message", e.getMessage());
            error.put("healthy", false);
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    // Helper methods
    
    private Map<String, Object> getQueueStats(String queueName) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            Properties queueProperties = amqpAdmin.getQueueProperties(queueName);
            if (queueProperties != null) {
                stats.put("messageCount", queueProperties.get("QUEUE_MESSAGE_COUNT"));
                stats.put("consumerCount", queueProperties.get("QUEUE_CONSUMER_COUNT"));
                stats.put("exists", true);
            } else {
                stats.put("messageCount", 0);
                stats.put("consumerCount", 0);
                stats.put("exists", false);
            }
        } catch (Exception e) {
            stats.put("error", e.getMessage());
            stats.put("exists", false);
        }
        
        return stats;
    }
    
    private int getMessageCount(String queueName) {
        try {
            Properties queueProperties = amqpAdmin.getQueueProperties(queueName);
            if (queueProperties != null) {
                Object messageCount = queueProperties.get("QUEUE_MESSAGE_COUNT");
                return messageCount != null ? (Integer) messageCount : 0;
            }
        } catch (Exception e) {
            System.err.println("Error getting message count for queue " + queueName + ": " + e.getMessage());
        }
        return 0;
    }
    
    private Map<String, Object> getExchangeInfo() {
        Map<String, Object> exchanges = new HashMap<>();
        
        exchanges.put("student.exchange", Map.of(
            "type", "topic",
            "durable", true,
            "autoDelete", false
        ));
        
        exchanges.put("notification.exchange", Map.of(
            "type", "topic", 
            "durable", true,
            "autoDelete", false
        ));
        
        return exchanges;
    }
    
    private Map<String, Object> getConnectionInfo() {
        Map<String, Object> connections = new HashMap<>();
        
        // Simplified connection info
        connections.put("active", 1);
        connections.put("host", "localhost");
        connections.put("port", 5672);
        connections.put("virtualHost", "/");
        
        return connections;
    }
    
    private boolean checkRabbitMQConnection() {
        try {
            // Simple connection test
            rabbitTemplate.execute(channel -> {
                return channel.isOpen();
            });
            return true;
        } catch (Exception e) {
            System.err.println("RabbitMQ connection check failed: " + e.getMessage());
            return false;
        }
    }
    
    private boolean checkQueueHealth(String queueName) {
        try {
            Properties queueProperties = amqpAdmin.getQueueProperties(queueName);
            return queueProperties != null;
        } catch (Exception e) {
            System.err.println("Queue health check failed for " + queueName + ": " + e.getMessage());
            return false;
        }
    }
}