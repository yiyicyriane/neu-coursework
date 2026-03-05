/*
1.button跳转，跳转到chats, contacts, settings界面。
2.和服务器获取这个人当前的所有聊天室，加载到chatlistview界面上。
要具备更新功能，如我收到了新的消息（之前没有聊天过的人的消息）要在窗口中加入一个新的对话框。
如果我收到了之前聊过天的人发送的新消息，要在chatlistview这个界面中和这个人的对话框中更新显示收到的最新的消息。
3. 界面跳转，点击聊天对话框之后会跳转到跟这个人的聊天窗口界面chatwindowview。
4. 和服务器获取这个chatroom的chatRoomName显示在chatwindowview的最上方，并且找到这个chatroom对应的头像显示在最上方。
5. 和服务器获取和这个人的聊天记录加载到chatwindowview的界面。如果我对方发送了新的消息，这个界面要更新，如果我发送了新的消息，我也要传回给服务器，然后界面更新。
6. 和服务器获取List<String> memberIdList, 点击member button之后会显示群成员列表。
7. 有一个删除聊天记录的功能，当这条聊天记录是自己的时候就可以删除。返回服务器更新
*/

package com.chat.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.chat.model.ChatItem;
import com.chat.model.ChatItemList;
import com.chat.model.ChatRoom;
import com.chat.model.ChatWindow;

import com.chat.model.Message;
import com.chat.model.UserServer;
import com.chat.service.AuthService;
import com.chat.service.ChatRoomService;
import com.chat.service.MessageService;
import com.chat.util.CurrentUserContext;
import com.chat.util.TimestampFormatter;

public class ChatController {

    private final ChatRoomService chatRoomService;
    private final AuthService authService;
    private final MessageService messageService;

    public ChatController() throws Exception {
        // 初始化聊天项列表
        this.chatRoomService = new ChatRoomService();
        this.authService = new AuthService();
        this.messageService = new MessageService();
    }

    public String getChatRoomName(ChatRoom chatRoom, String userId) throws Exception {
        String chatRoomName = chatRoom.getChatRoomName();
        if (!chatRoom.isGroupChatRoom()) {
            List<String> memberIdList = chatRoom.getMemberIdList();
            memberIdList.remove(userId);
            String friendId = memberIdList.get(0);
            chatRoomName = authService.getContact(friendId).getUsername();
        }
        return chatRoomName;
    }

    public Message getLastMessage(String chatRoomId) throws Exception {
        List<Message> messages = messageService.getMessages(chatRoomId);
        if (messages.isEmpty()) return new Message();
        return messages.get(messages.size() - 1);
    }

    /**
     * 获取聊天项列表
     *
     * @return 返回当前的聊天窗口列表
     */

    public ChatItemList getChatItemList() throws Exception {
        String userId = currentUserId();
        ChatItemList chatItemList = new ChatItemList(); // 存储聊天项列表
        Set<String> chatRoomIdSet = authService.getChatRoomIdSet(userId);
        if (chatRoomIdSet.isEmpty()) return new ChatItemList();
        for (String chatRoomId : chatRoomIdSet) {
            ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
            Message lastMessage = getLastMessage(chatRoomId);
            String timestamp = "";
            if (!lastMessage.equals(new Message()))
                timestamp = TimestampFormatter.timestampToString(lastMessage.getTimestamp());
            ChatItem chatItem = new ChatItem(chatRoom.getChatRoomId(),
                    this.getChatRoomName(chatRoom, userId),
                    lastMessage.getContent(),
                    timestamp);
            chatItemList.addChatItem(chatItem);
        }
        return chatItemList;
    }


    //我需要的是model里的ChatWindow里的数据，因为是从聊天列表里点击才会进入和某一个人的聊天对话框，所以会传入这个chatroom的Id.
    public ChatWindow getChatWindowById (String chatRoomId) throws Exception{
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
        String userId = currentUserId();
        String chatRoomName = getChatRoomName(chatRoom, userId);
        String profile = "";
        if (!chatRoom.isGroupChatRoom()) {
            List<String> memberIdList = chatRoom.getMemberIdList();
            memberIdList.remove(userId);
            String friendId = memberIdList.get(0);
            UserServer friend = authService.getContact(friendId);
            profile = friend.getProfile();
        }
        ChatWindow chatWindow = new ChatWindow(chatRoomId, chatRoomName, chatRoom.isGroupChatRoom(), 
            profile, chatRoom.getMemberIdList(), messageService.getMessages(chatRoomId));
        return chatWindow;
    }

    //用于在view里判断sender是不是当前的用户，以及获取当前发送的信息的senderid
    public String currentUserId () {
        return CurrentUserContext.getInstance().getCurrentUser().getUserId();
    }

    //把我在聊天输入框里输入的内容加到后端。
    public boolean addMessageToChatRoom (Message message) throws Exception {
        String messageSendResponse = messageService.sendMessage(message.getChatRoomId(), message);
        if (messageSendResponse.equals("Message sent."))
            return true;
        else
            return false;
    }

    //把我在聊天记录里想删掉的自己的那条聊天记录从后端删除。
    public void removeMessageFromChatRoom (String chatRoomId, long timestamp) throws Exception{
        messageService.deleteMessage(chatRoomId, timestamp);
    }

    public List<String> getMemberNameList(List<String> memberIdList) throws Exception {
        List<String> memberNameList = new ArrayList<>();
        for (String memberId: memberIdList) {
            UserServer userServer = authService.getContact(memberId);
            memberNameList.add(userServer.getUsername());
        }
        return memberNameList;
    }
}



