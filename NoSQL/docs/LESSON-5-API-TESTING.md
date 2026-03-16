# 🧪 LESSON 5: API Testing Guide - MongoDB

## 🎯 Test Student Management API với MongoDB

### **Prerequisites:**
- Spring Boot app running on `http://localhost:8080`
- MongoDB running on `localhost:27017`
- MongoDB Compass connected
- Document: Student (ObjectId, name, email, age)

---

## 🚀 Quick Start Tests

### **Test 1: Health Check**
```bash
curl http://localhost:8080/api/students
```
**Expected:** `[]` (empty array)

### **Test 2: Create First Student**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nguyen Van A",
    "email": "test1@email.com",
    "age": 20
  }'
```
**Expected Response:**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "name": "Nguyen Van A",
  "email": "test1@email.com",
  "age": 20
}
```

### **Test 3: Get All Students**
```bash
curl http://localhost:8080/api/students
```

### **Test 4: Get Student by ObjectId**
```bash
curl http://localhost:8080/api/students/507f1f77bcf86cd799439011
```

### **Test 5: Update Student**
```bash
curl -X PUT http://localhost:8080/api/students/507f1f77bcf86cd799439011 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nguyen Van A Updated",
    "email": "test1@email.com",
    "age": 21
  }'
```

### **Test 6: Delete Student**
```bash
curl -X DELETE http://localhost:8080/api/students/507f1f77bcf86cd799439011
```

---

## 🔍 Search Tests

### **Search by Name:**
```bash
curl "http://localhost:8080/api/students/search/name?name=Nguyen"
```

### **Search by Email:**
```bash
curl "http://localhost:8080/api/students/search/email?email=test1@email.com"
```

### **Search by Age:**
```bash
curl "http://localhost:8080/api/students/search/age?age=20"
```

### **Search by Min Age:**
```bash
curl "http://localhost:8080/api/students/search/min-age?minAge=18"
```

### **Search by Age Range:**
```bash
curl "http://localhost:8080/api/students/search/age-range?minAge=18&maxAge=25"
```

### **Search by Name and Age:**
```bash
curl "http://localhost:8080/api/students/search/name-and-age?name=Nguyen&age=20"
```

---

## 📈 Statistics Tests (MongoDB Aggregation)

### **Count by Age:**
```bash
curl "http://localhost:8080/api/students/statistics/count-by-age?age=20"
```

### **Average Age (Aggregation):**
```bash
curl "http://localhost:8080/api/students/statistics/average-age"
```

### **Youngest Students:**
```bash
curl "http://localhost:8080/api/students/statistics/youngest"
```

### **Oldest Students:**
```bash
curl "http://localhost:8080/api/students/statistics/oldest"
```

---

## 🍃 MongoDB Specific Tests

### **Test Flexible Schema:**
```bash
# MongoDB cho phép thêm fields mới mà không cần migration
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Flexible Student",
    "email": "flexible@email.com",
    "age": 22,
    "extraField": "This works in MongoDB!"
  }'
```

### **Test ObjectId Format:**
- MongoDB IDs là strings dài 24 characters
- Format: `507f1f77bcf86cd799439011`
- Khác với MySQL auto-increment integers

---

## 🧭 MongoDB Compass Verification

### **Bước 1: Mở MongoDB Compass**
1. Connect to `mongodb://localhost:27017`
2. Navigate to `student_management` database
3. Click on `students` collection

### **Bước 2: Xem Documents**
- Sẽ thấy documents dạng JSON
- Mỗi document có `_id` ObjectId
- Fields: name, email, age, createdAt, updatedAt

### **Bước 3: Query trong Compass**
```javascript
// Find by age
{ "age": 20 }

// Find by name (regex)
{ "name": { "$regex": "Nguyen", "$options": "i" } }

// Find age range
{ "age": { "$gte": 18, "$lte": 25 } }
```

---

## 🚨 Validation Error Tests

### **Test Name Validation:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "email": "test@email.com",
    "age": 20
  }'
```

### **Test Email Validation:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Student",
    "email": "invalid-email",
    "age": 20
  }'
```

### **Test Duplicate Email:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Another Student",
    "email": "test1@email.com",
    "age": 22
  }'
```

### **Test Age Validation:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Young Student",
    "email": "young@email.com",
    "age": 17
  }'
```

---

## 🚨 Error Handling Tests

### **Invalid ObjectId:**
```bash
curl http://localhost:8080/api/students/invalid-id
```
**Expected:** 400 Bad Request

### **Non-existent ObjectId:**
```bash
curl http://localhost:8080/api/students/507f1f77bcf86cd799439999
```
**Expected:** 404 Not Found

---

## 📋 Test Data Setup

### **Create Multiple Students:**

**Student 1:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tran Thi B",
    "email": "test2@email.com",
    "age": 22
  }'
```

**Student 2:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Le Van C",
    "email": "test3@email.com",
    "age": 19
  }'
```

**Student 3:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pham Thi D",
    "email": "test4@email.com",
    "age": 25
  }'
```

---

## ✅ Success Indicators

### **MongoDB Integration Working When:**
- ✅ All endpoints return proper HTTP status codes
- ✅ JSON responses contain ObjectId strings
- ✅ Documents visible in MongoDB Compass
- ✅ Unique email constraint enforced
- ✅ Timestamps (createdAt, updatedAt) auto-generated
- ✅ Aggregation queries work for statistics

### **Expected Response Format:**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "name": "Student Name",
  "email": "student@email.com",
  "age": 20
}
```

### **MongoDB Document Format:**
```json
{
  "_id": ObjectId("507f1f77bcf86cd799439011"),
  "name": "Student Name",
  "email": "student@email.com",
  "age": 20,
  "createdAt": ISODate("2024-01-15T10:30:00.000Z"),
  "updatedAt": ISODate("2024-01-15T10:30:00.000Z"),
  "_class": "com.example.demo.document.Student"
}
```

---

## 🔄 Migration Verification

### **Differences from MySQL:**
- **ID Type:** String ObjectId vs Long auto-increment
- **Storage:** JSON documents vs table rows
- **Schema:** Flexible vs fixed columns
- **Queries:** MongoDB query language vs SQL

### **Same API Behavior:**
- All endpoints work identically
- Same JSON request/response format
- Same validation rules
- Same business logic

---

<div align="center">
  <b>🍃 MongoDB API Testing Complete! 🍃</b>
  
  <br><br>
  
  **NoSQL migration successful!**
</div>