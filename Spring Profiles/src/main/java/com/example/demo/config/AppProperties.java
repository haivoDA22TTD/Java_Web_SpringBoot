package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * LESSON 3: Configuration Properties
 * Bind application.properties values vào Java objects
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    
    private String name;
    private String version;
    private String description;
    private String environment;
    
    private Features features = new Features();
    private External external = new External();
    
    // Nested class cho features
    public static class Features {
        private boolean emailEnabled;
        private boolean smsEnabled;
        private boolean debugMode;
        
        // Getters and Setters
        public boolean isEmailEnabled() {
            return emailEnabled;
        }
        
        public void setEmailEnabled(boolean emailEnabled) {
            this.emailEnabled = emailEnabled;
        }
        
        public boolean isSmsEnabled() {
            return smsEnabled;
        }
        
        public void setSmsEnabled(boolean smsEnabled) {
            this.smsEnabled = smsEnabled;
        }
        
        public boolean isDebugMode() {
            return debugMode;
        }
        
        public void setDebugMode(boolean debugMode) {
            this.debugMode = debugMode;
        }
    }
    
    // Nested class cho external APIs
    public static class External {
        private String paymentApi;
        private String notificationApi;
        
        // Getters and Setters
        public String getPaymentApi() {
            return paymentApi;
        }
        
        public void setPaymentApi(String paymentApi) {
            this.paymentApi = paymentApi;
        }
        
        public String getNotificationApi() {
            return notificationApi;
        }
        
        public void setNotificationApi(String notificationApi) {
            this.notificationApi = notificationApi;
        }
    }
    
    // Main class Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    
    public Features getFeatures() {
        return features;
    }
    
    public void setFeatures(Features features) {
        this.features = features;
    }
    
    public External getExternal() {
        return external;
    }
    
    public void setExternal(External external) {
        this.external = external;
    }
}