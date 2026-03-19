# 📮 LESSON 7: Postman Testing Guide - JWT Authentication

## 🚨 FIX LỖI 400 BAD REQUEST (QUAN TRỌNG!)

### **Vấn đề bạn đang gặp:**
Khi test endpoint `POST /auth/register`, bạn nhận được lỗi 400 Bad Request.

### **Nguyên nhân:**
Data trong body không đáp ứng validation requirements nghiêm ngặt.

### **❌ Body SAI (như bạn đang dùng):**
```json
{
  "username": "namBlue",      // ❌ Có chữ hoa B
  "email": "namblue.com",     // ❌ Thiếu @
  "password": "9999",         // ❌ Quá yếu
  "confirmPassword": "9999"   // ❌ Quá yếu
}
```

### **✅ Body ĐÚNG (copy paste cái này vào Postman):**
```json
{
  "username": "namblue",
  "email": "namblue@gmail.com",
  "password": "Password123!",
  "confirmPassword": "Password123!"
}
```

### **🔴 VALIDATION RULES:**
- **Password:** Tối thiểu 8 ký tự + 1 chữ thường + 1 chữ HOA + 1 số + 1 ký tự đặc biệt (@$!%*?&)
- **Username:** 3-50 ký tự, chỉ chữ cái, số, dấu gạch dưới (_)
- **Email:** Phải có @ và domain hợp lệ
- **ConfirmPassword:** Phải giống password

---

## 🎯 Test Security & Authentication với Postman

### **Prerequisites:**
- Spring Boot app với Security running on `http://localhost:8080`
- MongoDB running với User collection
- Postman installed
- JWT authentication implemented

---

## 🚀 Postman Collection Setup

### **Bước 1: Tạo New Collection**
1. **Mở Postman**
2. **Click "New" → "Collection"**
3. **Name:** `Student Management - JWT Auth`
4. **Description:** `API testing với JWT authentication`

### **Bước 2: Setup Environment Variables**
1. **Click "Environments" → "Create Environment"**
2. **Name:** `Student API - Local`
3. **Add Variables:**
   - `baseUrl`: `http://localhost:8080`
   - `jwtToken`: (empty - sẽ set sau khi login)
   - `userId`: (empty - sẽ set sau khi register)

---

## 🔐 Authentication Tests

### **Test 1: User Registration**

**Method:** `POST`
**URL:** `{{baseUrl}}/auth/register`
**Headers:**
```
Content-Type: application/json
```

**🚨 FIX LỖI 400 CỦA BẠN:**

**❌ Body SAI (như bạn đang dùng):**
```json
{
  "username": "namBlue",      // ❌ Có chữ hoa B
  "email": "namblue.com",     // ❌ Thiếu @
  "password": "9999",         // ❌ Quá yếu
  "confirmPassword": "9999"   // ❌ Quá yếu
}
```

**✅ Body ĐÚNG (dùng cái này):**
```json
{
  "username": "namblue",           // ✅ Chỉ chữ thường, số, _
  "email": "namblue@gmail.com",    // ✅ Có @ và domain
  "password": "Password123!",      // ✅ Đủ mạnh
  "confirmPassword": "Password123!" // ✅ Khớp với password
}
```

**🔴 YÊU CẦU PASSWORD NGHIÊM NGẶT:**
- ✅ Tối thiểu 8 ký tự
- ✅ Có ít nhất 1 chữ thường (a-z)
- ✅ Có ít nhất 1 chữ HOA (A-Z)  
- ✅ Có ít nhất 1 số (0-9)
- ✅ Có ít nhất 1 ký tự đặc biệt (@$!%*?&)

**🔴 YÊU CẦU USERNAME:**
- ✅ 3-50 ký tự
- ✅ Chỉ chữ cái, số và dấu gạch dưới (_)
- ✅ Không có ký tự đặc biệt khác

**🔴 YÊU CẦU EMAIL:**
- ✅ Định dạng email hợp lệ (có @)

**Expected Response (201 Created):**
```json
{
  "message": "User registered successfully",
  "userId": "507f1f77bcf86cd799439011",
  "username": "namblue"
}
```

**Error Response (400) - Validation:**
```json
{
  "error": "Registration failed",
  "message": "Password phải chứa ít nhất 1 chữ thường, 1 chữ hoa, 1 số và 1 ký tự đặc biệt"
}
```

**Postman Test Script:**
```javascript
pm.test("Registration successful", function () {
    pm.response.to.have.status(201);
    const response = pm.response.json();
    pm.expect(response.message).to.include("successfully");
    pm.environment.set("userId", response.userId);
});
```

---

### **Test 2: User Login**

**Method:** `POST`
**URL:** `{{baseUrl}}/auth/login`
**Headers:**
```
Content-Type: application/json
```
**Body (JSON):**
```json
{
  "username": "namblue",
  "password": "Password123!"
}
```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "namblue",
  "email": "namblue@gmail.com",
  "roles": ["USER"],
  "expiresIn": 86400
}
```

**Postman Test Script:**
```javascript
pm.test("Login successful", function () {
    pm.response.to.have.status(200);
    const response = pm.response.json();
    pm.expect(response.token).to.exist;
    pm.environment.set("jwtToken", response.token);
});

pm.test("JWT token format", function () {
    const response = pm.response.json();
    pm.expect(response.token).to.match(/^eyJ/); // JWT starts with eyJ
    pm.expect(response.type).to.equal("Bearer");
});
```

---

### **Test 3: Get User Profile (Protected)**

**Method:** `GET`
**URL:** `{{baseUrl}}/auth/profile`
**Headers:**
```
Authorization: Bearer {{jwtToken}}
Content-Type: application/json
```

**Expected Response (200 OK):**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "username": "namblue",
  "email": "namblue@gmail.com",
  "roles": ["USER"],
  "enabled": true,
  "createdAt": "2024-01-15T10:30:00Z"
}
```

**Postman Test Script:**
```javascript
pm.test("Profile retrieved successfully", function () {
    pm.response.to.have.status(200);
    const response = pm.response.json();
    pm.expect(response.username).to.equal("namblue");
    pm.expect(response.email).to.equal("namblue@gmail.com");
});
```

---

## 🛡️ Protected Student API Tests

### **Test 4: Get All Students (Protected)**

**Method:** `GET`
**URL:** `{{baseUrl}}/api/students`
**Headers:**
```
Authorization: Bearer {{jwtToken}}
Content-Type: application/json
```

**Expected Response (200 OK):**
```json
[]
```

**Postman Test Script:**
```javascript
pm.test("Protected endpoint accessible with JWT", function () {
    pm.response.to.have.status(200);
});

pm.test("Response is array", function () {
    const response = pm.response.json();
    pm.expect(response).to.be.an('array');
});
```

---

### **Test 5: Create Student (Protected)**

**Method:** `POST`
**URL:** `{{baseUrl}}/api/students`
**Headers:**
```
Authorization: Bearer {{jwtToken}}
Content-Type: application/json
```
**Body (JSON):**
```json
{
  "name": "Nguyen Van A",
  "email": "student1@email.com",
  "age": 20
}
```

**Expected Response (201 Created):**
```json
{
  "id": "507f1f77bcf86cd799439022",
  "name": "Nguyen Van A",
  "email": "student1@email.com",
  "age": 20
}
```

**Postman Test Script:**
```javascript
pm.test("Student created successfully", function () {
    pm.response.to.have.status(201);
    const response = pm.response.json();
    pm.expect(response.name).to.equal("Nguyen Van A");
    pm.environment.set("studentId", response.id);
});
```

---

## 🚫 Unauthorized Access Tests

### **Test 6: Access Protected Endpoint Without Token**

**Method:** `GET`
**URL:** `{{baseUrl}}/api/students`
**Headers:**
```
Content-Type: application/json
```
**(No Authorization header)**

**Expected Response (401 Unauthorized):**
```json
{
  "timestamp": "2024-01-15T10:30:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "JWT token is missing",
  "path": "/api/students"
}
```

**Postman Test Script:**
```javascript
pm.test("Unauthorized access blocked", function () {
    pm.response.to.have.status(401);
});

pm.test("Error message present", function () {
    const response = pm.response.json();
    pm.expect(response.error).to.equal("Unauthorized");
});
```

---

### **Test 7: Access with Invalid Token**

**Method:** `GET`
**URL:** `{{baseUrl}}/api/students`
**Headers:**
```
Authorization: Bearer invalid_token_here
Content-Type: application/json
```

**Expected Response (401 Unauthorized):**
```json
{
  "timestamp": "2024-01-15T10:30:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "JWT token is invalid",
  "path": "/api/students"
}
```

---

## 🔄 Token Refresh Tests

### **Test 8: Expired Token Handling**

**Method:** `GET`
**URL:** `{{baseUrl}}/api/students`
**Headers:**
```
Authorization: Bearer {{expiredToken}}
Content-Type: application/json
```

**Expected Response (401 Unauthorized):**
```json
{
  "timestamp": "2024-01-15T10:30:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "JWT token has expired",
  "path": "/api/students"
}
```

---

## 📊 Complete API Test Flow

### **Test Sequence:**
1. **Register User** → Get userId
2. **Login User** → Get JWT token
3. **Get Profile** → Verify token works
4. **Create Student** → Test protected endpoint
5. **Get All Students** → Verify data
6. **Update Student** → Test with token
7. **Delete Student** → Test with token
8. **Test Unauthorized Access** → Verify security

---

## 🛠️ Postman Collection Variables

### **Environment Variables:**
```json
{
  "baseUrl": "http://localhost:8080",
  "jwtToken": "",
  "userId": "",
  "studentId": "",
  "expiredToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.expired"
}
```

### **Collection Variables:**
```json
{
  "testUsername": "john_doe",
  "testEmail": "john@email.com",
  "testPassword": "SecurePass123!"
}
```

---

## 🧪 Advanced Testing Scenarios

### **Test 9: Password Validation**

**Method:** `POST`
**URL:** `{{baseUrl}}/auth/register`
**Body (JSON):**
```json
{
  "username": "weak_user",
  "email": "weak@email.com",
  "password": "123",
  "confirmPassword": "123"
}
```

**Expected Response (400 Bad Request):**
```json
{
  "timestamp": "2024-01-15T10:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Password must be at least 8 characters",
  "path": "/auth/register"
}
```

---

### **Test 10: Duplicate Username**

**Method:** `POST`
**URL:** `{{baseUrl}}/auth/register`
**Body (JSON):**
```json
{
  "username": "john_doe",
  "email": "another@email.com",
  "password": "SecurePass123!",
  "confirmPassword": "SecurePass123!"
}
```

**Expected Response (409 Conflict):**
```json
{
  "timestamp": "2024-01-15T10:30:00.000+00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Username already exists",
  "path": "/auth/register"
}
```

---

## 📋 Postman Collection Export

### **Collection Structure:**
```
📁 Student Management - JWT Auth
├── 📁 Authentication
│   ├── 🟢 Register User
│   ├── 🟢 Login User
│   ├── 🟢 Get Profile
│   └── 🟢 Update Profile
├── 📁 Protected Student API
│   ├── 🟢 Get All Students
│   ├── 🟢 Create Student
│   ├── 🟢 Get Student by ID
│   ├── 🟢 Update Student
│   └── 🟢 Delete Student
├── 📁 Search & Statistics
│   ├── 🟢 Search by Name
│   ├── 🟢 Search by Age
│   └── 🟢 Get Statistics
└── 📁 Security Tests
    ├── 🔴 Unauthorized Access
    ├── 🔴 Invalid Token
    └── 🔴 Expired Token
```

---

## ✅ Success Indicators

### **Authentication Working When:**
- ✅ User registration returns 201
- ✅ Login returns JWT token
- ✅ Protected endpoints accessible with token
- ✅ Unauthorized access returns 401
- ✅ Invalid tokens rejected
- ✅ Token format is valid JWT

### **Security Working When:**
- ✅ All student endpoints require authentication
- ✅ Password validation enforced
- ✅ Duplicate usernames rejected
- ✅ JWT tokens expire correctly
- ✅ Error messages are informative

---

<div align="center">
  <b>📮 Postman JWT Testing Complete! 📮</b>
  
  <br><br>
  
  **Secure API testing with authentication!**
</div>