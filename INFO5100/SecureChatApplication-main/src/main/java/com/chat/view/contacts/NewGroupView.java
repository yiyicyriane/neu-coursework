package com.chat.view.contacts;

import com.chat.util.ControllerManager;
import com.chat.controller.ContactController;
import com.chat.model.MembersInContactList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class NewGroupView {
    private ContactController contactController;
    private BiConsumer<String, List<String>> onGroupCreated; // Callback for group creation
    // private Stage stage;

    public NewGroupView() throws Exception {
        this.contactController = ControllerManager.getInstance().getContactController();
    }


    // show() method to display the New Group interface
    public void show(Stage primaryStage, BiConsumer<String, List<String>> onGroupCreated) {
        // stage = primaryStage;
        this.onGroupCreated = onGroupCreated;

        // Step 1: Create Group Name Input
        VBox root = new VBox(15);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);

        // Group Name Input Field
        Label groupNameLabel = new Label("Enter Group Name:");
        TextField groupNameField = new TextField();
        groupNameField.setPromptText("Enter your group name");

        // Create Group Button (Initially disabled)
        Button createButton = new Button("Create Group");
        createButton.setDisable(true);

        // Enable button if group name is not empty
        groupNameField.textProperty().addListener((obs, oldText, newText) -> {
            createButton.setDisable(newText.trim().isEmpty());
        });

        createButton.setOnAction(e -> {
            String groupName = groupNameField.getText().trim();

            // Step 2: Show contacts list to select members
            try {
                showContactListScene(primaryStage, groupName);
            } catch (Exception e1) {
                System.err.println("Show contact list error");
                e1.printStackTrace();
            }
        });

        root.getChildren().addAll(groupNameLabel, groupNameField, createButton);

        // Set initial scene
        Scene scene = new Scene(root, 350, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create New Group");
        primaryStage.show();
        // stage = primaryStage;
    }

    private void showContactListScene(Stage primaryStage, String groupName) throws Exception {
        // Step 3: Create a new scene to display contacts
        VBox contactsRoot = new VBox(15);
        contactsRoot.setPadding(new Insets(15));
        contactsRoot.setAlignment(Pos.CENTER);

        // Contact List (multiple selection)
        Label contactsLabel = new Label("Select Contacts to Invite:");


        // Fetch all contacts from the controller
        List<MembersInContactList> allContacts = contactController.getContacts();
        // Filter out only personal contacts (those with isGroupChatRoom == false)
        List<MembersInContactList> personalContacts = allContacts.stream()
                .filter(contact -> !contact.isGroupChatRoom())  // Only personal contacts
                .collect(Collectors.toList());
        // Prepare the contact list for display (showing both userId and name)
        ListView<String> contactsListView = new ListView<>();
        personalContacts.forEach(contact ->
                contactsListView.getItems().add(contact.getUserId())); //  + " - " + contact.getName()));
        // Set up the selection mode for multiple selections
        contactsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        // Invite Friends Button
        Button inviteButton = new Button("Invite Friends");
        inviteButton.setOnAction(e -> {
            List<String> selectedContacts = contactsListView.getSelectionModel().getSelectedItems();
            if (!selectedContacts.isEmpty()) {
                // Notify success and close the window
                if (onGroupCreated != null) {
                    onGroupCreated.accept(groupName, selectedContacts);
                }
                int groupMemberNum = selectedContacts.size() + 1;
                showAlert("Group " + groupName + "Created", "Group successfully created with " + groupMemberNum + " members.");
                primaryStage.close();
            }
        });

        contactsRoot.getChildren().addAll(contactsLabel, contactsListView, inviteButton);

        // Set the new scene
        Scene contactsScene = new Scene(contactsRoot, 400, 300);
        primaryStage.setScene(contactsScene);
    }

    // Show alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
