package com.example.demo.service;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * LESSON 4: Student Service - Simplified
 * Business logic layer cho Student management với 3 trường cơ bản
 */
@Service
@Transactional
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    // ===== BASIC CRUD OPERATIONS =====
    
    /**
     * Lấy tất cả students
     */
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    /**
     * Lấy student theo ID
     */
    @Transactional(readOnly = true)
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
    /**
     * Lấy student theo email
     */
    @Transactional(readOnly = true)
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
    public Student updateStudent(Long id, Student studentDetails) {
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
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với ID: " + id));
        studentRepository.delete(student);
    }
    
    // ===== SEARCH OPERATIONS =====
    
    /**
     * Tìm students theo tên
     */
    @Transactional(readOnly = true)
    public List<Student> searchStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }
    
    /**
     * Tìm students theo tuổi
     */
    @Transactional(readOnly = true)
    public List<Student> getStudentsByAge(Integer age) {
        return studentRepository.findByAge(age);
    }
    
    /**
     * Tìm students có tuổi từ X trở lên
     */
    @Transactional(readOnly = true)
    public List<Student> getStudentsByMinAge(Integer minAge) {
        return studentRepository.findByAgeGreaterThanEqual(minAge);
    }
    
    /**
     * Tìm students trong khoảng tuổi
     */
    @Transactional(readOnly = true)
    public List<Student> getStudentsByAgeRange(Integer minAge, Integer maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }
    
    /**
     * Tìm students theo tên và tuổi
     */
    @Transactional(readOnly = true)
    public List<Student> searchStudentsByNameAndAge(String name, Integer age) {
        return studentRepository.findByNameAndAge(name, age);
    }
    
    // ===== STATISTICS =====
    
    /**
     * Đếm students theo tuổi
     */
    @Transactional(readOnly = true)
    public Long countStudentsByAge(Integer age) {
        return studentRepository.countStudentsByAge(age);
    }
    
    /**
     * Tính tuổi trung bình
     */
    @Transactional(readOnly = true)
    public Double calculateAverageAge() {
        return studentRepository.calculateAverageAge();
    }
    
    /**
     * Tìm students trẻ nhất
     */
    @Transactional(readOnly = true)
    public List<Student> getYoungestStudents() {
        return studentRepository.findYoungestStudents();
    }
    
    /**
     * Tìm students già nhất
     */
    @Transactional(readOnly = true)
    public List<Student> getOldestStudents() {
        return studentRepository.findOldestStudents();
    }
}