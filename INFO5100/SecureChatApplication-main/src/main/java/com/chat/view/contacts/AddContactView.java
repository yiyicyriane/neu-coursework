package com.chat.view.contacts;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.util.function.Consumer;


public class AddContactView {


    // private Consumer<String> onContactAdded;


    // show() 方法，用来展示界面
    public void show(Stage primaryStage, boolean isGroup, Consumer<String> onContactAdded) {
        // this.onContactAdded = onContactAdded;


        // 创建根布局
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER_LEFT); // 左对齐
        root.setPadding(new Insets(15)); // 添加内边距


        // 添加说明标签
        String labelString = isGroup ? "group's" : "user's";
        Label instructionLabel = new Label("Please enter the " + labelString + " ID:");
        instructionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");


        // 创建联系人ID输入框
        TextField contactIdField = new TextField();
        contactIdField.setPromptText("Enter Contact ID");
        contactIdField.setMaxWidth(250); // 限制文本框宽度


        // 创建添加按钮
        String buttonString = isGroup ? "Join" : "Add";
        Button addButton = new Button(buttonString);
        addButton.setOnAction(e -> {
            String newContact = contactIdField.getText().trim();
            if (!newContact.isEmpty() && onContactAdded != null) {
                onContactAdded.accept(newContact); // 触发回调函数，传递新的联系人ID
                primaryStage.close(); // 添加完成后关闭窗口
            }
        });


        // 将所有元素添加到根布局
        root.getChildren().addAll(instructionLabel, contactIdField, addButton);


        // 创建并显示场景
        String titleString = isGroup ? "Join Group" : "Add Contact";
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle(titleString);
        primaryStage.show();
    }
}
