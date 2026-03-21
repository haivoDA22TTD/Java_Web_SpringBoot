# 🐰 LESSON 9: RabbitMQ Setup với Docker Compose

## 🎯 Hướng dẫn setup RabbitMQ và Message Queue

### **Yêu cầu:**
- Docker Desktop đã cài đặt
- Spring Boot project từ Lesson 8
- Postman cho API testing

---

## 🚀 Bước 1: Update Docker Compose

### **Cập nhật file `docker-compose.yml`:**

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

  # RabbitMQ Server with Management UI
  rabbitmq:
    image: rabbitmq:3-management
    container_name: student_rabbitmq
    restart: unless-stopped
    ports:
      - "5672:5672"     # AMQP port
      - "15672:15672"   # Management UI port
    environment:
      RABBITMQ_DEFAULT_USER: admin
      RABBITMQ_DEFAULT_PASS: admin123
      RABBITMQ_DEFAULT_VHOST: /
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq
    networks:
      - student_network
    healthcheck:
      test: ["CMD", "rabbitmq-diagnostics", "ping"]
      interval: 30s
      timeout: 10s
      retries: 3

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
  rabbitmq_data:
    driver: local
  mongodb_data:
    driver: local

networks:
  student_network:
    driver: bridge
```

---

## 🚀 Bước 2: Start Services

### **Start all services:**
```bash
# Stop existing containers (if any)
docker-compose down

# Start all services including RabbitMQ
docker-compose up -d

# Check status
docker-compose ps
```

### **Expected Output:**
```
NAME                    IMAGE                           STATUS
student_redis           redis:7-alpine                  Up (healthy)
student_redis_insight   redislabs/redisinsight:latest   Up
student_rabbitmq        rabbitmq:3-management           Up (healthy)
student_mongodb         mongo:7                         Up
```

---

## 🔍 Bước 3: Verify RabbitMQ Connection

### **Test RabbitMQ CLI:**
```bash
# Connect to RabbitMQ container
docker exec -it student_rabbitmq bash

# Inside container - check status
rabbitmqctl status

# List queues
rabbitmqctl list_queues

# List exchanges
rabbitmqctl list_exchanges

# Exit container
exit
```

### **Check RabbitMQ Logs:**
```bash
# View RabbitMQ logs
docker logs student_rabbitmq

# Follow logs in real-time
docker logs -f student_rabbitmq
```

---

## 🖥️ Bước 4: Access RabbitMQ Management UI

### **Open Management Interface:**
1. **URL**: http://localhost:15672
2. **Username**: admin
3. **Password**: admin123

### **Management UI Features:**
- **Overview**: Server status, message rates, connections
- **Connections**: Active connections from applications
- **Channels**: Communication channels
- **Exchanges**: Message routing components
- **Queues**: Message storage and consumption
- **Admin**: User management, virtual hosts

### **Key Sections to Monitor:**
- **Queues Tab**: See all queues, message counts, consumers
- **Exchanges Tab**: View exchanges and bindings
- **Connections Tab**: Monitor application connections

---

## 📧 Bước 5: Email Configuration (Optional)

### **Gmail Setup (for email notifications):**

#### **1. Enable 2-Factor Authentication:**
- Go to Google Account settings
- Enable 2-Factor Authentication

#### **2. Generate App Password:**
- Go to Google Account → Security
- App passwords → Generate password for "Mail"
- Copy the generated password

#### **3. Update application.properties:**
```properties
# Email Configuration (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=3000
spring.mail.properties.mail.smtp.writetimeout=5000
```

### **Alternative Email Providers:**
```properties
# Outlook/Hotmail
spring.mail.host=smtp-mail.outlook.com
spring.mail.port=587

# Yahoo
spring.mail.host=smtp.mail.yahoo.com
spring.mail.port=587

# For testing - use MailHog (fake SMTP)
# docker run -d -p 1025:1025 -p 8025:8025 mailhog/mailhog
spring.mail.host=localhost
spring.mail.port=1025
```

---

## 🧪 Bước 6: Test RabbitMQ Connection từ Spring Boot

### **Test Connection:**
```bash
# Start Spring Boot app
./mvnw spring-boot:run

# Check logs for RabbitMQ connection
# Should see: "Created new connection: rabbitConnectionFactory"
```

### **Test Message Publishing:**
```java
// In your service or controller
@Autowired
private RabbitTemplate rabbitTemplate;

public void testRabbitMQ() {
    rabbitTemplate.convertAndSend("test.queue", "Hello RabbitMQ!");
    System.out.println("Message sent to RabbitMQ");
}
```

---

## 🔧 Troubleshooting

### **Common Issues:**

#### **1. Port Already in Use:**
```bash
# Check what's using port 5672 or 15672
netstat -an | findstr 5672
netstat -an | findstr 15672

# Kill process if needed
taskkill /PID <process_id> /F
```

#### **2. RabbitMQ Connection Refused:**
```bash
# Check RabbitMQ container logs
docker logs student_rabbitmq

# Restart RabbitMQ container
docker-compose restart rabbitmq

# Check if RabbitMQ is ready
docker exec student_rabbitmq rabbitmqctl status
```

#### **3. Authentication Issues:**
```bash
# Reset RabbitMQ user (inside container)
docker exec -it student_rabbitmq bash
rabbitmqctl add_user admin admin123
rabbitmqctl set_user_tags admin administrator
rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"
```

#### **4. Memory Issues:**
```bash
# Check RabbitMQ memory usage
docker exec student_rabbitmq rabbitmqctl status | grep memory

# Clear all queues (if needed)
docker exec student_rabbitmq rabbitmqctl purge_queue <queue_name>
```

---

## 📊 Monitoring Commands

### **RabbitMQ Health Check:**
```bash
# Check RabbitMQ status
docker exec student_rabbitmq rabbitmqctl status

# List all queues with details
docker exec student_rabbitmq rabbitmqctl list_queues name messages consumers

# List exchanges
docker exec student_rabbitmq rabbitmqctl list_exchanges name type

# List bindings
docker exec student_rabbitmq rabbitmqctl list_bindings
```

### **Docker Health Check:**
```bash
# Check all container health
docker-compose ps

# View RabbitMQ container stats
docker stats student_rabbitmq

# Check container resource usage
docker exec student_rabbitmq df -h
```

---

## 🎛️ RabbitMQ Management Commands

### **Queue Management:**
```bash
# Create queue
docker exec student_rabbitmq rabbitmqctl declare queue name=test.queue durable=true

# Delete queue
docker exec student_rabbitmq rabbitmqctl delete_queue test.queue

# Purge queue (remove all messages)
docker exec student_rabbitmq rabbitmqctl purge_queue test.queue
```

### **User Management:**
```bash
# List users
docker exec student_rabbitmq rabbitmqctl list_users

# Add user
docker exec student_rabbitmq rabbitmqctl add_user newuser password123

# Set permissions
docker exec student_rabbitmq rabbitmqctl set_permissions -p / newuser ".*" ".*" ".*"
```

---

## 🚀 Next Steps

### **After Setup Complete:**
1. ✅ RabbitMQ server running on port 5672
2. ✅ Management UI accessible on port 15672
3. ✅ Spring Boot can connect to RabbitMQ
4. ✅ Email configuration ready (optional)
5. ✅ Ready to implement message queues

### **Ready for:**
- Spring AMQP configuration
- Message publishing and consuming
- Queue management APIs
- Async processing implementation

---

## 🔗 Useful Links

- **RabbitMQ Documentation**: https://www.rabbitmq.com/documentation.html
- **Spring AMQP**: https://spring.io/projects/spring-amqp
- **RabbitMQ Management**: https://www.rabbitmq.com/management.html
- **Docker RabbitMQ**: https://hub.docker.com/_/rabbitmq

---

## 📋 Quick Reference

### **Access Points:**
- **RabbitMQ AMQP**: localhost:5672
- **RabbitMQ Management**: http://localhost:15672
- **Redis**: localhost:6379
- **Redis Insight**: http://localhost:8001
- **MongoDB**: localhost:27017

### **Default Credentials:**
- **RabbitMQ**: admin / admin123
- **Redis**: No password
- **MongoDB**: No authentication

---

<div align="center">
  <b>🐰 RabbitMQ Setup Complete! 🐰</b>
  
  <br><br>
  
  **Ready for async message processing!**
</div>