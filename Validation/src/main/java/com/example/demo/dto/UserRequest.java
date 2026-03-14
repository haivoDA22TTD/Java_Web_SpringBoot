package com.example.demo.dto;

import jakarta.validation.constraints.*;

/**
 * LESSON 2: DTO với validation annotations
 * Data Transfer Object để nhận data từ client
 */
public class UserRequest {
    
    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 50, message = "Tên phải từ 2-50 ký tự")
    private String name;
    
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;
    
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại phải là 10-11 chữ số")
    private String phone;
    
    @Min(value = 18, message = "Tuổi phải từ 18 trở lên")
    @Max(value = 100, message = "Tuổi không được quá 100")
    private Integer age;
    
    // Constructors
    public UserRequest() {}
    
    public UserRequest(String name, String email, String phone, Integer age) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
    }
    
    // Getters and Setters
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
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
}