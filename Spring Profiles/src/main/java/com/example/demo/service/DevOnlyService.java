package com.example.demo.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * LESSON 3: Profile-specific Service
 * Service này chỉ hoạt động trong development environment
 */
@Service
@Profile("dev")
public class DevOnlyService {
    
    /**
     * Debug utilities chỉ có trong dev environment
     */
    public String getDebugInfo() {
        return "🔧 [DEV ONLY] Debug utilities available:\n" +
               "- Memory usage: " + getMemoryUsage() + "\n" +
               "- Active threads: " + Thread.activeCount() + "\n" +
               "- System properties: " + System.getProperties().size() + " items";
    }
    
    /**
     * Simulate database reset (chỉ cho dev)
     */
    public String resetDatabase() {
        return "🗑️ [DEV ONLY] Database reset completed! All test data cleared.";
    }
    
    /**
     * Generate test data
     */
    public String generateTestData() {
        return "🎲 [DEV ONLY] Test data generated:\n" +
               "- 10 sample users created\n" +
               "- 50 sample orders created\n" +
               "- 100 sample products created";
    }
    
    private String getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        return String.format("%.2f MB / %.2f MB", 
            usedMemory / 1024.0 / 1024.0, 
            totalMemory / 1024.0 / 1024.0);
    }
}