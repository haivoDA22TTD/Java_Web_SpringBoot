# 🔐 LESSON 7: Security & JWT Authentication

## 🎯 Mục tiêu Lesson 7

Thêm bảo mật và xác thực vào Student Management System:
- **Spring Security** integration
- **JWT (JSON Web Token)** authentication
- **User registration & login**
- **Protected API endpoints**
- **Role-based access control**
- **Password encryption**

---

## 🏗️ Kiến trúc Security

### **Package Structure:**
```
src/main/java/com/example/demo/
├── dto/            # Request/Response DTOs
├── document/       # MongoDB Documents (Student, User)
├── repository/     # MongoDB Repositories
├── service/        # Business Logic + Security Services
├── controller/     # REST API + Auth Controllers
├── security/       # Security Configuration
│   ├── JwtAuthenticationEntryPoint.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtTokenProvider.java
│   └── SecurityConfig.java
└── SpringBootDemoApplication.java
```

### **Security Flow:**
1. **User Registration** → Create account với encrypted password
2. **User Login** → Verify credentials → Generate JWT token
3. **API Requests** → Include JWT in Authorization header
4. **Token Validation** → Verify JWT → Allow/Deny access

---

## 🔑 Authentication Features

### **User Management:**
- User registration với validation
- Password encryption (BCrypt)
- User login với JWT response
- User profile management

### **JWT Token:**
- Stateless authentication
- Token expiration (24 hours)
- Refresh token support
- Secure token generation

### **Protected Endpoints:**
- Public: `/auth/register`, `/auth/login`
- Protected: All `/api/students/*` endpoints
- Admin only: User management endpoints

---

## 🗄️ User Document Structure

### **User Document:**
```json
{
  "_id": "ObjectId",
  "username": "john_doe",
  "email": "john@email.com",
  "password": "$2a$10$encrypted_password_hash",
  "roles": ["USER"],
  "enabled": true,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

### **JWT Token Structure:**
```json
{
  "sub": "john_doe",
  "email": "john@email.com",
  "roles": ["USER"],
  "iat": 1642248600,
  "exp": 1642335000
}
```

---

## 🔧 Technology Stack

### **Security Dependencies:**
- **Spring Security** - Core security framework
- **JWT (jjwt)** - JSON Web Token implementation
- **BCrypt** - Password hashing
- **Spring Security MongoDB** - MongoDB integration

### **New Endpoints:**
- `POST /auth/register` - User registration
- `POST /auth/login` - User login
- `GET /auth/profile` - Get user profile
- `PUT /auth/profile` - Update user profile

### **Protected Endpoints:**
- All existing `/api/students/*` endpoints require JWT token

---

## 🚀 Key Security Features

### **1. Password Security:**
- BCrypt hashing với salt
- Minimum password requirements
- Password validation rules
- Secure password storage

### **2. JWT Implementation:**
- Stateless authentication
- Token-based authorization
- Configurable expiration
- Secure secret key

### **3. Request Security:**
- CORS configuration
- CSRF protection disabled (for API)
- HTTP headers security
- Request rate limiting (optional)

### **4. Role-Based Access:**
- USER role: Basic student operations
- ADMIN role: User management
- Flexible role system
- Permission-based access

---

## 📊 API Endpoints Overview

### **Authentication Endpoints (4 new):**
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login user
- `GET /auth/profile` - Get current user
- `PUT /auth/profile` - Update user profile

### **Protected Student Endpoints (15 existing):**
- All `/api/students/*` endpoints require JWT token
- Same functionality, now with security

### **Security Headers Required:**
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

**Total: 19 endpoints (4 auth + 15 protected student)**

---

## 🎓 Learning Outcomes

Sau Lesson 7, bạn sẽ nắm vững:

### **Spring Security Concepts:**
- Security configuration
- Authentication vs Authorization
- Filter chains
- Security context

### **JWT Authentication:**
- Token generation
- Token validation
- Stateless authentication
- Token expiration handling

### **Password Security:**
- BCrypt hashing
- Salt generation
- Password policies
- Secure storage

### **API Security:**
- Protected endpoints
- Bearer token authentication
- CORS configuration
- Security headers

### **Production Security:**
- Environment-based secrets
- Token refresh strategies
- Security best practices
- Vulnerability prevention

---

## 🔗 Related Files

### **Security Implementation:**
- `src/main/java/com/example/demo/document/User.java`
- `src/main/java/com/example/demo/security/SecurityConfig.java`
- `src/main/java/com/example/demo/security/JwtTokenProvider.java`
- `src/main/java/com/example/demo/security/JwtAuthenticationFilter.java`
- `src/main/java/com/example/demo/controller/AuthController.java`

### **DTOs:**
- `src/main/java/com/example/demo/dto/RegisterRequest.java`
- `src/main/java/com/example/demo/dto/LoginRequest.java`
- `src/main/java/com/example/demo/dto/AuthResponse.java`

### **Configuration:**
- `src/main/resources/application.properties` (JWT secrets)
- `pom.xml` (Security dependencies)

### **Documentation:**
- `docs/LESSON-7-POSTMAN-TESTING.md` - Postman collection
- `docs/LESSON-7-SECURITY-GUIDE.md` - Security best practices

---

## 🔄 Migration from Lesson 5

### **New Dependencies:**
- Spring Security Starter
- JWT (jjwt) library
- Spring Security Test

### **New Components:**
- User document & repository
- Authentication controller
- Security configuration
- JWT token provider
- Authentication filters

### **Updated Components:**
- All student endpoints now protected
- Error handling for auth failures
- CORS configuration for frontend

---

## 🎯 Security Best Practices

### **JWT Security:**
- Strong secret key (256-bit)
- Reasonable expiration time
- Secure token storage (client-side)
- Token refresh mechanism

### **Password Security:**
- BCrypt with high cost factor
- Password complexity requirements
- Account lockout policies
- Secure password reset

### **API Security:**
- HTTPS in production
- Rate limiting
- Input validation
- SQL injection prevention
- XSS protection

---

## 🔮 Next Steps

### **Immediate:**
1. Add security dependencies
2. Create User document
3. Implement JWT provider
4. Configure Spring Security
5. Test authentication flow

### **Advanced (Future Lessons):**
- OAuth2 integration
- Multi-factor authentication
- Session management
- Security monitoring
- Audit logging

---

<div align="center">
  <b>🔐 Lesson 7: Security & JWT Complete! 🔐</b>
  
  <br><br>
  
  **Production-ready authentication system!**
</div>