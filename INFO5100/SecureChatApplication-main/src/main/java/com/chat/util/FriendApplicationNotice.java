package com.chat.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class FriendApplicationNotice {
    public static boolean notice (String friendId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("New Friend Application");
        alert.setHeaderText(null);
        alert.setContentText("Do you accept the friend application from the user ID: " + friendId);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) 
            return true;         
        else
            return false;
    }
}
