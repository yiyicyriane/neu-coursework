package com.chat.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.chat.model.ChatRoom;
import com.chat.util.FileLoadUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatRoomService {
    private final String serverUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ChatRoomService() throws Exception {
        this.serverUrl = FileLoadUtil.getServerUrl() + "/api/chatrooms";
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String postChatRoom(ChatRoom chatRoom) throws Exception {
        String endpoint = serverUrl;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(chatRoom)))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
        // Chat room id
    }

    public ChatRoom getChatRoom(String chatRoomId) throws Exception {
        String endpoint = serverUrl + "/" + chatRoomId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), ChatRoom.class);
    }

    public String putChatRoomMember(String chatRoomId, String userId) throws Exception {
        String endpoint = serverUrl + "/" + chatRoomId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .PUT(HttpRequest.BodyPublishers.ofString(userId))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
        // "Chat room members updated."
        // TODO: update chatroom member
    }

    public String deleteChatRoom(String chatRoomId, String senderId) throws Exception {
        String endpoint = serverUrl + "/" + chatRoomId + "?senderId=" + senderId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
        // "Chat room deleted."
        // "Insufficient permission to delete group chat room."
        // TODO: updata chatroom id set. if deleted the current chat, return to the chat list view
    }

    public String deleteChatRoomMember(String chatRoomId, String userId, String senderId) throws Exception {
        String endpoint = serverUrl + "/" + chatRoomId + "/" + userId + "?senderId=" + senderId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); 
        // "Chat room member removed."
        // "Insufficient permission to remove chat room member."
        // TODO: update chat room member
    }
}
