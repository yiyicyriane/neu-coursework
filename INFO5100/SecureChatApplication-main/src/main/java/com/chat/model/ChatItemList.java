package com.chat.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor                // For frameworks like Spring to create empty objects
// @RequiredArgsConstructor         // constructor with NonNull args
@AllArgsConstructor               // Optional if you still want a constructor with all args
public class ChatItemList {
    private List<ChatItem> chatItemList = new ArrayList<>(); // Stores a list of ChatItem objects.

    /**
     * Returns a copy of the chat item list to prevent modification of the original list.
     * @return A new List containing ChatItem objects.
     */
    public List<ChatItem> getChatItemList() {
        return new ArrayList<>(chatItemList);
    }

    /**
     * Sets the chat item list with a new list of ChatItem objects.
     * A copy of the input list is stored to maintain immutability.
     * @param chatItemList A List of ChatItem objects.
     */
    public void setChatItemList(List<ChatItem> chatItemList) {
        this.chatItemList = new ArrayList<>(chatItemList);
    }

    /**
     * Adds a single ChatItem to the list.
     * @param chatItem The ChatItem to be added.
     */
    public void addChatItem(ChatItem chatItem) {
        this.chatItemList.add(chatItem);
    }

    /**
     * Removes a ChatItem from the list by its index.
     * @param index The index of the ChatItem to remove.
     */
    public void removeChatItem(int index) {
        if (index >= 0 && index < chatItemList.size()) {
            this.chatItemList.remove(index);
        }
    }

}

