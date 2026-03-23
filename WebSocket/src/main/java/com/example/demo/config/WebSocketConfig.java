package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * LESSON 10: WebSocket Configuration
 * Cấu hình WebSocket với STOMP protocol cho real-time communication
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Cấu hình message broker
     * - /topic: cho broadcast messages (1-to-many)
     * - /queue: cho private messages (1-to-1)
     * - /app: prefix cho application destinations
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple broker cho /topic và /queue destinations
        config.enableSimpleBroker("/topic", "/queue");
        
        // Set application destination prefix
        config.setApplicationDestinationPrefixes("/app");
        
        // Set user destination prefix cho private messages
        config.setUserDestinationPrefix("/user");
    }

    /**
     * Đăng ký STOMP endpoints
     * Client sẽ connect tới endpoint này
     */
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