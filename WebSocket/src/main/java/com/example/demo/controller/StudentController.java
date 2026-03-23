package com.example.demo.controller;

import com.example.demo.dto.StudentRequest;
import com.example.demo.dto.StudentResponse;
import com.example.demo.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LESSON 8: Student Controller với Redis Caching
 * REST API controller với caching support
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    // ===== BASIC CRUD ENDPOINTS =====
    
    /**
     * GET /api/students - Lấy tất cả students
     */
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    /**
     * GET /api/students/{id} - Lấy student theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable String id) {
        StudentResponse student = studentService.getStudentById(id);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
    }
    
    /**
     * POST /api/students - Tạo student mới
     */
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        try {
            StudentResponse createdStudent = studentService.createStudent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * PUT /api/students/{id} - Cập nhật student
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable String id, 
                                                       @Valid @RequestBody StudentRequest request) {
        try {
            StudentResponse updatedStudent = studentService.updateStudent(id, request);
            return updatedStudent != null ? ResponseEntity.ok(updatedStudent) : ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * DELETE /api/students/{id} - Xóa student
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        try {
            boolean deleted = studentService.deleteStudent(id);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // ===== SEARCH ENDPOINTS =====
    
    /**
     * GET /api/students/search/name?name=xxx - Tìm theo tên
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<StudentResponse>> searchByName(@RequestParam String name) {
        List<StudentResponse> students = studentService.searchByName(name);
        return ResponseEntity.ok(students);
    }
    
    /**
     * GET /api/students/search/age-range?minAge=xx&maxAge=yy - Tìm trong khoảng tuổi
     */
    @GetMapping("/search/age-range")
    public ResponseEntity<List<StudentResponse>> searchByAgeRange(@RequestParam Integer minAge, 
                                                                @RequestParam Integer maxAge) {
        List<StudentResponse> students = studentService.getStudentsByAgeRange(minAge, maxAge);
        return ResponseEntity.ok(students);
    }
    
    /**
     * GET /api/students/statistics - Lấy thống kê students
     */
    @GetMapping("/statistics")
    public ResponseEntity<java.util.Map<String, Object>> getStatistics() {
        java.util.Map<String, Object> stats = studentService.getStudentStatistics();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * POST /api/students/cache/clear - Clear all student caches
     */
    @PostMapping("/cache/clear")
    public ResponseEntity<java.util.Map<String, String>> clearCache() {
        studentService.clearAllCache();
        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("message", "All student caches cleared successfully");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST /api/students/cache/warmup - Warm up student caches
     */
    @PostMapping("/cache/warmup")
    public ResponseEntity<java.util.Map<String, String>> warmUpCache() {
        long startTime = System.currentTimeMillis();
        studentService.warmUpCache();
        long endTime = System.currentTimeMillis();
        
        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("message", "Student caches warmed up successfully");
        response.put("duration", (endTime - startTime) + "ms");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }
}