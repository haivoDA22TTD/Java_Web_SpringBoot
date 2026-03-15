# 📚 LESSON 4: Student Management System với MySQL

## 🎯 Mục tiêu Lesson 4

Xây dựng hệ thống quản lý sinh viên hoàn chỉnh với:
- **Spring Boot** + **MySQL** database
- **JPA/Hibernate** ORM tự động tạo tables
- **REST API** với 30+ endpoints
- **Validation** đầy đủ cho data
- **Error handling** chuyên nghiệp
- **Complex queries** và **statistics**

---

## 🏗️ Kiến trúc Project

### **Package Structure:**
```
src/main/java/com/example/demo/
├── dto/            # Data Transfer Objects (StudentRequest, StudentResponse)
├── model/          # JPA Entities (Student.java)
├── repository/     # Data Access Layer (StudentRepository.java)  
├── service/        # Business Logic Layer (StudentService.java)
├── controller/     # REST API Layer (StudentController.java)
└── SpringBootDemoApplication.java
```

### **Database Schema:**
- **Database:** `student_management`
- **Table:** `students` (auto-created by Hibernate)
- **Columns:** id, name, email, age
- **Engine:** MySQL 8.0 với UTF-8 support

---

## 🗄️ Student Model Features

### **Core Fields:**
- `id` - Auto-increment primary key
- `name` - Tên sinh viên (2-100 chars)
- `email` - Email unique với validation
- `age` - Tuổi (18-100)

### **DTO Pattern:**
- `StudentRequest` - Input DTO với validation
- `StudentResponse` - Output DTO với ID

### **Validation Rules:**
- `name`: NotBlank, 2-100 ký tự
- `email`: Email format, unique
- `age`: 18-100, NotNull

---

## 🔧 Technology Stack

### **Backend:**
- **Spring Boot 3.2.3** - Main framework
- **Spring Data JPA** - ORM layer
- **Hibernate 6.4.4** - JPA implementation
- **MySQL Connector** - Database driver
- **Bean Validation** - Input validation
- **HikariCP** - Connection pooling

### **Database:**
- **MySQL 8.0** - Primary database
- **H2** - Backup in-memory database
- **Auto DDL** - Tables created automatically

### **Tools:**
- **MySQL Workbench** - Database management
- **Postman/cURL** - API testing
- **IntelliJ IDEA** - IDE với profile support

---

## 🚀 Key Features

### **1. Auto Database Setup:**
- Database tự động tạo khi app start
- Table `students` với 3 columns: name, email, age
- UTF-8 encoding cho tiếng Việt
- Unique constraint trên email

### **2. DTO Pattern Implementation:**
- Separation of concerns giữa Entity và API
- Input validation trong StudentRequest
- Clean output format với StudentResponse
- Type safety và API stability

### **3. Simple Query Methods:**
- Basic CRUD operations
- Search by name, email, age
- Age range queries
- Statistical queries (count, average, min/max age)

### **4. Professional Error Handling:**
- Validation error responses
- HTTP status codes
- Runtime exception handling
- Clean error messages

### **5. Clean Architecture:**
- Entity-DTO conversion
- Service layer business logic
- Repository data access
- Controller API endpoints

---

## 📊 API Endpoints Overview

### **Basic CRUD (5 endpoints):**
- `GET /api/students` - Lấy tất cả
- `GET /api/students/{id}` - Lấy theo ID
- `POST /api/students` - Tạo mới
- `PUT /api/students/{id}` - Cập nhật
- `DELETE /api/students/{id}` - Xóa

### **Search & Filter (6 endpoints):**
- Search by name: `/search/name?name=xxx`
- Search by email: `/search/email?email=xxx`
- Search by age: `/search/age?age=xx`
- Search by min age: `/search/min-age?minAge=xx`
- Search by age range: `/search/age-range?minAge=xx&maxAge=yy`
- Search by name and age: `/search/name-and-age?name=xxx&age=xx`

### **Statistics & Reports (4 endpoints):**
- Count by age: `/statistics/count-by-age?age=xx`
- Average age: `/statistics/average-age`
- Youngest students: `/statistics/youngest`
- Oldest students: `/statistics/oldest`

**Total: 15 REST endpoints**

---

## 🎓 Learning Outcomes

Sau Lesson 4, bạn sẽ nắm vững:

### **Spring Boot Concepts:**
- Auto-configuration
- Dependency injection
- Application properties
- Component scanning

### **JPA/Hibernate:**
- Simple entity mapping
- Basic query methods
- Auto DDL generation
- Database connections

### **DTO Pattern:**
- Input/Output separation
- Validation strategies
- Entity-DTO conversion
- API design best practices

### **REST API Design:**
- HTTP methods
- Status codes
- Request/Response structure
- Error handling
- Parameter validation

### **Clean Architecture:**
- Layered architecture
- Separation of concerns
- Dependency management
- Code organization

---

## 🔗 Related Files

### **Core Implementation:**
- `src/main/java/com/example/demo/model/Student.java`
- `src/main/java/com/example/demo/dto/StudentRequest.java`
- `src/main/java/com/example/demo/dto/StudentResponse.java`
- `src/main/java/com/example/demo/repository/StudentRepository.java`
- `src/main/java/com/example/demo/service/StudentService.java`
- `src/main/java/com/example/demo/controller/StudentController.java`

### **Configuration:**
- `src/main/resources/application.properties`
- `pom.xml`

### **Documentation:**
- `docs/LESSON-4-API-TESTING.md` - API testing guide
- `docs/MYSQL-WORKBENCH-SETUP.md` - Database setup
- `docs/ANNOTATIONS-GUIDE.md` - Annotation explanations

---

## 🎯 Next Steps

### **Immediate:**
1. Test all API endpoints
2. Verify database operations
3. Check data persistence
4. Validate error handling

### **Advanced (Future Lessons):**
- Security (JWT, Authentication)
- Caching (Redis)
- Logging & Monitoring
- Docker containerization
- CI/CD pipeline

---

<div align="center">
  <b>🎉 Lesson 4: Complete Student Management System! 🎉</b>
  
  <br><br>
  
  **Ready for production-level Spring Boot development!**
</div>