package com.chat;

import com.chat.controller.AuthController;
import com.chat.view.auth.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 创建 AuthController 对象
        AuthController authController = new AuthController();

        // 1. 创建并显示登录界面
        Stage loginStage = new Stage();
        LoginView loginView = new LoginView(loginStage, authController); // 传递 AuthController
        loginView.show();
    }

    public static void main(String[] args) {
        launch(args); // 启动 JavaFX 应用程序
    }
}
