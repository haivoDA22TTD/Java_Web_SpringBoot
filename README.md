# 🌱 Spring Boot Learning Journey

<div align="center">
  <img src="https://spring.io/img/spring-2.svg" alt="Spring Boot" width="200"/>
  
  [![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
  [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
  [![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green.svg)](https://www.mongodb.com/)
  [![Security](https://img.shields.io/badge/Spring%20Security-JWT-red.svg)](https://spring.io/projects/spring-security)
  [![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
  [![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
</div>

## 📖 Giới thiệu

Đây là dự án học Spring Boot từ cơ bản đến nâng cao, được thiết kế theo từng lesson để bạn có thể học một cách có hệ thống. Hiện tại đã hoàn thành **7 lessons** với **Student Management System** sử dụng **MongoDB** và **JWT Authentication**.

## 🎯 Tình trạng hiện tại

### ✅ **ĐÃ HOÀN THÀNH:**
- **Lesson 1**: REST API cơ bản ✅
- **Lesson 2**: Validation & Error Handling ✅
- **Lesson 3**: Configuration & Profiles ✅
- **Lesson 4**: SQL Database với JPA/Hibernate + MySQL ✅
- **Lesson 5**: NoSQL Database với MongoDB + Compass ✅
- **Lesson 7**: Security & JWT Authentication ✅ (đang implement)

### � **HIỆN TẠI ĐANG LÀM:**
- **Lesson 7**: JWT Authentication system với Spring Security
- User registration & login
- Protected API endpoints
- MongoDB user management

---

## 🏗️ Kiến trúc hiện tại (Lesson 7)

### **Technology Stack:**
- **Backend:** Spring Boot 3.2.3 + Spring Security
- **Database:** MongoDB 7.0 với MongoDB Compass
- **Authentication:** JWT (JSON Web Token)
- **Validation:** Bean Validation
- **Build Tool:** Maven

### **Project Structure:**
```
src/main/java/com/example/demo/
├── document/           # MongoDB Documents
│   ├── Student.java    # Student entity
│   └── User.java       # User entity (NEW)
├── dto/               # Data Transfer Objects
│   ├── StudentRequest.java
│   ├── StudentResponse.java
│   ├── RegisterRequest.java    # NEW
│   ├── LoginRequest.java       # NEW
│   └── AuthResponse.java       # NEW
├── repository/        # MongoDB Repositories
│   ├── StudentMongoRepository.java
│   └── UserRepository.java     # NEW
├── service/          # Business Logic
│   ├── StudentService.java
│   └── UserService.java        # NEW
├── controller/       # REST Controllers
│   ├── StudentController.java
│   └── AuthController.java     # NEW
├── security/         # Security Configuration (NEW)
│   └── SecurityConfig.java
└── SpringBootDemoApplication.java
```

---

## 🔐 API Endpoints

### **Authentication Endpoints (NEW):**
- `POST /auth/register` - User registration
- `POST /auth/login` - User login
- `GET /auth/test` - Test auth endpoints

### **Protected Student Endpoints:**
- `GET /api/students` - Get all students (requires JWT)
- `POST /api/students` - Create student (requires JWT)
- `GET /api/students/{id}` - Get student by ID (requires JWT)
- `PUT /api/students/{id}` - Update student (requires JWT)
- `DELETE /api/students/{id}` - Delete student (requires JWT)
- **+ 10 more search & statistics endpoints**

---

## 🚀 Cách chạy dự án

### **Yêu cầu:**
- Java 17+
- Maven 3.6+
- MongoDB Community Server
- MongoDB Compass (optional, for GUI)

### **Bước 1: Setup MongoDB**
```bash
# Install MongoDB Community Server
# Install MongoDB Compass
# Start MongoDB service
net start MongoDB
```

### **Bước 2: Chạy ứng dụng**
```bash
mvn spring-boot:run
```

### **Bước 3: Test Authentication**
```bash
# Test auth endpoint
curl http://localhost:8080/auth/test

# Register new user
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@email.com",
    "password": "SecurePass123!",
    "confirmPassword": "SecurePass123!"
  }'

# Login user
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePass123!"
  }'
```

---

## 🧪 Testing với Postman

### **Postman Collection:**
Xem chi tiết trong: [`docs/LESSON-7-POSTMAN-TESTING.md`](docs/LESSON-7-POSTMAN-TESTING.md)

**Test Flow:**
1. **Register User** → Get userId
2. **Login User** → Get JWT token
3. **Access Protected Endpoints** → Use JWT token
4. **Test Security** → Verify unauthorized access blocked

---

## � Documentation

### **📖 Lesson Guides:**
- [`docs/LESSON-4-GUIDE.md`](docs/LESSON-4-GUIDE.md) - SQL Database với MySQL
- [`docs/LESSON-5-GUIDE.md`](docs/LESSON-5-GUIDE.md) - NoSQL với MongoDB
- [`docs/LESSON-7-GUIDE.md`](docs/LESSON-7-GUIDE.md) - Security & JWT Authentication

### **🧪 API Testing:**
- [`docs/LESSON-4-API-TESTING.md`](docs/LESSON-4-API-TESTING.md) - MySQL API testing
- [`docs/LESSON-5-API-TESTING.md`](docs/LESSON-5-API-TESTING.md) - MongoDB API testing
- [`docs/LESSON-7-POSTMAN-TESTING.md`](docs/LESSON-7-POSTMAN-TESTING.md) - JWT Authentication testing

### **� Setup Guides:**
- [`docs/LESSON-5-MONGODB-SETUP.md`](docs/LESSON-5-MONGODB-SETUP.md) - MongoDB installation

---

## 🎯 Roadmap tiếp theo

### � **MESSAGING & COMMUNICATION**
- **Lesson 8**: Message Queue (RabbitMQ, Apache Kafka)
- **Lesson 9**: WebSocket & Real-time Communication

### ⚡ **PERFORMANCE & CACHING**
- **Lesson 10**: Redis Caching Strategies
- **Lesson 11**: Database Query Optimization

### 📊 **OBSERVABILITY**
- **Lesson 12**: Logging (Logback, Structured Logging)
- **Lesson 13**: Monitoring (Micrometer + Prometheus + Grafana)

### 🛡️ **RESILIENCE & RELIABILITY**
- **Lesson 14**: Circuit Breaker (Resilience4j)
- **Lesson 15**: Rate Limiting & API Security

### 🐳 **DEPLOYMENT & DEVOPS**
- **Lesson 16**: Docker & Containerization
- **Lesson 17**: CI/CD Pipeline
- **Lesson 18**: Production Best Practices

---

## 🔥 Features hiện tại

### **✅ Student Management:**
- CRUD operations với MongoDB
- Search & filtering
- Statistics & aggregation
- DTO pattern implementation
- Validation & error handling

### **✅ Authentication & Security:**
- User registration với password validation
- BCrypt password encryption
- JWT token-based authentication
- Protected API endpoints
- CORS configuration

### **✅ Database:**
- MongoDB document storage
- Flexible schema
- Auto-indexing
- MongoDB Compass integration

---

## 🎉 Kết quả đạt được

Sau 7 lessons, bạn đã có:
- ✅ **Production-ready REST API** với authentication
- ✅ **NoSQL database** integration
- ✅ **Security system** với JWT
- ✅ **Clean architecture** với DTO pattern
- ✅ **Comprehensive testing** với Postman
- ✅ **Professional documentation**

---

## 📞 Liên hệ & Hỗ trợ

Nếu gặp khó khăn:
- 📧 Tạo issue trên GitHub
- 💬 Hỏi trong comment
- 📖 Đọc documentation trong `docs/`

---

<div align="center">
  <b>🌟 Spring Boot Learning Journey - Lesson 7 🌟</b>
  
  <br><br>
  
  **Current:** Security & JWT Authentication 🔐
  
  **Next:** Message Queue & Real-time Communication 🔄
  
  ### 🗺️ **Progress:**
  ```
  Foundation ✅ → Database ✅ → Security 🔄 → Performance → 
  Observability → Messaging → Resilience → DevOps
  ```
  
  **🎯 Mục tiêu:** Production-ready microservice với full authentication system!
</div>