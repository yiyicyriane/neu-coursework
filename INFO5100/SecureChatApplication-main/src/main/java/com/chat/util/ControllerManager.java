package com.chat.util;

import com.chat.controller.ChatController;
import com.chat.controller.ContactController;
import com.chat.controller.SettingsController;

public class ControllerManager {
    private static ControllerManager instance;

    // 控制器实例
    private ChatController chatController;
    private ContactController contactController;
    private SettingsController settingsController;

    // 私有化构造函数，防止外部创建新的实例
    private ControllerManager() {}

    // 获取唯一的实例
    public static ControllerManager getInstance() {
        if (instance == null) {
            instance = new ControllerManager();
        }
        return instance;
    }

    // 获取 ChatController 的实例
    public ChatController getChatController() throws Exception {
        if (chatController == null) {
            chatController = new ChatController();
        }
        return chatController;
    }

    // 获取 ContactController 的实例
    public ContactController getContactController() throws Exception {
        if (contactController == null) {
            contactController = new ContactController();
        }
        return contactController;
    }

    // 获取 SettingsController 的实例
    public SettingsController getSettingsController() throws Exception {
        if (settingsController == null) {
            settingsController = new SettingsController();
        }
        return settingsController;
    }
}
