package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * LESSON 9: Health Check Controller
 * Simple health check endpoints để test system
 */
@RestController
@RequestMapping("/health")
@CrossOrigin(origins = "*")
public class HealthController {
    
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * Basic health check
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        health.put("application", "Student Management System");
        health.put("version", "Lesson 9 - Message Queues");
        
        return ResponseEntity.ok(health);
    }
    
    /**
     * Detailed system health
     */
    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> health = new HashMap<>();
        
        // Basic info
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        
        // Check Redis
        boolean redisHealthy = checkRedisHealth();
        health.put("redis", redisHealthy ? "UP" : "DOWN");
        
        // Overall status
        health.put("overall", redisHealthy ? "HEALTHY" : "DEGRADED");
        
        return ResponseEntity.ok(health);
    }
    
    private boolean checkRedisHealth() {
        if (redisTemplate == null) {
            return false;
        }
        
        try {
            redisTemplate.opsForValue().set("health-check", "test");
            String value = (String) redisTemplate.opsForValue().get("health-check");
            redisTemplate.delete("health-check");
            return "test".equals(value);
        } catch (Exception e) {
            return false;
        }
    }
}