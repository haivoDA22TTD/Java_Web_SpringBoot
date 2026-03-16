package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * LESSON 4: Student Model - Simplified
 * Model đơn giản với 3 trường cơ bản
 */
@Entity
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 100, message = "Tên phải từ 2-100 ký tự")
    @Column(nullable = false, length = 100)
    private String name;
    
    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Min(value = 18, message = "Tuổi phải từ 18 trở lên")
    @Max(value = 100, message = "Tuổi không được quá 100")
    @Column(nullable = false)
    private Integer age;
    
    // Constructors
    public Student() {}
    
    public Student(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}