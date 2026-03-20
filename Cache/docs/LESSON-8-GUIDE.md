# ⚡ LESSON 8: Redis Caching & Performance Optimization

## 🎯 Mục tiêu Lesson 8

Thêm Redis caching vào Student Management System để tối ưu performance:
- **Redis** integration với Docker Compose
- **Spring Cache** abstraction
- **Redis Insight** để monitor và debug
- **Caching strategies** cho các API endpoints
- **Cache invalidation** và TTL management
- **Performance testing** với Postman

---

## 🏗️ Kiến trúc Caching

### **Caching Layers:**
```
Client Request
     ↓
Spring Boot App
     ↓
Spring Cache (Abstraction)
     ↓
Redis (Cache Store)
     ↓
MongoDB (Primary Database)
```

### **Cache Strategy:**
- **Cache-Aside Pattern**: App quản lý cache manually
- **TTL (Time To Live)**: Auto-expire cached data
- **Cache Keys**: Structured naming convention
- **Eviction Policy**: LRU (Least Recently Used)

---

## 🐳 Docker Setup

### **Docker Compose Configuration:**
```yaml
version: '3.8'
services:
  redis:
    image: redis:7-alpine
    container_name: student_redis
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    command: redis-server --appendonly yes
    
  redis-insight:
    image: redislabs/redisinsight:latest
    container_name: student_redis_insight
    ports:
      - "8001:8001"
    depends_on:
      - redis

volumes:
  redis_data:
```

### **Redis Configuration:**
- **Port**: 6379 (Redis server)
- **Port**: 8001 (Redis Insight GUI)
- **Persistence**: AOF (Append Only File)
- **Memory Policy**: allkeys-lru

---

## 🔧 Spring Boot Integration

### **Dependencies (pom.xml):**
```xml
<!-- Redis & Caching -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

### **Application Properties:**
```properties
# Redis Configuration
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.timeout=2000ms
spring.data.redis.lettuce.pool.max-active=8
spring.data.redis.lettuce.pool.max-idle=8
spring.data.redis.lettuce.pool.min-idle=0

# Cache Configuration
spring.cache.type=redis
spring.cache.redis.time-to-live=600000
spring.cache.redis.cache-null-values=false
```

---

## 📊 Caching Implementation

### **Cache Configuration:**
```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))
            .serializeKeysWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer()));
        
        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .build();
    }
}
```

### **Cached Methods:**
```java
@Service
public class StudentService {
    
    @Cacheable(value = "students", key = "#id")
    public StudentResponse getStudentById(String id) {
        // Database query only if not in cache
    }
    
    @Cacheable(value = "students-list", key = "'all'")
    public List<StudentResponse> getAllStudents() {
        // Expensive query cached for 10 minutes
    }
    
    @CacheEvict(value = "students", key = "#id")
    public StudentResponse updateStudent(String id, StudentRequest request) {
        // Remove from cache after update
    }
    
    @CacheEvict(value = {"students", "students-list"}, allEntries = true)
    public void clearAllCache() {
        // Clear all student caches
    }
}
```

---

## 🔑 Cache Keys Strategy

### **Naming Convention:**
```
students:{id}           # Single student by ID
students:list:all       # All students list
students:search:{query} # Search results
students:stats          # Statistics data
users:{username}        # User profile cache
```

### **TTL Strategy:**
- **Student Data**: 10 minutes (frequently updated)
- **Search Results**: 5 minutes (dynamic content)
- **Statistics**: 30 minutes (expensive aggregation)
- **User Profiles**: 60 minutes (rarely changed)

---

## 📈 Performance Benefits

### **Before Caching:**
- **Database Query**: ~100-500ms per request
- **Complex Aggregation**: ~1-3 seconds
- **Concurrent Users**: Limited by DB connections

### **After Caching:**
- **Cache Hit**: ~1-5ms response time
- **Cache Miss**: Same as before + cache write
- **Concurrent Users**: Significantly higher throughput

### **Cache Hit Ratio Target:**
- **80%+** for frequently accessed data
- **60%+** for search queries
- **90%+** for static/reference data

---

## 🛠️ Redis Insight Usage

### **Connection Setup:**
1. **Open Redis Insight**: http://localhost:8001
2. **Add Database**: 
   - Host: localhost
   - Port: 6379
   - Name: Student Management Cache

### **Monitoring Features:**
- **Memory Usage**: Track cache size
- **Key Browser**: Explore cached data
- **Performance Metrics**: Hit/miss ratios
- **TTL Monitoring**: Key expiration times

### **Debug Commands:**
```redis
# View all keys
KEYS *

# Get specific student
GET students:507f1f77bcf86cd799439011

# Check TTL
TTL students:list:all

# Memory usage
MEMORY USAGE students:507f1f77bcf86cd799439011

# Cache statistics
INFO stats
```

---

## 🧪 Testing Strategy

### **Cache Testing Scenarios:**
1. **Cold Cache**: First request (cache miss)
2. **Warm Cache**: Subsequent requests (cache hit)
3. **Cache Invalidation**: Update/delete operations
4. **TTL Expiration**: Wait for cache expiry
5. **Memory Pressure**: Large dataset caching

### **Performance Metrics:**
- **Response Time**: Before/after caching
- **Database Load**: Query reduction
- **Memory Usage**: Redis memory consumption
- **Hit Ratio**: Cache effectiveness

---

## 🔄 Cache Invalidation Strategies

### **Manual Invalidation:**
```java
@CacheEvict(value = "students", key = "#id")
public void invalidateStudent(String id) {
    // Remove specific student from cache
}

@CacheEvict(value = "students-list", allEntries = true)
public void invalidateAllStudents() {
    // Clear all student list caches
}
```

### **Automatic Invalidation:**
- **TTL Expiration**: Time-based invalidation
- **Event-Driven**: Database change triggers
- **Scheduled Cleanup**: Periodic cache refresh

### **Cache Warming:**
```java
@EventListener(ApplicationReadyEvent.class)
public void warmUpCache() {
    // Pre-load frequently accessed data
    getAllStudents();
    getStudentStatistics();
}
```

---

## 🚀 Advanced Caching Patterns

### **1. Cache-Aside (Lazy Loading):**
```java
@Cacheable(value = "students", key = "#id")
public StudentResponse getStudent(String id) {
    return studentRepository.findById(id)
        .map(this::convertToResponse)
        .orElse(null);
}
```

### **2. Write-Through:**
```java
@CachePut(value = "students", key = "#result.id")
public StudentResponse createStudent(StudentRequest request) {
    Student student = studentRepository.save(convertToEntity(request));
    return convertToResponse(student);
}
```

### **3. Write-Behind (Write-Back):**
```java
@Async
@CacheEvict(value = "students", key = "#id")
public void updateStudentAsync(String id, StudentRequest request) {
    // Async database update
    studentRepository.updateById(id, request);
}
```

---

## 📊 Monitoring & Metrics

### **Spring Boot Actuator:**
```properties
# Enable cache metrics
management.endpoints.web.exposure.include=health,info,metrics,caches
management.endpoint.caches.enabled=true
```

### **Cache Metrics:**
- `cache.gets` - Total cache get operations
- `cache.puts` - Total cache put operations  
- `cache.evictions` - Cache evictions count
- `cache.hit.ratio` - Cache hit percentage

### **Redis Metrics:**
- Memory usage and fragmentation
- Connected clients count
- Commands processed per second
- Key expiration events

---

## 🔗 Related Files

### **Configuration:**
- `src/main/java/com/example/demo/config/CacheConfig.java`
- `src/main/resources/application.properties`
- `docker-compose.yml`

### **Service Layer:**
- `src/main/java/com/example/demo/service/StudentService.java` (updated)
- `src/main/java/com/example/demo/service/CacheService.java` (new)

### **Testing:**
- `docs/LESSON-8-REDIS-SETUP.md` - Docker & Redis Insight setup
- `docs/LESSON-8-POSTMAN-TESTING.md` - Performance testing guide

---

## 🎓 Learning Outcomes

Sau Lesson 8, bạn sẽ nắm vững:

### **Redis Fundamentals:**
- Redis data structures and commands
- Docker containerization
- Redis Insight monitoring

### **Spring Cache:**
- Cache annotations (@Cacheable, @CacheEvict, @CachePut)
- Cache configuration and customization
- Multiple cache providers

### **Performance Optimization:**
- Caching strategies and patterns
- Cache invalidation techniques
- Performance measurement

### **Production Considerations:**
- Memory management
- Cache warming strategies
- Monitoring and alerting

---

## 🔮 Next Steps

### **Immediate (Lesson 8):**
1. Setup Redis with Docker Compose
2. Configure Spring Cache
3. Implement caching in StudentService
4. Test with Redis Insight
5. Performance testing with Postman

### **Advanced (Future):**
- Distributed caching
- Cache clustering
- Custom cache serialization
- Cache-based session management

---

<div align="center">
  <b>⚡ Lesson 8: Redis Caching Complete! ⚡</b>
  
  <br><br>
  
  **Blazing fast performance with intelligent caching!**
</div>