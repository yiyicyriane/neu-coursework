package com.chat.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor                // For frameworks like Spring to create empty objects
@RequiredArgsConstructor         // constructor with NonNull args
@AllArgsConstructor               // Optional if you still want a constructor with all args
public class ChatRoom {
    private String chatRoomId;

    @NonNull private boolean isGroupChatRoom;
    @NonNull private List<String> memberIdList;
    @NonNull private String chatRoomName;

    // get copy of list
    public List<String> getMemberIdList() {
        return new ArrayList<>(memberIdList);
    }

    public void setMemberIdList(List<String> memberIdlist) {
        this.memberIdList = new ArrayList<>(memberIdlist);
    }
}
