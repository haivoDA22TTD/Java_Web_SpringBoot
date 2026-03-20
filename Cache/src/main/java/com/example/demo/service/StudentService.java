package com.example.demo.service;

import com.example.demo.document.Student;
import com.example.demo.dto.StudentRequest;
import com.example.demo.dto.StudentResponse;
import com.example.demo.repository.StudentMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * LESSON 8: Student Service với Redis Caching
 * Business logic cho Student management với caching
 */
@Service
public class StudentService {
    
    @Autowired
    private StudentMongoRepository studentRepository;
    
    /**
     * Get all students - Cached for 5 minutes
     */
    @Cacheable(value = "students-list", key = "'all'")
    public List<StudentResponse> getAllStudents() {
        System.out.println("🔍 Cache MISS - Fetching all students from database");
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get student by ID - Cached for 10 minutes
     */
    @Cacheable(value = "students", key = "#id")
    public StudentResponse getStudentById(String id) {
        System.out.println("🔍 Cache MISS - Fetching student " + id + " from database");
        Optional<Student> student = studentRepository.findById(id);
        return student.map(this::convertToResponse).orElse(null);
    }
    
    /**
     * Create student - Cache the result and invalidate list cache
     */
    @Caching(
        put = @CachePut(value = "students", key = "#result.id"),
        evict = @CacheEvict(value = "students-list", allEntries = true)
    )
    public StudentResponse createStudent(StudentRequest request) {
        System.out.println("💾 Creating new student and updating cache");
        Student student = convertToEntity(request);
        Student savedStudent = studentRepository.save(student);
        return convertToResponse(savedStudent);
    }
    
    /**
     * Update student - Update cache and invalidate list cache
     */
    @Caching(
        put = @CachePut(value = "students", key = "#id"),
        evict = @CacheEvict(value = "students-list", allEntries = true)
    )
    public StudentResponse updateStudent(String id, StudentRequest request) {
        System.out.println("✏️ Updating student " + id + " and refreshing cache");
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            student.setName(request.getName());
            student.setEmail(request.getEmail());
            student.setAge(request.getAge());
            Student updatedStudent = studentRepository.save(student);
            return convertToResponse(updatedStudent);
        }
        return null;
    }
    
    /**
     * Delete student - Remove from cache and invalidate list cache
     */
    @Caching(
        evict = {
            @CacheEvict(value = "students", key = "#id"),
            @CacheEvict(value = "students-list", allEntries = true)
        }
    )
    public boolean deleteStudent(String id) {
        System.out.println("🗑️ Deleting student " + id + " and removing from cache");
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Search students by name - Cached for 3 minutes
     */
    @Cacheable(value = "students-search", key = "#name")
    public List<StudentResponse> searchByName(String name) {
        System.out.println("🔍 Cache MISS - Searching students by name: " + name);
        List<Student> students = studentRepository.findByNameContainingIgnoreCase(name);
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get students by age range - Cached for 3 minutes
     */
    @Cacheable(value = "students-search", key = "'age_' + #minAge + '_' + #maxAge")
    public List<StudentResponse> getStudentsByAgeRange(Integer minAge, Integer maxAge) {
        System.out.println("🔍 Cache MISS - Fetching students by age range: " + minAge + "-" + maxAge);
        List<Student> students = studentRepository.findByAgeBetween(minAge, maxAge);
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get student statistics - Cached for 30 minutes (expensive operation)
     */
    @Cacheable(value = "students-stats", key = "'statistics'")
    public Map<String, Object> getStudentStatistics() {
        System.out.println("📊 Cache MISS - Calculating expensive statistics from database");
        
        // Simulate expensive calculation
        try {
            Thread.sleep(1000); // 1 second delay to simulate heavy computation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long totalStudents = studentRepository.count();
        List<Student> allStudents = studentRepository.findAll();
        
        double averageAge = allStudents.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
        
        int minAge = allStudents.stream()
                .mapToInt(Student::getAge)
                .min()
                .orElse(0);
        
        int maxAge = allStudents.stream()
                .mapToInt(Student::getAge)
                .max()
                .orElse(0);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", totalStudents);
        stats.put("averageAge", Math.round(averageAge * 100.0) / 100.0);
        stats.put("minAge", minAge);
        stats.put("maxAge", maxAge);
        stats.put("calculatedAt", System.currentTimeMillis());
        
        return stats;
    }
    
    /**
     * Get paginated students - Cached per page
     */
    @Cacheable(value = "students-list", key = "'page_' + #page + '_' + #size + '_' + #sortBy")
    public Page<StudentResponse> getStudentsPaginated(int page, int size, String sortBy) {
        System.out.println("🔍 Cache MISS - Fetching paginated students: page=" + page + ", size=" + size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage.map(this::convertToResponse);
    }
    
    /**
     * Clear all student caches - Manual cache management
     */
    @CacheEvict(value = {"students", "students-list", "students-search", "students-stats"}, allEntries = true)
    public void clearAllCache() {
        System.out.println("🧹 Clearing all student caches");
    }
    
    /**
     * Warm up cache - Pre-load frequently accessed data
     */
    public void warmUpCache() {
        System.out.println("🔥 Warming up cache with frequently accessed data");
        getAllStudents();
        getStudentStatistics();
    }
    
    // Helper methods
    private StudentResponse convertToResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setName(student.getName());
        response.setEmail(student.getEmail());
        response.setAge(student.getAge());
        return response;
    }
    
    private Student convertToEntity(StudentRequest request) {
        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setAge(request.getAge());
        return student;
    }
}