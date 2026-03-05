package com.chat.controller;

import java.util.Set;

import com.chat.model.User;
import com.chat.model.UserServer;
import com.chat.service.AuthService;
import com.chat.util.HashUtil;

import lombok.AllArgsConstructor;
import lombok.Data;

//yiyi add
import com.chat.view.chat.ChatListView;
import com.chat.util.CurrentUserContext;
import com.chat.util.FriendApplicationNotice;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

@Data
@AllArgsConstructor 
public class AuthController {
    private User registeredUser;
    private UserServer registeredUsersServer;
    private final AuthService authService;
    private String salt;

    // Constructor
    public AuthController() throws Exception {
        authService = new AuthService();
    }

    //handle user log in
    public void handleSignIn(String userId, String password, Button signInButton) throws Exception {
        boolean signInSuccess = login(userId, password);

        if (signInSuccess) {
            // If sign-in is successful, get the logged-in user
            User loggedInUser = registeredUser;  // get user by ID

            // Store the logged-in user in CurrentUserContext
            CurrentUserContext.getInstance().setCurrentUser(loggedInUser);
            CurrentUserContext.getInstance().setCurrentUserServer(registeredUsersServer);

            // Close the current login window and open the chat list window
            Stage currentStage = (Stage) signInButton.getScene().getWindow();
            currentStage.close();


            //navigate to chatlistview page
            ChatListView chatListView = new ChatListView();
            Stage chatListStage = new Stage();
            try {
                chatListView.start(chatListStage);
                Set<String> friendApplicationSenderIdSet = authService.getFriendApplicationSenderIdSet(userId);
                for (String friendId: friendApplicationSenderIdSet) {
                    if (FriendApplicationNotice.notice(friendId)) {
                        authService.postNewFriendApplicationSenderIdSet(userId, friendId);        
                    }
                }
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
    }

    //handle user registration
    public void registerUser(String userId, String name, String password, String profilePicture) {
        User newUser = new User(userId, name, password, profilePicture);
        boolean registrationSuccess = register(newUser);

        if (registrationSuccess) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Successful");
            alert.setHeaderText("You have successfully registered!");
            ButtonType okButton = new ButtonType("OK");
            alert.getButtonTypes().setAll(okButton);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Failed");
            alert.setHeaderText("User ID already exists!");
            ButtonType okButtonFail = new ButtonType("OK");
            alert.getButtonTypes().setAll(okButtonFail);
            alert.showAndWait();
        }
    }

    /**
     * Register a new user
     *
     * @param user The new user to register
     * @return Whether the registration was successful
     */
    public boolean register(User user) {
        // TODO: get information from view
        registeredUser = user;
        // generate a new key pair
        try {
            salt = HashUtil.generateSalt();
            String password = registeredUser.getPassword();
            String encryptPassword = HashUtil.passwordHash(password, salt);
            registeredUsersServer = new UserServer(registeredUser.getUserId(), registeredUser.getName(), encryptPassword, salt, registeredUser.getProfilePicture());
            String response = authService.register(registeredUsersServer);
            if (response.equals("User registered."))
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sign in a user
     *
     * @param userId   User ID
     * @param password User password
     * @return Whether the sign-in was successful
     */
    public boolean login(String userId, String password) {
        try {
            salt = authService.getPublicKey(userId);
            String encryptPassword = HashUtil.passwordHash(password, salt);
            registeredUsersServer = authService.login(userId, encryptPassword);
            if (registeredUsersServer == null) return false;

            // update public key and encrypted password
            salt = HashUtil.generateSalt();
            String newPublicKey = salt;
            String newEncryptPassword = HashUtil.passwordHash(password, salt);
            authService.putPassword(userId, encryptPassword, newEncryptPassword, newPublicKey);
            
            registeredUsersServer.setPassword(newEncryptPassword);
            registeredUsersServer.setPublicKey(newPublicKey);
            registeredUser = new User(userId, registeredUsersServer.getUsername(), registeredUsersServer.getPassword(), registeredUsersServer.getProfile());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get a user by their user ID
     *
     * @param userId The ID of the user to retrieve
     * @return The User object associated with the provided user ID, or null if no user is found
     */
    // public User getUserById(String userId) {
    //     return registeredUser.get(userId); // Return the user associated with the given user ID
    // }
}
