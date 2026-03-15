# 🧪 LESSON 4: API Testing Guide - Simplified with DTO

## 🎯 Test Student Management API với DTO Pattern

### **Prerequisites:**
- Spring Boot app running on `http://localhost:8080`
- MySQL database connected
- Model: Student (id, name, email, age)
- DTO: StudentRequest (input), StudentResponse (output)

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
  "id": 1,
  "name": "Nguyen Van A",
  "email": "test1@email.com",
  "age": 20
}
```

### **Test 3: Get All Students**
```bash
curl http://localhost:8080/api/students
```

### **Test 4: Get Student by ID**
```bash
curl http://localhost:8080/api/students/1
```

### **Test 5: Update Student**
```bash
curl -X PUT http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nguyen Van A Updated",
    "email": "test1@email.com",
    "age": 21
  }'
```

### **Test 6: Delete Student**
```bash
curl -X DELETE http://localhost:8080/api/students/1
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

## 📈 Statistics Tests

### **Count by Age:**
```bash
curl "http://localhost:8080/api/students/statistics/count-by-age?age=20"
```

### **Average Age:**
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

## 🚨 Validation Error Tests

### **Test Name Validation:**

**Empty Name:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "email": "test@email.com",
    "age": 20
  }'
```

**Name Too Short:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "A",
    "email": "test@email.com",
    "age": 20
  }'
```

### **Test Email Validation:**

**Invalid Email:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Student",
    "email": "invalid-email",
    "age": 20
  }'
```

**Duplicate Email:**
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

**Age Too Young:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Young Student",
    "email": "young@email.com",
    "age": 17
  }'
```

**Age Too Old:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Old Student",
    "email": "old@email.com",
    "age": 101
  }'
```

---

## 🚨 Error Handling Tests

### **Not Found Errors:**

**Get Non-existent Student:**
```bash
curl http://localhost:8080/api/students/999
```

**Update Non-existent Student:**
```bash
curl -X PUT http://localhost:8080/api/students/999 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test",
    "email": "test@email.com",
    "age": 20
  }'
```

---

## 📋 Test Data Setup

### **Create Multiple Students:**

**Student 2:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tran Thi B",
    "email": "test2@email.com",
    "age": 22
  }'
```

**Student 3:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Le Van C",
    "email": "test3@email.com",
    "age": 19
  }'
```

**Student 4:**
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

### **API Working Correctly When:**
- All endpoints return proper HTTP status codes
- JSON responses follow StudentResponse DTO format
- Validation errors return 400 with details
- Not found errors return 404
- Data persists in MySQL database
- Search queries return filtered results
- Statistics calculations are accurate

### **DTO Pattern Working When:**
- Input validation works on StudentRequest
- Output format matches StudentResponse
- Entity-DTO conversion is seamless
- API responses are consistent

### **Database Integration Working When:**
- Data visible in MySQL Workbench
- Auto-increment IDs working
- Email unique constraint enforced
- Age validation enforced

### **Expected Response Format:**
```json
{
  "id": 1,
  "name": "Student Name",
  "email": "student@email.com",
  "age": 20
}
```

---

<div align="center">
  <b>🎉 Simplified API Testing Complete! 🎉</b>
  
  <br><br>
  
  **All 15 endpoints tested with DTO pattern!**
</div>