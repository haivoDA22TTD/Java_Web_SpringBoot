# 📮 LESSON 9: Postman Testing Guide - Message Queues

## 🎯 Test Message Queues với RabbitMQ

Hướng dẫn test message queue functionality với RabbitMQ và Spring AMQP.

---

## 🚀 Setup Testing Environment

### **Prerequisites:**
- RabbitMQ running via Docker Compose
- RabbitMQ Management UI accessible
- Spring Boot app với message queues enabled
- JWT token từ authentication

### **Base URL:**
```
http://localhost:8080
```

### **RabbitMQ Management:**
```
http://localhost:15672
Username: admin
Password: admin123
```

### **Headers cho tất cả requests:**
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

---

## 🐰 RabbitMQ Management UI Tests

### **Test 1: Access RabbitMQ Management**

#### **1.1 Open Management UI**
**URL:** http://localhost:15672
**Login:** admin / admin123

#### **1.2 Verify Queues**
Navigate to **Queues** tab, should see:
- `student.created`
- `student.updated` 
- `student.deleted`
- `email.notification`
- `audit.log`

#### **1.3 Verify Exchanges**
Navigate to **Exchanges** tab, should see:
- `student.exchange` (topic)
- `notification.exchange` (topic)

#### **1.4 Verify Bindings**
Navigate to **Exchanges** → Click exchange → **Bindings** section
Should see routing key bindings for each queue.

---

## 📊 Queue Management API Tests

### **Test 2: Queue Information**

#### **2.1 Get Queue Info**
**GET** `/api/queues/info`

**Expected Response:**
```json
{
  "queues": {
    "student.created": {
      "messageCount": 0,
      "consumerCount": 1,
      "exists": true
    },
    "student.updated": {
      "messageCount": 0,
      "consumerCount": 1,
      "exists": true
    },
    "student.deleted": {
      "messageCount": 0,
      "consumerCount": 1,
      "exists": true
    },
    "email.notification": {
      "messageCount": 0,
      "consumerCount": 1,
      "exists": true
    },
    "audit.log": {
      "messageCount": 0,
      "consumerCount": 1,
      "exists": true
    }
  },
  "exchanges": {
    "student.exchange": {
      "type": "topic",
      "durable": true,
      "autoDelete": false
    },
    "notification.exchange": {
      "type": "topic",
      "durable": true,
      "autoDelete": false
    }
  },
  "connections": {
    "active": 1,
    "host": "localhost",
    "port": 5672,
    "virtualHost": "/"
  },
  "timestamp": 1642248600000
}
```

#### **2.2 Get Queue Statistics**
**GET** `/api/queues/stats`

**Expected Response:**
```json
{
  "totalQueues": 5,
  "totalExchanges": 2,
  "activeConnections": 1,
  "messageCounts": {
    "student.created": 0,
    "student.updated": 0,
    "student.deleted": 0,
    "email.notification": 0,
    "audit.log": 0
  },
  "timestamp": 1642248600000
}
```

#### **2.3 Check Queue Health**
**GET** `/api/queues/health`

**Expected Response:**
```json
{
  "rabbitMQConnection": true,
  "queues": {
    "student.created": true,
    "student.updated": true,
    "student.deleted": true,
    "email.notification": true,
    "audit.log": true
  },
  "overallHealth": true,
  "timestamp": 1642248600000
}
```

---

## 🧪 Message Testing APIs

### **Test 3: Test Student Created Message**

#### **3.1 Send Test Student Created Message**
**POST** `/api/queues/test/student-created`

**Body:**
```json
{
  "studentId": "test-student-123",
  "studentName": "Test Student",
  "studentEmail": "test@example.com",
  "studentAge": 25,
  "userId": "test-user"
}
```

**Expected Response:**
```json
{
  "message": "Test student created message sent successfully",
  "studentId": "test-student-123",
  "queue": "student.created",
  "timestamp": "1642248600000"
}
```

**Console Output to Watch:**
```
📤 Published STUDENT_CREATED event for: Test Student
📥 Received STUDENT_CREATED event:
   Student ID: test-student-123
   Student Name: Test Student
   Student Email: test@example.com
   Student Age: 25
   Action By: test-user
🔍 Processing student created event...
✅ Student created event processed successfully
```

#### **3.2 Verify in RabbitMQ Management**
- Go to **Queues** → `student.created`
- Should see message count increase then decrease
- Check **Get messages** to see message content

---

### **Test 4: Test Email Notification**

#### **4.1 Send Test Email**
**POST** `/api/queues/test/email`

**Body:**
```json
{
  "to": "test@example.com",
  "subject": "Test Email Notification",
  "body": "This is a test email notification from the queue system."
}
```

**Expected Response:**
```json
{
  "message": "Test email notification sent successfully",
  "to": "test@example.com",
  "subject": "Test Email Notification",
  "queue": "email.notification",
  "timestamp": "1642248600000"
}
```

**Console Output to Watch:**
```
📧 Published EMAIL_NOTIFICATION to: test@example.com
📧 Received EMAIL_NOTIFICATION:
   To: test@example.com
   Subject: Test Email Notification
📤 Processing email notification...
📧 ========== SIMULATED EMAIL ==========
From: noreply@studentmanagement.com
To: test@example.com
Subject: Test Email Notification
✅ Email notification processed successfully
```

---

### **Test 5: Test Audit Log**

#### **5.1 Send Test Audit Log**
**POST** `/api/queues/test/audit`

**Body:**
```json
{
  "action": "TEST_ACTION",
  "entityType": "TestEntity",
  "entityId": "test-entity-123",
  "userId": "test-user"
}
```

**Expected Response:**
```json
{
  "message": "Test audit log sent successfully",
  "action": "TEST_ACTION",
  "entityType": "TestEntity",
  "entityId": "test-entity-123",
  "userId": "test-user",
  "queue": "audit.log",
  "timestamp": "1642248600000"
}
```

**Console Output to Watch:**
```
📝 Published AUDIT_LOG: TEST_ACTION on TestEntity by test-user
📝 Received AUDIT_LOG:
   Action: TEST_ACTION
   Entity Type: TestEntity
   Entity ID: test-entity-123
   User ID: test-user
📊 Processing audit log...
💾 Audit log saved to database:
   ID: AUDIT_1642248600000_123
✅ Audit log processed successfully
```

---

### **Test 6: Custom Test Message**

#### **6.1 Send Custom Message**
**POST** `/api/queues/test/custom`

**Body:**
```json
{
  "queue": "student.created",
  "message": {
    "customField": "customValue",
    "testData": "This is a custom test message"
  }
}
```

**Expected Response:**
```json
{
  "message": "Custom test message sent successfully",
  "queue": "student.created",
  "timestamp": "1642248600000"
}
```

---

## 🎯 End-to-End Message Flow Tests

### **Test 7: Complete Student Lifecycle**

#### **7.1 Create Student (Triggers Messages)**
**POST** `/api/students`

**Body:**
```json
{
  "name": "Message Test Student",
  "email": "messagetest@example.com",
  "age": 22
}
```

**Expected Messages Generated:**
1. **Student Created Event** → `student.created` queue
2. **Welcome Email** → `email.notification` queue  
3. **Audit Log** → `audit.log` queue

**Console Output to Watch:**
```
💾 Creating new student and updating cache
📤 Published STUDENT_CREATED event for: Message Test Student
📧 Published EMAIL_NOTIFICATION to: messagetest@example.com
📝 Published AUDIT_LOG: STUDENT_CREATED on Student by john_doe

📥 Received STUDENT_CREATED event:
📧 Received EMAIL_NOTIFICATION:
📝 Received AUDIT_LOG:
```

#### **7.2 Update Student (Triggers Messages)**
**PUT** `/api/students/{id}`

**Body:**
```json
{
  "name": "Updated Message Test Student",
  "email": "messagetest@example.com",
  "age": 23
}
```

**Expected Messages Generated:**
1. **Student Updated Event** → `student.updated` queue
2. **Update Notification Email** → `email.notification` queue
3. **Audit Log with Details** → `audit.log` queue

#### **7.3 Delete Student (Triggers Messages)**
**DELETE** `/api/students/{id}`

**Expected Messages Generated:**
1. **Student Deleted Event** → `student.deleted` queue
2. **Audit Log with Details** → `audit.log` queue

---

## 📊 RabbitMQ Management Monitoring

### **Test 8: Monitor Message Flow**

#### **8.1 Real-time Message Monitoring**
1. **Open RabbitMQ Management** → **Queues**
2. **Perform student operations** (create/update/delete)
3. **Watch message counts** increase/decrease in real-time
4. **Check message rates** in the graphs

#### **8.2 Message Content Inspection**
1. **Go to specific queue** (e.g., `student.created`)
2. **Click "Get messages"**
3. **Set "Messages" to 1**
4. **Click "Get Message(s)"**
5. **Inspect message payload and headers**

**Example Message Content:**
```json
{
  "eventType": "CREATED",
  "studentId": "507f1f77bcf86cd799439011",
  "studentName": "Test Student",
  "studentEmail": "test@example.com",
  "studentAge": 25,
  "actionBy": "john_doe",
  "timestamp": "2024-01-15T10:30:00"
}
```

#### **8.3 Connection Monitoring**
1. **Go to "Connections" tab**
2. **Verify Spring Boot connection** is active
3. **Check channels** and their status
4. **Monitor message rates**

---

## 🔄 Error Handling Tests

### **Test 9: Invalid Message Tests**

#### **9.1 Send Invalid Email (Missing Required Fields)**
**POST** `/api/queues/test/email`

**Body:**
```json
{
  "to": "",
  "subject": "",
  "body": ""
}
```

**Expected Console Output:**
```
📧 Received EMAIL_NOTIFICATION:
❌ Email validation failed: 'to' field is empty
```

#### **9.2 Send Invalid Audit Log**
**POST** `/api/queues/test/audit`

**Body:**
```json
{
  "action": "",
  "entityType": "",
  "entityId": "",
  "userId": ""
}
```

**Expected Console Output:**
```
📝 Received AUDIT_LOG:
❌ Audit validation failed: 'action' field is empty
```

---

### **Test 10: Queue Failure Simulation**

#### **10.1 Stop RabbitMQ Container**
```bash
docker-compose stop rabbitmq
```

#### **10.2 Try Queue Operations**
**GET** `/api/queues/health`

**Expected Response:**
```json
{
  "error": "Health check failed",
  "message": "Connection refused",
  "healthy": false
}
```

#### **10.3 Restart RabbitMQ**
```bash
docker-compose start rabbitmq
```

#### **10.4 Verify Recovery**
**GET** `/api/queues/health`

**Expected Response:**
```json
{
  "rabbitMQConnection": true,
  "overallHealth": true
}
```

---

## 📈 Performance Testing

### **Test 11: High Volume Message Testing**

#### **11.1 Send Multiple Messages Rapidly**
Create a Postman collection with:
- 10 student creation requests
- 10 email notification tests  
- 10 audit log tests

**Run collection** with:
- **Iterations**: 1
- **Delay**: 100ms between requests

#### **11.2 Monitor Performance**
Watch in RabbitMQ Management:
- **Message rates** (messages/second)
- **Queue depths** (pending messages)
- **Consumer performance**
- **Memory usage**

#### **11.3 Expected Results**
- All messages should be processed
- No message loss
- Reasonable processing times
- Stable memory usage

---

## 📋 Testing Checklist

### **Message Queue Functionality:**
- [ ] All queues are created and bound correctly
- [ ] Messages are published successfully
- [ ] Messages are consumed and processed
- [ ] Error handling works for invalid messages
- [ ] Queue health monitoring works

### **Student Lifecycle Integration:**
- [ ] Student creation triggers all expected messages
- [ ] Student updates trigger appropriate messages
- [ ] Student deletion triggers cleanup messages
- [ ] Audit logs capture all operations
- [ ] Email notifications are sent

### **RabbitMQ Integration:**
- [ ] Management UI is accessible
- [ ] Queue statistics are accurate
- [ ] Message content is correct
- [ ] Connection monitoring works
- [ ] Error recovery works

### **Performance & Reliability:**
- [ ] High volume message processing
- [ ] No message loss under load
- [ ] Graceful error handling
- [ ] Connection recovery after failures
- [ ] Memory usage is stable

---

## 🎯 Success Criteria

### **Functionality Targets:**
- **Message Delivery**: 100% success rate
- **Processing Time**: < 100ms per message
- **Error Handling**: Graceful failure recovery
- **Queue Health**: All queues operational

### **Integration Targets:**
- **Student Operations**: All trigger appropriate messages
- **Email Notifications**: All sent successfully
- **Audit Logging**: Complete operation tracking
- **Cache Integration**: Works with message processing

### **Performance Targets:**
- **Throughput**: > 100 messages/second
- **Latency**: < 50ms average processing time
- **Memory Usage**: < 200MB for message processing
- **Connection Stability**: 99.9% uptime

---

<div align="center">
  <b>📮 Message Queue Testing Complete! 📮</b>
  
  <br><br>
  
  **Reliable asynchronous processing with RabbitMQ!**
</div>