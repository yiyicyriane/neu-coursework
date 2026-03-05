package com.chat.view.chat;

import com.chat.controller.ChatController;
import com.chat.model.ChatItem;
import com.chat.model.ChatItemList;
import com.chat.service.WebSocketService;
import com.chat.util.ControllerManager;
import com.chat.util.CurrentChatWindowViewContext;
import com.chat.util.CurrentViewContext;
import com.chat.view.contacts.ContactListView;
import com.chat.view.settings.ProfileSettingsView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class ChatListView extends Application {
    private VBox chatList; // 显示聊天列表的容器
    private ChatController chatController; //存储chatcontroller的实例
    private ChatItemList chatItemList;
    private Stage stage;

    public ChatListView() throws Exception {
        this.chatList = new VBox(10); // 初始化聊天列表容器
        //从ControllerManager中获取ChatController实例
        this.chatController = ControllerManager.getInstance().getChatController();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // 创建主布局
        //VBox root = new VBox(10);
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-padding: 20; -fx-background-color: #f0f0f0;");


        //创建 ScrollPane 包裹 chatList
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(chatList);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setStyle("-fx-background: #f0f0f0;");

        // 设置 ScrollPane 的锚点，使其占满上面区域直到底部导航栏
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 50.0); // 底部留出空间给导航栏

        //设置ScrollPane伸展规则，填充整个可用空间
       // VBox.setVgrow(scrollPane, Priority.ALWAYS);

        //将ScrollPane添加到主布局
        root.getChildren().add(scrollPane);


        // 获取聊天项列表并创建聊天框
        updateChatList();

        // 将聊天列表容器添加到主布局
        root.getChildren().add(chatList);

        // 添加底部导航栏
       //Region spacer = new Region();
        //VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        //root.getChildren().add(spacer);

        HBox bottomBar = createBottomBar(stage);
        root.getChildren().add(bottomBar);

        // 设置场景和舞台
        this.stage = stage;
        Scene scene = new Scene(root, 400, 600);
        stage.setTitle("Chat List");
        stage.setScene(scene);
        stage.show();

        CurrentViewContext.getInstance().setCurrentView(this);
        
        WebSocketService.getInstance().subscribeFriendApplication();
        WebSocketService.getInstance().subscribeChatRoomUpdate();
    }

    public void updateChatListView() throws Exception {
        start(stage);
    }

    /**
     * 更新聊天列表界面。
     */
    public void updateChatList() throws Exception {
        chatList.getChildren().clear(); // 清空旧的聊天项

        // 从控制器获取最新的 ChatItemList 数据
        chatItemList = chatController.getChatItemList();

        // 遍历每个 ChatItem 并添加到界面上
        for (ChatItem chatItem : chatItemList.getChatItemList()) {
            HBox chatBox = createChatItem(chatItem); // 根据 ChatItem 创建对话框
            chatList.getChildren().add(chatBox);
        }
    }

    // 创建聊天框的方法
    private HBox createChatItem(ChatItem chatItem) {
        HBox chatItemBox = new HBox(10);
        chatItemBox.setStyle("-fx-padding: 10; -fx-background-color: #ffffff; -fx-border-radius: 5; -fx-background-radius: 5; -fx-border-color: #dcdcdc;");

        // 聊天室名称
        Label nameLabel = new Label(chatItem.getChatRoomName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // 最后一条消息
        Label lastMessageLabel = new Label(chatItem.getLastMessage());
        lastMessageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");

        // 最后一条消息的时间
        Label timeLabel = new Label(chatItem.getLastMessageTime());
        timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #888888;");

        // 布局配置
        VBox chatInfo = new VBox(5);
        chatInfo.getChildren().addAll(nameLabel, lastMessageLabel);
        HBox.setHgrow(chatInfo, javafx.scene.layout.Priority.ALWAYS);

        chatItemBox.getChildren().addAll(chatInfo, timeLabel);

        // 为聊天框添加点击事件
        chatItemBox.setOnMouseClicked((MouseEvent event) -> {
           // chatController.openChatWindow(chatItem.getChatRoomId()); // 调用控制器打开聊天窗口
            try {
                CurrentChatWindowViewContext.getInstance().setChatWindowView(new ChatWindowView(chatItem.getChatRoomId()));
            } catch (Exception e) {
                System.err.println("New ChatWindowView error");
                e.printStackTrace();
            } //以新窗口的形式打开。
        });

        return chatItemBox;
    }

    // 创建底部导航栏
    private HBox createBottomBar(Stage stage) {
        HBox bottomBar = new HBox(30);

        bottomBar.setMinHeight(40); // 设置底部栏高度
        AnchorPane.setBottomAnchor(bottomBar, 0.0);
        AnchorPane.setLeftAnchor(bottomBar, 0.0);
        AnchorPane.setRightAnchor(bottomBar, 0.0);

        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setStyle("-fx-background-color: #55AD9B; -fx-padding: 10;");

        Button chatsButton = new Button("Chats");
        Button contactsButton = new Button("Contacts");
        Button settingsButton = new Button("Settings");

        chatsButton.setOnAction(e -> System.out.println("Chats clicked"));
        contactsButton.setOnAction(e -> {
            System.out.println("Navigate to Contact view");
            ContactListView contactListView = null;
            try {
                contactListView = new ContactListView();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            contactListView.start(stage);
        }); // 打开联系人视图

        settingsButton.setOnAction(e -> {
            try {
                new ProfileSettingsView().start(stage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }); // 打开设置视图

        bottomBar.getChildren().addAll(chatsButton, contactsButton, settingsButton);
        return bottomBar;
    }

}
