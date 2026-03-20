package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * LESSON 8: Spring Boot Application with Redis Caching
 * Main application class với caching enabled
 */
@SpringBootApplication
@EnableCaching
public class SpringBootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
        System.out.println("🚀 Student Management System Started!");
        System.out.println("📊 MongoDB: localhost:27017");
        System.out.println("⚡ Redis: localhost:6379");
        System.out.println("🌐 API: http://localhost:8080");
        System.out.println("🔍 Redis Insight: http://localhost:8001");
    }
}