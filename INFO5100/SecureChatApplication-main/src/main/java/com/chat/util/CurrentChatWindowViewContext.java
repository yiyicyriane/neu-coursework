package com.chat.util;

import com.chat.view.chat.ChatWindowView;

import javafx.stage.Stage;

public class CurrentChatWindowViewContext {
    private static CurrentChatWindowViewContext instance;
    private ChatWindowView chatWindowView;

    private CurrentChatWindowViewContext() {}

    public static synchronized CurrentChatWindowViewContext getInstance() {
        if (instance == null) {
            instance = new CurrentChatWindowViewContext();
        }
        return instance;
    }

    public ChatWindowView getChatWindowView() {
        return chatWindowView;
    }

    public void setChatWindowView(ChatWindowView newChatWindowView) throws Exception {
        Stage stage;
        if (chatWindowView != null) {
            stage = chatWindowView.getStage();
            if (!stage.isShowing())
                stage = new Stage();
        }
        else
            stage = new Stage();
        chatWindowView = newChatWindowView;
        chatWindowView.start(stage);
    }
}

