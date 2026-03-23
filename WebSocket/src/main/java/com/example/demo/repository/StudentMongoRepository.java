package com.example.demo.repository;

import com.example.demo.document.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * LESSON 5: Student MongoDB Repository
 * Repository cho MongoDB với query methods
 */
@Repository
public interface StudentMongoRepository extends MongoRepository<Student, String> {
    
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
     * Tìm students theo tên và tuổi (MongoDB query)
     */
    @Query("{ 'name': { $regex: ?0, $options: 'i' }, 'age': ?1 }")
    List<Student> findByNameAndAge(String name, Integer age);
    
    /**
     * Đếm students theo độ tuổi
     */
    @Query(value = "{ 'age': ?0 }", count = true)
    Long countStudentsByAge(Integer age);
    
    /**
     * Tìm students trẻ nhất
     */
    @Query("{ 'age': { $exists: true } }")
    List<Student> findAllOrderByAgeAsc();
    
    /**
     * Tìm students già nhất
     */
    @Query("{ 'age': { $exists: true } }")
    List<Student> findAllOrderByAgeDesc();
    
    /**
     * Tìm students theo regex pattern trong tên
     */
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Student> findByNameRegex(String pattern);
    
    /**
     * Tìm students có email domain cụ thể
     */
    @Query("{ 'email': { $regex: ?0 } }")
    List<Student> findByEmailDomain(String domain);
}