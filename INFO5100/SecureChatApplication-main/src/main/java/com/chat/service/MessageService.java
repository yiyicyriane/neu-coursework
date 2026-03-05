package com.chat.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.chat.model.Message;
import com.chat.util.FileLoadUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageService {
    private final String serverUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public MessageService() throws Exception {
        this.serverUrl = FileLoadUtil.getServerUrl() + "/api/messages";
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<Message> getMessages(String chatRoomId) throws Exception {
        String endpoint = serverUrl + "/" + chatRoomId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), new TypeReference<List<Message>>() {});
    }

    public String sendMessage(String chatRoomId, Message message) throws Exception {
        String endpoint = serverUrl + "/" + chatRoomId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(message)))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
        // "Message sent." "Message send failed."
    }

    public String deleteMessage(String chatRoomId, long timestamp) throws Exception {
        String endpoint = serverUrl + "/" + chatRoomId + "?timestamp=" + timestamp;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
        // "Message deleted."
        // TODO: update the message history in view, others update when they do sth
    }
}
