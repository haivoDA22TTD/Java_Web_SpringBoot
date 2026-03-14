package com.example.demo.controller;

import com.example.demo.dto.UserRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LESSON 2: Controller với Validation & Error Handling
 * Học cách validate input và xử lý lỗi chuyên nghiệp
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    // Giả lập database với List
    private List<Map<String, Object>> users = new ArrayList<>();
    private Long nextId = 1L;
    
    /**
     * Tạo user mới với validation
     * POST /api/users
     * 
     * Test với data hợp lệ:
     * {
     *   "name": "Nguyen Van A",
     *   "email": "a@gmail.com", 
     *   "phone": "0123456789",
     *   "age": 25
     * }
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody UserRequest userRequest) {
        
        // Kiểm tra email đã tồn tại chưa
        boolean emailExists = users.stream()
            .anyMatch(user -> user.get("email").equals(userRequest.getEmail()));
        
        if (emailExists) {
            throw new IllegalArgumentException("Email đã tồn tại: " + userRequest.getEmail());
        }
        
        // Tạo user mới
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("id", nextId++);
        newUser.put("name", userRequest.getName());
        newUser.put("email", userRequest.getEmail());
        newUser.put("phone", userRequest.getPhone());
        newUser.put("age", userRequest.getAge());
        
        users.add(newUser);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    
    /**
     * Lấy tất cả users
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        return ResponseEntity.ok(users);
    }
    
    /**
     * Lấy user theo ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Map<String, Object> user = users.stream()
            .filter(u -> u.get("id").equals(id))
            .findFirst()
            .orElse(null);
        
        if (user == null) {
            throw new IllegalArgumentException("Không tìm thấy user với ID: " + id);
        }
        
        return ResponseEntity.ok(user);
    }
    
    /**
     * Cập nhật user
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id, 
            @Valid @RequestBody UserRequest userRequest) {
        
        Map<String, Object> user = users.stream()
            .filter(u -> u.get("id").equals(id))
            .findFirst()
            .orElse(null);
        
        if (user == null) {
            throw new IllegalArgumentException("Không tìm thấy user với ID: " + id);
        }
        
        // Kiểm tra email trùng với user khác
        boolean emailExists = users.stream()
            .anyMatch(u -> !u.get("id").equals(id) && u.get("email").equals(userRequest.getEmail()));
        
        if (emailExists) {
            throw new IllegalArgumentException("Email đã tồn tại: " + userRequest.getEmail());
        }
        
        // Cập nhật thông tin
        user.put("name", userRequest.getName());
        user.put("email", userRequest.getEmail());
        user.put("phone", userRequest.getPhone());
        user.put("age", userRequest.getAge());
        
        return ResponseEntity.ok(user);
    }
    
    /**
     * Xóa user
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        boolean removed = users.removeIf(user -> user.get("id").equals(id));
        
        if (!removed) {
            throw new IllegalArgumentException("Không tìm thấy user với ID: " + id);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đã xóa user thành công");
        response.put("id", id.toString());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * API test lỗi server
     * GET /api/users/test-error
     */
    @GetMapping("/test-error")
    public ResponseEntity<String> testError() {
        // Cố tình tạo lỗi để test GlobalExceptionHandler
        throw new RuntimeException("Đây là lỗi test để kiểm tra error handling");
    }
}