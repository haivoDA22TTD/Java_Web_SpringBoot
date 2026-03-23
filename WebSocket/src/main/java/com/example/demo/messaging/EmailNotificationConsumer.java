package com.example.demo.messaging;

import com.example.demo.messaging.dto.EmailNotificationMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * LESSON 9: Email Notification Consumer
 * Consumer để xử lý email notifications từ RabbitMQ
 */
@Service
public class EmailNotificationConsumer {
    
    @Value("${spring.mail.enabled:false}")
    private boolean emailEnabled;
    
    @Value("${app.email.from:noreply@studentmanagement.com}")
    private String fromEmail;
    
    /**
     * Consumer cho email notifications
     */
    @RabbitListener(queues = "email.notification")
    public void handleEmailNotification(EmailNotificationMessage message) {
        System.out.println("📧 Received EMAIL_NOTIFICATION:");
        System.out.println("   To: " + message.getTo());
        System.out.println("   Subject: " + message.getSubject());
        System.out.println("   Template: " + message.getTemplate());
        System.out.println("   Timestamp: " + message.getTimestamp());
        
        // Process email notification
        processEmailNotification(message);
    }
    
    /**
     * Xử lý email notification
     */
    private void processEmailNotification(EmailNotificationMessage message) {
        try {
            System.out.println("📤 Processing email notification...");
            
            // Validate email message
            if (!isValidEmailMessage(message)) {
                System.err.println("❌ Invalid email message: " + message);
                return;
            }
            
            // Check if email service is enabled
            if (!emailEnabled) {
                System.out.println("📧 Email service disabled - simulating email send");
                simulateEmailSend(message);
                return;
            }
            
            // Send actual email
            sendEmail(message);
            
            // Log successful send
            logEmailSent(message);
            
            System.out.println("✅ Email notification processed successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Error processing email notification: " + e.getMessage());
            handleEmailError(message, e);
        }
    }
    
    /**
     * Validate email message
     */
    private boolean isValidEmailMessage(EmailNotificationMessage message) {
        if (message.getTo() == null || message.getTo().trim().isEmpty()) {
            System.err.println("❌ Email validation failed: 'to' field is empty");
            return false;
        }
        
        if (message.getSubject() == null || message.getSubject().trim().isEmpty()) {
            System.err.println("❌ Email validation failed: 'subject' field is empty");
            return false;
        }
        
        if (message.getBody() == null || message.getBody().trim().isEmpty()) {
            System.err.println("❌ Email validation failed: 'body' field is empty");
            return false;
        }
        
        // Basic email format validation
        if (!message.getTo().contains("@")) {
            System.err.println("❌ Email validation failed: invalid email format");
            return false;
        }
        
        return true;
    }
    
    /**
     * Simulate email sending (for development)
     */
    private void simulateEmailSend(EmailNotificationMessage message) {
        System.out.println("📧 ========== SIMULATED EMAIL ==========");
        System.out.println("From: " + fromEmail);
        System.out.println("To: " + message.getTo());
        System.out.println("Subject: " + message.getSubject());
        System.out.println("Template: " + (message.getTemplate() != null ? message.getTemplate() : "default"));
        System.out.println("Sent At: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println("----------------------------------------");
        System.out.println(message.getBody());
        System.out.println("========================================");
        
        // Simulate processing time
        try {
            Thread.sleep(100); // 100ms delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Send actual email (production implementation)
     */
    private void sendEmail(EmailNotificationMessage message) {
        System.out.println("📤 Sending actual email...");
        
        // TODO: Implement actual email sending
        // Example with Spring Mail:
        /*
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(message.getTo());
        mailMessage.setSubject(message.getSubject());
        mailMessage.setText(message.getBody());
        
        if (message.getTemplate() != null) {
            // Use template engine (Thymeleaf, Freemarker, etc.)
            String htmlBody = templateEngine.process(message.getTemplate(), message.getVariables());
            // Send HTML email
        }
        
        mailSender.send(mailMessage);
        */
        
        // For now, simulate
        simulateEmailSend(message);
    }
    
    /**
     * Log successful email send
     */
    private void logEmailSent(EmailNotificationMessage message) {
        System.out.println("📝 Email sent successfully:");
        System.out.println("   Recipient: " + message.getTo());
        System.out.println("   Subject: " + message.getSubject());
        System.out.println("   Sent at: " + LocalDateTime.now());
        
        // In production: log to database, file, or monitoring system
    }
    
    /**
     * Handle email sending errors
     */
    private void handleEmailError(EmailNotificationMessage message, Exception error) {
        System.err.println("📧 Email sending failed:");
        System.err.println("   Recipient: " + message.getTo());
        System.err.println("   Subject: " + message.getSubject());
        System.err.println("   Error: " + error.getMessage());
        
        // In production:
        // 1. Log error to monitoring system
        // 2. Retry mechanism (with exponential backoff)
        // 3. Dead letter queue for failed emails
        // 4. Alert administrators
        
        // Simulate retry logic
        retryEmailSend(message, error);
    }
    
    /**
     * Retry email sending
     */
    private void retryEmailSend(EmailNotificationMessage message, Exception originalError) {
        System.out.println("🔄 Scheduling email retry...");
        
        // In production: implement proper retry mechanism
        // - Use RabbitMQ retry headers
        // - Exponential backoff
        // - Maximum retry attempts
        // - Dead letter queue after max retries
        
        System.out.println("📧 Email retry scheduled for: " + message.getTo());
    }
    
    /**
     * Process template-based emails
     */
    private String processEmailTemplate(String template, Map<String, Object> variables) {
        if (template == null || variables == null) {
            return null;
        }
        
        System.out.println("🎨 Processing email template: " + template);
        
        // Simple template processing (in production use Thymeleaf, Freemarker, etc.)
        String processedTemplate = template;
        
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            processedTemplate = processedTemplate.replace(placeholder, value);
        }
        
        return processedTemplate;
    }
    
    /**
     * Send welcome email for new students
     */
    public void sendWelcomeEmail(String studentEmail, String studentName) {
        EmailNotificationMessage message = new EmailNotificationMessage(
            studentEmail,
            "Welcome to Student Management System",
            String.format(
                "Dear %s,\n\n" +
                "Welcome to our Student Management System!\n\n" +
                "Your account has been created successfully. You can now access all student services.\n\n" +
                "If you have any questions, please contact our support team.\n\n" +
                "Best regards,\n" +
                "Student Management Team",
                studentName
            )
        );
        
        // This would normally be called by the MessagePublisher
        handleEmailNotification(message);
    }
    
    /**
     * Send student update notification
     */
    public void sendUpdateNotification(String studentEmail, String studentName) {
        EmailNotificationMessage message = new EmailNotificationMessage(
            studentEmail,
            "Student Information Updated",
            String.format(
                "Dear %s,\n\n" +
                "Your student information has been updated successfully.\n\n" +
                "If you did not make this change, please contact support immediately.\n\n" +
                "Best regards,\n" +
                "Student Management Team",
                studentName
            )
        );
        
        handleEmailNotification(message);
    }
}