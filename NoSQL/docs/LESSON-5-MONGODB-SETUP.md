# 🍃 LESSON 5: MongoDB Setup Guide

## 🎯 Cài đặt MongoDB Community Server + Compass

### **Bước 1: Download MongoDB**
1. **Truy cập:** https://www.mongodb.com/try/download/community
2. **Chọn:**
   - Version: Latest (7.0.x)
   - Platform: Windows
   - Package: msi
3. **Click "Download"**

### **Bước 2: Cài đặt MongoDB Community Server**
1. **Chạy file .msi** vừa download
2. **Setup Type:** Complete
3. **Service Configuration:**
   - ✅ Install MongoDB as a Service
   - ✅ Run service as Network Service user
   - Service Name: `MongoDB`
4. **MongoDB Compass:** ✅ Install MongoDB Compass
5. **Click "Install"**

---

## 🧭 MongoDB Compass Setup

### **Bước 1: Mở MongoDB Compass**
- Compass sẽ tự động mở sau khi cài đặt
- Hoặc tìm "MongoDB Compass" trong Start Menu

### **Bước 2: Connect to MongoDB**
1. **Connection String:** `mongodb://localhost:27017`
2. **Click "Connect"**
3. **Sẽ thấy MongoDB instance đang chạy**

### **Bước 3: Tạo Database**
1. **Click "Create Database"**
2. **Database Name:** `student_management`
3. **Collection Name:** `students`
4. **Click "Create Database"**

---

## ✅ Kiểm tra MongoDB đã chạy

### **Cách 1: Services**
```bash
# Mở Services
Win + R → services.msc
```
- Tìm service **"MongoDB"**
- Status phải là **"Running"**

### **Cách 2: Command Line**
```bash
# Test connection
mongo --eval "db.adminCommand('ismaster')"
```

### **Cách 3: MongoDB Compass**
- Mở Compass
- Connect thành công → MongoDB OK!

---

## 🚀 Test với Spring Boot

### **Bước 1: Chạy Application**
```bash
mvn spring-boot:run
```

### **Bước 2: Kiểm tra Logs**
```
MongoDB connection successful
Connected to MongoDB: student_management
Collection 'students' ready
Started SpringBootDemoApplication in X seconds
```

### **Bước 3: Test API**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Student",
    "email": "test@email.com",
    "age": 20
  }'
```

### **Bước 4: Xem trong Compass**
1. **Refresh database** trong Compass
2. **Click "student_management" → "students"**
3. **Sẽ thấy document vừa tạo**

---

## 🔧 MongoDB Configuration

### **Default Settings:**
- **Host:** localhost
- **Port:** 27017
- **Database:** student_management
- **Collection:** students
- **Auth:** None (local development)

### **Connection String:**
```
mongodb://localhost:27017/student_management
```

### **Data Directory:**
- **Windows:** `C:\Program Files\MongoDB\Server\7.0\data\`
- **Logs:** `C:\Program Files\MongoDB\Server\7.0\log\`

---

## 🚨 Troubleshooting

### **Lỗi 1: MongoDB service không start**
```bash
# Start service manually
net start MongoDB
```

### **Lỗi 2: Connection refused**
- Kiểm tra MongoDB service đang chạy
- Kiểm tra port 27017 không bị block

### **Lỗi 3: Compass không connect được**
- Thử connection string: `mongodb://127.0.0.1:27017`
- Restart MongoDB service

### **Lỗi 4: Spring Boot không connect được**
- Kiểm tra `application.properties`
- Kiểm tra MongoDB đang chạy trên port 27017

---

## 📊 MongoDB vs MySQL Comparison

| Feature | MySQL | MongoDB |
|---------|-------|---------|
| **Type** | Relational | Document |
| **Schema** | Fixed | Flexible |
| **Query** | SQL | MongoDB Query Language |
| **Joins** | Yes | No (Embedded docs) |
| **Transactions** | ACID | ACID (4.0+) |
| **Scaling** | Vertical | Horizontal |
| **ID Type** | Auto-increment | ObjectId |

---

## 🎯 Ready Indicators

### **MongoDB hoạt động tốt khi:**
- ✅ Service "MongoDB" đang Running
- ✅ Compass connect được localhost:27017
- ✅ Database "student_management" được tạo
- ✅ Spring Boot logs không có lỗi connection
- ✅ API endpoints hoạt động bình thường
- ✅ Documents được lưu trong collection

### **Success Message:**
```
Connected to MongoDB: student_management
Collection indexed: students.email (unique)
Application ready for NoSQL operations!
```

---

<div align="center">
  <b>🍃 MongoDB Setup Complete! 🍃</b>
  
  <br><br>
  
  **Ready for NoSQL development!**
</div>