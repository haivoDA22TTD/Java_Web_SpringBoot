# 🌱 Spring Boot Learning Journey

<div align="center">
  <img src="https://spring.io/img/spring-2.svg" alt="Spring Boot" width="200"/>
  
  [![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
  [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
  [![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
  [![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
</div>

## 📖 Giới thiệu

Đây là dự án học Spring Boot từ cơ bản đến nâng cao, được thiết kế theo từng lesson để bạn có thể học một cách có hệ thống.

## 🎯 Roadmap học tập toàn diện

### 🌱 **FOUNDATION (Nền tảng)**
- ✅ **Lesson 1**: REST API cơ bản (HOÀN THÀNH)
- ✅ **Lesson 2**: Validation & Error Handling (HOÀN THÀNH)
- ✅ **Lesson 3**: Configuration & Profiles (HOÀN THÀNH)
- ⏳ **Lesson 3**: Configuration & Profiles

### 💾 **DATABASE & PERSISTENCE**
- ⏳ **Lesson 4**: SQL Database (JPA/Hibernate + MySQL/PostgreSQL)
- ⏳ **Lesson 5**: NoSQL Database (MongoDB, Redis)
- ⏳ **Lesson 6**: Database Migration (Flyway/Liquibase)

### 🔐 **SECURITY & AUTHENTICATION**
- ⏳ **Lesson 7**: JWT Authentication & Authorization
- ⏳ **Lesson 8**: Token Blacklist (Redis) & Refresh Token
- ⏳ **Lesson 9**: Rate Limiting & API Security

### ⚡ **PERFORMANCE & CACHING**
- ⏳ **Lesson 10**: Redis Caching Strategies
- ⏳ **Lesson 11**: Database Query Optimization
- ⏳ **Lesson 12**: Async Processing

### 📊 **OBSERVABILITY**
- ⏳ **Lesson 13**: Logging (Logback, Structured Logging)
- ⏳ **Lesson 14**: Tracing (Sleuth, Zipkin)
- ⏳ **Lesson 15**: Monitoring (Micrometer + Prometheus + Grafana)

### 🔄 **MESSAGING & COMMUNICATION**
- ⏳ **Lesson 16**: Message Queue (RabbitMQ, Apache Kafka)
- ⏳ **Lesson 17**: WebSocket & Real-time Communication
- ⏳ **Lesson 18**: REST Client & Service Communication

### 🛡️ **RESILIENCE & RELIABILITY**
- ⏳ **Lesson 19**: Circuit Breaker (Resilience4j)
- ⏳ **Lesson 20**: Retry & Timeout Patterns
- ⏳ **Lesson 21**: Health Checks & Graceful Shutdown

### 🐳 **DEPLOYMENT & DEVOPS**
- ⏳ **Lesson 22**: Docker & Containerization
- ⏳ **Lesson 23**: CI/CD Pipeline (GitHub Actions/Jenkins)
- ⏳ **Lesson 24**: Kubernetes Deployment
- ⏳ **Lesson 25**: Production Best Practices

## 🚀 Lesson 1: REST API Cơ bản

### Những gì bạn sẽ học:
- Tạo Spring Boot project
- Hiểu cấu trúc thư mục
- Tạo Controller đầu tiên
- Các annotation cơ bản: `@RestController`, `@GetMapping`
- Path Variable và Query Parameter

### Cấu trúc dự án hiện tại:
```
spring-boot-demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── SpringBootDemoApplication.java  # Main class
│   │   │   └── controller/
│   │   │       └── HelloController.java        # REST Controller
│   │   └── resources/
│   │       └── application.properties          # Cấu hình
├── pom.xml                                     # Maven dependencies
└── README.md                                   # File này
```

## 🏃‍♂️ Cách chạy dự án

### Yêu cầu:
- Java 17+
- Maven 3.6+

### Các bước:

1. **Clone hoặc tải project**
2. **Chạy ứng dụng:**
   ```bash
   mvn spring-boot:run
   ```
3. **Mở trình duyệt và test các API:**

   - **Hello đơn giản:** http://localhost:8080/hello
   - **Hello với tên:** http://localhost:8080/hello/TenCuaBan
   - **Greet với parameters:** http://localhost:8080/greet?name=Minh&age=25

## 🧪 Test API

Bạn có thể test bằng:

### 1. Trình duyệt web:
```
http://localhost:8080/hello
http://localhost:8080/hello/Minh
http://localhost:8080/greet?name=Minh&age=25
```

### 2. Curl command:
```bash
curl http://localhost:8080/hello
curl http://localhost:8080/hello/Minh
curl "http://localhost:8080/greet?name=Minh&age=25"
```

### 3. Postman hoặc các REST client khác

## 📚 Tài liệu chi tiết

### 📖 [Hướng dẫn Annotations & Cấu trúc](docs/ANNOTATIONS-GUIDE.md)
Đọc file này để hiểu rõ:
- 🏗️ Cấu trúc dự án Spring Boot
- 🏷️ Giải thích chi tiết từng annotation
- 🔄 Luồng hoạt động của ứng dụng
- 💡 Tips và tricks cho người mới

### 🗺️ [Roadmap chi tiết 25 Lessons](docs/ROADMAP-DETAILS.md)
Xem roadmap đầy đủ từ cơ bản đến advanced:
- 🌱 Foundation → 💾 Database → 🔐 Security → ⚡ Performance
- 📊 Observability → 🔄 Messaging → 🛡️ Resilience → 🐳 DevOps
- 🎯 **Mục tiêu:** Trở thành Spring Boot Expert trong 25 lessons!

### 📖 [Lesson 2: Validation & Error Handling](docs/LESSON-2-GUIDE.md)
Chi tiết về validation và xử lý lỗi:
- 🏷️ Validation annotations: `@Valid`, `@NotBlank`, `@Email`, etc.
- 🛡️ Global Exception Handler với `@RestControllerAdvice`
- 📋 DTO pattern và Error Response chuẩn
- 🧪 Test cases và best practices

### 🚀 [Hướng dẫn Test với Postman](docs/POSTMAN-TESTING-GUIDE.md)
Step-by-step test với Postman:
- ✅ 9 test cases chi tiết với expected responses
- 🎯 Test scenarios: success, validation errors, business logic errors
- 💡 Postman tips: Environment, Tests, Pre-request Scripts
- 🔍 Troubleshooting guide

### 📖 [Lesson 3: Configuration & Profiles](docs/LESSON-3-GUIDE.md)
Chi tiết về configuration và profiles:
- 🏷️ `@ConfigurationProperties`, `@Value`, `@Profile`
- 🌍 Multi-environment setup: dev, staging, prod
- 🔧 External configuration và environment variables
- 🎯 Feature flags và conditional beans
- 🧪 Test APIs với different profiles

### 🚀 [Setup Profiles trong IntelliJ IDEA](docs/INTELLIJ-PROFILE-SETUP.md)
Hướng dẫn setup profiles trong IntelliJ:
- ⚙️ Tạo Run Configurations cho từng profile
- 🎯 Quick switch giữa dev/staging/prod
- 🔧 Environment variables và VM options
- 💡 Tips và troubleshooting

## 📚 Kiến thức đã học (Lesson 1)

### 1. **@RestController**
- Kết hợp `@Controller` + `@ResponseBody`
- Tự động convert return value thành JSON/XML

### 2. **@GetMapping**
- Xử lý HTTP GET requests
- Có thể map với URL cụ thể

### 3. **@PathVariable**
- Lấy giá trị từ URL path
- VD: `/hello/{name}` → `@PathVariable String name`

### 4. **@RequestParam**
- Lấy giá trị từ query parameters
- VD: `?name=Minh&age=25`
- Có thể set default value

## 🔥 Thử thách

Hãy thử tự tạo thêm:

1. **API trả về thông tin cá nhân:**
   ```java
   @GetMapping("/profile/{id}")
   public String getProfile(@PathVariable int id) {
       // Code của bạn ở đây
   }
   ```

2. **API tính toán đơn giản:**
   ```java
   @GetMapping("/calculate")
   public String calculate(@RequestParam int a, @RequestParam int b) {
       // Code của bạn ở đây
   }
   ```

## 🎉 Kết quả mong đợi

Sau Lesson 1, bạn sẽ:
- ✅ Hiểu cấu trúc Spring Boot project
- ✅ Tạo được REST API đơn giản
- ✅ Biết cách sử dụng Path Variable và Query Parameter
- ✅ Chạy và test được ứng dụng

## 📞 Liên hệ & Hỗ trợ

Nếu gặp khó khăn, hãy:
- 📧 Tạo issue trên GitHub
- 💬 Hỏi trong comment
- 📖 Đọc lại documentation

---

<div align="center">
  <b>🌟 Chúc bạn học tập vui vẻ và thành công! 🌟</b>
  
  <br><br>
  
  **Next:** [Lesson 2 - Validation & Error Handling](docs/lesson-2.md) (Coming soon...)
  
  ### 🗺️ **Roadmap Overview:**
  ```
  Foundation → Database → Security → Performance → 
  Observability → Messaging → Resilience → DevOps
  ```
  
  **🎯 Mục tiêu cuối khóa:** Xây dựng được một microservice production-ready hoàn chỉnh!
</div>