# 📚 Spring Boot Annotations & Structure Guide

## 🏗️ Cấu trúc dự án Spring Boot

### 📁 Cấu trúc thư mục chuẩn:
```
spring-boot-demo/
├── src/
│   ├── main/
│   │   ├── java/                          # Source code Java
│   │   │   └── com/example/demo/          # Package chính
│   │   │       ├── SpringBootDemoApplication.java  # Main class
│   │   │       ├── controller/            # REST Controllers
│   │   │       ├── service/              # Business Logic (sẽ có ở Lesson 2)
│   │   │       ├── repository/           # Data Access (sẽ có ở Lesson 2)
│   │   │       ├── model/                # Entity classes (sẽ có ở Lesson 2)
│   │   │       └── config/               # Configuration classes
│   │   └── resources/
│   │       ├── application.properties     # Cấu hình ứng dụng
│   │       ├── static/                   # CSS, JS, images
│   │       └── templates/                # HTML templates (Thymeleaf)
│   └── test/                             # Test code
├── target/                               # Compiled files (tự động tạo)
├── pom.xml                              # Maven configuration
└── README.md                            # Documentation
```

### 🎯 Nguyên tắc đặt tên package:
- `com.example.demo` = domain.company.project
- `controller` = Xử lý HTTP requests
- `service` = Business logic
- `repository` = Database operations
- `model/entity` = Data models

---

## 🏷️ Annotations đã sử dụng trong Lesson 1

### 1. `@SpringBootApplication`
```java
@SpringBootApplication
public class SpringBootDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }
}
```

**Giải thích:**
- **Vị trí:** Main class của ứng dụng
- **Chức năng:** Kết hợp 3 annotations:
  - `@Configuration` - Đánh dấu class có cấu hình
  - `@EnableAutoConfiguration` - Tự động cấu hình Spring Boot
  - `@ComponentScan` - Tự động scan các component trong package

**Tại sao quan trọng:**
- Đây là điểm khởi đầu của ứng dụng
- Spring Boot sẽ tự động tìm và cấu hình mọi thứ từ đây

---

### 2. `@RestController`
```java
@RestController
public class HelloController {
    // Methods here
}
```

**Giải thích:**
- **Vị trí:** Trên class Controller
- **Chức năng:** Kết hợp 2 annotations:
  - `@Controller` - Đánh dấu đây là Controller
  - `@ResponseBody` - Tự động convert return value thành JSON/XML

**So sánh với `@Controller`:**
```java
// Cách cũ - phải thêm @ResponseBody cho mỗi method
@Controller
public class OldController {
    @GetMapping("/hello")
    @ResponseBody  // Phải thêm này
    public String hello() {
        return "Hello";
    }
}

// Cách mới - @RestController tự động có @ResponseBody
@RestController
public class NewController {
    @GetMapping("/hello")
    public String hello() {  // Không cần @ResponseBody
        return "Hello";
    }
}
```

---

### 3. `@GetMapping`
```java
@GetMapping("/hello")
public String hello() {
    return "Xin chào!";
}
```

**Giải thích:**
- **Vị trí:** Trên method trong Controller
- **Chức năng:** Xử lý HTTP GET requests đến URL `/hello`
- **Tương đương với:**
```java
@RequestMapping(value = "/hello", method = RequestMethod.GET)
```

**Các mapping khác:**
- `@PostMapping` - HTTP POST
- `@PutMapping` - HTTP PUT  
- `@DeleteMapping` - HTTP DELETE
- `@PatchMapping` - HTTP PATCH

---

### 4. `@PathVariable`
```java
@GetMapping("/hello/{name}")
public String helloWithName(@PathVariable String name) {
    return "Xin chào " + name;
}
```

**Giải thích:**
- **Vị trí:** Trước parameter trong method
- **Chức năng:** Lấy giá trị từ URL path
- **URL:** `/hello/Minh` → `name = "Minh"`

**Ví dụ nâng cao:**
```java
// URL: /user/123/profile/456
@GetMapping("/user/{userId}/profile/{profileId}")
public String getProfile(@PathVariable Long userId, 
                        @PathVariable Long profileId) {
    return "User: " + userId + ", Profile: " + profileId;
}

// Đặt tên khác cho variable
@GetMapping("/hello/{userName}")
public String hello(@PathVariable("userName") String name) {
    return "Hello " + name;
}
```

---

### 5. `@RequestParam`
```java
@GetMapping("/greet")
public String greet(@RequestParam String name, 
                   @RequestParam(defaultValue = "0") int age) {
    return String.format("Xin chào %s, %d tuổi!", name, age);
}
```

**Giải thích:**
- **Vị trí:** Trước parameter trong method
- **Chức năng:** Lấy giá trị từ query parameters
- **URL:** `/greet?name=Minh&age=25`

**Các tùy chọn:**
```java
@RequestParam String name                    // Bắt buộc
@RequestParam(required = false) String name  // Không bắt buộc
@RequestParam(defaultValue = "Guest") String name  // Có giá trị mặc định
@RequestParam("user_name") String name       // Tên parameter khác
```

---

## 🔧 File cấu hình: application.properties

```properties
# LESSON 1: Cấu hình cơ bản nhất
server.port=8080                    # Port của server
spring.application.name=Spring Boot Demo  # Tên ứng dụng
```

**Giải thích:**
- `server.port=8080` - Ứng dụng chạy trên port 8080
- `spring.application.name` - Tên hiển thị của ứng dụng

**Các cấu hình khác sẽ học:**
```properties
# Database (Lesson 2)
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop

# Logging (Lesson 3)
logging.level.com.example.demo=DEBUG

# Security (Lesson 4)
spring.security.user.name=admin
spring.security.user.password=123456
```

---

## 🔄 Luồng hoạt động của Spring Boot

### 1. **Khởi động ứng dụng:**
```
SpringBootDemoApplication.main()
    ↓
@SpringBootApplication được xử lý
    ↓
Spring Boot tự động scan package com.example.demo
    ↓
Tìm thấy @RestController → Tạo HelloController bean
    ↓
Server khởi động trên port 8080
```

### 2. **Xử lý HTTP request:**
```
Client gửi GET /hello/Minh
    ↓
Spring Boot tìm method có @GetMapping("/hello/{name}")
    ↓
Gọi helloWithName("Minh")
    ↓
Return "Xin chào Minh!"
    ↓
Spring Boot convert thành HTTP response
```

---

## 🎯 Tóm tắt Lesson 1

### ✅ Đã học:
1. **Cấu trúc dự án** Spring Boot chuẩn
2. **@SpringBootApplication** - Main class
3. **@RestController** - REST API controller
4. **@GetMapping** - HTTP GET mapping
5. **@PathVariable** - Lấy data từ URL path
6. **@RequestParam** - Lấy data từ query parameters

### 🔜 Lesson 2 sẽ học:
- **@Entity, @Table** - Database entities
- **@Repository** - Data access layer
- **@Service** - Business logic layer
- **@Autowired** - Dependency injection

---

## 💡 Tips cho người mới

### 1. **Thứ tự học annotations:**
```
@SpringBootApplication (Main class)
    ↓
@RestController (Controller layer)
    ↓
@GetMapping, @PostMapping (HTTP methods)
    ↓
@PathVariable, @RequestParam (Parameters)
    ↓
@Service, @Repository (Business & Data layers)
```

### 2. **Cách debug:**
- Thêm `System.out.println()` trong method
- Sử dụng browser Developer Tools
- Check console logs

### 3. **Cách test:**
- Browser: `http://localhost:8080/hello`
- Curl: `curl http://localhost:8080/hello`
- Postman hoặc REST client

---

<div align="center">
  <b>🎉 Chúc mừng! Bạn đã hiểu cơ bản về Spring Boot! 🎉</b>
</div>