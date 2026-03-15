package com.example.demo.service;

import com.example.demo.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * LESSON 3: Service sử dụng Configuration Properties
 * Demonstrate cách sử dụng config trong business logic
 */
@Service
public class NotificationService {
    
    @Autowired
    private AppProperties appProperties;
    
    /**
     * Gửi email notification
     */
    public String sendEmail(String to, String subject, String content) {
        if (!appProperties.getFeatures().isEmailEnabled()) {
            return "Email feature is disabled in " + appProperties.getEnvironment() + " environment";
        }
        
        // Giả lập gửi email
        String message = String.format(
            "📧 [%s] Sending email to: %s, Subject: %s, Content: %s", 
            appProperties.getEnvironment().toUpperCase(), 
            to, 
            subject, 
            content
        );
        
        if (appProperties.getFeatures().isDebugMode()) {
            message += " | Debug: Using API " + appProperties.getExternal().getNotificationApi();
        }
        
        return message;
    }
    
    /**
     * Gửi SMS notification
     */
    public String sendSms(String phoneNumber, String message) {
        if (!appProperties.getFeatures().isSmsEnabled()) {
            return "SMS feature is disabled in " + appProperties.getEnvironment() + " environment";
        }
        
        // Giả lập gửi SMS
        String response = String.format(
            "📱 [%s] Sending SMS to: %s, Message: %s", 
            appProperties.getEnvironment().toUpperCase(), 
            phoneNumber, 
            message
        );
        
        if (appProperties.getFeatures().isDebugMode()) {
            response += " | Debug: Using API " + appProperties.getExternal().getNotificationApi();
        }
        
        return response;
    }
    
    /**
     * Lấy thông tin app hiện tại
     */
    public String getAppInfo() {
        return String.format(
            "🚀 App: %s v%s | Environment: %s | Features: Email=%s, SMS=%s, Debug=%s",
            appProperties.getName(),
            appProperties.getVersion(),
            appProperties.getEnvironment(),
            appProperties.getFeatures().isEmailEnabled(),
            appProperties.getFeatures().isSmsEnabled(),
            appProperties.getFeatures().isDebugMode()
        );
    }
}