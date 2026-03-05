package com.chat.view.settings;

import com.chat.util.ControllerManager;
import com.chat.util.CurrentViewContext;
import com.chat.util.ImageCropUtil;
import com.chat.view.chat.ChatListView;
import com.chat.view.contacts.ContactListView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.chat.model.User;
import com.chat.controller.SettingsController;

import java.io.File;

public class ProfileSettingsView extends Application {

    private SettingsController settingsController;
    private User currentUser; // Current user fetched from the global context
    //private boolean receiveNotifications; // Notification setting, default is off
    private ImageView profileImageView;

    public ProfileSettingsView() throws Exception{
        this.settingsController = ControllerManager.getInstance().getSettingsController();
        //this.receiveNotifications = false;
    }

    @Override
    public void start(Stage stage) {
        // Fetch current user from controller
        currentUser = settingsController.getCurrentUser();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF;");

        // Create top area
        VBox topArea = createTopArea();
        // Create Profile Setting area and Notification Setting area
       // VBox centerArea = new VBox(2, createProfileSettingArea(stage), createNotificationSettingArea());
        VBox centerArea = new VBox(1, createProfileSettingArea(stage));
        VBox.setVgrow(centerArea, Priority.ALWAYS);
        centerArea.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 20 0;");

        // Create bottom navigation bar
        HBox bottomBar = createBottomBar(stage);

        // Add all parts to the root layout
        root.setTop(topArea);
        root.setCenter(centerArea);
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 400, 600);
        stage.setScene(scene);
        stage.setTitle("Profile Settings");
        stage.show();

        CurrentViewContext.getInstance().setCurrentView(this);
    }

    // Create the top area with profile picture and username
    private VBox createTopArea() {
        VBox topArea = new VBox();
        topArea.setStyle("-fx-background-color: #55AD9B; -fx-padding: 10 10 30 10;");
        topArea.setAlignment(Pos.CENTER_LEFT);

        HBox content = new HBox(10);
        content.setAlignment(Pos.CENTER_LEFT);

        // Display user's profile picture
        profileImageView = new ImageView();
        if (!currentUser.getProfilePicture().isEmpty()) {
            try {
                Image profileImage = new Image(currentUser.getProfilePicture()); // Fetch profile picture from currentUser
                profileImage = ImageCropUtil.cropToSquare(profileImage);
                profileImageView.setImage(profileImage);
            } catch (Exception e) {
                System.out.println("Profile image does not exist.");
            }
            
        }
        profileImageView.setFitWidth(80);
        profileImageView.setFitHeight(80);
        profileImageView.setPreserveRatio(true);
        Circle clip = new Circle(40, 40, 40); // Circular clip
        profileImageView.setClip(clip);
        
        // Display user's name
        Label userNameLabel = new Label(currentUser.getName()); // Fetch name from currentUser
        userNameLabel.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-font-weight: bold;");

        content.getChildren().addAll(profileImageView, userNameLabel);
        topArea.getChildren().add(content);

        return topArea;
    }


    // Create the Profile Setting area
    private VBox createProfileSettingArea(Stage stage) {
        VBox profileSettingArea = new VBox(10);
        profileSettingArea.setStyle("-fx-background-color: #95D2B3; -fx-padding: 30 10 30 10;");
        profileSettingArea.setAlignment(Pos.CENTER_LEFT);

        Label profileSettingLabel = new Label("Profile Setting");
        profileSettingLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button updateProfilePictureButton = new Button("Update Profile Picture");
        updateProfilePictureButton.setStyle("-fx-alignment: CENTER_LEFT;");
        updateProfilePictureButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                // Image newProfileImage = new Image(file.toURI().toString());
                try {
                    if(settingsController.updateProfilePicture(file.toURI().toString())){//// Call controller to update profile picture
                        updateProfilePicture(file.toURI().toString());
                        showAlert("Success", "Profile picture updated successfully!");
                    }else{
                        showAlert("Error", "Failed to update profile picture.");
                    }
                } catch (Exception e1) {
                    System.err.println("update profile error");
                    e1.printStackTrace();
                }
            }
        });

        profileSettingArea.getChildren().addAll(profileSettingLabel, updateProfilePictureButton);
        return profileSettingArea;
    }

    //update profile picture in the view
    public void updateProfilePicture(String newProfilePicturePath) {
        // Update the profile picture path in currentUser (this is the model)
        currentUser.setProfilePicture(newProfilePicturePath);
        // Update the profileImageView to show the new profile picture
        Image profileImage = new Image(newProfilePicturePath);
        profileImage = ImageCropUtil.cropToSquare(profileImage);
        profileImageView.setImage(profileImage); // Refresh the profile image view
    }

/*
    // Create the Notification Setting area
    private VBox createNotificationSettingArea() {
        VBox notificationSettingArea = new VBox(10);
        notificationSettingArea.setStyle("-fx-background-color: #95D2B3; -fx-padding: 30 10 30 10;");
        notificationSettingArea.setAlignment(Pos.CENTER_LEFT);

        Label notificationSettingLabel = new Label("Notification Setting");
        notificationSettingLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        CheckBox notificationCheckBox = new CheckBox("Receive Notifications");
        notificationCheckBox.setSelected(receiveNotifications);
        notificationCheckBox.setOnAction(e -> receiveNotifications = notificationCheckBox.isSelected());

        notificationSettingArea.getChildren().addAll(notificationSettingLabel, notificationCheckBox);
        return notificationSettingArea;
    }

 */

    // Create the bottom navigation bar
    private HBox createBottomBar(Stage stage) {
        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setStyle("-fx-background-color: #55AD9B; -fx-padding: 10;");
        bottomBar.setPrefHeight(50);
        bottomBar.setSpacing(30);

        Button chatsButton = new Button("Chats");
        Button contactsButton = new Button("Contacts");
        Button settingsButton = new Button("Settings");

        chatsButton.setOnAction(e -> {
            try {
                openView(new ChatListView(), stage);
            } catch (Exception e1) {
                System.err.println("ChatListView open error");
                e1.printStackTrace();
            }
        });
        contactsButton.setOnAction(e -> {
            System.out.println("Navigate to Contact view");
            ContactListView contactListView = null;
            try {
                contactListView = new ContactListView();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            contactListView.start(stage);
        });
        settingsButton.setOnAction(e -> System.out.println("Already in Settings"));

        bottomBar.getChildren().addAll(chatsButton, contactsButton, settingsButton);
        return bottomBar;
    }

    // Generic method to open a new view
    private void openView(Application view, Stage stage) {
        try {
            view.start(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Display an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
