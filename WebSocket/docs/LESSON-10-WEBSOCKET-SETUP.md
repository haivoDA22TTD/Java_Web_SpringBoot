# 🔌 LESSON 10: WebSocket Setup & Configuration

## 📋 Prerequisites

Trước khi bắt đầu với WebSocket, đảm bảo bạn đã:

- ✅ Completed Lessons 1-9
- ✅ MongoDB running on localhost:27017
- ✅ Redis running on localhost:6379
- ✅ RabbitMQ running on localhost:5672
- ✅ Spring Boot application running on localhost:8080

---

## 🛠️ WebSocket Dependencies

WebSocket dependency đã được thêm vào `pom.xml`:

```xml
<!-- Lesson 10: WebSocket & Real-time Communication -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

---

## ⚙️ Configuration Files

### **1. WebSocket Configuration**

File: `src/main/java/com/example/demo/config/WebSocketConfig.java`

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple broker cho /topic và /queue destinations
        config.enableSimpleBroker("/topic", "/queue");
        
        // Set application destination prefix
        config.setApplicationDestinationPrefixes("/app");
        
        // Set user destination prefix cho private messages
        config.setUserDestinationPrefix("/user");
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Đăng ký endpoint /ws với SockJS fallback
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        
        // Đăng ký endpoint /websocket cho native WebSocket
        registry.addEndpoint("/websocket")
                .setAllowedOriginPatterns("*");
    }
}
```

### **2. Application Properties**

Không cần thêm configuration mới cho WebSocket trong `application.properties`. Existing configuration đã đủ.

---

## 🚀 Starting the Application

### **1. Start Required Services**

```bash
# Start MongoDB (if not running)
mongod

# Start Redis (if not running)
redis-server

# Start RabbitMQ (if not running)
rabbitmq-server
```

### **2. Start Spring Boot Application**

```bash
# Using Maven
mvn spring-boot:run

# Or using Java
java -jar target/spring-boot-demo-0.0.1-SNAPSHOT.jar
```

### **3. Verify WebSocket Endpoints**

Application sẽ start với messages:

```
🚀 Student Management System Started!
📊 MongoDB: localhost:27017
⚡ Redis: localhost:6379
🐰 RabbitMQ: localhost:5672
🌐 API: http://localhost:8080
🔍 Redis Insight: http://localhost:8001
🐰 RabbitMQ Management: http://localhost:15672
🔌 WebSocket: ws://localhost:8080/ws
💬 Real-time Features: ENABLED
```

---

## 🧪 Testing WebSocket Connection

### **1. Using Browser Console**

Mở browser console và test WebSocket connection:

```javascript
// Test SockJS connection
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('✅ Connected: ' + frame);
    
    // Subscribe to system notifications
    stompClient.subscribe('/topic/system', function(message) {
        console.log('📨 System message:', JSON.parse(message.body));
    });
    
    // Subscribe to student notifications
    stompClient.subscribe('/topic/students', function(message) {
        console.log('👨‍🎓 Student notification:', JSON.parse(message.body));
    });
    
    // Test ping
    stompClient.send('/app/system/ping', {}, '{}');
});
```

### **2. Using REST API**

Test WebSocket functionality qua REST endpoints:

```bash
# Check WebSocket status
curl -X GET http://localhost:8080/api/websocket/status

# Expected response:
{
  "activeUsers": 0,
  "sessionUsers": 0,
  "timestamp": 1234567890123,
  "status": "ACTIVE"
}

# Send broadcast message
curl -X POST http://localhost:8080/api/websocket/broadcast \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Hello WebSocket World!",
    "category": "SYSTEM",
    "level": "INFO"
  }'

# Test cache notification
curl -X POST http://localhost:8080/api/websocket/test/cache

# Test queue notification
curl -X POST http://localhost:8080/api/websocket/test/queue
```

---

## 🌐 WebSocket Endpoints

### **Connection Endpoints:**
- **SockJS:** `ws://localhost:8080/ws`
- **Native WebSocket:** `ws://localhost:8080/websocket`

### **Subscription Destinations:**
- `/topic/students` - Student event notifications
- `/topic/system` - System notifications
- `/topic/chat/{roomId}` - Chat messages
- `/topic/messages` - General messages
- `/user/queue/notifications` - Private notifications
- `/user/queue/private` - Private messages

### **Send Destinations:**
- `/app/chat/{roomId}` - Send chat message
- `/app/private` - Send private message
- `/app/message` - Send general message
- `/app/system/status` - Get system status
- `/app/system/ping` - Ping system

---

## 📱 Client Examples

### **1. HTML + JavaScript Client**

```html
<!DOCTYPE html>
<html>
<head>
    <title>WebSocket Test</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@stomp/stompjs@7/bundles/stomp.umd.min.js"></script>
</head>
<body>
    <div id="messages"></div>
    <input type="text" id="messageInput" placeholder="Type message...">
    <button onclick="sendMessage()">Send</button>

    <script>
        const socket = new SockJS('http://localhost:8080/ws');
        const stompClient = Stomp.over(socket);
        
        stompClient.connect({}, function(frame) {
            console.log('Connected: ' + frame);
            
            // Subscribe to all notifications
            stompClient.subscribe('/topic/system', function(message) {
                displayMessage('SYSTEM', JSON.parse(message.body));
            });
            
            stompClient.subscribe('/topic/students', function(message) {
                displayMessage('STUDENT', JSON.parse(message.body));
            });
        });
        
        function sendMessage() {
            const input = document.getElementById('messageInput');
            const message = input.value;
            
            stompClient.send('/app/message', {}, JSON.stringify({
                content: message,
                type: 'USER_MESSAGE'
            }));
            
            input.value = '';
        }
        
        function displayMessage(type, message) {
            const messagesDiv = document.getElementById('messages');
            messagesDiv.innerHTML += `<div><strong>${type}:</strong> ${message.content}</div>`;
        }
    </script>
</body>
</html>
```

### **2. React Component Example**

```jsx
import React, { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

const WebSocketComponent = () => {
    const [stompClient, setStompClient] = useState(null);
    const [connected, setConnected] = useState(false);
    const [messages, setMessages] = useState([]);
    const [inputMessage, setInputMessage] = useState('');

    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws');
        const client = Stomp.over(socket);

        client.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            setConnected(true);
            setStompClient(client);

            // Subscribe to notifications
            client.subscribe('/topic/system', (message) => {
                const notification = JSON.parse(message.body);
                setMessages(prev => [...prev, { type: 'SYSTEM', ...notification }]);
            });

            client.subscribe('/topic/students', (message) => {
                const notification = JSON.parse(message.body);
                setMessages(prev => [...prev, { type: 'STUDENT', ...notification }]);
            });
        });

        return () => {
            if (client) {
                client.disconnect();
            }
        };
    }, []);

    const sendMessage = () => {
        if (stompClient && inputMessage) {
            stompClient.send('/app/message', {}, JSON.stringify({
                content: inputMessage,
                type: 'USER_MESSAGE'
            }));
            setInputMessage('');
        }
    };

    return (
        <div>
            <h3>WebSocket Status: {connected ? '🟢 Connected' : '🔴 Disconnected'}</h3>
            
            <div style={{ height: '300px', overflow: 'auto', border: '1px solid #ccc', padding: '10px' }}>
                {messages.map((msg, index) => (
                    <div key={index}>
                        <strong>{msg.type}:</strong> {msg.content} 
                        <small>({new Date(msg.timestamp).toLocaleTimeString()})</small>
                    </div>
                ))}
            </div>
            
            <div style={{ marginTop: '10px' }}>
                <input
                    type="text"
                    value={inputMessage}
                    onChange={(e) => setInputMessage(e.target.value)}
                    placeholder="Type message..."
                    onKeyPress={(e) => e.key === 'Enter' && sendMessage()}
                />
                <button onClick={sendMessage} disabled={!connected}>
                    Send
                </button>
            </div>
        </div>
    );
};

export default WebSocketComponent;
```

---

## 🔧 Troubleshooting

### **1. Connection Issues**

```bash
# Check if application is running
curl -X GET http://localhost:8080/api/health

# Check WebSocket status
curl -X GET http://localhost:8080/api/websocket/status

# Check if ports are open
netstat -an | grep 8080
```

### **2. CORS Issues**

Nếu gặp CORS errors, kiểm tra WebSocket configuration:

```java
@Override
public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*") // Allow all origins for development
            .withSockJS();
}
```

### **3. Authentication Issues**

Nếu cần authentication cho WebSocket:

```javascript
const headers = {
    'Authorization': 'Bearer ' + jwtToken
};

stompClient.connect(headers, function(frame) {
    // Connected with authentication
});
```

---

## 📊 Monitoring WebSocket

### **1. Active Connections**

```bash
# Get active users count
curl -X GET http://localhost:8080/api/websocket/users

# Response:
{
  "count": 2,
  "sessions": {
    "session1": "user1",
    "session2": "user2"
  },
  "timestamp": 1234567890123
}
```

### **2. System Status**

```bash
# Get overall system status
curl -X GET http://localhost:8080/api/websocket/status

# Response:
{
  "activeUsers": 2,
  "sessionUsers": 2,
  "timestamp": 1234567890123,
  "status": "ACTIVE"
}
```

---

## 🎯 Testing Real-time Features

### **1. Test Student Notifications**

```bash
# Create a student (should trigger WebSocket notification)
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "WebSocket Test Student",
    "email": "websocket@test.com",
    "age": 25
  }'

# Update student (should trigger WebSocket notification)
curl -X PUT http://localhost:8080/api/students/STUDENT_ID \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Updated WebSocket Student",
    "email": "updated@test.com",
    "age": 26
  }'
```

### **2. Test Cache Notifications**

```bash
# Clear cache (should trigger WebSocket notification)
curl -X POST http://localhost:8080/api/cache/clear \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Clear specific cache
curl -X POST http://localhost:8080/api/cache/clear/students \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## ✅ Verification Checklist

- [ ] WebSocket server starts successfully
- [ ] Can connect to `/ws` endpoint
- [ ] Can subscribe to `/topic/system`
- [ ] Can subscribe to `/topic/students`
- [ ] Receives system notifications
- [ ] Receives student event notifications
- [ ] Can send messages to `/app/message`
- [ ] REST API endpoints work
- [ ] Active user count updates
- [ ] Connection/disconnection events work

---

<div align="center">
  <b>🎉 WebSocket Setup Complete! 🎉</b>
  
  <br><br>
  
  **Ready for Real-time Communication!**
</div>