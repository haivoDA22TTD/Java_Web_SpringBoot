# 🧪 LESSON 8: Postman Testing Guide - Redis Caching

## 🎯 Performance Testing với Redis Cache

Hướng dẫn test performance improvements với Redis caching.

---

## 🚀 Setup Testing Environment

### **Prerequisites:**
- Redis running via Docker Compose
- Redis Insight connected
- Spring Boot app với caching enabled
- JWT token từ authentication

### **Base URL:**
```
http://localhost:8080
```

### **Headers cho tất cả requests:**
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

---

## ⚡ Cache Performance Tests

### **Test 1: Cold Cache vs Warm Cache**

#### **1.1 Get All Students (Cold Cache)**
**GET** `/api/students`

**Expected Response Time:** ~100-500ms (database query)
**Console Log:** `🔍 Cache MISS - Fetching all students from database`

#### **1.2 Get All Students (Warm Cache)**
**GET** `/api/students` (same request immediately)

**Expected Response Time:** ~1-10ms (cache hit)
**Console Log:** No cache miss message

#### **Performance Comparison:**
- **Cold Cache**: Database query time
- **Warm Cache**: 10-50x faster response

---

### **Test 2: Individual Student Caching**

#### **2.1 Get Student by ID (Cold Cache)**
**GET** `/api/students/{id}`

Replace `{id}` with actual student ID.

**Expected Response Time:** ~50-200ms
**Console Log:** `🔍 Cache MISS - Fetching student {id} from database`

#### **2.2 Get Same Student (Warm Cache)**
**GET** `/api/students/{id}` (same ID)

**Expected Response Time:** ~1-5ms
**Console Log:** No cache miss message

---

### **Test 3: Search Results Caching**

#### **3.1 Search by Name (Cold Cache)**
**GET** `/api/students/search/name?name=John`

**Expected Response Time:** ~100-300ms
**Console Log:** `🔍 Cache MISS - Searching students by name: John`

#### **3.2 Same Search (Warm Cache)**
**GET** `/api/students/search/name?name=John`

**Expected Response Time:** ~1-10ms
**Console Log:** No cache miss message

---

### **Test 4: Expensive Statistics Caching**

#### **4.1 Get Statistics (Cold Cache)**
**GET** `/api/students/statistics`

**Expected Response Time:** ~1000-1500ms (includes 1s simulation delay)
**Console Log:** `📊 Cache MISS - Calculating expensive statistics from database`

#### **4.2 Get Statistics (Warm Cache)**
**GET** `/api/students/statistics`

**Expected Response Time:** ~1-10ms
**Console Log:** No cache miss message

**Performance Improvement:** ~100-150x faster!

---

## 🔄 Cache Invalidation Tests

### **Test 5: Cache Invalidation on Create**

#### **5.1 Get All Students (Warm Cache)**
**GET** `/api/students`
**Expected:** Fast response from cache

#### **5.2 Create New Student**
**POST** `/api/students`
```json
{
  "name": "Cache Test Student",
  "email": "cache@test.com",
  "age": 25
}
```
**Console Log:** `💾 Creating new student and updating cache`

#### **5.3 Get All Students (Cache Invalidated)**
**GET** `/api/students`
**Expected:** Slower response (cache miss) with new student included
**Console Log:** `🔍 Cache MISS - Fetching all students from database`

---

### **Test 6: Cache Invalidation on Update**

#### **6.1 Get Student by ID (Warm Cache)**
**GET** `/api/students/{id}`
**Expected:** Fast response from cache

#### **6.2 Update Student**
**PUT** `/api/students/{id}`
```json
{
  "name": "Updated Name",
  "email": "updated@email.com",
  "age": 26
}
```
**Console Log:** `✏️ Updating student {id} and refreshing cache`

#### **6.3 Get Updated Student (Cache Refreshed)**
**GET** `/api/students/{id}`
**Expected:** Fast response with updated data from cache

---

### **Test 7: Cache Invalidation on Delete**

#### **7.1 Get Student by ID (Warm Cache)**
**GET** `/api/students/{id}`
**Expected:** Fast response from cache

#### **7.2 Delete Student**
**DELETE** `/api/students/{id}`
**Console Log:** `🗑️ Deleting student {id} and removing from cache`

#### **7.3 Get Deleted Student (Cache Cleared)**
**GET** `/api/students/{id}`
**Expected:** 404 Not Found

---

## 🛠️ Cache Management Tests

### **Test 8: Cache Information**

#### **8.1 Get Cache Info**
**GET** `/api/cache/info`

**Expected Response:**
```json
{
  "cacheNames": ["students", "students-list", "students-search", "students-stats"],
  "totalKeys": 15,
  "redisKeys": ["students:507f...", "students-list:all", ...],
  "cacheDescriptions": {
    "studentsCache": "Individual student records",
    "studentsListCache": "Student lists and pagination",
    "studentsSearchCache": "Search results",
    "studentsStatsCache": "Statistics and aggregations"
  }
}
```

#### **8.2 Get Cache Statistics**
**GET** `/api/cache/stats`

**Expected Response:**
```json
{
  "totalKeys": 15,
  "studentKeys": 5,
  "listKeys": 3,
  "searchKeys": 4,
  "statsKeys": 1,
  "cacheBreakdown": {
    "students": 5,
    "students-list": 3,
    "students-search": 4,
    "students-stats": 1
  },
  "timestamp": 1642248600000
}
```

---

### **Test 9: Manual Cache Management**

#### **9.1 Clear All Caches**
**POST** `/api/cache/clear`

**Expected Response:**
```json
{
  "message": "All caches cleared successfully",
  "timestamp": "1642248600000"
}
```
**Console Log:** `🧹 Clearing all student caches`

#### **9.2 Clear Specific Cache**
**POST** `/api/cache/clear/students`

**Expected Response:**
```json
{
  "message": "Cache 'students' cleared successfully",
  "cacheName": "students",
  "timestamp": "1642248600000"
}
```

#### **9.3 Warm Up Caches**
**POST** `/api/cache/warmup`

**Expected Response:**
```json
{
  "message": "Cache warmed up successfully",
  "duration": "1250ms",
  "timestamp": "1642248600000"
}
```
**Console Log:** `🔥 Warming up cache with frequently accessed data`

---

### **Test 10: Redis Key Management**

#### **10.1 Get All Redis Keys**
**GET** `/api/cache/keys`

**Expected Response:**
```json
{
  "totalKeys": 15,
  "keys": [
    "students:507f1f77bcf86cd799439011",
    "students-list:all",
    "students-search:John",
    "students-stats:statistics"
  ]
}
```

#### **10.2 Get Keys by Pattern**
**GET** `/api/cache/keys/students:*`

**Expected Response:**
```json
{
  "pattern": "students:*",
  "matchingKeys": 5,
  "keys": [
    "students:507f1f77bcf86cd799439011",
    "students:507f1f77bcf86cd799439012",
    "students:507f1f77bcf86cd799439013"
  ]
}
```

#### **10.3 Get Cached Value**
**GET** `/api/cache/value/students-list:all`

**Expected Response:**
```json
{
  "key": "students-list:all",
  "value": [{"id": "507f...", "name": "John", ...}],
  "ttl": 245,
  "exists": true
}
```

#### **10.4 Delete Specific Key**
**DELETE** `/api/cache/key/students-search:John`

**Expected Response:**
```json
{
  "key": "students-search:John",
  "deleted": "true",
  "message": "Key deleted successfully"
}
```

---

## 📊 Performance Benchmarking

### **Benchmark Test Scenario:**

#### **Setup:**
1. Clear all caches
2. Create 10 students for testing
3. Run performance tests

#### **Test Sequence:**
```
1. GET /api/students (Cold) → Record time
2. GET /api/students (Warm) → Record time
3. GET /api/students/statistics (Cold) → Record time
4. GET /api/students/statistics (Warm) → Record time
5. Search by name (Cold) → Record time
6. Search by name (Warm) → Record time
```

#### **Expected Performance Improvements:**
- **Student List**: 10-50x faster
- **Statistics**: 100-150x faster  
- **Search Results**: 10-30x faster
- **Individual Students**: 5-20x faster

---

## 🔍 Redis Insight Monitoring

### **Monitor Cache Activity:**

#### **1. Open Redis Insight:**
- **URL**: http://localhost:8001
- **Desktop App**: Launch Redis Insight

#### **2. Watch Real-time Activity:**
- **Browser Tab**: View all cached keys
- **Profiler Tab**: Monitor Redis commands in real-time
- **Analysis Tab**: Memory usage analysis

#### **3. Key Patterns to Watch:**
```
students:*           # Individual student cache
students-list:*      # List and pagination cache
students-search:*    # Search results cache
students-stats:*     # Statistics cache
```

#### **4. Commands to Monitor:**
- `GET` - Cache hits
- `SET` - Cache writes
- `DEL` - Cache invalidation
- `EXPIRE` - TTL management

---

## 🧪 Advanced Testing Scenarios

### **Test 11: TTL Expiration**

#### **11.1 Cache Data with TTL**
**GET** `/api/students/statistics`
**Note:** Statistics cache has 30-minute TTL

#### **11.2 Check TTL in Redis Insight**
```redis
TTL students-stats:statistics
# Returns remaining seconds
```

#### **11.3 Wait for Expiration (or manually expire)**
```redis
EXPIRE students-stats:statistics 1
# Set to expire in 1 second
```

#### **11.4 Test After Expiration**
**GET** `/api/students/statistics`
**Expected:** Cache miss, database query

---

### **Test 12: Concurrent Access**

#### **12.1 Multiple Simultaneous Requests**
Send 10 concurrent requests to:
**GET** `/api/students/statistics`

**Expected Behavior:**
- First request: Cache miss, database query
- Remaining 9 requests: Cache hit (if first completes quickly)

---

### **Test 13: Memory Usage**

#### **13.1 Monitor Memory Before**
Check Redis memory usage in Redis Insight

#### **13.2 Load Test Data**
Create 100 students and access various endpoints

#### **13.3 Monitor Memory After**
Compare memory usage increase

#### **13.4 Clear Caches**
**POST** `/api/cache/clear`

#### **13.5 Verify Memory Cleanup**
Check memory usage after cache clear

---

## 📋 Testing Checklist

### **Cache Functionality:**
- [ ] Cache hits return data instantly
- [ ] Cache misses query database
- [ ] Cache invalidation works on CRUD operations
- [ ] TTL expiration works correctly
- [ ] Manual cache management works

### **Performance Improvements:**
- [ ] 10x+ improvement on repeated requests
- [ ] Statistics endpoint shows dramatic improvement
- [ ] Search results are cached effectively
- [ ] Individual student lookups are fast

### **Cache Management:**
- [ ] Cache info endpoint works
- [ ] Cache clearing works (all and specific)
- [ ] Cache warming works
- [ ] Redis key management works

### **Redis Integration:**
- [ ] Redis Insight shows cached data
- [ ] Key patterns are correct
- [ ] TTL values are appropriate
- [ ] Memory usage is reasonable

---

## 🎯 Success Criteria

### **Performance Targets:**
- **Cache Hit Response Time**: < 10ms
- **Cache Miss Response Time**: Similar to non-cached
- **Statistics Endpoint**: 100x+ improvement
- **Memory Usage**: < 100MB for test data

### **Functionality Targets:**
- **Cache Hit Ratio**: > 80% for repeated requests
- **Cache Invalidation**: Immediate on data changes
- **TTL Management**: Automatic expiration working
- **Manual Management**: All cache operations working

---

<div align="center">
  <b>🧪 Redis Caching Performance Testing Complete! 🧪</b>
  
  <br><br>
  
  **Blazing fast performance with intelligent caching!**
</div>