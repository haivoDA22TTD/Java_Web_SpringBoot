package com.example.demo.service;

import com.example.demo.document.Student;
import com.example.demo.repository.StudentMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * LESSON 5: Student Service - MongoDB
 * Business logic layer cho Student management với MongoDB
 */
@Service
public class StudentService {
    
    @Autowired
    private StudentMongoRepository studentRepository;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    // ===== BASIC CRUD OPERATIONS =====
    
    /**
     * Lấy tất cả students
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    /**
     * Lấy student theo ID
     */
    public Optional<Student> getStudentById(String id) {
        return studentRepository.findById(id);
    }
    
    /**
     * Lấy student theo email
     */
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    
    /**
     * Tạo student mới
     */
    public Student createStudent(Student student) {
        // Kiểm tra email đã tồn tại chưa
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + student.getEmail());
        }
        
        return studentRepository.save(student);
    }
    
    /**
     * Cập nhật student
     */
    public Student updateStudent(String id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với ID: " + id));
        
        // Kiểm tra email trùng với student khác
        if (!student.getEmail().equals(studentDetails.getEmail()) && 
            studentRepository.existsByEmail(studentDetails.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + studentDetails.getEmail());
        }
        
        // Update fields
        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());
        student.setAge(studentDetails.getAge());
        
        return studentRepository.save(student);
    }
    
    /**
     * Xóa student
     */
    public void deleteStudent(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với ID: " + id));
        studentRepository.delete(student);
    }
    
    // ===== SEARCH OPERATIONS =====
    
    /**
     * Tìm students theo tên
     */
    public List<Student> searchStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }
    
    /**
     * Tìm students theo tuổi
     */
    public List<Student> getStudentsByAge(Integer age) {
        return studentRepository.findByAge(age);
    }
    
    /**
     * Tìm students có tuổi từ X trở lên
     */
    public List<Student> getStudentsByMinAge(Integer minAge) {
        return studentRepository.findByAgeGreaterThanEqual(minAge);
    }
    
    /**
     * Tìm students trong khoảng tuổi
     */
    public List<Student> getStudentsByAgeRange(Integer minAge, Integer maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }
    
    /**
     * Tìm students theo tên và tuổi
     */
    public List<Student> searchStudentsByNameAndAge(String name, Integer age) {
        return studentRepository.findByNameAndAge(name, age);
    }
    
    // ===== STATISTICS với MongoDB Aggregation =====
    
    /**
     * Đếm students theo tuổi
     */
    public Long countStudentsByAge(Integer age) {
        return studentRepository.countStudentsByAge(age);
    }
    
    /**
     * Tính tuổi trung bình bằng Aggregation
     */
    public Double calculateAverageAge() {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.group().avg("age").as("averageAge")
        );
        
        AggregationResults<AverageAgeResult> results = mongoTemplate.aggregate(
            aggregation, "students", AverageAgeResult.class
        );
        
        AverageAgeResult result = results.getUniqueMappedResult();
        return result != null ? result.getAverageAge() : 0.0;
    }
    
    /**
     * Tìm students trẻ nhất
     */
    public List<Student> getYoungestStudents() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, "age")).limit(10);
        return mongoTemplate.find(query, Student.class);
    }
    
    /**
     * Tìm students già nhất
     */
    public List<Student> getOldestStudents() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "age")).limit(10);
        return mongoTemplate.find(query, Student.class);
    }
    
    // ===== MONGODB SPECIFIC OPERATIONS =====
    
    /**
     * Tìm students theo regex pattern
     */
    public List<Student> searchStudentsByPattern(String pattern) {
        return studentRepository.findByNameRegex(pattern);
    }
    
    /**
     * Tìm students theo email domain
     */
    public List<Student> getStudentsByEmailDomain(String domain) {
        return studentRepository.findByEmailDomain(".*@" + domain);
    }
    
    /**
     * Đếm tổng số students
     */
    public long getTotalStudentsCount() {
        return studentRepository.count();
    }
    
    // Inner class cho aggregation result
    public static class AverageAgeResult {
        private Double averageAge;
        
        public Double getAverageAge() {
            return averageAge;
        }
        
        public void setAverageAge(Double averageAge) {
            this.averageAge = averageAge;
        }
    }
}