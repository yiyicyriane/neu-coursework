package com.chat.controller;

import com.chat.model.User;
import com.chat.model.UserServer;
import com.chat.service.AuthService;
import com.chat.util.CurrentUserContext;

public class SettingsController {
/*暂时不展示notification设置的功能。
    private boolean notificationsEnabled = true; // 默认开启通知

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean enabled) {
        this.notificationsEnabled = enabled;
        // 保存到数据库或配置文件（根据需求实现）
    }
 */
    private final AuthService authService;

    public SettingsController() throws Exception {
        authService = new AuthService();
    }

    public User getCurrentUser() {
        return CurrentUserContext.getInstance().getCurrentUser();
    }

    // method to update profile picture, 返回true说明后端更新成功。
    public boolean updateProfilePicture(String profile) throws Exception {
        String userId = CurrentUserContext.getInstance().getCurrentUser().getUserId();
        String password = CurrentUserContext.getInstance().getCurrentUser().getPassword();
        UserServer newUserServer = authService.login(userId, password);
        newUserServer.setProfile(profile);
        CurrentUserContext.getInstance().setCurrentUserServer(newUserServer);
        if (authService.putUser(newUserServer).equals("User updated."))
            return true;
        else 
            return false;
    }
}
