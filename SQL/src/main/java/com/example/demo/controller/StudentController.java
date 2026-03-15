package com.example.demo.controller;

import com.example.demo.dto.StudentRequest;
import com.example.demo.dto.StudentResponse;
import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * LESSON 4: Student Controller - Simplified with DTO
 * REST API controller với DTO pattern
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    // ===== HELPER METHODS =====
    
    /**
     * Convert Student entity to StudentResponse DTO
     */
    private StudentResponse convertToResponse(Student student) {
        return new StudentResponse(
            student.getId(),
            student.getName(),
            student.getEmail(),
            student.getAge()
        );
    }
    
    /**
     * Convert StudentRequest DTO to Student entity
     */
    private Student convertToEntity(StudentRequest request) {
        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setAge(request.getAge());
        return student;
    }
    
    // ===== BASIC CRUD ENDPOINTS =====
    
    /**
     * GET /api/students - Lấy tất cả students
     */
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        List<StudentResponse> responses = students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    /**
     * GET /api/students/{id} - Lấy student theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(s -> ResponseEntity.ok(convertToResponse(s)))
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * POST /api/students - Tạo student mới
     */
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        try {
            Student student = convertToEntity(request);
            Student createdStudent = studentService.createStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(createdStudent));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * PUT /api/students/{id} - Cập nhật student
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id, 
                                                       @Valid @RequestBody StudentRequest request) {
        try {
            Student studentDetails = convertToEntity(request);
            Student updatedStudent = studentService.updateStudent(id, studentDetails);
            return ResponseEntity.ok(convertToResponse(updatedStudent));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * DELETE /api/students/{id} - Xóa student
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
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
        List<Student> students = studentService.searchStudentsByName(name);
        List<StudentResponse> responses = students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    /**
     * GET /api/students/search/email?email=xxx - Tìm theo email
     */
    @GetMapping("/search/email")
    public ResponseEntity<StudentResponse> searchByEmail(@RequestParam String email) {
        Optional<Student> student = studentService.getStudentByEmail(email);
        return student.map(s -> ResponseEntity.ok(convertToResponse(s)))
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * GET /api/students/search/age?age=xx - Tìm theo tuổi
     */
    @GetMapping("/search/age")
    public ResponseEntity<List<StudentResponse>> searchByAge(@RequestParam Integer age) {
        List<Student> students = studentService.getStudentsByAge(age);
        List<StudentResponse> responses = students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    /**
     * GET /api/students/search/min-age?minAge=xx - Tìm từ tuổi X trở lên
     */
    @GetMapping("/search/min-age")
    public ResponseEntity<List<StudentResponse>> searchByMinAge(@RequestParam Integer minAge) {
        List<Student> students = studentService.getStudentsByMinAge(minAge);
        List<StudentResponse> responses = students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    /**
     * GET /api/students/search/age-range?minAge=xx&maxAge=yy - Tìm trong khoảng tuổi
     */
    @GetMapping("/search/age-range")
    public ResponseEntity<List<StudentResponse>> searchByAgeRange(@RequestParam Integer minAge, 
                                                                @RequestParam Integer maxAge) {
        List<Student> students = studentService.getStudentsByAgeRange(minAge, maxAge);
        List<StudentResponse> responses = students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    /**
     * GET /api/students/search/name-and-age?name=xxx&age=xx - Tìm theo tên và tuổi
     */
    @GetMapping("/search/name-and-age")
    public ResponseEntity<List<StudentResponse>> searchByNameAndAge(@RequestParam String name, 
                                                                  @RequestParam Integer age) {
        List<Student> students = studentService.searchStudentsByNameAndAge(name, age);
        List<StudentResponse> responses = students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    // ===== STATISTICS ENDPOINTS =====
    
    /**
     * GET /api/students/statistics/count-by-age?age=xx - Đếm theo tuổi
     */
    @GetMapping("/statistics/count-by-age")
    public ResponseEntity<Long> countByAge(@RequestParam Integer age) {
        Long count = studentService.countStudentsByAge(age);
        return ResponseEntity.ok(count);
    }
    
    /**
     * GET /api/students/statistics/average-age - Tuổi trung bình
     */
    @GetMapping("/statistics/average-age")
    public ResponseEntity<Double> getAverageAge() {
        Double averageAge = studentService.calculateAverageAge();
        return ResponseEntity.ok(averageAge);
    }
    
    /**
     * GET /api/students/statistics/youngest - Students trẻ nhất
     */
    @GetMapping("/statistics/youngest")
    public ResponseEntity<List<StudentResponse>> getYoungestStudents() {
        List<Student> students = studentService.getYoungestStudents();
        List<StudentResponse> responses = students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    /**
     * GET /api/students/statistics/oldest - Students già nhất
     */
    @GetMapping("/statistics/oldest")
    public ResponseEntity<List<StudentResponse>> getOldestStudents() {
        List<Student> students = studentService.getOldestStudents();
        List<StudentResponse> responses = students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}