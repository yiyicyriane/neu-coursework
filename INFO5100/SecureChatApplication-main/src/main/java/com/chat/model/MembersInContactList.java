package com.chat.model;

import lombok.*;

@Data
@NoArgsConstructor                // For frameworks like Spring to create empty objects
// @RequiredArgsConstructor         // constructor with NonNull args
@AllArgsConstructor               // Optional if you still want a constructor with all args
public class MembersInContactList {
    private boolean isGroupChatRoom;
    private String userId;
    private String name;
    private String chatRoomId;
}
