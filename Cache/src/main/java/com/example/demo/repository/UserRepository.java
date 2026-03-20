package com.example.demo.repository;

import com.example.demo.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * LESSON 7: User Repository
 * Repository cho User authentication
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    /**
     * Tìm user theo username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Tìm user theo email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Kiểm tra username đã tồn tại chưa
     */
    boolean existsByUsername(String username);
    
    /**
     * Kiểm tra email đã tồn tại chưa
     */
    boolean existsByEmail(String email);
}