package com.example.demo.websocket.dto;

/**
 * LESSON 10: Chat Message DTO
 * Message cho chat functionality
 */
public class ChatMessage extends WebSocketMessage {
    
    private String room;
    private String recipient; // For private messages
    
    public ChatMessage() {
        super();
        setType("CHAT_MESSAGE");
    }
    
    public ChatMessage(String content, String sender, String room) {
        this();
        setContent(content);
        setSender(sender);
        this.room = room;
    }
    
    public ChatMessage(String content, String sender, String recipient, boolean isPrivate) {
        this();
        setContent(content);
        setSender(sender);
        this.recipient = recipient;
        if (isPrivate) {
            setType("PRIVATE_MESSAGE");
        }
    }
    
    // Getters and Setters
    public String getRoom() {
        return room;
    }
    
    public void setRoom(String room) {
        this.room = room;
    }
    
    public String getRecipient() {
        return recipient;
    }
    
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}