package com.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor                // For frameworks like Spring to create empty objects
@AllArgsConstructor               // Optional if you still want a constructor with all args
public class Message {
    private String chatRoomId;  // receiverId
    
    private String senderId;
    private String content; // encrypted message

    private long timestamp;
}


