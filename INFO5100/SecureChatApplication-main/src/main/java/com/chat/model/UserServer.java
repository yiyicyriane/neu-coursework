package com.chat.model;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor                // For frameworks like Spring to create empty objects
@RequiredArgsConstructor         // constructor with NonNull args
@AllArgsConstructor              // Optional if you still want a constructor with all args
public class UserServer {
    @NonNull private String userId;
    
    @NonNull private String username;
    @NonNull private String password; // encrypted password
    @NonNull private String publicKey;
    @NonNull private String profile;

    // optional
    // private boolean disableNotification;
    private Set<String> friendIdSet = new HashSet<>(); // friend user id Set
    private Set<String> chatRoomIdSet = new HashSet<>();
    private Set<String> friendApplicationSenderIdSet = new HashSet<>();
    // user session setting in other model

    // return copy of set
    public Set<String> getFriendIdSet() {
        return new HashSet<>(friendIdSet);
    }
    public Set<String> getChatRoomIdSet() {
        return new HashSet<>(chatRoomIdSet);
    }
    public Set<String> getFriendApplicationSenderIdSet() {
        return new HashSet<>(friendApplicationSenderIdSet);
    }

    public void setFriendIdSet(Set<String> friendIdSet) {
        this.friendIdSet = new HashSet<>(friendIdSet);
    }
    public void setChatRoomIdSet(Set<String> chatRoomIdSet) {
        this.chatRoomIdSet = new HashSet<>(chatRoomIdSet);
    }
    public void setFriendApplicationSenderIdSet(Set<String> friendApplicationSenderIdSet) {
        this.friendApplicationSenderIdSet = new HashSet<>(friendApplicationSenderIdSet);
    }
}
