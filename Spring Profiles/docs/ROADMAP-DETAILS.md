# 🗺️ Spring Boot Learning Roadmap - Chi tiết

## 📋 Tổng quan Roadmap

Roadmap này được thiết kế để đưa bạn từ **Zero to Hero** trong Spring Boot, với focus vào **production-ready applications**.

---

## 🌱 **PHASE 1: FOUNDATION (Lesson 1-3)**
*Mục tiêu: Nắm vững cơ bản Spring Boot*

### **Lesson 1: REST API cơ bản** ✅
- **Đã học:** `@RestController`, `@GetMapping`, `@PathVariable`, `@RequestParam`
- **Kỹ năng:** Tạo REST endpoints đơn giản

### **Lesson 2: Validation & Error Handling**
- **Sẽ học:** `@Valid`, `@NotNull`, `@Size`, Custom Validators
- **Kỹ năng:** Validate input, xử lý lỗi chuyên nghiệp
- **Thực hành:** Global Exception Handler, Custom Error Response

### **Lesson 3: Configuration & Profiles**
- **Sẽ học:** `@ConfigurationProperties`, `@Profile`, Environment-specific configs
- **Kỹ năng:** Quản lý config cho dev/staging/prod
- **Thực hành:** YAML configs, External configurations

---

## 💾 **PHASE 2: DATABASE & PERSISTENCE (Lesson 4-6)**
*Mục tiêu: Master data persistence*

### **Lesson 4: SQL Database (JPA/Hibernate)**
- **Tech Stack:** MySQL/PostgreSQL + JPA + Hibernate
- **Sẽ học:** `@Entity`, `@Repository`, `@Transactional`, Query Methods
- **Kỹ năng:** CRUD operations, Relationships, Custom Queries
- **Thực hành:** User Management System với full CRUD

### **Lesson 5: NoSQL Database**
- **Tech Stack:** MongoDB + Redis
- **Sẽ học:** Document-based storage, Key-value caching
- **Kỹ năng:** Flexible schema design, Caching strategies
- **Thực hành:** Product Catalog với MongoDB + Redis cache

### **Lesson 6: Database Migration**
- **Tech Stack:** Flyway hoặc Liquibase
- **Sẽ học:** Version control cho database schema
- **Kỹ năng:** Safe database deployments
- **Thực hành:** Migration scripts cho production

---

## 🔐 **PHASE 3: SECURITY & AUTHENTICATION (Lesson 7-9)**
*Mục tiêu: Bảo mật ứng dụng như pro*

### **Lesson 7: JWT Authentication & Authorization**
- **Tech Stack:** Spring Security + JWT
- **Sẽ học:** `@PreAuthorize`, JWT tokens, Role-based access
- **Kỹ năng:** Secure APIs, User authentication
- **Thực hành:** Login/Register system với JWT

### **Lesson 8: Token Blacklist & Refresh Token**
- **Tech Stack:** Redis (blacklist storage) + JWT + Refresh tokens
- **Sẽ học:** 
  - Redis blacklist implementation
  - Token invalidation strategies
  - Refresh token rotation
- **Kỹ năng:** Advanced token management với Redis
- **Thực hành:** 
  - Secure logout với Redis blacklist
  - Token refresh mechanism
  - Auto-cleanup expired tokens

### **Lesson 9: Rate Limiting & API Security**
- **Tech Stack:** Bucket4j + Redis
- **Sẽ học:** Rate limiting algorithms, API throttling
- **Kỹ năng:** Protect APIs from abuse
- **Thực hành:** Different rate limits per user role

---

## ⚡ **PHASE 4: PERFORMANCE & CACHING (Lesson 10-12)**
*Mục tiêu: Optimize performance như senior dev*

### **Lesson 10: Redis Caching Strategies**
- **Tech Stack:** Spring Cache + Redis
- **Sẽ học:** `@Cacheable`, `@CacheEvict`, Cache patterns
- **Kỹ năng:** Multi-level caching, Cache invalidation
- **Thực hành:** E-commerce product caching

### **Lesson 11: Database Query Optimization**
- **Tech Stack:** JPA + Database profiling tools
- **Sẽ học:** N+1 problem, Lazy loading, Query optimization
- **Kỹ năng:** Performance tuning, Database indexing
- **Thực hành:** Optimize slow queries

### **Lesson 12: Async Processing**
- **Tech Stack:** `@Async`, CompletableFuture, Thread pools
- **Sẽ học:** Asynchronous programming, Non-blocking operations
- **Kỹ năng:** Handle concurrent requests efficiently
- **Thực hành:** Email sending, File processing

---

## 📊 **PHASE 5: OBSERVABILITY (Lesson 13-15)**
*Mục tiêu: Monitor và debug như DevOps engineer*

### **Lesson 13: Logging (Structured Logging)**
- **Tech Stack:** Logback + SLF4J + ELK Stack
- **Sẽ học:** Structured logging, Log levels, MDC
- **Kỹ năng:** Effective logging strategies
- **Thực hành:** Centralized logging với JSON format

### **Lesson 14: Tracing (Distributed Tracing)**
- **Tech Stack:** Spring Cloud Sleuth + Zipkin/Jaeger
- **Sẽ học:** Trace IDs, Span tracking, Request correlation
- **Kỹ năng:** Debug distributed systems
- **Thực hành:** Trace requests across microservices

### **Lesson 15: Monitoring (Metrics & Dashboards)**
- **Tech Stack:** Micrometer + Prometheus (metrics) + Grafana (dashboards)
- **Sẽ học:** 
  - Custom metrics với Micrometer
  - Prometheus metrics collection
  - Grafana dashboard creation
  - Health indicators & alerting
- **Kỹ năng:** Production monitoring và visualization
- **Thực hành:** 
  - Business metrics dashboard
  - System performance monitoring
  - Alerting rules setup

---

## 🔄 **PHASE 6: MESSAGING & COMMUNICATION (Lesson 16-18)**
*Mục tiêu: Build distributed systems*

### **Lesson 16: Message Queue**
- **Tech Stack:** RabbitMQ hoặc Apache Kafka
- **Sẽ học:** Pub/Sub patterns, Message durability, Dead letter queues
- **Kỹ năng:** Asynchronous communication between services
- **Thực hành:** Order processing system

### **Lesson 17: WebSocket & Real-time**
- **Tech Stack:** Spring WebSocket + STOMP
- **Sẽ học:** Real-time communication, Broadcasting
- **Kỹ năng:** Build chat applications, Live notifications
- **Thực hành:** Real-time chat application

### **Lesson 18: REST Client & Service Communication**
- **Tech Stack:** RestTemplate, WebClient, Feign
- **Sẽ học:** Service-to-service communication, Load balancing
- **Kỹ năng:** Microservices communication
- **Thực hành:** API Gateway pattern

---

## 🛡️ **PHASE 7: RESILIENCE & RELIABILITY (Lesson 19-21)**
*Mục tiêu: Build fault-tolerant systems*

### **Lesson 19: Circuit Breaker**
- **Tech Stack:** Resilience4j
- **Sẽ học:** Circuit breaker pattern, Fallback mechanisms
- **Kỹ năng:** Handle service failures gracefully
- **Thực hành:** Resilient microservice calls

### **Lesson 20: Retry & Timeout Patterns**
- **Tech Stack:** Resilience4j + Spring Retry
- **Sẽ học:** Exponential backoff, Jitter, Timeout strategies
- **Kỹ năng:** Handle transient failures
- **Thực hành:** Robust external API calls

### **Lesson 21: Health Checks & Graceful Shutdown**
- **Tech Stack:** Spring Actuator + Kubernetes probes
- **Sẽ học:** Health indicators, Graceful shutdown hooks
- **Kỹ năng:** Production-ready health monitoring
- **Thực hành:** Custom health checks

---

## 🐳 **PHASE 8: DEPLOYMENT & DEVOPS (Lesson 22-25)**
*Mục tiêu: Deploy và maintain production systems*

### **Lesson 22: Docker & Containerization**
- **Tech Stack:** Docker + Multi-stage builds
- **Sẽ học:** Dockerfile optimization, Container best practices
- **Kỹ năng:** Containerize Spring Boot apps efficiently
- **Thực hành:** Optimized Docker images

### **Lesson 23: CI/CD Pipeline**
- **Tech Stack:** GitHub Actions hoặc Jenkins
- **Sẽ học:** Automated testing, Build pipelines, Deployment strategies
- **Kỹ năng:** DevOps automation
- **Thực hành:** Complete CI/CD pipeline

### **Lesson 24: Kubernetes Deployment**
- **Tech Stack:** Kubernetes + Helm
- **Sẽ học:** Deployments, Services, ConfigMaps, Secrets
- **Kỹ năng:** Container orchestration
- **Thực hành:** Deploy microservices to K8s

### **Lesson 25: Production Best Practices**
- **Tech Stack:** Tổng hợp tất cả
- **Sẽ học:** Security hardening, Performance tuning, Monitoring
- **Kỹ năng:** Production readiness checklist
- **Thực hành:** Complete production deployment

---

## 🎯 **Dự án cuối khóa: E-Commerce Microservices**

Sau khi hoàn thành tất cả lessons, bạn sẽ xây dựng một hệ thống e-commerce hoàn chỉnh:

### **Architecture:**
```
API Gateway → [User Service, Product Service, Order Service, Payment Service]
                     ↓
[MySQL, MongoDB, Redis, RabbitMQ]
                     ↓
[Monitoring, Logging, Tracing]
```

### **Features:**
- ✅ User authentication với JWT
- ✅ Product catalog với caching
- ✅ Order processing với messaging
- ✅ Payment integration
- ✅ Real-time notifications
- ✅ Complete observability
- ✅ Production deployment

---

## 📈 **Skill Progression**

### **Sau Phase 1-2:** Junior Spring Boot Developer
- Tạo được REST APIs cơ bản
- Làm việc với database

### **Sau Phase 3-4:** Mid-level Spring Boot Developer  
- Implement security và caching
- Optimize performance

### **Sau Phase 5-6:** Senior Spring Boot Developer
- Design distributed systems
- Implement observability

### **Sau Phase 7-8:** Lead/Architect Level
- Build production-ready systems
- DevOps và deployment expertise

---

<div align="center">
  <b>🚀 Ready to become a Spring Boot Expert? Let's start! 🚀</b>
</div>

---

## 💡 **Tech Stack Chi Tiết**

### **Redis Usage Throughout Course:**
- **Lesson 5:** NoSQL Database - Redis basics, data types
- **Lesson 8:** Token Blacklist - Store invalidated JWT tokens
- **Lesson 9:** Rate Limiting - Store rate limit counters
- **Lesson 10:** Caching - Application-level caching
- **Lesson 13:** Logging - Store log aggregation data (optional)

### **Why Redis for Blacklist?**
```java
// Example: Token blacklist với Redis
@Service
public class TokenBlacklistService {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    // Thêm token vào blacklist khi logout
    public void blacklistToken(String token, long expirationTime) {
        String key = "blacklist:" + token;
        redisTemplate.opsForValue().set(key, "true", expirationTime, TimeUnit.SECONDS);
    }
    
    // Kiểm tra token có bị blacklist không
    public boolean isTokenBlacklisted(String token) {
        String key = "blacklist:" + token;
        return redisTemplate.hasKey(key);
    }
}
```

**Ưu điểm Redis cho blacklist:**
- ⚡ **Fast lookup** - O(1) time complexity
- 🕐 **TTL support** - Tự động xóa expired tokens
- 🔄 **Distributed** - Share blacklist across multiple instances
- 💾 **Memory efficient** - In-memory storage

### **Monitoring Stack Explained:**
```
Spring Boot App (Micrometer) 
        ↓ (expose /actuator/prometheus)
Prometheus (metrics collector)
        ↓ (scrape metrics)
Grafana (visualization & dashboards)
        ↓ (query Prometheus)
Alertmanager (notifications)
```

**Vai trò từng component:**
- **🔢 Micrometer:** Metrics library trong Spring Boot (counter, gauge, timer)
- **📊 Prometheus:** Time-series database, scrape và store metrics
- **📈 Grafana:** Dashboard tool, visualize metrics từ Prometheus
- **🚨 Alertmanager:** Send notifications khi có alert

**Example metrics:**
```java
// Custom business metrics
@Component
public class OrderMetrics {
    private final Counter orderCounter;
    private final Timer orderProcessingTime;
    
    public OrderMetrics(MeterRegistry meterRegistry) {
        this.orderCounter = Counter.builder("orders.created")
            .description("Total orders created")
            .register(meterRegistry);
            
        this.orderProcessingTime = Timer.builder("orders.processing.time")
            .description("Order processing time")
            .register(meterRegistry);
    }
}
```