package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * LESSON 1: Controller đơn giản nhất
 * Học cách tạo REST API cơ bản
 */
@RestController
public class HelloController {
    
    /**
     * API đơn giản nhất - trả về text
     * Truy cập: http://localhost:8080/hello
     */
    @GetMapping("/hello")
    public String hello() {
        return "Xin chào! Đây là Spring Boot đầu tiên của bạn! 🎉";
    }
    
    /**
     * API với parameter
     * Truy cập: http://localhost:8080/hello/Minh
     */
    @GetMapping("/hello/{name}")
    public String helloWithName(@PathVariable String name) {
        return "Xin chào " + name + "! Chào mừng bạn đến với Spring Boot! 👋";
    }
    
    /**
     * API với query parameter
     * Truy cập: http://localhost:8080/greet?name=Minh&age=25
     */
    @GetMapping("/greet")
    public String greet(@RequestParam String name, 
                       @RequestParam(defaultValue = "0") int age) {
        return String.format("Xin chào %s, %d tuổi! Bạn đang học Spring Boot! 📚", name, age);
    }
}