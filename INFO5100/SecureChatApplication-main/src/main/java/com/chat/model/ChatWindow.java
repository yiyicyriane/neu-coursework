package com.chat.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor                // For frameworks like Spring to create empty objects
// @RequiredArgsConstructor         // constructor with NonNull args
@AllArgsConstructor               // Optional if you still want a constructor with all args
public class ChatWindow {
    private String chatRoomId;
    private String chatRoomName;
    private boolean isGroupChatRoom;
    private String profilePicture;
    private List<String> memberIdList;
    private List<Message> messageList;

    // get copy of memberIdList
    public List<String> getMemberIdList() {
        return new ArrayList<>(memberIdList);
    }

    public void setMemberIdList(List<String> memberIdlist) {
        this.memberIdList = new ArrayList<>(memberIdlist);
    }

    // get copy of messageList
    public List<Message> getMessageList() {
        return new ArrayList<>(messageList);
    }

    public void setMessageList(List<Message> messagelist) {
        this.messageList = new ArrayList<>(messagelist);
    }
}












