# 🧪 LESSON 10: WebSocket Testing Guide

## 📋 Testing Overview

Comprehensive testing guide cho WebSocket functionality trong Student Management System. Chúng ta sẽ test:

- WebSocket connection và disconnection
- Real-time student notifications
- Chat functionality
- System monitoring
- Private messaging
- Error handling

---

## 🔧 Testing Tools

### **1. Browser-based Testing**
- Browser Developer Console
- WebSocket test clients
- Custom HTML test pages

### **2. API Testing Tools**
- Postman (REST endpoints)
- curl commands
- WebSocket testing extensions

### **3. Programming Libraries**
- SockJS client
- STOMP.js
- Native WebSocket API

---

## 🌐 WebSocket Connection Testing

### **1. Basic Connection Test**

```javascript
// Test 1: Basic SockJS Connection
const testBasicConnection = () => {
    console.log('🔌 Testing basic WebSocket connection...');
    
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function(frame) {
        console.log('✅ Connected successfully:', frame);
        
        // Test disconnect
        setTimeout(() => {
            stompClient.disconnect(() => {
                console.log('✅ Disconnected successfully');
            });
        }, 2000);
    }, function(error) {
        console.error('❌ Connection failed:', error);
    });
};

// Run test
testBasicConnection();
```

### **2. Native WebSocket Test**

```javascript
// Test 2: Native WebSocket Connection
const testNativeWebSocket = () => {
    console.log('🔌 Testing native WebSocket connection...');
    
    const ws = new WebSocket('ws://localhost:8080/websocket');
    
    ws.onopen = function(event) {
        console.log('✅ Native WebSocket connected');
        ws.close();
    };
    
    ws.onclose = function(event) {
        console.log('✅ Native WebSocket closed');
    };
    
    ws.onerror = function(error) {
        console.error('❌ Native WebSocket error:', error);
    };
};

// Run test
testNativeWebSocket();
```

---

## 📨 Message Subscription Testing

### **1. System Notifications Test**

```javascript
// Test 3: System Notifications Subscription
const testSystemNotifications = () => {
    console.log('📨 Testing system notifications...');
    
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function(frame) {
        console.log('Connected for system notifications test');
        
        // Subscribe to system notifications
        stompClient.subscribe('/topic/system', function(message) {
            const notification = JSON.parse(message.body);
            console.log('📢 System notification received:', notification);
            
            // Verify message structure
            if (notification.type && notification.content && notification.timestamp) {
                console.log('✅ System notification structure valid');
            } else {
                console.error('❌ Invalid system notification structure');
            }
        });
        
        // Test ping
        console.log('📤 Sending ping...');
        stompClient.send('/app/system/ping', {}, '{}');
        
        // Test system status request
        setTimeout(() => {
            console.log('📤 Requesting system status...');
            stompClient.send('/app/system/status', {}, '{}');
        }, 1000);
        
        // Cleanup after 5 seconds
        setTimeout(() => {
            stompClient.disconnect();
        }, 5000);
    });
};

// Run test
testSystemNotifications();
```

### **2. Student Notifications Test**

```javascript
// Test 4: Student Notifications Subscription
const testStudentNotifications = () => {
    console.log('👨‍🎓 Testing student notifications...');
    
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function(frame) {
        console.log('Connected for student notifications test');
        
        // Subscribe to student notifications
        stompClient.subscribe('/topic/students', function(message) {
            const notification = JSON.parse(message.body);
            console.log('👨‍🎓 Student notification received:', notification);
            
            // Verify notification structure
            if (notification.action && notification.student && notification.userId) {
                console.log('✅ Student notification structure valid');
                console.log(`   Action: ${notification.action}`);
                console.log(`   Student: ${notification.student.name}`);
                console.log(`   User: ${notification.userId}`);
            } else {
                console.error('❌ Invalid student notification structure');
            }
        });
        
        console.log('📝 Now create/update/delete a student via REST API to see notifications');
        
        // Keep connection open for manual testing
        setTimeout(() => {
            stompClient.disconnect();
        }, 30000); // 30 seconds
    });
};

// Run test
testStudentNotifications();
```

---

## 💬 Chat Functionality Testing

### **1. Chat Room Test**

```javascript
// Test 5: Chat Room Functionality
const testChatRoom = () => {
    console.log('💬 Testing chat room functionality...');
    
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);
    
    const roomId = 'test-room-' + Date.now();
    
    stompClient.connect({}, function(frame) {
        console.log('Connected for chat room test');
        
        // Subscribe to chat room
        stompClient.subscribe('/topic/chat/' + roomId, function(message) {
            const chatMessage = JSON.parse(message.body);
            console.log('💬 Chat message received:', chatMessage);
            
            if (chatMessage.type === 'USER_JOIN') {
                console.log('✅ User join message received');
            } else if (chatMessage.content && chatMessage.sender) {
                console.log('✅ Chat message structure valid');
                console.log(`   From: ${chatMessage.sender}`);
                console.log(`   Message: ${chatMessage.content}`);
            }
        });
        
        // Join room
        console.log('📤 Joining room:', roomId);
        stompClient.send('/app/join/' + roomId, {}, JSON.stringify({
            content: 'Joining room for test'
        }));
        
        // Send chat messages
        setTimeout(() => {
            console.log('📤 Sending chat message 1...');
            stompClient.send('/app/chat/' + roomId, {}, JSON.stringify({
                content: 'Hello from WebSocket test!',
                sender: 'TestUser'
            }));
        }, 1000);
        
        setTimeout(() => {
            console.log('📤 Sending chat message 2...');
            stompClient.send('/app/chat/' + roomId, {}, JSON.stringify({
                content: 'This is a test message',
                sender: 'TestUser'
            }));
        }, 2000);
        
        // Cleanup
        setTimeout(() => {
            stompClient.disconnect();
        }, 5000);
    });
};

// Run test
testChatRoom();
```

### **2. Private Message Test**

```javascript
// Test 6: Private Message Functionality
const testPrivateMessages = () => {
    console.log('🔒 Testing private message functionality...');
    
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function(frame) {
        console.log('Connected for private message test');
        
        // Subscribe to private messages
        stompClient.subscribe('/user/queue/private', function(message) {
            const privateMessage = JSON.parse(message.body);
            console.log('🔒 Private message received:', privateMessage);
            
            if (privateMessage.content && privateMessage.sender && privateMessage.recipient) {
                console.log('✅ Private message structure valid');
            } else {
                console.error('❌ Invalid private message structure');
            }
        });
        
        // Subscribe to private notifications
        stompClient.subscribe('/user/queue/notifications', function(message) {
            const notification = JSON.parse(message.body);
            console.log('🔔 Private notification received:', notification);
        });
        
        // Send private message (to self for testing)
        setTimeout(() => {
            console.log('📤 Sending private message...');
            stompClient.send('/app/private', {}, JSON.stringify({
                content: 'This is a private message test',
                sender: 'TestUser',
                recipient: 'TestUser'
            }));
        }, 1000);
        
        // Cleanup
        setTimeout(() => {
            stompClient.disconnect();
        }, 5000);
    });
};

// Run test
testPrivateMessages();
```

---

## 🔧 REST API Integration Testing

### **1. WebSocket Status API Test**

```bash
#!/bin/bash

echo "🧪 Testing WebSocket REST API endpoints..."

# Test 1: Get WebSocket status
echo "📊 Testing WebSocket status endpoint..."
curl -X GET http://localhost:8080/api/websocket/status \
  -H "Content-Type: application/json" | jq '.'

echo -e "\n"

# Test 2: Get active users
echo "👥 Testing active users endpoint..."
curl -X GET http://localhost:8080/api/websocket/users \
  -H "Content-Type: application/json" | jq '.'

echo -e "\n"

# Test 3: Send broadcast message
echo "📢 Testing broadcast message..."
curl -X POST http://localhost:8080/api/websocket/broadcast \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Test broadcast message from API",
    "category": "SYSTEM",
    "level": "INFO"
  }' | jq '.'

echo -e "\n"

# Test 4: Test cache notification
echo "⚡ Testing cache notification..."
curl -X POST http://localhost:8080/api/websocket/test/cache \
  -H "Content-Type: application/json" | jq '.'

echo -e "\n"

# Test 5: Test queue notification
echo "📨 Testing queue notification..."
curl -X POST http://localhost:8080/api/websocket/test/queue \
  -H "Content-Type: application/json" | jq '.'

echo -e "\n"

# Test 6: Test error notification
echo "❌ Testing error notification..."
curl -X POST http://localhost:8080/api/websocket/test/error \
  -H "Content-Type: application/json" | jq '.'

echo -e "\n✅ REST API tests completed!"
```

### **2. Student CRUD with WebSocket Notifications**

```bash
#!/bin/bash

echo "👨‍🎓 Testing Student CRUD with WebSocket notifications..."

# First, get JWT token (replace with actual login)
JWT_TOKEN="your-jwt-token-here"

# Test 1: Create student (should trigger WebSocket notification)
echo "➕ Creating student..."
STUDENT_ID=$(curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "name": "WebSocket Test Student",
    "email": "websocket@test.com",
    "age": 25
  }' | jq -r '.id')

echo "Created student with ID: $STUDENT_ID"
sleep 2

# Test 2: Update student (should trigger WebSocket notification)
echo "✏️ Updating student..."
curl -X PUT http://localhost:8080/api/students/$STUDENT_ID \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "name": "Updated WebSocket Student",
    "email": "updated@websocket.com",
    "age": 26
  }' | jq '.'

sleep 2

# Test 3: Delete student (should trigger WebSocket notification)
echo "🗑️ Deleting student..."
curl -X DELETE http://localhost:8080/api/students/$STUDENT_ID \
  -H "Authorization: Bearer $JWT_TOKEN"

echo -e "\n✅ Student CRUD tests completed!"
echo "Check WebSocket client for notifications!"
```

---

## 📊 Performance Testing

### **1. Connection Load Test**

```javascript
// Test 7: Multiple Connections Load Test
const testMultipleConnections = (connectionCount = 5) => {
    console.log(`🚀 Testing ${connectionCount} simultaneous connections...`);
    
    const connections = [];
    let connectedCount = 0;
    
    for (let i = 0; i < connectionCount; i++) {
        const socket = new SockJS('http://localhost:8080/ws');
        const stompClient = Stomp.over(socket);
        
        stompClient.connect({}, function(frame) {
            connectedCount++;
            console.log(`✅ Connection ${i + 1} established (${connectedCount}/${connectionCount})`);
            
            // Subscribe to system notifications
            stompClient.subscribe('/topic/system', function(message) {
                const notification = JSON.parse(message.body);
                console.log(`📨 Connection ${i + 1} received:`, notification.content);
            });
            
            // If all connections are established, send test messages
            if (connectedCount === connectionCount) {
                console.log('🎉 All connections established! Sending test messages...');
                
                // Send messages from each connection
                connections.forEach((client, index) => {
                    setTimeout(() => {
                        client.send('/app/message', {}, JSON.stringify({
                            content: `Test message from connection ${index + 1}`,
                            sender: `User${index + 1}`
                        }));
                    }, index * 100);
                });
                
                // Cleanup after 10 seconds
                setTimeout(() => {
                    console.log('🧹 Cleaning up connections...');
                    connections.forEach(client => client.disconnect());
                }, 10000);
            }
        }, function(error) {
            console.error(`❌ Connection ${i + 1} failed:`, error);
        });
        
        connections.push(stompClient);
    }
};

// Run test with 5 connections
testMultipleConnections(5);
```

### **2. Message Rate Test**

```javascript
// Test 8: Message Rate Testing
const testMessageRate = () => {
    console.log('⚡ Testing message rate...');
    
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function(frame) {
        console.log('Connected for message rate test');
        
        let messageCount = 0;
        const startTime = Date.now();
        
        // Subscribe to messages
        stompClient.subscribe('/topic/messages', function(message) {
            messageCount++;
            const msg = JSON.parse(message.body);
            
            if (messageCount % 10 === 0) {
                const elapsed = Date.now() - startTime;
                const rate = (messageCount / elapsed) * 1000;
                console.log(`📊 Received ${messageCount} messages, Rate: ${rate.toFixed(2)} msg/sec`);
            }
        });
        
        // Send 100 messages rapidly
        console.log('📤 Sending 100 messages rapidly...');
        for (let i = 0; i < 100; i++) {
            setTimeout(() => {
                stompClient.send('/app/message', {}, JSON.stringify({
                    content: `Rate test message ${i + 1}`,
                    sender: 'RateTestUser'
                }));
            }, i * 10); // 10ms interval
        }
        
        // Final report after 5 seconds
        setTimeout(() => {
            const elapsed = Date.now() - startTime;
            const rate = (messageCount / elapsed) * 1000;
            console.log(`📊 Final: ${messageCount} messages in ${elapsed}ms, Rate: ${rate.toFixed(2)} msg/sec`);
            stompClient.disconnect();
        }, 5000);
    });
};

// Run test
testMessageRate();
```

---

## 🔍 Error Handling Testing

### **1. Connection Error Test**

```javascript
// Test 9: Connection Error Handling
const testConnectionErrors = () => {
    console.log('❌ Testing connection error handling...');
    
    // Test 1: Invalid endpoint
    console.log('Testing invalid endpoint...');
    const invalidSocket = new SockJS('http://localhost:8080/invalid-ws');
    const invalidClient = Stomp.over(invalidSocket);
    
    invalidClient.connect({}, function(frame) {
        console.log('❌ Should not connect to invalid endpoint');
    }, function(error) {
        console.log('✅ Correctly failed to connect to invalid endpoint:', error);
    });
    
    // Test 2: Server not running (comment out if server is running)
    /*
    console.log('Testing server not running...');
    const noServerSocket = new SockJS('http://localhost:9999/ws');
    const noServerClient = Stomp.over(noServerSocket);
    
    noServerClient.connect({}, function(frame) {
        console.log('❌ Should not connect when server is not running');
    }, function(error) {
        console.log('✅ Correctly failed to connect when server not running:', error);
    });
    */
    
    // Test 3: Network interruption simulation
    setTimeout(() => {
        console.log('Testing network interruption...');
        const socket = new SockJS('http://localhost:8080/ws');
        const stompClient = Stomp.over(socket);
        
        stompClient.connect({}, function(frame) {
            console.log('Connected for network interruption test');
            
            // Simulate network interruption by closing socket
            setTimeout(() => {
                console.log('Simulating network interruption...');
                socket.close();
            }, 2000);
        }, function(error) {
            console.log('✅ Connection error handled:', error);
        });
    }, 1000);
};

// Run test
testConnectionErrors();
```

### **2. Invalid Message Test**

```javascript
// Test 10: Invalid Message Handling
const testInvalidMessages = () => {
    console.log('📝 Testing invalid message handling...');
    
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function(frame) {
        console.log('Connected for invalid message test');
        
        // Test 1: Send to invalid destination
        try {
            stompClient.send('/app/invalid-destination', {}, '{}');
            console.log('📤 Sent message to invalid destination');
        } catch (error) {
            console.log('✅ Caught error for invalid destination:', error);
        }
        
        // Test 2: Send invalid JSON
        try {
            stompClient.send('/app/message', {}, 'invalid-json');
            console.log('📤 Sent invalid JSON');
        } catch (error) {
            console.log('✅ Caught error for invalid JSON:', error);
        }
        
        // Test 3: Send empty message
        try {
            stompClient.send('/app/message', {}, '');
            console.log('📤 Sent empty message');
        } catch (error) {
            console.log('✅ Caught error for empty message:', error);
        }
        
        // Cleanup
        setTimeout(() => {
            stompClient.disconnect();
        }, 3000);
    });
};

// Run test
testInvalidMessages();
```

---

## 📋 Complete Test Suite

### **HTML Test Page**

```html
<!DOCTYPE html>
<html>
<head>
    <title>WebSocket Test Suite</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@stomp/stompjs@7/bundles/stomp.umd.min.js"></script>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .test-section { margin: 20px 0; padding: 15px; border: 1px solid #ddd; }
        .log { height: 300px; overflow-y: auto; background: #f5f5f5; padding: 10px; font-family: monospace; }
        button { margin: 5px; padding: 10px; }
        .success { color: green; }
        .error { color: red; }
        .info { color: blue; }
    </style>
</head>
<body>
    <h1>🧪 WebSocket Test Suite</h1>
    
    <div class="test-section">
        <h3>Connection Tests</h3>
        <button onclick="testBasicConnection()">Basic Connection</button>
        <button onclick="testMultipleConnections()">Multiple Connections</button>
        <button onclick="testConnectionErrors()">Error Handling</button>
    </div>
    
    <div class="test-section">
        <h3>Message Tests</h3>
        <button onclick="testSystemNotifications()">System Notifications</button>
        <button onclick="testStudentNotifications()">Student Notifications</button>
        <button onclick="testChatRoom()">Chat Room</button>
        <button onclick="testPrivateMessages()">Private Messages</button>
    </div>
    
    <div class="test-section">
        <h3>Performance Tests</h3>
        <button onclick="testMessageRate()">Message Rate</button>
        <button onclick="testLoadTest()">Load Test</button>
    </div>
    
    <div class="test-section">
        <h3>Manual Tests</h3>
        <input type="text" id="messageInput" placeholder="Type message..." style="width: 300px;">
        <button onclick="sendManualMessage()">Send Message</button>
        <button onclick="pingSystem()">Ping System</button>
        <button onclick="getSystemStatus()">Get Status</button>
    </div>
    
    <div class="test-section">
        <h3>Test Log</h3>
        <button onclick="clearLog()">Clear Log</button>
        <div id="testLog" class="log"></div>
    </div>

    <script>
        let stompClient = null;
        let connected = false;

        function log(message, type = 'info') {
            const logDiv = document.getElementById('testLog');
            const timestamp = new Date().toLocaleTimeString();
            const className = type === 'error' ? 'error' : type === 'success' ? 'success' : 'info';
            logDiv.innerHTML += `<div class="${className}">[${timestamp}] ${message}</div>`;
            logDiv.scrollTop = logDiv.scrollHeight;
        }

        function clearLog() {
            document.getElementById('testLog').innerHTML = '';
        }

        function connectWebSocket() {
            if (connected) return Promise.resolve();
            
            return new Promise((resolve, reject) => {
                const socket = new SockJS('http://localhost:8080/ws');
                stompClient = Stomp.over(socket);
                
                stompClient.connect({}, function(frame) {
                    connected = true;
                    log('✅ Connected to WebSocket', 'success');
                    
                    // Subscribe to all topics
                    stompClient.subscribe('/topic/system', function(message) {
                        const notification = JSON.parse(message.body);
                        log(`📢 System: ${notification.content}`, 'info');
                    });
                    
                    stompClient.subscribe('/topic/students', function(message) {
                        const notification = JSON.parse(message.body);
                        log(`👨‍🎓 Student ${notification.action}: ${notification.student?.name}`, 'info');
                    });
                    
                    stompClient.subscribe('/topic/messages', function(message) {
                        const msg = JSON.parse(message.body);
                        log(`💬 Message from ${msg.sender}: ${msg.content}`, 'info');
                    });
                    
                    resolve();
                }, function(error) {
                    log('❌ Connection failed: ' + error, 'error');
                    reject(error);
                });
            });
        }

        function testBasicConnection() {
            log('🔌 Testing basic connection...');
            connectWebSocket().then(() => {
                log('✅ Basic connection test passed', 'success');
            }).catch(error => {
                log('❌ Basic connection test failed: ' + error, 'error');
            });
        }

        function testSystemNotifications() {
            log('📨 Testing system notifications...');
            connectWebSocket().then(() => {
                stompClient.send('/app/system/ping', {}, '{}');
                log('📤 Sent ping message');
            });
        }

        function testStudentNotifications() {
            log('👨‍🎓 Testing student notifications...');
            log('ℹ️ Create/update/delete a student via REST API to see notifications');
        }

        function testChatRoom() {
            log('💬 Testing chat room...');
            connectWebSocket().then(() => {
                const roomId = 'test-room-' + Date.now();
                
                stompClient.subscribe('/topic/chat/' + roomId, function(message) {
                    const chatMessage = JSON.parse(message.body);
                    log(`💬 Chat [${roomId}] ${chatMessage.sender}: ${chatMessage.content}`, 'info');
                });
                
                stompClient.send('/app/join/' + roomId, {}, JSON.stringify({
                    content: 'Joining test room'
                }));
                
                setTimeout(() => {
                    stompClient.send('/app/chat/' + roomId, {}, JSON.stringify({
                        content: 'Hello from test suite!',
                        sender: 'TestUser'
                    }));
                }, 1000);
            });
        }

        function testPrivateMessages() {
            log('🔒 Testing private messages...');
            connectWebSocket().then(() => {
                stompClient.subscribe('/user/queue/private', function(message) {
                    const privateMessage = JSON.parse(message.body);
                    log(`🔒 Private from ${privateMessage.sender}: ${privateMessage.content}`, 'info');
                });
                
                stompClient.send('/app/private', {}, JSON.stringify({
                    content: 'Test private message',
                    sender: 'TestUser',
                    recipient: 'TestUser'
                }));
            });
        }

        function testMessageRate() {
            log('⚡ Testing message rate...');
            connectWebSocket().then(() => {
                const startTime = Date.now();
                let receivedCount = 0;
                
                const subscription = stompClient.subscribe('/topic/messages', function(message) {
                    receivedCount++;
                    if (receivedCount === 50) {
                        const elapsed = Date.now() - startTime;
                        const rate = (receivedCount / elapsed) * 1000;
                        log(`📊 Received ${receivedCount} messages in ${elapsed}ms, Rate: ${rate.toFixed(2)} msg/sec`, 'success');
                        subscription.unsubscribe();
                    }
                });
                
                // Send 50 messages
                for (let i = 0; i < 50; i++) {
                    setTimeout(() => {
                        stompClient.send('/app/message', {}, JSON.stringify({
                            content: `Rate test message ${i + 1}`,
                            sender: 'RateTestUser'
                        }));
                    }, i * 20);
                }
            });
        }

        function testMultipleConnections() {
            log('🚀 Testing multiple connections...');
            // This would require more complex implementation
            log('ℹ️ Multiple connection test requires separate implementation');
        }

        function testConnectionErrors() {
            log('❌ Testing connection errors...');
            const invalidSocket = new SockJS('http://localhost:8080/invalid-ws');
            const invalidClient = Stomp.over(invalidSocket);
            
            invalidClient.connect({}, function(frame) {
                log('❌ Should not connect to invalid endpoint', 'error');
            }, function(error) {
                log('✅ Correctly failed to connect to invalid endpoint', 'success');
            });
        }

        function testLoadTest() {
            log('🏋️ Load test not implemented in browser version');
        }

        function sendManualMessage() {
            const input = document.getElementById('messageInput');
            const message = input.value.trim();
            
            if (!message) {
                log('❌ Please enter a message', 'error');
                return;
            }
            
            connectWebSocket().then(() => {
                stompClient.send('/app/message', {}, JSON.stringify({
                    content: message,
                    sender: 'ManualUser'
                }));
                log(`📤 Sent manual message: ${message}`);
                input.value = '';
            });
        }

        function pingSystem() {
            connectWebSocket().then(() => {
                stompClient.send('/app/system/ping', {}, '{}');
                log('📤 Sent ping to system');
            });
        }

        function getSystemStatus() {
            connectWebSocket().then(() => {
                stompClient.send('/app/system/status', {}, '{}');
                log('📤 Requested system status');
            });
        }

        // Auto-connect on page load
        window.onload = function() {
            log('🌐 WebSocket Test Suite loaded');
            log('ℹ️ Click "Basic Connection" to start testing');
        };
    </script>
</body>
</html>
```

---

## ✅ Test Checklist

### **Connection Tests:**
- [ ] Basic SockJS connection works
- [ ] Native WebSocket connection works
- [ ] Connection error handling works
- [ ] Disconnection handling works
- [ ] Reconnection works after network interruption

### **Message Tests:**
- [ ] System notifications received
- [ ] Student notifications received
- [ ] Chat room messages work
- [ ] Private messages work
- [ ] Message structure validation passes

### **Performance Tests:**
- [ ] Multiple simultaneous connections work
- [ ] Message rate is acceptable
- [ ] No memory leaks with long connections
- [ ] Server handles connection load

### **Integration Tests:**
- [ ] Student CRUD triggers WebSocket notifications
- [ ] Cache operations trigger notifications
- [ ] Queue operations trigger notifications
- [ ] REST API endpoints work correctly

### **Error Handling Tests:**
- [ ] Invalid destinations handled gracefully
- [ ] Invalid JSON handled gracefully
- [ ] Network interruptions handled
- [ ] Server restart recovery works

---

<div align="center">
  <b>🎉 WebSocket Testing Complete! 🎉</b>

</div>
