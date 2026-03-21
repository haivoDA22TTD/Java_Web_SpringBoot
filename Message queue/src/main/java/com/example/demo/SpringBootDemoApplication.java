package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * LESSON 9: Spring Boot Application với Redis Caching và Message Queues
 * Main application class với caching và messaging enabled
 */
@SpringBootApplication
@EnableCaching
public class SpringBootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
        System.out.println("🚀 Student Management System Started!");
        System.out.println("📊 MongoDB: localhost:27017");
        System.out.println("⚡ Redis: localhost:6379");
        System.out.println("🐰 RabbitMQ: localhost:5672");
        System.out.println("🌐 API: http://localhost:8080");
        System.out.println("🔍 Redis Insight: http://localhost:8001");
        System.out.println("🐰 RabbitMQ Management: http://localhost:15672");
    }
}