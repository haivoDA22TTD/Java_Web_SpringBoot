package com.example.demo.controller;

import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * LESSON 8: Cache Management Controller
 * REST API cho cache management và monitoring
 */
@RestController
@RequestMapping("/api/cache")
@CrossOrigin(origins = "*")
public class CacheController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * GET /api/cache/info - Get cache information
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getCacheInfo() {
        Map<String, Object> cacheInfo = new HashMap<>();
        
        // Get cache names
        cacheInfo.put("cacheNames", cacheManager.getCacheNames());
        
        // Get Redis info
        Set<String> keys = redisTemplate.keys("*");
        cacheInfo.put("totalKeys", keys != null ? keys.size() : 0);
        cacheInfo.put("redisKeys", keys);
        
        // Cache statistics
        Map<String, Object> stats = new HashMap<>();
        stats.put("studentsCache", "Individual student records");
        stats.put("studentsListCache", "Student lists and pagination");
        stats.put("studentsSearchCache", "Search results");
        stats.put("studentsStatsCache", "Statistics and aggregations");
        cacheInfo.put("cacheDescriptions", stats);
        
        return ResponseEntity.ok(cacheInfo);
    }
    
    /**
     * POST /api/cache/clear - Clear all caches
     */
    @PostMapping("/clear")
    public ResponseEntity<Map<String, String>> clearAllCaches() {
        studentService.clearAllCache();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "All caches cleared successfully");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST /api/cache/clear/{cacheName} - Clear specific cache
     */
    @PostMapping("/clear/{cacheName}")
    public ResponseEntity<Map<String, String>> clearSpecificCache(@PathVariable String cacheName) {
        try {
            if (cacheManager.getCache(cacheName) != null) {
                cacheManager.getCache(cacheName).clear();
                
                Map<String, String> response = new HashMap<>();
                response.put("message", "Cache '" + cacheName + "' cleared successfully");
                response.put("cacheName", cacheName);
                response.put("timestamp", String.valueOf(System.currentTimeMillis()));
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Cache not found");
                error.put("cacheName", cacheName);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to clear cache");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * POST /api/cache/warmup - Warm up caches
     */
    @PostMapping("/warmup")
    public ResponseEntity<Map<String, String>> warmUpCaches() {
        long startTime = System.currentTimeMillis();
        
        studentService.warmUpCache();
        
        long endTime = System.currentTimeMillis();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cache warmed up successfully");
        response.put("duration", (endTime - startTime) + "ms");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/cache/keys - Get all Redis keys
     */
    @GetMapping("/keys")
    public ResponseEntity<Map<String, Object>> getAllKeys() {
        Set<String> keys = redisTemplate.keys("*");
        
        Map<String, Object> response = new HashMap<>();
        response.put("totalKeys", keys != null ? keys.size() : 0);
        response.put("keys", keys);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/cache/keys/{pattern} - Get keys by pattern
     */
    @GetMapping("/keys/{pattern}")
    public ResponseEntity<Map<String, Object>> getKeysByPattern(@PathVariable String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        
        Map<String, Object> response = new HashMap<>();
        response.put("pattern", pattern);
        response.put("matchingKeys", keys != null ? keys.size() : 0);
        response.put("keys", keys);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/cache/value/{key} - Get cached value by key
     */
    @GetMapping("/value/{key}")
    public ResponseEntity<Map<String, Object>> getCachedValue(@PathVariable String key) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            Long ttl = redisTemplate.getExpire(key);
            
            Map<String, Object> response = new HashMap<>();
            response.put("key", key);
            response.put("value", value);
            response.put("ttl", ttl);
            response.put("exists", value != null);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to get cached value");
            error.put("key", key);
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * DELETE /api/cache/key/{key} - Delete specific key
     */
    @DeleteMapping("/key/{key}")
    public ResponseEntity<Map<String, String>> deleteKey(@PathVariable String key) {
        try {
            Boolean deleted = redisTemplate.delete(key);
            
            Map<String, String> response = new HashMap<>();
            response.put("key", key);
            response.put("deleted", deleted.toString());
            response.put("message", deleted ? "Key deleted successfully" : "Key not found");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete key");
            error.put("key", key);
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * GET /api/cache/stats - Get cache statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Redis info
        Set<String> allKeys = redisTemplate.keys("*");
        Set<String> studentKeys = redisTemplate.keys("students:*");
        Set<String> listKeys = redisTemplate.keys("students-list:*");
        Set<String> searchKeys = redisTemplate.keys("students-search:*");
        Set<String> statsKeys = redisTemplate.keys("students-stats:*");
        
        stats.put("totalKeys", allKeys != null ? allKeys.size() : 0);
        stats.put("studentKeys", studentKeys != null ? studentKeys.size() : 0);
        stats.put("listKeys", listKeys != null ? listKeys.size() : 0);
        stats.put("searchKeys", searchKeys != null ? searchKeys.size() : 0);
        stats.put("statsKeys", statsKeys != null ? statsKeys.size() : 0);
        
        // Cache breakdown
        Map<String, Object> breakdown = new HashMap<>();
        breakdown.put("students", studentKeys != null ? studentKeys.size() : 0);
        breakdown.put("students-list", listKeys != null ? listKeys.size() : 0);
        breakdown.put("students-search", searchKeys != null ? searchKeys.size() : 0);
        breakdown.put("students-stats", statsKeys != null ? statsKeys.size() : 0);
        stats.put("cacheBreakdown", breakdown);
        
        stats.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(stats);
    }
}