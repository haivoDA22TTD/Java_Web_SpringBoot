# 📚 LESSON 5: NoSQL với MongoDB + Compass

## 🎯 Mục tiêu Lesson 5

Chuyển đổi từ MySQL sang MongoDB NoSQL database:
- **Spring Boot** + **MongoDB** database
- **Spring Data MongoDB** thay vì JPA
- **MongoDB Compass** để quản lý database
- **Document-based** thay vì relational
- **Flexible schema** với JSON documents
- **Aggregation queries** và **complex operations**

---

## 🏗️ Kiến trúc Project

### **Package Structure:**
```
src/main/java/com/example/demo/
├── dto/            # Data Transfer Objects (StudentRequest, StudentResponse)
├── document/       # MongoDB Documents (Student.java)
├── repository/     # MongoDB Repository (StudentMongoRepository.java)  
├── service/        # Business Logic Layer (StudentService.java)
├── controller/     # REST API Layer (StudentController.java)
└── SpringBootDemoApplication.java
```

### **Database Schema:**
- **Database:** `student_management`
- **Collection:** `students` (MongoDB collection)
- **Document Structure:** JSON với flexible fields
- **Engine:** MongoDB với BSON storage

---

## 🗄️ Student Document Features

### **Core Fields:**
- `_id` - MongoDB ObjectId (auto-generated)
- `name` - Tên sinh viên (String)
- `email` - Email unique (String)
- `age` - Tuổi (Integer)

### **MongoDB Advantages:**
- **Flexible Schema** - Có thể thêm fields mới dễ dàng
- **JSON Native** - Lưu trữ dạng document
- **No Joins** - Embedded documents thay vì foreign keys
- **Horizontal Scaling** - Sharding support

### **Document Example:**
```json
{
  "_id": "507f1f77bcf86cd799439011",
  "name": "Nguyen Van A",
  "email": "test@email.com",
  "age": 20,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

---

## 🔧 Technology Stack

### **Backend:**
- **Spring Boot 3.2.3** - Main framework
- **Spring Data MongoDB** - MongoDB integration
- **MongoDB Java Driver** - Database driver
- **Bean Validation** - Input validation
- **Jackson** - JSON serialization

### **Database:**
- **MongoDB Community** - NoSQL database
- **MongoDB Compass** - GUI management tool
- **BSON** - Binary JSON storage format
- **GridFS** - File storage (optional)

### **Tools:**
- **MongoDB Compass** - Visual database management
- **Postman/cURL** - API testing
- **IntelliJ IDEA** - IDE với MongoDB plugin

---

## 🚀 Key Features

### **1. Document-Based Storage:**
- JSON documents thay vì rows
- Flexible schema evolution
- Embedded documents support
- Array fields support

### **2. Spring Data MongoDB:**
- MongoRepository interface
- Query methods với method names
- Custom queries với @Query
- Aggregation framework support

### **3. MongoDB Compass Integration:**
- Visual query builder
- Document viewer/editor
- Index management
- Performance monitoring

### **4. NoSQL Advantages:**
- Schema flexibility
- Horizontal scaling
- Better performance cho read-heavy workloads
- JSON-native operations

### **5. Advanced MongoDB Features:**
- Text search capabilities
- Geospatial queries
- Aggregation pipelines
- Change streams

---

## 📊 API Endpoints (Same as Lesson 4)

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

**Total: 15 REST endpoints (same API, different database)**

---

## 🎓 Learning Outcomes

Sau Lesson 5, bạn sẽ nắm vững:

### **NoSQL Concepts:**
- Document vs Relational databases
- Schema flexibility
- BSON data types
- Collection operations

### **Spring Data MongoDB:**
- MongoRepository interface
- Query method conventions
- Custom queries
- Aggregation operations

### **MongoDB Operations:**
- CRUD operations
- Indexing strategies
- Query optimization
- Aggregation pipelines

### **MongoDB Compass:**
- Database navigation
- Document management
- Query building
- Performance analysis

### **Migration Strategies:**
- SQL to NoSQL migration
- Data modeling differences
- Performance considerations
- Best practices

---

## 🔗 Related Files

### **Core Implementation:**
- `src/main/java/com/example/demo/document/Student.java`
- `src/main/java/com/example/demo/dto/StudentRequest.java`
- `src/main/java/com/example/demo/dto/StudentResponse.java`
- `src/main/java/com/example/demo/repository/StudentMongoRepository.java`
- `src/main/java/com/example/demo/service/StudentService.java`
- `src/main/java/com/example/demo/controller/StudentController.java`

### **Configuration:**
- `src/main/resources/application.properties`
- `pom.xml`

### **Documentation:**
- `docs/LESSON-5-MONGODB-SETUP.md` - MongoDB installation guide
- `docs/LESSON-5-API-TESTING.md` - API testing với MongoDB
- `docs/MONGODB-COMPASS-GUIDE.md` - Compass usage guide

---

## 🎯 Migration from Lesson 4

### **Changes Made:**
1. **Dependencies:** MySQL → MongoDB
2. **Annotations:** JPA → Spring Data MongoDB
3. **Repository:** JpaRepository → MongoRepository
4. **ID Type:** Long → String (ObjectId)
5. **Package:** model → document

### **What Stays Same:**
- DTO classes (no changes)
- Service logic (minimal changes)
- Controller endpoints (no changes)
- API contracts (same JSON)

---

## 🔄 Next Steps

### **Immediate:**
1. Install MongoDB Community Server
2. Install MongoDB Compass
3. Update dependencies in pom.xml
4. Migrate model to document
5. Test all endpoints

### **Advanced (Future Lessons):**
- Aggregation pipelines
- Text search indexing
- Geospatial queries
- Replica sets
- Sharding strategies

---

<div align="center">
  <b>🎉 Lesson 5: NoSQL Migration Complete! 🎉</b>
  
  <br><br>
  
  **From Relational to Document Database!**
</div>