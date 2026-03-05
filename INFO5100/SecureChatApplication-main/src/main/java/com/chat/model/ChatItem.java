package com.chat.model;

import lombok.*;


@Data
@NoArgsConstructor                // For frameworks like Spring to create empty objects
// @RequiredArgsConstructor         // constructor with NonNull args
@AllArgsConstructor               // Optional if you still want a constructor with all args
public class ChatItem {
    private String chatRoomId;
    private String chatRoomName;
    private String lastMessage;
    private String lastMessageTime;
}


