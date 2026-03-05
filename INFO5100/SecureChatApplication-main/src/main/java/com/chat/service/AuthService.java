package com.chat.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;

import com.chat.model.UserServer;
import com.chat.util.FileLoadUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthService {
    private final String serverUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AuthService() throws Exception {
        this.serverUrl = FileLoadUtil.getServerUrl() + "/api/auth";
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String register(UserServer userServer) throws Exception {
        String endpoint = serverUrl + "/user";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userServer)))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
        // "User registered." "User id existed."
    }

    public UserServer login(String userId, String password) throws Exception {
        String endpoint = serverUrl + "/login/" + userId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(password))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), UserServer.class);
        } else {
            return null;
        }
    }

    public UserServer getContact(String userId) throws Exception {
        String endpoint = serverUrl + "/user/" + userId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), UserServer.class);
        } else {
            return null;
        }
    }

    public Set<String> getFriendApplicationSenderIdSet(String userId) throws Exception {
        String endpoint = serverUrl + "/" + userId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), new TypeReference<Set<String>>() {});
    }

    public String postNewFriendApplicationSenderIdSet(String userId, String friendId) throws Exception {
        String endpoint = serverUrl + "/" + userId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(friendId))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); 
        // "Invalid Friend ID." "Friend application accepted." 
        // "Friend already exists." "Friend application sent."
        // TODO: update friend set when showing contact, using login service to request friend set
    }

    public String deleteFriend(String userId, String friendId) throws Exception {
        String endpoint = serverUrl + "/" + userId + "/" + friendId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); 
        // "Friend delete successful."
        // TODO: update chat room set when receive the reply
    }

    public String putPassword(String userId, String password, String newPassword, String publicKey) throws Exception {
        String endpoint = serverUrl + "/password/" + userId + "?password=" + password + "&newPassword=" + newPassword;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .PUT(HttpRequest.BodyPublishers.ofString(publicKey))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); 
        // "Password changed." "Password change failed."
    }

    public String putUser(UserServer userServer) throws Exception {
        String endpoint = serverUrl + "/user/" + userServer.getUserId();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userServer)))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); 
        // "User updated."
    }

    public String getPublicKey(String userId) throws Exception {
        String endpoint = serverUrl + "/publickey/" + userId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); 
        // String of publicKey
    }

    public Set<String> getChatRoomIdSet(String userId) throws Exception {
        String endpoint = serverUrl + "/chatrooms/" + userId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), new TypeReference<Set<String>>() {});
    }
}
