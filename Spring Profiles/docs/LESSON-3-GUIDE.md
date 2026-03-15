# 📚 Lesson 3: Configuration & Profiles

## 🎯 Mục tiêu Lesson 3

Học cách quản lý configuration cho các môi trường khác nhau (dev, staging, prod) và sử dụng Spring Profiles một cách chuyên nghiệp.

---

## 🏷️ Annotations & Concepts mới học

### 1. **@ConfigurationProperties**
```java
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private String version;
    // Getters and Setters
}
```
- **Chức năng:** Bind properties từ application.properties vào Java objects
- **Ưu điểm:** Type-safe, IDE support, validation
- **Prefix:** Nhóm các properties liên quan

### 2. **@Value**
```java
@Value("${server.port}")
private int serverPort;

@Value("${app.name:Default App Name}")
private String appName; // Default value nếu không có
```
- **Chức năng:** Inject single property value
- **Syntax:** `${property.name:defaultValue}`

### 3. **@Profile**
```java
@Service
@Profile("dev")
public class DevOnlyService {
    // Chỉ hoạt động khi active profile là "dev"
}

@Profile({"staging", "prod"})
public class ProductionService {
    // Hoạt động khi profile là staging HOẶC prod
}

@Profile("!dev")
public class NonDevService {
    // Hoạt động khi profile KHÔNG phải dev
}
```
- **Chức năng:** Conditional bean creation dựa trên active profile
- **Operators:** `!` (NOT), `&` (AND), `|` (OR)

---

## 📁 Cấu trúc Configuration Files

### **Profile-specific Properties:**
```
src/main/resources/
├── application.properties          # Default config
├── application-dev.properties      # Development
├── application-staging.properties  # Staging  
└── application-prod.properties     # Production
```

### **Thứ tự ưu tiên:**
1. **application-{profile}.properties** (cao nhất)
2. **application.properties** (default)
3. **Environment variables**
4. **Command line arguments** (cao nhất)

---

## 🌍 Environment Profiles

### **Development Profile (dev):**
```properties
# application-dev.properties
server.port=8080
logging.level.com.example.demo=DEBUG
app.features.debug-mode=true
app.features.email-enabled=true
app.features.sms-enabled=false
```

**Đặc điểm:**
- ✅ Debug logging enabled
- ✅ Development tools available
- ✅ Relaxed security
- ✅ In-memory database

### **Staging Profile (staging):**
```properties
# application-staging.properties
server.port=8081
logging.level.com.example.demo=INFO
app.features.debug-mode=false
app.features.email-enabled=true
app.features.sms-enabled=true
```

**Đặc điểm:**
- ✅ Production-like environment
- ✅ All features enabled for testing
- ✅ Moderate logging
- ✅ External database

### **Production Profile (prod):**
```properties
# application-prod.properties
server.port=80
logging.level.com.example.demo=WARN
app.features.debug-mode=false
server.error.include-stacktrace=never
```

**Đặc điểm:**
- ✅ Minimal logging
- ✅ Security hardened
- ✅ Error details hidden
- ✅ Production database

---

## 🚀 Cách chạy với Profiles

### **1. IDE (IntelliJ/Eclipse):**
```
VM Options: -Dspring.profiles.active=dev
Environment Variables: SPRING_PROFILES_ACTIVE=dev
```

### **2. Maven:**
```bash
# Development
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Staging
mvn spring-boot:run -Dspring-boot.run.profiles=staging

# Production
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### **3. JAR file:**
```bash
java -jar app.jar --spring.profiles.active=prod
java -Dspring.profiles.active=prod -jar app.jar
```

### **4. Environment Variable:**
```bash
export SPRING_PROFILES_ACTIVE=dev
java -jar app.jar
```

---

## 🧪 Test APIs

### **Test 1: App Info**
```bash
GET http://localhost:8080/api/config/info
```

**Response:**
```json
{
  "applicationName": "Spring Boot Demo",
  "serverPort": 8080,
  "activeProfiles": ["dev"],
  "appName": "Spring Boot Demo Application",
  "appVersion": "1.0.0",
  "environment": "development",
  "features": {
    "emailEnabled": true,
    "smsEnabled": false,
    "debugMode": true
  },
  "externalApis": {
    "paymentApi": "https://dev-payment-api.example.com",
    "notificationApi": "https://dev-notification-api.example.com"
  }
}
```

### **Test 2: Notification Service**
```bash
POST http://localhost:8080/api/config/test-notification
Content-Type: application/x-www-form-urlencoded

type=email&recipient=test@example.com&message=Hello World
```

**Response (Dev Profile):**
```json
{
  "status": "success",
  "result": "📧 [DEVELOPMENT] Sending email to: test@example.com, Subject: Test Subject, Content: Hello World | Debug: Using API https://dev-notification-api.example.com",
  "appInfo": "🚀 App: Spring Boot Demo Application v1.0.0 | Environment: development | Features: Email=true, SMS=false, Debug=true"
}
```

### **Test 3: Dev-only Endpoints**
```bash
# Chỉ hoạt động với profile dev
GET http://localhost:8080/api/config/dev/debug
```

**Response (Dev Profile):**
```json
{
  "debugInfo": "🔧 [DEV ONLY] Debug utilities available:\n- Memory usage: 45.23 MB / 128.00 MB\n- Active threads: 12\n- System properties: 65 items",
  "environment": "development"
}
```

**Response (Non-Dev Profile):**
```json
{
  "error": "Dev-only service not available",
  "message": "This endpoint is only available in development profile",
  "activeProfiles": "staging"
}
```

---

## 🔧 Configuration Best Practices

### **1. Externalize Configuration**
```properties
# ❌ Hard-coded values
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.password=hardcodedpassword

# ✅ Environment variables
spring.datasource.url=${DATABASE_URL:jdbc:h2:mem:testdb}
spring.datasource.password=${DATABASE_PASSWORD:}
```

### **2. Use Type-Safe Configuration**
```java
// ❌ @Value everywhere
@Value("${app.features.email-enabled}")
private boolean emailEnabled;

@Value("${app.features.sms-enabled}")
private boolean smsEnabled;

// ✅ @ConfigurationProperties
@ConfigurationProperties(prefix = "app.features")
public class FeatureProperties {
    private boolean emailEnabled;
    private boolean smsEnabled;
    // Getters and Setters
}
```

### **3. Profile-specific Beans**
```java
@Configuration
public class DatabaseConfig {
    
    @Bean
    @Profile("dev")
    public DataSource devDataSource() {
        return new H2DataSource(); // In-memory for dev
    }
    
    @Bean
    @Profile({"staging", "prod"})
    public DataSource prodDataSource() {
        return new MySQLDataSource(); // Real DB for staging/prod
    }
}
```

### **4. Feature Flags**
```java
@Service
public class PaymentService {
    
    @Value("${app.features.payment-enabled:true}")
    private boolean paymentEnabled;
    
    public String processPayment(PaymentRequest request) {
        if (!paymentEnabled) {
            return "Payment feature is currently disabled";
        }
        // Process payment logic
    }
}
```

---

## 🎯 Scenarios thực tế

### **Scenario 1: Multi-Environment Deployment**
```bash
# Local development
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Deploy to staging
docker run -e SPRING_PROFILES_ACTIVE=staging myapp:latest

# Deploy to production
kubectl set env deployment/myapp SPRING_PROFILES_ACTIVE=prod
```

### **Scenario 2: Feature Toggle**
```properties
# Enable new feature in staging for testing
app.features.new-payment-gateway=true

# Keep old feature in production until stable
app.features.new-payment-gateway=false
```

### **Scenario 3: External Configuration**
```bash
# Production secrets từ environment
export DATABASE_PASSWORD=super-secret-password
export API_KEY=prod-api-key-12345

java -jar app.jar --spring.profiles.active=prod
```

---

## 💡 Tips & Tricks

### **1. Multiple Profiles:**
```bash
# Activate multiple profiles
--spring.profiles.active=prod,monitoring,security
```

### **2. Profile Groups:**
```properties
# application.properties
spring.profiles.group.production=prod,monitoring,security
spring.profiles.group.development=dev,debug,h2
```

### **3. Conditional Properties:**
```java
@ConditionalOnProperty(name = "app.features.email-enabled", havingValue = "true")
@Service
public class EmailService {
    // Chỉ tạo bean khi email feature enabled
}
```

---

## 🎉 Tóm tắt Lesson 3

### ✅ **Đã học:**
1. **@ConfigurationProperties** - Type-safe configuration binding
2. **@Value** - Single property injection
3. **@Profile** - Environment-specific beans
4. **Profile-specific properties** - Dev, Staging, Prod configs
5. **External configuration** - Environment variables, command line args
6. **Feature flags** - Toggle features per environment

### 🔜 **Lesson 4 sẽ học:**
- **SQL Database** - JPA/Hibernate + MySQL/PostgreSQL
- **@Entity, @Repository, @Transactional**
- **Database relationships và queries**

---

<div align="center">
  <b>🎉 Chúc mừng! Bạn đã master Configuration & Profiles! 🎉</b>
</div>