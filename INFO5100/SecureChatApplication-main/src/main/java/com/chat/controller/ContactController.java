package com.chat.controller;

import com.chat.model.MembersInContactList;
import com.chat.model.ChatRoom;
import com.chat.service.AuthService;
import com.chat.service.ChatRoomService;
import com.chat.util.CurrentUserContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactController {

    private final AuthService authService;
    private final ChatRoomService chatRoomService;

    public ContactController() throws Exception {
        this.authService = new AuthService(); // Utility class for HTTP requests
        this.chatRoomService = new ChatRoomService();
    }

    //判断当前用户
    public String currentUserId(){
        return CurrentUserContext.getInstance().getCurrentUser().getUserId();
    }

    public String currentPassword(){
        return CurrentUserContext.getInstance().getCurrentUser().getPassword();
    }

    public String getFriendId(ChatRoom chatRoom) throws Exception {
        if (!chatRoom.isGroupChatRoom()) {
            List<String> memberIdList = chatRoom.getMemberIdList();
            memberIdList.remove(currentUserId());
            String friendId = memberIdList.get(0);
            return friendId;
        }
        return "";
    }

    public String getChatRoomName(ChatRoom chatRoom) throws Exception {
        String chatRoomName = chatRoom.getChatRoomName();
        if (!chatRoom.isGroupChatRoom()) 
            chatRoomName = authService.getContact(getFriendId(chatRoom)).getUsername();
        return chatRoomName;
    }

    // Method to add a contact
    public String addContact(String userId, String contactId) {
        try {
            // Send HTTP POST request to the backend API
            String response = authService.postNewFriendApplicationSenderIdSet(userId, contactId);
            // Backend response determines the result
            if (response.equals("Friend already exists.") || 
                response.equals("Invalid Friend ID.") || 
                response.equals("Friend application sent.")) {
                return response;
            } else if (response.equals("Friend application accepted.")) {
                return "success";
                // TODO: notice and update contact in view
            } else {
                return "Server error.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error.";
        }
    }

    // Method to join a group by searching chatRoomId
    public void joinGroupContact(String userId, String chatRoomId) throws Exception {
        chatRoomService.putChatRoomMember(chatRoomId, userId);
    }

    //返回所有的list,这里不需要区分group还是personal
    public List<MembersInContactList> getContacts() throws Exception {
        List<MembersInContactList> membersInContactLists = new ArrayList<>();
        CurrentUserContext.getInstance().setCurrentUserServer(authService.login(currentUserId(), currentPassword()));
        Set<String> chatRoomIdSet = new HashSet<>(CurrentUserContext.getInstance().getCurrentUserServer().getChatRoomIdSet());
        Set<String> friendIdSet = new HashSet<>(CurrentUserContext.getInstance().getCurrentUserServer().getFriendIdSet());
        for (String chatRoomId: chatRoomIdSet) {
            ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
            if (chatRoom.isGroupChatRoom()) {
                String friendId = getFriendId(chatRoom);
                friendIdSet.remove(friendId);
                membersInContactLists.add(new MembersInContactList(
                    chatRoom.isGroupChatRoom(), friendId, 
                    getChatRoomName(chatRoom), chatRoomId));
            }
        }
        for (String friendId: friendIdSet) {
            membersInContactLists.add(new MembersInContactList(
                false, friendId, 
                authService.getContact(friendId).getUsername(), ""));
        }
        return membersInContactLists;
    }

    //在个人联系人列表点击删除按钮，删除好友
    public void removeContact(String userId, String friendsUserId) throws Exception {
        authService.deleteFriend(userId, friendsUserId);
    }

    //退群
    public void removeGroupContact(String userId, String chatRoomId) throws Exception {
        chatRoomService.deleteChatRoomMember(chatRoomId, userId, userId);
    }

    public String createNewIndividualChatRoom(String friendId) throws Exception {
        ChatRoom chatRoom = new ChatRoom(false, new ArrayList<>(List.of(currentUserId(), friendId)), "");
        return chatRoomService.postChatRoom(chatRoom);
    }

    //根据用户id,用户写的群名和用户选择的好友列表创建群聊，返回创建的新群的chatroomid, 用于在view中直接跳转到这个聊天窗口。
    public String createNewGroup(String creatorId, String groupName, List<String> selectedMembers) throws Exception {
        List<String> memberList = new ArrayList<>(selectedMembers);
        memberList.add(0, creatorId); 
        ChatRoom chatRoom = new ChatRoom(true, memberList, groupName);
        return chatRoomService.postChatRoom(chatRoom);
    }

    public String existsChatRoomWith(String friendId) throws Exception {
        Set<String> chatRoomIdSet = authService.getChatRoomIdSet(currentUserId());
        for (String chatRoomId: chatRoomIdSet) {
            ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
            if (!chatRoom.isGroupChatRoom()) {
                if (chatRoom.getMemberIdList().contains(friendId))
                    return chatRoomId;
            }
        }
        return "";
    }
}
