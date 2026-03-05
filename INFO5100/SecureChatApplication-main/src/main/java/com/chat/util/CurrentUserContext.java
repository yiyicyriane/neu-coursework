package com.chat.util;

import com.chat.model.User;
import com.chat.model.UserServer;

public class CurrentUserContext {
    private static CurrentUserContext instance;
    private User currentUser;
    private UserServer currentUserServer;

    private CurrentUserContext() {}

    public static synchronized CurrentUserContext getInstance() {
        if (instance == null) {
            instance = new CurrentUserContext();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public UserServer getCurrentUserServer() {
        return currentUserServer;
    }

    public void setCurrentUserServer(UserServer userServer) {
        this.currentUserServer = userServer;
    }
}
