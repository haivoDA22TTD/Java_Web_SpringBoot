# 🐳 LESSON 8: Redis Setup với Docker Compose

## 🎯 Hướng dẫn setup Redis và Redis Insight

### **Yêu cầu:**
- Docker Desktop đã cài đặt
- Redis Insight app đã cài đặt
- Spring Boot project từ Lesson 7

---

## 🚀 Bước 1: Tạo Docker Compose

### **Tạo file `docker-compose.yml` trong root project:**

```yaml
version: '3.8'

services:
  # Redis Server
  redis:
    image: redis:7-alpine
    container_name: student_redis
    restart: unless-stopped
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
      - ./redis.conf:/usr/local/etc/redis/redis.conf
    command: redis-server /usr/local/etc/redis/redis.conf
    networks:
      - student_network
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 30s
      timeout: 10s
      retries: 3

  # Redis Insight - Web GUI
  redis-insight:
    image: redislabs/redisinsight:latest
    container_name: student_redis_insight
    restart: unless-stopped
    ports:
      - "8001:8001"
    depends_on:
      - redis
    networks:
      - student_network
    environment:
      - REDIS_HOSTS=local:redis:6379

  # MongoDB (existing)
  mongodb:
    image: mongo:7
    container_name: student_mongodb
    restart: unless-stopped
    ports:
      - "27017:27017"
    volumes:
      - mongodb_data:/data/db
    networks:
      - student_network
    environment:
      MONGO_INITDB_DATABASE: student_management

volumes:
  redis_data:
    driver: local
  mongodb_data:
    driver: local

networks:
  student_network:
    driver: bridge
```

---

## ⚙️ Bước 2: Tạo Redis Configuration

### **Tạo file `redis.conf` trong root project:**

```conf
# Redis Configuration for Student Management System

# Network
bind 0.0.0.0
port 6379
timeout 0
tcp-keepalive 300

# General
daemonize no
supervised no
pidfile /var/run/redis_6379.pid
loglevel notice
logfile ""

# Snapshotting
save 900 1
save 300 10
save 60 10000
stop-writes-on-bgsave-error yes
rdbcompression yes
rdbchecksum yes
dbfilename dump.rdb
dir /data

# Append Only File
appendonly yes
appendfilename "appendonly.aof"
appendfsync everysec
no-appendfsync-on-rewrite no
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
aof-load-truncated yes

# Memory Management
maxmemory 256mb
maxmemory-policy allkeys-lru
maxmemory-samples 5

# Lazy Freeing
lazyfree-lazy-eviction no
lazyfree-lazy-expire no
lazyfree-lazy-server-del no
replica-lazy-flush no

# Security
# requirepass your_password_here

# Clients
maxclients 10000

# Slow Log
slowlog-log-slower-than 10000
slowlog-max-len 128

# Latency Monitor
latency-monitor-threshold 100
```

---

## 🚀 Bước 3: Chạy Docker Compose

### **Start services:**
```bash
# Chạy tất cả services
docker-compose up -d

# Hoặc chỉ chạy Redis
docker-compose up -d redis redis-insight

# Check status
docker-compose ps
```

### **Expected Output:**
```
NAME                    IMAGE                           STATUS
student_redis           redis:7-alpine                  Up (healthy)
student_redis_insight   redislabs/redisinsight:latest   Up
student_mongodb         mongo:7                         Up
```

---

## 🔍 Bước 4: Verify Redis Connection

### **Test Redis CLI:**
```bash
# Connect to Redis container
docker exec -it student_redis redis-cli

# Test commands
127.0.0.1:6379> ping
PONG

127.0.0.1:6379> set test "Hello Redis"
OK

127.0.0.1:6379> get test
"Hello Redis"

127.0.0.1:6379> exit
```

### **Check Redis Info:**
```bash
# Redis server info
docker exec -it student_redis redis-cli info server

# Memory usage
docker exec -it student_redis redis-cli info memory

# Connected clients
docker exec -it student_redis redis-cli info clients
```

---

## 🖥️ Bước 5: Setup Redis Insight (Web GUI)

### **Access Redis Insight:**
1. **Open browser**: http://localhost:8001
2. **Add Database**:
   - **Host**: localhost (hoặc redis nếu trong Docker network)
   - **Port**: 6379
   - **Name**: Student Management Cache
   - **Username**: (để trống)
   - **Password**: (để trống nếu không set requirepass)

### **Redis Insight Features:**
- **Browser**: Xem tất cả keys và values
- **Workbench**: Chạy Redis commands
- **Analysis**: Memory usage analysis
- **Profiler**: Monitor real-time commands
- **Pub/Sub**: Message monitoring

---

## 📱 Bước 6: Setup Redis Insight Desktop App

### **Download & Install:**
1. **Download**: https://redis.com/redis-enterprise/redis-insight/
2. **Install** Redis Insight desktop app
3. **Launch** application

### **Add Connection:**
1. **Click "Add Redis Database"**
2. **Connection Details**:
   - **Host**: localhost
   - **Port**: 6379
   - **Name**: Student Management
   - **Username**: (empty)
   - **Password**: (empty)
3. **Test Connection** → **Add Database**

### **Desktop vs Web:**
- **Desktop App**: More features, better performance
- **Web GUI**: Accessible from anywhere, lighter

---

## 🧪 Bước 7: Test Redis Connection từ Spring Boot

### **Test Connection:**
```bash
# Start Spring Boot app
./mvnw spring-boot:run

# Check logs for Redis connection
# Should see: "Lettuce connection established"
```

### **Test Redis Commands:**
```java
// In your service or controller
@Autowired
private RedisTemplate<String, Object> redisTemplate;

public void testRedis() {
    redisTemplate.opsForValue().set("test-key", "test-value");
    String value = (String) redisTemplate.opsForValue().get("test-key");
    System.out.println("Redis value: " + value);
}
```

---

## 🔧 Troubleshooting

### **Common Issues:**

#### **1. Port Already in Use:**
```bash
# Check what's using port 6379
netstat -an | findstr 6379

# Kill process if needed
taskkill /PID <process_id> /F
```

#### **2. Docker Permission Issues:**
```bash
# Run as administrator
# Or add user to docker group (Linux/Mac)
sudo usermod -aG docker $USER
```

#### **3. Redis Connection Refused:**
```bash
# Check Redis container logs
docker logs student_redis

# Restart Redis container
docker-compose restart redis
```

#### **4. Memory Issues:**
```bash
# Check Redis memory usage
docker exec -it student_redis redis-cli info memory

# Clear all data if needed
docker exec -it student_redis redis-cli flushall
```

---

## 📊 Monitoring Commands

### **Redis Health Check:**
```bash
# Ping Redis
docker exec -it student_redis redis-cli ping

# Check Redis info
docker exec -it student_redis redis-cli info

# Monitor real-time commands
docker exec -it student_redis redis-cli monitor
```

### **Docker Health Check:**
```bash
# Check container health
docker-compose ps

# View container logs
docker-compose logs redis
docker-compose logs redis-insight

# Container resource usage
docker stats student_redis
```

---

## 🚀 Next Steps

### **After Setup Complete:**
1. ✅ Redis server running on port 6379
2. ✅ Redis Insight accessible on port 8001
3. ✅ Spring Boot can connect to Redis
4. ✅ Ready to implement caching in Lesson 8

### **Ready for:**
- Spring Cache configuration
- Caching implementation in services
- Performance testing with Postman
- Cache monitoring with Redis Insight

---

## 🔗 Useful Links

- **Redis Documentation**: https://redis.io/documentation
- **Redis Insight**: https://redis.com/redis-enterprise/redis-insight/
- **Spring Data Redis**: https://spring.io/projects/spring-data-redis
- **Docker Compose**: https://docs.docker.com/compose/

---

<div align="center">
  <b>🐳 Redis Setup Complete! 🐳</b>
  
  <br><br>
  
  **Ready for high-performance caching!**
</div>