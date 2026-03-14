# 📚 Lesson 2: Validation & Error Handling

## 🎯 Mục tiêu Lesson 2

Học cách validate input data và xử lý lỗi một cách chuyên nghiệp như các ứng dụng production.

---

## 🏷️ Annotations mới học

### 1. **Validation Annotations**

#### `@Valid`
```java
@PostMapping
public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest userRequest) {
    // Spring tự động validate userRequest trước khi vào method
}
```
- **Vị trí:** Trước parameter
- **Chức năng:** Kích hoạt validation cho object
- **Kết hợp với:** Các annotation validation khác

#### `@NotBlank`
```java
@NotBlank(message = "Tên không được để trống")
private String name;
```
- **Chức năng:** Không được null, empty, hoặc chỉ có space
- **Khác với `@NotNull`:** `@NotNull` chỉ check null, `@NotBlank` check cả empty

#### `@Size`
```java
@Size(min = 2, max = 50, message = "Tên phải từ 2-50 ký tự")
private String name;
```
- **Chức năng:** Kiểm tra độ dài string, size của collection
- **Tham số:** `min`, `max`, `message`

#### `@Email`
```java
@Email(message = "Email không đúng định dạng")
private String email;
```
- **Chức năng:** Validate format email
- **Pattern:** Sử dụng regex chuẩn email

#### `@Pattern`
```java
@Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại phải là 10-11 chữ số")
private String phone;
```
- **Chức năng:** Validate theo regex pattern
- **Ví dụ:** Phone number, mã số, format đặc biệt

#### `@Min` và `@Max`
```java
@Min(value = 18, message = "Tuổi phải từ 18 trở lên")
@Max(value = 100, message = "Tuổi không được quá 100")
private Integer age;
```
- **Chức năng:** Validate giá trị số
- **Áp dụng:** Integer, Long, BigDecimal, etc.

### 2. **Exception Handling Annotations**

#### `@RestControllerAdvice`
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handle exceptions globally
}
```
- **Chức năng:** Xử lý exception cho tất cả controllers
- **Kết hợp với:** `@ExceptionHandler`

#### `@ExceptionHandler`
```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex, WebRequest request) {
    // Xử lý lỗi validation
}
```
- **Chức năng:** Xử lý exception cụ thể
- **Tham số:** Class của exception cần handle

---

## 🏗️ Cấu trúc mới

### **DTO (Data Transfer Object)**
```
src/main/java/com/example/demo/dto/
├── UserRequest.java     # Input validation
└── ErrorResponse.java   # Standardized error format
```

**Tại sao cần DTO?**
- ✅ **Validation:** Tách validation khỏi business logic
- ✅ **Security:** Không expose internal model
- ✅ **Flexibility:** Có thể có nhiều DTO cho cùng 1 entity

### **Exception Package**
```
src/main/java/com/example/demo/exception/
└── GlobalExceptionHandler.java  # Centralized error handling
```

---

## 🔄 Luồng Validation

### 1. **Request → Validation → Controller**
```
Client gửi POST /api/users với JSON
    ↓
Spring deserialize JSON → UserRequest object
    ↓
@Valid trigger validation annotations
    ↓
Nếu có lỗi → MethodArgumentNotValidException
    ↓
GlobalExceptionHandler.handleValidationException()
    ↓
Return ErrorResponse với HTTP 400
```

### 2. **Business Logic Error**
```
Controller method execution
    ↓
Business logic check (email exists, etc.)
    ↓
throw IllegalArgumentException
    ↓
GlobalExceptionHandler.handleIllegalArgumentException()
    ↓
Return ErrorResponse với HTTP 400
```

---

## 🧪 Test Cases

### **1. Test Validation Success**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nguyen Van A",
    "email": "a@gmail.com",
    "phone": "0123456789",
    "age": 25
  }'
```

**Expected Response (201 Created):**
```json
{
  "id": 1,
  "name": "Nguyen Van A",
  "email": "a@gmail.com",
  "phone": "0123456789",
  "age": 25
}
```

### **2. Test Validation Errors**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "email": "invalid-email",
    "phone": "123",
    "age": 15
  }'
```

**Expected Response (400 Bad Request):**
```json
{
  "timestamp": "2024-03-14T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Dữ liệu đầu vào không hợp lệ",
  "path": "/api/users",
  "details": [
    "name: Tên không được để trống",
    "email: Email không đúng định dạng",
    "phone: Số điện thoại phải là 10-11 chữ số",
    "age: Tuổi phải từ 18 trở lên"
  ]
}
```

### **3. Test Business Logic Error**
```bash
# Tạo user đầu tiên
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "User 1", "email": "test@gmail.com", "phone": "0123456789", "age": 25}'

# Tạo user thứ 2 với email trùng
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "User 2", "email": "test@gmail.com", "phone": "0987654321", "age": 30}'
```

**Expected Response (400 Bad Request):**
```json
{
  "timestamp": "2024-03-14T10:35:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Email đã tồn tại: test@gmail.com",
  "path": "/api/users"
}
```

### **4. Test Server Error**
```bash
curl http://localhost:8080/api/users/test-error
```

**Expected Response (500 Internal Server Error):**
```json
{
  "timestamp": "2024-03-14T10:40:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Đây là lỗi test để kiểm tra error handling",
  "path": "/api/users/test-error"
}
```

---

## 💡 Best Practices

### **1. Validation Messages**
```java
// ❌ Không tốt - message không rõ ràng
@NotBlank
private String name;

// ✅ Tốt - message rõ ràng, user-friendly
@NotBlank(message = "Tên không được để trống")
private String name;
```

### **2. Custom Validation**
```java
// Tạo custom validator cho business rules phức tạp
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "Email đã tồn tại";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

### **3. Error Response Structure**
```java
// ✅ Consistent error format
{
  "timestamp": "2024-03-14T10:30:00",
  "status": 400,
  "error": "Validation Failed", 
  "message": "User-friendly message",
  "path": "/api/users",
  "details": ["field1: error1", "field2: error2"]
}
```

---

## 🎯 Tóm tắt Lesson 2

### ✅ **Đã học:**
1. **Validation Annotations:** `@Valid`, `@NotBlank`, `@Size`, `@Email`, `@Pattern`, `@Min`, `@Max`
2. **Exception Handling:** `@RestControllerAdvice`, `@ExceptionHandler`
3. **DTO Pattern:** Tách validation và data transfer
4. **Error Response:** Standardized error format
5. **Global Exception Handler:** Centralized error handling

### 🔜 **Lesson 3 sẽ học:**
- **Configuration & Profiles:** `@ConfigurationProperties`, `@Profile`
- **Environment-specific configs:** dev, staging, prod
- **External configurations:** YAML, Properties files

---

<div align="center">
  <b>🎉 Chúc mừng! Bạn đã master Validation & Error Handling! 🎉</b>
</div>