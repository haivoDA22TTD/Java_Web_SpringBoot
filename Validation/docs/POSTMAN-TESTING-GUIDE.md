# 🚀 Hướng dẫn Test với Postman - Lesson 2

## 📋 Chuẩn bị

### 1. **Khởi động ứng dụng:**
```bash
mvn spring-boot:run
```
Đợi thấy log: `Started SpringBootDemoApplication in X seconds`

### 2. **Mở Postman và tạo Collection mới:**
- Tên collection: `Spring Boot Demo - Lesson 2`

---

## 🧪 Test Cases với Postman

### **Test 1: Tạo User Thành Công** ✅

**Request:**
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/users`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body (raw JSON):**
  ```json
  {
    "name": "Nguyen Van A",
    "email": "nguyenvana@gmail.com",
    "phone": "0123456789",
    "age": 25
  }
  ```

**Expected Response:**
- **Status:** `201 Created`
- **Body:**
  ```json
  {
    "id": 1,
    "name": "Nguyen Van A",
    "email": "nguyenvana@gmail.com",
    "phone": "0123456789",
    "age": 25
  }
  ```

---

### **Test 2: Validation Errors** ❌

**Request:**
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/users`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body (raw JSON):**
  ```json
  {
    "name": "",
    "email": "invalid-email",
    "phone": "123",
    "age": 15
  }
  ```

**Expected Response:**
- **Status:** `400 Bad Request`
- **Body:**
  ```json
  {
    "timestamp": "2024-03-14T...",
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

---

### **Test 3: Email Đã Tồn Tại** ❌

**Bước 1:** Tạo user đầu tiên (như Test 1)

**Bước 2:** Tạo user thứ 2 với email trùng
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/users`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body (raw JSON):**
  ```json
  {
    "name": "Tran Thi B",
    "email": "nguyenvana@gmail.com",
    "phone": "0987654321",
    "age": 30
  }
  ```

**Expected Response:**
- **Status:** `400 Bad Request`
- **Body:**
  ```json
  {
    "timestamp": "2024-03-14T...",
    "status": 400,
    "error": "Bad Request",
    "message": "Email đã tồn tại: nguyenvana@gmail.com",
    "path": "/api/users"
  }
  ```

---

### **Test 4: Lấy Tất Cả Users** ✅

**Request:**
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/users`

**Expected Response:**
- **Status:** `200 OK`
- **Body:**
  ```json
  [
    {
      "id": 1,
      "name": "Nguyen Van A",
      "email": "nguyenvana@gmail.com",
      "phone": "0123456789",
      "age": 25
    }
  ]
  ```

---

### **Test 5: Lấy User Theo ID** ✅

**Request:**
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/users/1`

**Expected Response:**
- **Status:** `200 OK`
- **Body:**
  ```json
  {
    "id": 1,
    "name": "Nguyen Van A",
    "email": "nguyenvana@gmail.com",
    "phone": "0123456789",
    "age": 25
  }
  ```

---

### **Test 6: User Không Tồn Tại** ❌

**Request:**
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/users/999`

**Expected Response:**
- **Status:** `400 Bad Request`
- **Body:**
  ```json
  {
    "timestamp": "2024-03-14T...",
    "status": 400,
    "error": "Bad Request",
    "message": "Không tìm thấy user với ID: 999",
    "path": "/api/users/999"
  }
  ```

---

### **Test 7: Cập Nhật User** ✅

**Request:**
- **Method:** `PUT`
- **URL:** `http://localhost:8080/api/users/1`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body (raw JSON):**
  ```json
  {
    "name": "Nguyen Van A Updated",
    "email": "nguyenvana.updated@gmail.com",
    "phone": "0123456788",
    "age": 26
  }
  ```

**Expected Response:**
- **Status:** `200 OK`
- **Body:**
  ```json
  {
    "id": 1,
    "name": "Nguyen Van A Updated",
    "email": "nguyenvana.updated@gmail.com",
    "phone": "0123456788",
    "age": 26
  }
  ```

---

### **Test 8: Xóa User** ✅

**Request:**
- **Method:** `DELETE`
- **URL:** `http://localhost:8080/api/users/1`

**Expected Response:**
- **Status:** `200 OK`
- **Body:**
  ```json
  {
    "message": "Đã xóa user thành công",
    "id": "1"
  }
  ```

---

### **Test 9: Test Server Error** ❌

**Request:**
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/users/test-error`

**Expected Response:**
- **Status:** `500 Internal Server Error`
- **Body:**
  ```json
  {
    "timestamp": "2024-03-14T...",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Đây là lỗi test để kiểm tra error handling",
    "path": "/api/users/test-error"
  }
  ```

---

## 📝 Postman Collection Setup

### **Tạo Environment Variables:**
1. Trong Postman, tạo Environment mới: `Spring Boot Local`
2. Thêm variable:
   - **Variable:** `baseUrl`
   - **Initial Value:** `http://localhost:8080`
   - **Current Value:** `http://localhost:8080`

### **Sử dụng Environment:**
- Thay `http://localhost:8080` bằng `{{baseUrl}}`
- VD: `{{baseUrl}}/api/users`

---

## 🎯 Test Scenarios Nâng Cao

### **Scenario 1: Complete User Lifecycle**
1. **POST** `/api/users` - Tạo user mới
2. **GET** `/api/users` - Xem danh sách users
3. **GET** `/api/users/{id}` - Xem chi tiết user
4. **PUT** `/api/users/{id}` - Cập nhật user
5. **DELETE** `/api/users/{id}` - Xóa user
6. **GET** `/api/users/{id}` - Verify user đã bị xóa

### **Scenario 2: Validation Testing**
1. Test từng field validation riêng lẻ:
   - Name empty
   - Email invalid format
   - Phone wrong pattern
   - Age under 18
2. Test multiple validation errors cùng lúc

### **Scenario 3: Edge Cases**
1. **Boundary Values:**
   - Name: exactly 2 chars, exactly 50 chars
   - Age: exactly 18, exactly 100
   - Phone: exactly 10 digits, exactly 11 digits

2. **Special Characters:**
   - Name với ký tự đặc biệt
   - Email với format phức tạp

---

## 💡 Postman Tips

### **1. Sử dụng Tests Tab:**
```javascript
// Kiểm tra status code
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

// Kiểm tra response body
pm.test("Response has id field", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
});
```

### **2. Sử dụng Pre-request Script:**
```javascript
// Generate random email
pm.globals.set("randomEmail", "user" + Math.floor(Math.random() * 1000) + "@test.com");
```

### **3. Chain Requests:**
- Lưu ID từ response của POST request
- Sử dụng ID đó cho GET/PUT/DELETE requests

---

## 🔍 Troubleshooting

### **Lỗi thường gặp:**

1. **Connection refused:**
   - ✅ Check app đã chạy chưa: `mvn spring-boot:run`
   - ✅ Check port 8080 có bị chiếm chưa

2. **404 Not Found:**
   - ✅ Check URL đúng chưa: `/api/users`
   - ✅ Check method đúng chưa: POST, GET, PUT, DELETE

3. **400 Bad Request:**
   - ✅ Check Content-Type header: `application/json`
   - ✅ Check JSON syntax hợp lệ
   - ✅ Check validation rules

4. **500 Internal Server Error:**
   - ✅ Check console logs của Spring Boot
   - ✅ Check GlobalExceptionHandler có hoạt động không

---

<div align="center">
  <b>🎉 Happy Testing với Postman! 🎉</b>
  
  <br><br>
  
  **Tip:** Save tất cả requests vào Collection để reuse sau này!
</div>