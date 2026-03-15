package com.example.demo.repository;

import com.example.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * LESSON 4: Student Repository - Simplified
 * Repository đơn giản với 3 trường cơ bản
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // ===== BASIC QUERY METHODS =====
    
    /**
     * Tìm student theo email
     */
    Optional<Student> findByEmail(String email);
    
    /**
     * Kiểm tra email đã tồn tại chưa
     */
    boolean existsByEmail(String email);
    
    /**
     * Tìm students theo tên (không phân biệt hoa thường)
     */
    List<Student> findByNameContainingIgnoreCase(String name);
    
    /**
     * Tìm students theo độ tuổi
     */
    List<Student> findByAge(Integer age);
    
    /**
     * Tìm students có tuổi lớn hơn hoặc bằng
     */
    List<Student> findByAgeGreaterThanEqual(Integer age);
    
    /**
     * Tìm students có tuổi trong khoảng
     */
    List<Student> findByAgeBetween(Integer minAge, Integer maxAge);
    
    // ===== CUSTOM QUERIES với @Query =====
    
    /**
     * Tìm students theo tên và tuổi
     */
    @Query("SELECT s FROM Student s WHERE s.name LIKE %:name% AND s.age = :age")
    List<Student> findByNameAndAge(@Param("name") String name, @Param("age") Integer age);
    
    /**
     * Đếm students theo độ tuổi
     */
    @Query("SELECT COUNT(s) FROM Student s WHERE s.age = :age")
    Long countStudentsByAge(@Param("age") Integer age);
    
    /**
     * Tính tuổi trung bình
     */
    @Query("SELECT AVG(s.age) FROM Student s")
    Double calculateAverageAge();
    
    /**
     * Tìm students trẻ nhất
     */
    @Query("SELECT s FROM Student s WHERE s.age = (SELECT MIN(s2.age) FROM Student s2)")
    List<Student> findYoungestStudents();
    
    /**
     * Tìm students già nhất
     */
    @Query("SELECT s FROM Student s WHERE s.age = (SELECT MAX(s2.age) FROM Student s2)")
    List<Student> findOldestStudents();
}