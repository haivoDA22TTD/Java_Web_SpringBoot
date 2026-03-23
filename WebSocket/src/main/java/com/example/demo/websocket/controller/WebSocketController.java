package com.example.demo.websocket.controller;

import com.example.demo.websocket.dto.ChatMessage;
import com.example.demo.websocket.dto.WebSocketMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * LESSON 10: WebSocket Controller
 * Xử lý WebSocket messages và routing
 */
@Controller
public class WebSocketController {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    /**
     * Xử lý chat messages trong room
     * Client gửi tới: /app/chat/{roomId}
     * Broadcast tới: /topic/chat/{roomId}
     */
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage sendChatMessage(@DestinationVariable String roomId,
                                     @Payload ChatMessage message,
                                     Principal principal) {
        // Set sender từ authenticated user
        if (principal != null) {
            message.setSender(principal.getName());
        }
        message.setRoom(roomId);
        
        System.out.println("📨 Chat message in room " + roomId + ": " + message.getContent());
        return message;
    }
    
    /**
     * Xử lý private messages
     * Client gửi tới: /app/private
     * Gửi tới user cụ thể: /queue/private
     */
    @MessageMapping("/private")
    public void sendPrivateMessage(@Payload ChatMessage message, Principal principal) {
        if (principal != null) {
            message.setSender(principal.getName());
        }
        
        // Gửi private message tới recipient
        messagingTemplate.convertAndSendToUser(
                message.getRecipient(),
                "/queue/private",
                message
        );
        
        System.out.println("🔒 Private message from " + message.getSender() + 
                          " to " + message.getRecipient() + ": " + message.getContent());
    }
    
    /**
     * Xử lý general messages
     * Client gửi tới: /app/message
     * Broadcast tới: /topic/messages
     */
    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public WebSocketMessage sendMessage(@Payload WebSocketMessage message,
                                      Principal principal) {
        if (principal != null) {
            message.setSender(principal.getName());
        }
        
        System.out.println("📢 General message: " + message.getContent());
        return message;
    }
    
    /**
     * User join notification
     * Client gửi tới: /app/join/{roomId}
     * Broadcast tới: /topic/chat/{roomId}
     */
    @MessageMapping("/join/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public WebSocketMessage userJoin(@DestinationVariable String roomId,
                                   @Payload WebSocketMessage message,
                                   SimpMessageHeaderAccessor headerAccessor,
                                   Principal principal) {
        
        String username = principal != null ? principal.getName() : "Anonymous";
        
        // Add username to websocket session
        headerAccessor.getSessionAttributes().put("username", username);
        headerAccessor.getSessionAttributes().put("room", roomId);
        
        message.setType("USER_JOIN");
        message.setSender("SYSTEM");
        message.setContent(username + " joined the room");
        
        System.out.println("👋 User " + username + " joined room " + roomId);
        return message;
    }
}