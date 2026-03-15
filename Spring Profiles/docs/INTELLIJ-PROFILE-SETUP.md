# 🚀 Setup Profiles trong IntelliJ IDEA

## 📋 Hướng dẫn setup profiles trong IntelliJ

### **Cách 1: Run Configuration (Khuyến nghị)**

#### **Bước 1: Tạo Run Configuration cho từng profile**

1. **Mở IntelliJ IDEA**
2. **Click vào dropdown** bên cạnh nút Run (góc trên bên phải)
3. **Chọn "Edit Configurations..."**

#### **Bước 2: Tạo configuration cho Development**
1. **Click "+" → "Spring Boot"**
2. **Name:** `Spring Boot Demo - DEV`
3. **Main class:** `com.example.demo.SpringBootDemoApplication`
4. **Program arguments:** (để trống)
5. **VM options:** `-Dspring.profiles.active=dev`
6. **Environment variables:** (để trống hoặc thêm nếu cần)
7. **Click "Apply"**

#### **Bước 3: Tạo configuration cho Staging**
1. **Click "+" → "Spring Boot"**
2. **Name:** `Spring Boot Demo - STAGING`
3. **Main class:** `com.example.demo.SpringBootDemoApplication`
4. **VM options:** `-Dspring.profiles.active=staging`
5. **Click "Apply"**

#### **Bước 4: Tạo configuration cho Production**
1. **Click "+" → "Spring Boot"**
2. **Name:** `Spring Boot Demo - PROD`
3. **Main class:** `com.example.demo.SpringBootDemoApplication`
4. **VM options:** `-Dspring.profiles.active=prod`
5. **Click "Apply"**

---

### **Cách 2: Environment Variables**

#### **Setup trong Run Configuration:**
1. **Edit Configurations...**
2. **Chọn configuration muốn setup**
3. **Environment variables:** Click vào icon folder
4. **Thêm:** `SPRING_PROFILES_ACTIVE=dev` (hoặc staging, prod)

---

### **Cách 3: Program Arguments**

#### **Trong Run Configuration:**
- **Program arguments:** `--spring.profiles.active=dev`

---

## 🎯 Kết quả mong đợi

### **Khi chạy với profile DEV:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::               (v3.2.3)

2024-03-14 10:30:00.123  INFO --- [main] c.e.demo.SpringBootDemoApplication : Starting SpringBootDemoApplication using Java 17
2024-03-14 10:30:00.125  INFO --- [main] c.e.demo.SpringBootDemoApplication : The following 1 profile is active: dev
2024-03-14 10:30:01.234  INFO --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat started on port(s): 8080 (http)
```

### **Khi chạy với profile STAGING:**
```
2024-03-14 10:30:00.125  INFO --- [main] c.e.demo.SpringBootDemoApplication : The following 1 profile is active: staging
2024-03-14 10:30:01.234  INFO --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat started on port(s): 8081 (http)
```

---

## 🧪 Test Profile hoạt động

### **Test 1: Kiểm tra active profile**
```bash
GET http://localhost:8080/api/config/info
```

**Response sẽ khác nhau tùy profile:**
```json
{
  "activeProfiles": ["dev"],        // hoặc ["staging"], ["prod"]
  "serverPort": 8080,               // 8080 (dev), 8081 (staging), 80 (prod)
  "environment": "development",     // "staging", "production"
  "features": {
    "debugMode": true,              // true (dev), false (staging/prod)
    "emailEnabled": true,
    "smsEnabled": false             // false (dev), true (staging/prod)
  }
}
```

### **Test 2: Dev-only endpoint**
```bash
GET http://localhost:8080/api/config/dev/debug
```

**Với profile DEV:**
```json
{
  "debugInfo": "🔧 [DEV ONLY] Debug utilities available...",
  "environment": "development"
}
```

**Với profile STAGING/PROD:**
```json
{
  "error": "Dev-only service not available",
  "message": "This endpoint is only available in development profile",
  "activeProfiles": "staging"
}
```

---

## 💡 Tips cho IntelliJ

### **1. Quick Switch Profiles:**
- Tạo sẵn 3 run configurations
- Dùng dropdown để switch nhanh
- Mỗi profile sẽ có màu log khác nhau

### **2. Debug với Profile:**
- Click **Debug** thay vì **Run**
- Profile vẫn hoạt động bình thường
- Có thể debug code với config khác nhau

### **3. Multiple Instances:**
- Có thể chạy đồng thời nhiều profiles
- Dev: port 8080
- Staging: port 8081
- Prod: port 80

### **4. Environment Variables trong IntelliJ:**
```
DATABASE_URL=jdbc:h2:mem:testdb
API_KEY=dev-api-key-123
DEBUG_MODE=true
```

---

## 🔧 Troubleshooting

### **Profile không active:**
1. **Check VM options:** `-Dspring.profiles.active=dev`
2. **Check Environment variables:** `SPRING_PROFILES_ACTIVE=dev`
3. **Check Program arguments:** `--spring.profiles.active=dev`
4. **Check console log:** Tìm dòng "The following 1 profile is active: dev"

### **Port conflict:**
- Dev: 8080
- Staging: 8081  
- Prod: 80 (có thể conflict, đổi thành 8082 cho test)

### **Service not found:**
- DevOnlyService chỉ có trong profile "dev"
- Check `@Profile("dev")` annotation
- Check active profile có đúng không

---

## 🎯 Best Practices

### **1. Naming Convention:**
```
Spring Boot Demo - DEV
Spring Boot Demo - STAGING  
Spring Boot Demo - PROD
```

### **2. Color Coding:**
- IntelliJ tự động assign màu khác nhau cho mỗi configuration
- Dễ phân biệt khi chạy multiple instances

### **3. Default Profile:**
- Nếu không set profile → default profile
- Recommend: luôn set explicit profile

### **4. Shared Configuration:**
- Common config trong `application.properties`
- Profile-specific config trong `application-{profile}.properties`

---

<div align="center">
  <b>🎉 Giờ bạn có thể dễ dàng switch profiles trong IntelliJ! 🎉</b>
  
  <br><br>
  
  **Tip:** Bookmark các run configurations để access nhanh!
</div>