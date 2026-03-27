# 📨 LESSON 9: Message Queues & Async Processing

## 🎯 Mục tiêu Lesson 9

Thêm Message Queue vào Student Management System để xử lý bất đồng bộ:
- **RabbitMQ** integration với Spring Boot
- **Async Processing** cho các tác vụ nặng
- **Event-Driven Architecture** với message publishing
- **Queue Management** và monitoring
- **Email Notifications** và background tasks
- **Microservices Communication** patterns

---

## 🏗️ Kiến trúc Message Queue

### **Message Flow:**
```
Student API Request
     ↓
Sync Response (Fast)
     ↓
Publish Message to Queue
     ↓
RabbitMQ Queue
     ↓
Background Consumer
     ↓
Process Task (Email, Logging, etc.)
```

### **Queue Types:**
- **Direct Queue**: Point-to-point messaging
- **Fanout Exchange**: Broadcast to all queues
- **Topic Exchange**: Routing based on patterns
- **Dead Letter Queue**: Failed message handling

---

## 🐰 RabbitMQ Setup

### **Docker Compose Integration:**
```yaml
services:
  rabbitmq:
    image: rabbitmq:3-management
    container_name: student_rabbitmq
    ports:
      - "5672:5672"     # AMQP port
      - "15672:15672"   # Management UI
    environment:
      RABBITMQ_DEFAULT_USER: admin
      RABBITMQ_DEFAULT_PASS: admin123
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq
    networks:
      - student_network
```

### **Management UI:**
- **URL**: http://localhost:15672
- **Username**: admin
- **Password**: admin123
- **Features**: Queue monitoring, message tracking, performance metrics

---

## 🔧 Spring Boot Integration

### **Dependencies (pom.xml):**
```xml
<!-- RabbitMQ & AMQP -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>

<!-- Email Support -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>

<!-- JSON Processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

### **Application Properties:**
```properties
# RabbitMQ Configuration
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=admin
spring.rabbitmq.password=admin123
spring.rabbitmq.virtual-host=/

# Connection Pool
spring.rabbitmq.connection-timeout=60000
spring.rabbitmq.listener.simple.retry.enabled=true
spring.rabbitmq.listener.simple.retry.max-attempts=3

# Email Configuration (Gmail example)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## 📊 Message Queue Implementation

### **Queue Configuration:**
```java
@Configuration
@EnableRabbit
public class RabbitConfig {
    
    // Queue Names
    public static final String STUDENT_CREATED_QUEUE = "student.created";
    public static final String STUDENT_UPDATED_QUEUE = "student.updated";
    public static final String STUDENT_DELETED_QUEUE = "student.deleted";
    public static final String EMAIL_NOTIFICATION_QUEUE = "email.notification";
    public static final String AUDIT_LOG_QUEUE = "audit.log";
    
    // Exchange Names
    public static final String STUDENT_EXCHANGE = "student.exchange";
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    
    // Routing Keys
    public static final String STUDENT_CREATED_ROUTING_KEY = "student.created";
    public static final String STUDENT_UPDATED_ROUTING_KEY = "student.updated";
    public static final String STUDENT_DELETED_ROUTING_KEY = "student.deleted";
    
    @Bean
    public TopicExchange studentExchange() {
        return new TopicExchange(STUDENT_EXCHANGE);
    }
    
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }
    
    @Bean
    public Queue studentCreatedQueue() {
        return QueueBuilder.durable(STUDENT_CREATED_QUEUE).build();
    }
    
    @Bean
    public Queue emailNotificationQueue() {
        return QueueBuilder.durable(EMAIL_NOTIFICATION_QUEUE).build();
    }
    
    @Bean
    public Queue auditLogQueue() {
        return QueueBuilder.durable(AUDIT_LOG_QUEUE).build();
    }
    
    // Bindings
    @Bean
    public Binding studentCreatedBinding() {
        return BindingBuilder
            .bind(studentCreatedQueue())
            .to(studentExchange())
            .with(STUDENT_CREATED_ROUTING_KEY);
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
}
```

### **Message DTOs:**
```java
// Student Event Message
public class StudentEventMessage {
    private String eventType; // CREATED, UPDATED, DELETED
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String timestamp;
    private String userId; // Who performed the action
    
    // Constructors, getters, setters
}

// Email Notification Message
public class EmailNotificationMessage {
    private String to;
    private String subject;
    private String body;
    private String template;
    private Map<String, Object> variables;
    
    // Constructors, getters, setters
}

// Audit Log Message
public class AuditLogMessage {
    private String action;
    private String entityType;
    private String entityId;
    private String userId;
    private String timestamp;
    private Map<String, Object> details;
    
    // Constructors, getters, setters
}
```

---

## 📤 Message Publishing

### **Message Publisher Service:**
```java
@Service
public class MessagePublisher {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void publishStudentCreated(StudentResponse student, String userId) {
        StudentEventMessage message = new StudentEventMessage(
            "CREATED",
            student.getId(),
            student.getName(),
            student.getEmail(),
            Instant.now().toString(),
            userId
        );
        
        rabbitTemplate.convertAndSend(
            RabbitConfig.STUDENT_EXCHANGE,
            RabbitConfig.STUDENT_CREATED_ROUTING_KEY,
            message
        );
        
        System.out.println("📤 Published STUDENT_CREATED event for: " + student.getName());
    }
    
    public void publishEmailNotification(String to, String subject, String body) {
        EmailNotificationMessage message = new EmailNotificationMessage(
            to, subject, body, "default", new HashMap<>()
        );
        
        rabbitTemplate.convertAndSend(
            RabbitConfig.NOTIFICATION_EXCHANGE,
            "email.send",
            message
        );
        
        System.out.println("📧 Published EMAIL_NOTIFICATION to: " + to);
    }
    
    public void publishAuditLog(String action, String entityType, String entityId, String userId) {
        AuditLogMessage message = new AuditLogMessage(
            action, entityType, entityId, userId, 
            Instant.now().toString(), new HashMap<>()
        );
        
        rabbitTemplate.convertAndSend(
            RabbitConfig.STUDENT_EXCHANGE,
            "audit.log",
            message
        );
        
        System.out.println("📝 Published AUDIT_LOG: " + action + " on " + entityType);
    }
}
```

### **Updated Student Service:**
```java
@Service
public class StudentService {
    
    @Autowired
    private MessagePublisher messagePublisher;
    
    @Caching(
        put = @CachePut(value = "students", key = "#result.id"),
        evict = @CacheEvict(value = "students-list", allEntries = true)
    )
    public StudentResponse createStudent(StudentRequest request) {
        System.out.println("💾 Creating new student and updating cache");
        Student student = convertToEntity(request);
        Student savedStudent = studentRepository.save(student);
        StudentResponse response = convertToResponse(savedStudent);
        
        // Publish async events
        messagePublisher.publishStudentCreated(response, getCurrentUserId());
        messagePublisher.publishEmailNotification(
            response.getEmail(),
            "Welcome to Student Management System",
            "Hello " + response.getName() + ", your account has been created successfully!"
        );
        messagePublisher.publishAuditLog("CREATE", "STUDENT", response.getId(), getCurrentUserId());
        
        return response;
    }
    
    // Similar updates for update and delete methods
}
```

---

## 📥 Message Consumers

### **Student Event Consumer:**
```java
@Component
public class StudentEventConsumer {
    
    @RabbitListener(queues = RabbitConfig.STUDENT_CREATED_QUEUE)
    public void handleStudentCreated(StudentEventMessage message) {
        System.out.println("🎉 Processing STUDENT_CREATED event: " + message.getStudentName());
        
        try {
            // Background processing
            Thread.sleep(2000); // Simulate processing time
            
            // Log to database
            logStudentEvent(message);
            
            // Update statistics
            updateStudentStatistics();
            
            // Notify admin
            notifyAdminOfNewStudent(message);
            
            System.out.println("✅ Successfully processed STUDENT_CREATED event");
            
        } catch (Exception e) {
            System.err.println("❌ Error processing STUDENT_CREATED event: " + e.getMessage());
            throw new RuntimeException("Failed to process student created event", e);
        }
    }
    
    @RabbitListener(queues = RabbitConfig.STUDENT_UPDATED_QUEUE)
    public void handleStudentUpdated(StudentEventMessage message) {
        System.out.println("📝 Processing STUDENT_UPDATED event: " + message.getStudentName());
        // Similar processing logic
    }
    
    @RabbitListener(queues = RabbitConfig.STUDENT_DELETED_QUEUE)
    public void handleStudentDeleted(StudentEventMessage message) {
        System.out.println("🗑️ Processing STUDENT_DELETED event: " + message.getStudentName());
        // Cleanup logic, archive data, etc.
    }
    
    private void logStudentEvent(StudentEventMessage message) {
        // Save to audit log table
        System.out.println("📝 Logged student event to database");
    }
    
    private void updateStudentStatistics() {
        // Update cached statistics
        System.out.println("📊 Updated student statistics");
    }
    
    private void notifyAdminOfNewStudent(StudentEventMessage message) {
        // Send notification to admin
        System.out.println("🔔 Notified admin of new student: " + message.getStudentName());
    }
}
```

### **Email Notification Consumer:**
```java
@Component
public class EmailNotificationConsumer {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @RabbitListener(queues = RabbitConfig.EMAIL_NOTIFICATION_QUEUE)
    public void handleEmailNotification(EmailNotificationMessage message) {
        System.out.println("📧 Processing EMAIL_NOTIFICATION to: " + message.getTo());
        
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(message.getTo());
            email.setSubject(message.getSubject());
            email.setText(message.getBody());
            email.setFrom("noreply@studentmanagement.com");
            
            // Simulate email sending (comment out for real sending)
            // mailSender.send(email);
            
            // For demo purposes, just log
            System.out.println("📬 Email sent successfully to: " + message.getTo());
            System.out.println("📄 Subject: " + message.getSubject());
            
        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
```

### **Audit Log Consumer:**
```java
@Component
public class AuditLogConsumer {
    
    @RabbitListener(queues = RabbitConfig.AUDIT_LOG_QUEUE)
    public void handleAuditLog(AuditLogMessage message) {
        System.out.println("📝 Processing AUDIT_LOG: " + message.getAction() + " on " + message.getEntityType());
        
        try {
            // Save to audit log database/file
            saveAuditLog(message);
            
            // Check for suspicious activities
            checkSecurityAlerts(message);
            
            System.out.println("✅ Audit log processed successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to process audit log: " + e.getMessage());
        }
    }
    
    private void saveAuditLog(AuditLogMessage message) {
        // Save to database or file
        System.out.println("💾 Saved audit log: " + message.getAction());
    }
    
    private void checkSecurityAlerts(AuditLogMessage message) {
        // Check for suspicious patterns
        System.out.println("🔍 Checked security alerts for: " + message.getAction());
    }
}
```

---

## 🎛️ Queue Management API

### **Queue Management Controller:**
```java
@RestController
@RequestMapping("/api/queues")
@CrossOrigin(origins = "*")
public class QueueController {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private RabbitAdmin rabbitAdmin;
    
    /**
     * GET /api/queues/info - Get queue information
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getQueueInfo() {
        Map<String, Object> info = new HashMap<>();
        
        // Queue names
        List<String> queues = Arrays.asList(
            RabbitConfig.STUDENT_CREATED_QUEUE,
            RabbitConfig.EMAIL_NOTIFICATION_QUEUE,
            RabbitConfig.AUDIT_LOG_QUEUE
        );
        
        info.put("queues", queues);
        info.put("exchanges", Arrays.asList(
            RabbitConfig.STUDENT_EXCHANGE,
            RabbitConfig.NOTIFICATION_EXCHANGE
        ));
        info.put("status", "connected");
        info.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(info);
    }
    
    /**
     * POST /api/queues/test-message - Send test message
     */
    @PostMapping("/test-message")
    public ResponseEntity<Map<String, String>> sendTestMessage(@RequestParam String queueName, 
                                                              @RequestParam String message) {
        try {
            rabbitTemplate.convertAndSend(queueName, message);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Test message sent successfully");
            response.put("queue", queueName);
            response.put("content", message);
            response.put("timestamp", String.valueOf(System.currentTimeMillis()));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to send message");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * POST /api/queues/purge/{queueName} - Purge queue
     */
    @PostMapping("/purge/{queueName}")
    public ResponseEntity<Map<String, String>> purgeQueue(@PathVariable String queueName) {
        try {
            rabbitAdmin.purgeQueue(queueName);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Queue purged successfully");
            response.put("queue", queueName);
            response.put("timestamp", String.valueOf(System.currentTimeMillis()));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to purge queue");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
```

---

## 📊 Benefits & Use Cases

### **Performance Benefits:**
- **Faster API Response**: Sync response + async processing
- **Scalability**: Multiple consumers can process messages
- **Reliability**: Message persistence and retry mechanisms
- **Decoupling**: Services communicate via messages

### **Use Cases:**
1. **Email Notifications**: Welcome emails, password resets
2. **Audit Logging**: Track all user actions
3. **Data Processing**: Heavy calculations in background
4. **Integration**: Communicate with external systems
5. **Event Sourcing**: Track all domain events

### **Real-world Examples:**
- **Student Registration**: Send welcome email + update statistics
- **Grade Updates**: Notify students + log changes
- **Bulk Operations**: Process large datasets asynchronously
- **Report Generation**: Generate reports in background

---

## 🔗 Related Files

### **Configuration:**
- `src/main/java/com/example/demo/config/RabbitConfig.java`
- `src/main/resources/application.properties`
- `docker-compose.yml` (updated with RabbitMQ)

### **Message Handling:**
- `src/main/java/com/example/demo/messaging/MessagePublisher.java`
- `src/main/java/com/example/demo/messaging/consumers/`
- `src/main/java/com/example/demo/messaging/dto/`

### **API Controllers:**
- `src/main/java/com/example/demo/controller/QueueController.java`
- Updated `StudentService.java` with message publishing

### **Documentation:**
- `docs/LESSON-9-RABBITMQ-SETUP.md` - RabbitMQ setup guide
- `docs/LESSON-9-POSTMAN-TESTING.md` - Message queue testing

---

## 🎓 Learning Outcomes

Sau Lesson 9, bạn sẽ nắm vững:

### **Message Queue Concepts:**
- Queue types and exchange patterns
- Message routing and binding
- Async processing benefits

### **RabbitMQ Integration:**
- Spring AMQP configuration
- Message publishing and consuming
- Queue management and monitoring

### **Event-Driven Architecture:**
- Domain event publishing
- Loose coupling between services
- Scalable system design

### **Production Considerations:**
- Message persistence and reliability
- Error handling and dead letter queues
- Performance monitoring and scaling

---



---

<div align="center">
  <b>📨 Lesson 9: Message Queues Complete! 📨</b>
  
  <br><br>
  
  **Scalable async processing with RabbitMQ!**
</div>
