# 🌐 LESSON 10: WebSocket & Real-time Communication

## 📖 Tổng quan

Lesson 10 tích hợp **WebSocket** vào Spring Boot application để tạo **real-time communication**. Chúng ta sẽ học cách:

- Cấu hình WebSocket với STOMP protocol
- Tạo real-time notifications cho student events
- Implement chat functionality
- Monitor system status real-time
- Tích hợp WebSocket với existing message queue system

---

## 🎯 Mục tiêu học tập

Sau lesson này, bạn sẽ nắm vững:

### **WebSocket Fundamentals:**
- WebSocket vs HTTP differences
- STOMP protocol và message brokers
- Client-server real-time communication
- Connection lifecycle management

### **Spring WebSocket Integration:**
- `@EnableWebSocketMessageBroker` configuration
- Message mapping và routing
- Broadcast vs private messaging
- Session management và user tracking

### **Real-time Features:**
- Student event notifications
- System monitoring dashboard
- Chat functionality
- Cache và queue status updates

### **Production Considerations:**
- Connection scaling và load balancing
- Error handling và reconnection
- Security với WebSocket authentication
- Performance monitoring

---

## 🏗️ Kiến trúc WebSocket

```
┌─────────────────┐    WebSocket     ┌─────────────────┐
│   Client Apps   │ ←──────────────→ │  Spring Boot    │
│                 │    STOMP/SockJS  │   Application   │
│ • Web Browser   │                  │                 │
│ • Mobile App    │                  │ • Message       │
│ • Desktop App   │                  │   Broker        │
└─────────────────┘                  │ • Controllers   │
                                     │ • Services      │
                                     └─────────────────┘
                                             │
                                             ▼
                                     ┌─────────────────┐
                                     │   Integration   │
                                     │                 │
                                     │ • MongoDB       │
                                     │ • Redis Cache   │
                                     │ • RabbitMQ      │
                                     └─────────────────┘
```

---

## 🔧 Cấu hình WebSocket

### **1. WebSocket Configuration**

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple broker
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/websocket").setAllowedOriginPatterns("*");
    }
}
```

### **2. Message DTOs**

```java
// Base WebSocket Message
public class WebSocketMessage {
    private String type;
    private String content;
    private String sender;
    private LocalDateTime timestamp;
}

// Student Notification
public class StudentNotification extends WebSocketMessage {
    private String action; // CREATE, UPDATE, DELETE
    private StudentResponse student;
    private String userId;
}

// System Notification
public class SystemNotification extends WebSocketMessage {
    private String category; // CACHE, QUEUE, SYSTEM, ERROR
    private String level;    // INFO, WARNING, ERROR
    private Object data;
}
```

---

## 🎮 WebSocket Controllers

### **1. Chat Controller**

```java
@Controller
public class WebSocketController {
    
    // Chat trong room
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage sendChatMessage(@DestinationVariable String roomId,
                                     @Payload ChatMessage message) {
        return message;
    }
    
    // Private message
    @MessageMapping("/private")
    public void sendPrivateMessage(@Payload ChatMessage message) {
        messagingTemplate.convertAndSendToUser(
            message.getRecipient(), "/queue/private", message);
    }
}
```

### **2. System Monitoring Controller**

```java
@Controller
public class SystemWebSocketController {
    
    @MessageMapping("/system/status")
    @SendTo("/topic/system")
    public SystemNotification getSystemStatus() {
        // Return system status
    }
    
    @MessageMapping("/system/ping")
    @SendTo("/topic/system")
    public SystemNotification pingSystem() {
        return new SystemNotification("SYSTEM", "INFO", "Pong!");
    }
}
```

---

## 🔔 Real-time Notifications

### **1. Student Event Notifications**

```java
@Service
public class WebSocketNotificationService {
    
    public void sendStudentNotification(String action, StudentResponse student, String userId) {
        StudentNotification notification = new StudentNotification(action, student, userId);
        messagingTemplate.convertAndSend("/topic/students", notification);
    }
    
    public void sendSystemNotification(SystemNotification notification) {
        messagingTemplate.convertAndSend("/topic/system", notification);
    }
}
```

### **2. Integration với Student Service**

```java
@Service
public class StudentService {
    
    @Autowired
    private WebSocketNotificationService webSocketNotificationService;
    
    public StudentResponse createStudent(StudentRequest request) {
        // ... create student logic
        
        // Send WebSocket notification
        webSocketNotificationService.sendStudentNotification("CREATE", response, currentUser);
        
        return response;
    }
}
```

---

## 📊 WebSocket Destinations

### **Broadcast Destinations (1-to-many):**
- `/topic/students` - Student events notifications
- `/topic/system` - System notifications
- `/topic/chat/{roomId}` - Chat messages trong room
- `/topic/messages` - General messages

### **Private Destinations (1-to-1):**
- `/user/queue/notifications` - Private notifications
- `/user/queue/private` - Private messages

### **Application Destinations (Client → Server):**
- `/app/chat/{roomId}` - Send chat message
- `/app/private` - Send private message
- `/app/message` - Send general message
- `/app/system/status` - Request system status
- `/app/system/ping` - Ping system

---

## 🔌 Connection Management

### **1. Event Listener**

```java
@Component
public class WebSocketEventListener {
    
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        // User connected
        int currentUsers = activeUsers.incrementAndGet();
        notificationService.sendUserCountUpdate(currentUsers);
    }
    
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // User disconnected
        int currentUsers = activeUsers.decrementAndGet();
        notificationService.sendUserCountUpdate(currentUsers);
    }
}
```

### **2. Session Tracking**

```java
// Track active users
private final AtomicInteger activeUsers = new AtomicInteger(0);
private final ConcurrentHashMap<String, String> sessionUsers = new ConcurrentHashMap<>();

public int getActiveUsersCount() {
    return activeUsers.get();
}
```

---

## 🌐 Client Integration Examples

### **1. JavaScript Client (SockJS + STOMP)**

```javascript
// Connect to WebSocket
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    
    // Subscribe to student notifications
    stompClient.subscribe('/topic/students', function(message) {
        const notification = JSON.parse(message.body);
        showStudentNotification(notification);
    });
    
    // Subscribe to system notifications
    stompClient.subscribe('/topic/system', function(message) {
        const notification = JSON.parse(message.body);
        showSystemNotification(notification);
    });
});

// Send message
function sendChatMessage(roomId, message) {
    stompClient.send('/app/chat/' + roomId, {}, JSON.stringify({
        content: message,
        sender: currentUser
    }));
}
```

### **2. React Hook Example**

```javascript
import { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

export const useWebSocket = () => {
    const [stompClient, setStompClient] = useState(null);
    const [connected, setConnected] = useState(false);
    const [notifications, setNotifications] = useState([]);
    
    useEffect(() => {
        const socket = new SockJS('/ws');
        const client = Stomp.over(socket);
        
        client.connect({}, () => {
            setConnected(true);
            setStompClient(client);
            
            // Subscribe to notifications
            client.subscribe('/topic/students', (message) => {
                const notification = JSON.parse(message.body);
                setNotifications(prev => [...prev, notification]);
            });
        });
        
        return () => {
            if (client) {
                client.disconnect();
            }
        };
    }, []);
    
    return { stompClient, connected, notifications };
};
```

---

## 🧪 Testing WebSocket

### **1. REST API Testing**

```bash
# Get WebSocket status
curl -X GET http://localhost:8080/api/websocket/status

# Send broadcast message
curl -X POST http://localhost:8080/api/websocket/broadcast \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello everyone!", "category": "SYSTEM", "level": "INFO"}'

# Send private notification
curl -X POST http://localhost:8080/api/websocket/notify/user123 \
  -H "Content-Type: application/json" \
  -d '{"message": "Private message for you!"}'

# Test cache notification
curl -X POST http://localhost:8080/api/websocket/test/cache

# Test queue notification
curl -X POST http://localhost:8080/api/websocket/test/queue

# Get active users
curl -X GET http://localhost:8080/api/websocket/users
```

### **2. WebSocket Client Testing**

```javascript
// Test connection
const testConnection = () => {
    const socket = new SockJS('/ws');
    const client = Stomp.over(socket);
    
    client.connect({}, (frame) => {
        console.log('✅ Connected:', frame);
        
        // Test ping
        client.send('/app/system/ping', {}, '{}');
        
        // Test system status
        client.send('/app/system/status', {}, '{}');
    });
};
```

---

## 📈 Performance & Monitoring

### **1. Connection Metrics**

```java
@RestController
@RequestMapping("/api/websocket")
public class WebSocketController {
    
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getWebSocketStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("activeUsers", eventListener.getActiveUsersCount());
        status.put("sessionUsers", eventListener.getSessionUsers().size());
        status.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(status);
    }
}
```

### **2. Message Rate Limiting**

```java
// Rate limiting cho WebSocket messages
@Component
public class WebSocketRateLimiter {
    
    private final Map<String, RateLimiter> userLimiters = new ConcurrentHashMap<>();
    
    public boolean isAllowed(String userId) {
        RateLimiter limiter = userLimiters.computeIfAbsent(userId, 
            k -> RateLimiter.create(10.0)); // 10 messages per second
        return limiter.tryAcquire();
    }
}
```

---

## 🔒 Security Considerations

### **1. Authentication Integration**

```java
@Configuration
public class WebSocketSecurityConfig {
    
    @Bean
    public AuthChannelInterceptor authChannelInterceptor() {
        return new AuthChannelInterceptor();
    }
}

public class AuthChannelInterceptor implements ChannelInterceptor {
    
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Validate JWT token
            String token = accessor.getFirstNativeHeader("Authorization");
            if (!isValidToken(token)) {
                throw new IllegalArgumentException("Invalid token");
            }
        }
        
        return message;
    }
}
```

### **2. Message Validation**

```java
@MessageMapping("/chat/{roomId}")
public ChatMessage sendChatMessage(@DestinationVariable String roomId,
                                 @Payload @Valid ChatMessage message,
                                 Principal principal) {
    // Validate user permissions
    if (!hasAccessToRoom(principal.getName(), roomId)) {
        throw new AccessDeniedException("No access to room");
    }
    
    // Sanitize message content
    message.setContent(sanitizeContent(message.getContent()));
    
    return message;
}
```

---

## 🚀 Production Deployment

### **1. Load Balancing**

```yaml
# nginx.conf
upstream websocket_backend {
    ip_hash; # Sticky sessions for WebSocket
    server app1:8080;
    server app2:8080;
}

server {
    location /ws {
        proxy_pass http://websocket_backend;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
    }
}
```

### **2. Redis Message Broker**

```java
@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Use Redis for clustering
        config.enableStompBrokerRelay("/topic", "/queue")
              .setRelayHost("redis-server")
              .setRelayPort(6379);
    }
}
```

---

## 📝 Best Practices

### **1. Message Design**
- Keep messages small và focused
- Use proper message types và categories
- Include timestamps cho debugging
- Implement message acknowledgment

### **2. Connection Management**
- Handle reconnection gracefully
- Implement heartbeat/ping mechanism
- Clean up resources on disconnect
- Monitor connection health

### **3. Error Handling**
- Validate all incoming messages
- Handle network interruptions
- Implement retry logic
- Log errors properly

### **4. Performance**
- Use message batching khi possible
- Implement rate limiting
- Monitor memory usage
- Optimize message serialization

---

## 🎓 Tổng kết

Lesson 10 đã successfully implement:

✅ **WebSocket Configuration** với STOMP protocol  
✅ **Real-time Student Notifications** cho CRUD operations  
✅ **Chat Functionality** với rooms và private messages  
✅ **System Monitoring** với live status updates  
✅ **Integration** với existing Redis và RabbitMQ  
✅ **Connection Management** với user tracking  
✅ **REST API** cho WebSocket management  
✅ **Error Handling** và security considerations  

**Next Steps:** Testing, Monitoring, và Production Deployment!

---

<div align="center">
  <b>🎉 Lesson 10 Complete! 🎉</b>
  
  <br><br>
  
  **Real-time Spring Boot Application với WebSocket!**
</div>