package com.example.demo.controller;

import com.example.demo.config.AppProperties;
import com.example.demo.service.DevOnlyService;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * LESSON 3: Configuration & Profiles Controller
 * Demonstrate cách sử dụng configuration và profiles
 */
@RestController
@RequestMapping("/api/config")
public class ConfigController {
    
    @Autowired
    private AppProperties appProperties;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired(required = false) // Optional vì chỉ có trong dev profile
    private DevOnlyService devOnlyService;
    
    @Autowired
    private Environment environment;
    
    // Inject single property
    @Value("${server.port}")
    private int serverPort;
    
    @Value("${spring.application.name}")
    private String applicationName;
    
    /**
     * Lấy thông tin app và environment
     * GET /api/config/info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getAppInfo() {
        Map<String, Object> info = new HashMap<>();
        
        // Basic info
        info.put("applicationName", applicationName);
        info.put("serverPort", serverPort);
        info.put("activeProfiles", environment.getActiveProfiles());
        info.put("defaultProfiles", environment.getDefaultProfiles());
        
        // App properties
        info.put("appName", appProperties.getName());
        info.put("appVersion", appProperties.getVersion());
        info.put("appDescription", appProperties.getDescription());
        info.put("environment", appProperties.getEnvironment());
        
        // Features
        Map<String, Boolean> features = new HashMap<>();
        features.put("emailEnabled", appProperties.getFeatures().isEmailEnabled());
        features.put("smsEnabled", appProperties.getFeatures().isSmsEnabled());
        features.put("debugMode", appProperties.getFeatures().isDebugMode());
        info.put("features", features);
        
        // External APIs
        Map<String, String> externalApis = new HashMap<>();
        externalApis.put("paymentApi", appProperties.getExternal().getPaymentApi());
        externalApis.put("notificationApi", appProperties.getExternal().getNotificationApi());
        info.put("externalApis", externalApis);
        
        return ResponseEntity.ok(info);
    }
    
    /**
     * Test notification service
     * POST /api/config/test-notification
     */
    @PostMapping("/test-notification")
    public ResponseEntity<Map<String, String>> testNotification(
            @RequestParam String type,
            @RequestParam String recipient,
            @RequestParam String message) {
        
        Map<String, String> response = new HashMap<>();
        
        try {
            String result;
            if ("email".equalsIgnoreCase(type)) {
                result = notificationService.sendEmail(recipient, "Test Subject", message);
            } else if ("sms".equalsIgnoreCase(type)) {
                result = notificationService.sendSms(recipient, message);
            } else {
                throw new IllegalArgumentException("Invalid notification type. Use 'email' or 'sms'");
            }
            
            response.put("status", "success");
            response.put("result", result);
            response.put("appInfo", notificationService.getAppInfo());
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Dev-only endpoints (chỉ có khi chạy với profile dev)
     * GET /api/config/dev/debug
     */
    @GetMapping("/dev/debug")
    public ResponseEntity<?> getDebugInfo() {
        if (devOnlyService == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Dev-only service not available");
            error.put("message", "This endpoint is only available in development profile");
            error.put("activeProfiles", String.join(", ", environment.getActiveProfiles()));
            return ResponseEntity.badRequest().body(error);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("debugInfo", devOnlyService.getDebugInfo());
        response.put("environment", "development");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Reset database (dev only)
     * POST /api/config/dev/reset-db
     */
    @PostMapping("/dev/reset-db")
    public ResponseEntity<?> resetDatabase() {
        if (devOnlyService == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Dev-only service not available");
            error.put("message", "This endpoint is only available in development profile");
            return ResponseEntity.badRequest().body(error);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("result", devOnlyService.resetDatabase());
        response.put("testData", devOnlyService.generateTestData());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Lấy environment variables (để test external config)
     * GET /api/config/env/{key}
     */
    @GetMapping("/env/{key}")
    public ResponseEntity<Map<String, String>> getEnvironmentVariable(@PathVariable String key) {
        Map<String, String> response = new HashMap<>();
        
        String value = environment.getProperty(key);
        if (value != null) {
            response.put("key", key);
            response.put("value", value);
            response.put("source", "environment");
        } else {
            response.put("key", key);
            response.put("message", "Environment variable not found");
        }
        
        return ResponseEntity.ok(response);
    }
}