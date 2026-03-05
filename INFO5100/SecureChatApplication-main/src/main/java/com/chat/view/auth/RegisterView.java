package com.chat.view.auth;

import com.chat.controller.AuthController; // Import AuthController for handling registration logic
import com.chat.util.ProfilePictureHandler; // Import ProfilePictureHandler for handling profile picture logic
import javafx.geometry.Pos; // For setting layout alignment
import javafx.scene.Scene; // For creating the scene
import javafx.scene.control.*; // Import JavaFX control classes (Button, TextField, PasswordField, Label, etc.)
import javafx.scene.layout.GridPane; // For layout management
import javafx.scene.layout.HBox; // For horizontal layout
import javafx.scene.layout.VBox; // For vertical layout
import javafx.scene.shape.Circle;
import javafx.scene.text.Font; // For setting font
import javafx.stage.FileChooser; // For file chooser dialog
import javafx.stage.Stage; // For stage (window)
import javafx.scene.image.Image; // For setting ImageView image
import javafx.scene.image.ImageView; // For displaying profile picture

import java.io.File; // For handling file selection

public class RegisterView {
    private Stage stage;
    private AuthController authController;
    private TextField userIdField;
    private TextField nameField;
    private PasswordField passwordField;
    private ImageView profileImageView;
    private File profileFile;

    // Constructor
    public RegisterView(Stage stage, AuthController authController) {
        this.stage = stage;
        this.authController = authController;
        this.profileImageView = new ImageView();
        this.userIdField = new TextField();
        this.nameField = new TextField();
        this.passwordField = new PasswordField();
    }

    // Display the registration view
    public void show() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setStyle("-fx-padding: 20;");

        // User ID input
        Label userIdLabel = new Label("User ID: ");
        userIdLabel.setFont(Font.font("Arial", 11));
        userIdField.setPrefWidth(200);
        VBox userIdBox = new VBox(5);
        userIdBox.getChildren().addAll(userIdLabel, userIdField);
        grid.add(userIdBox, 0, 0);

        // Name input
        Label nameLabel = new Label("Name:");
        nameLabel.setFont(Font.font("Arial", 11));
        nameField.setPrefWidth(200);
        VBox nameBox = new VBox(5);
        nameBox.getChildren().addAll(nameLabel, nameField);
        grid.add(nameBox, 0, 1);

        // Password input
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", 11));
        passwordField.setPrefWidth(200);
        VBox passwordBox = new VBox(5);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        grid.add(passwordBox, 0, 2);

        // Profile picture section
        Label profilePicLabel = new Label("Profile Picture:");
        profilePicLabel.setFont(Font.font("Arial", 11));
        Button uploadButton = new Button("Upload Picture");
        uploadButton.setStyle("-fx-background-color: #95D2B3; -fx-text-fill: white;");
        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg"));
            fileChooser.setTitle("Choose Profile Picture");
            profileFile = fileChooser.showOpenDialog(stage);
            if (profileFile != null) {
                Image circularImage = ProfilePictureHandler.createCircularProfilePicture(profileFile);
                System.out.println("file chose");
                if (circularImage != null) {
                    profileImageView.setImage(circularImage);
                    System.out.println("image choosed: " + circularImage);
                } else {
                    System.out.println("Failed to load profile picture.");
                }
            }
        });

        profileImageView.setFitWidth(100);
        profileImageView.setFitHeight(100);
        profileImageView.setPreserveRatio(true);
        Circle clip = new Circle(40, 40, 40); // Circular clip
        profileImageView.setClip(clip);

        VBox profilePicBox = new VBox(5);
        profilePicBox.getChildren().addAll(profilePicLabel, uploadButton, profileImageView);
        grid.add(profilePicBox, 0, 3);

        // Register button
        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #55AD9B; -fx-text-fill: white;");
        registerButton.setOnAction(e -> {
            String userId = userIdField.getText();
            String name = nameField.getText();
            String password = passwordField.getText();
            String profilePicture = profileImageView.getImage() != null ? profileFile.toURI().toString() : "";
            if (userId.isEmpty() || name.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Registration Warning");
                alert.setHeaderText("User information cannot be empty!");
                ButtonType okButton = new ButtonType("OK");
                alert.getButtonTypes().setAll(okButton);
                alert.showAndWait();
            }
            else
                authController.registerUser(userId, name, password, profilePicture);
        });

        HBox registerButtonBox = new HBox(10);
        registerButtonBox.setAlignment(Pos.CENTER);
        registerButtonBox.getChildren().add(registerButton);
        grid.add(registerButtonBox, 0, 4);

        Scene scene = new Scene(grid, 300, 500);
        stage.setScene(scene);
        stage.setTitle("Register");
        stage.show();
    }

    // Getter methods for user input fields
    public String getUserId() {
        return userIdField.getText();
    }

    public String getName() {
        return nameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public ImageView getProfileImageView() {
        return profileImageView;
    }
}
