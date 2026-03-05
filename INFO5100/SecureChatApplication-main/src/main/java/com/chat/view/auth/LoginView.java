/*
package com.chat.view.auth;


import com.chat.controller.AuthController;
import com.chat.view.chat.ChatListView;
import com.chat.util.CurrentUserContext;  // Import CurrentUserContext
import com.chat.model.User;  // Import User model
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginView {
    private Stage stage;
    private AuthController authController;

    public LoginView(Stage stage, AuthController authController) {
        this.stage = stage;
        this.authController = authController;
    }

    public void show() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        // User ID field setup
        Label userIdLabel = new Label("User ID:");
        userIdLabel.setFont(Font.font("Arial", 11));
        TextField userIdField = new TextField();
        userIdField.setPrefWidth(200);
        userIdField.setMaxWidth(200);
        userIdField.setMinWidth(200);
        VBox userIdBox = new VBox(5);
        userIdBox.getChildren().addAll(userIdLabel, userIdField);
        grid.add(userIdBox, 0, 0);

        // Password field setup
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", 11));
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(200);
        passwordField.setMaxWidth(200);
        passwordField.setMinWidth(200);
        VBox passwordBox = new VBox(5);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        grid.add(passwordBox, 0, 1);

        // Sign-in button logic
        Button signInButton = new Button("Sign In");
        signInButton.setPrefWidth(200);
        signInButton.setStyle("-fx-background-color: #55AD9B; -fx-text-fill: white;");
        signInButton.setOnAction(e -> {
            String userId = userIdField.getText();
            String password = passwordField.getText();

            // Attempt to sign in the user through the AuthController
            boolean signInSuccess = authController.login(userId, password);

            if (signInSuccess) {
                // If sign-in is successful, get the logged-in user
                User loggedInUser = authController.getRegisteredUser();  // Assume a method to get user by ID

                // Store the logged-in user in CurrentUserContext
                CurrentUserContext.getInstance().setCurrentUser(loggedInUser);

                // Close the current login window and open the chat list window
                Stage currentStage = (Stage) signInButton.getScene().getWindow();
                currentStage.close();

                ChatListView chatListView = new ChatListView();
                Stage chatListStage = new Stage();
                try {
                    chatListView.start(chatListStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                // Show an error message if login fails
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sign In Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid User ID or Password.");
                alert.showAndWait();
            }
        });
        grid.add(signInButton, 0, 2);

        // Bottom buttons for Sign Up
        HBox bottomButtons = new HBox(10);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setMaxWidth(200);

        Button signUpButton = new Button("Sign Up");
        signUpButton.setPrefWidth(95);
        signUpButton.setStyle("-fx-background-color: #95D2B3; -fx-text-fill: white;");
        signUpButton.setOnAction(e -> {
            Stage registerStage = new Stage();
            RegisterView registerView = new RegisterView(registerStage, authController);
            registerView.show();
        });

        bottomButtons.getChildren().add(signUpButton);
        grid.add(bottomButtons, 0, 3);

        // Create scene and display the login window
        Scene scene = new Scene(grid, 300, 250);
        stage.setScene(scene);
        stage.setTitle("Sign in");
        stage.show();
    }
}
*/
package com.chat.view.auth;

import com.chat.controller.AuthController;
import com.chat.util.CurrentViewContext;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginView {
    private Stage stage;
    private AuthController authController;

    private TextField userIdField;  // for user ID input
    private PasswordField passwordField;  // for password input

    public LoginView(Stage stage, AuthController authController) {
        this.stage = stage;
        this.authController = authController;
    }

    public void show() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        // User ID field setup
        Label userIdLabel = new Label("User ID:");
        userIdLabel.setFont(Font.font("Arial", 11));
        userIdField = new TextField();  // Initialize the field
        userIdField.setPrefWidth(200);
        VBox userIdBox = new VBox(5);
        userIdBox.getChildren().addAll(userIdLabel, userIdField);
        grid.add(userIdBox, 0, 0);

        // Password field setup
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", 11));
        passwordField = new PasswordField();  // Initialize the field
        passwordField.setPrefWidth(200);
        VBox passwordBox = new VBox(5);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        grid.add(passwordBox, 0, 1);

        // Sign-in button logic (delegated to AuthController)
        Button signInButton = new Button("Sign In");
        signInButton.setPrefWidth(200);
        signInButton.setStyle("-fx-background-color: #55AD9B; -fx-text-fill: white;");
        signInButton.setOnAction(e -> {
            // Pass user input to the AuthController
            try {
                authController.handleSignIn(getUserId(), getPassword(), signInButton);
            } catch (Exception e1) {
                System.err.println("Authentication controller sign in error");
                e1.printStackTrace();
            }
        });
        grid.add(signInButton, 0, 2);

        // Bottom buttons for Sign Up
        HBox bottomButtons = new HBox(10);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setMaxWidth(200);

        Button signUpButton = new Button("Sign Up");
        signUpButton.setPrefWidth(95);
        signUpButton.setStyle("-fx-background-color: #95D2B3; -fx-text-fill: white;");
        signUpButton.setOnAction(e -> {
            Stage registerStage = new Stage();
            RegisterView registerView = new RegisterView(registerStage, authController);
            registerView.show();
        });

        bottomButtons.getChildren().add(signUpButton);
        grid.add(bottomButtons, 0, 3);

        // Create scene and display the login window
        Scene scene = new Scene(grid, 300, 250);
        stage.setScene(scene);
        stage.setTitle("Sign in");
        stage.show();
        CurrentViewContext.getInstance().setCurrentView(this);
    }

    // Getter methods for user input fields
    public String getUserId() {
        return userIdField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }
}
