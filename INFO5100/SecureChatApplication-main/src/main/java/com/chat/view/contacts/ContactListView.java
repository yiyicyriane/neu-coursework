package com.chat.view.contacts;

import com.chat.controller.ContactController;
import com.chat.model.MembersInContactList;
import com.chat.util.ControllerManager;
import com.chat.util.CurrentChatWindowViewContext;
import com.chat.util.CurrentViewContext;
import com.chat.view.chat.ChatListView;
import com.chat.view.chat.ChatWindowView;
import com.chat.view.settings.ProfileSettingsView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class ContactListView extends Application {

    private ContactController contactController;
    private HBox contactsContainer; // Contacts container to update dynamically
    private Stage stage;

    public ContactListView() throws Exception{
        this.contactController = ControllerManager.getInstance().getContactController();
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize layout
        VBox root = new VBox();
        root.setFillWidth(true);

        VBox mainContent = new VBox();
        mainContent.setFillWidth(true);
        mainContent.setPadding(new Insets(10));
        mainContent.setSpacing(20);

        // New chat section
        VBox newChatSection = createNewChatSection(primaryStage);

        // Contacts list section
        contactsContainer = new HBox();
        contactsContainer.setFillHeight(true);

        VBox personalContactsSection = createContactsSection("Personal Contacts");
        personalContactsSection.setPrefWidth(285);
        VBox groupContactsSection = createContactsSection("Group Contacts");
        groupContactsSection.setPrefWidth(285);

        contactsContainer.setSpacing(20);
        contactsContainer.getChildren().addAll(personalContactsSection, groupContactsSection);

        mainContent.getChildren().addAll(newChatSection, contactsContainer);

        // Bottom navigation bar
        HBox bottomBar = createBottomBar(primaryStage);

        VBox.setVgrow(mainContent, Priority.ALWAYS);
        root.getChildren().addAll(mainContent, bottomBar);

        // Set the scene and display the window
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Contact List");
        primaryStage.setScene(scene);
        primaryStage.show();
        stage = primaryStage;

        // Initial update to display contacts
        updateContacts();

        CurrentViewContext.getInstance().setCurrentView(this);
    }

    public void updateContactsListView() {
        start(stage);
    }

    // Create new chat section
    private VBox createNewChatSection(Stage primaryStage) {
        VBox newChatSection = new VBox();
        newChatSection.setStyle("-fx-background-color: #95D2B3; -fx-padding: 10;");

        Text newChatText = new Text("New Chat");
        newChatText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button addContactButton = new Button("Add Contact");
        Button joinGroupButton = new Button("Join Group");
        Button createNewGroupButton = new Button("Create New Group");

        // Set button width
        addContactButton.setMinWidth(150);
        joinGroupButton.setMinWidth(150);
        createNewGroupButton.setMinWidth(150);

        newChatSection.setAlignment(Pos.CENTER);
        newChatSection.setSpacing(15);
        newChatSection.getChildren().addAll(newChatText, addContactButton, joinGroupButton, createNewGroupButton);

        // Set actions for buttons
        addContactButton.setOnAction(e -> {
            Stage addContactStage = new Stage();
            AddContactView addContactView = new AddContactView();
            addContactView.show(addContactStage, false, newContact -> {
                String response = contactController.addContact(contactController.currentUserId(), newContact);
                System.out.println(response);
                Alert alert;
                if (response.equals("Friend application sent.")) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Message Sent");
                    alert.setHeaderText(null);
                }
                else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message Sent Failed");
                    alert.setHeaderText(null);
                }
                alert.setContentText(response);
                alert.showAndWait();
            });
        });

        joinGroupButton.setOnAction(e -> {
            Stage joinGroupStage = new Stage();
            AddContactView joinGroupView = new AddContactView();
            joinGroupView.show(joinGroupStage, true, newContact -> {
                System.out.println("New group contact added: " + newContact);
                try {
                    contactController.joinGroupContact(contactController.currentUserId(), newContact);
                } catch (Exception e1) {
                    System.err.println("join group error");
                    e1.printStackTrace();
                }
                updateContacts();  // Refresh the contact list
            });
        });

        createNewGroupButton.setOnAction(e -> {
            Stage newGroupStage = new Stage();
            NewGroupView newGroupView = null;
            try {
                newGroupView = new NewGroupView();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            newGroupView.show(newGroupStage, (groupName, selectedMembers) -> {
                System.out.println("New group created: " + groupName);
                System.out.println("Selected members: " + selectedMembers);

                //后端创建，并跳转到新群聊界面
                String newGroupId;
                try {
                    newGroupId = contactController.createNewGroup(contactController.currentUserId(), groupName, selectedMembers);
                    openChatWindowView(newGroupId);
                } catch (Exception e1) {
                    System.err.println("new group error");
                    e1.printStackTrace();
                }
                updateContacts();  // Refresh the contact list
            });
        });

        return newChatSection;
    }

    // Create contacts section (Personal or Group)
    private VBox createContactsSection(String sectionName) {
        VBox contactsSection = new VBox();
        contactsSection.setStyle("-fx-padding: 10; -fx-background-color: #95D2B3;");

        Text sectionTitle = new Text(sectionName);
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox contactsList = new VBox(10);
        //contactsSection.getChildren().addAll(sectionTitle, contactsList);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(contactsList);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPrefHeight(200);

        contactsSection.setAlignment(Pos.CENTER);
        contactsSection.setSpacing(20);
        contactsSection.getChildren().addAll(sectionTitle, scrollPane);


        // Populate contacts list (initially empty)
        updateContactsList(contactsList, sectionName);

        return contactsSection;
    }

    // Update the contacts list section dynamically
    private void updateContactsList(VBox contactsList, String sectionName) {
        contactsList.getChildren().clear();  // Clear the current list

        // Fetch updated contacts from the controller
        List<MembersInContactList> contacts;
        try {
            contacts = contactController.getContacts();
            if (sectionName.equals("Personal Contacts")) {
                for (MembersInContactList contact : contacts) {
                    if (!contact.isGroupChatRoom()) 
                        contactsList.getChildren().add(createPersonalContactItem(contact));
                }
            }
            else {
                for (MembersInContactList contact : contacts) {
                    if (contact.isGroupChatRoom()) 
                        contactsList.getChildren().add(createGroupContactItem(contact));
                }
            }
        } catch (Exception e) {
            System.err.println("Update contact error");
            e.printStackTrace();
        }
        
    }

    // Update method to refresh the contacts container
    private void updateContacts() {
        // Update each section (personal and group contacts)
        VBox personalSection = (VBox) contactsContainer.getChildren().get(0);
        VBox groupSection = (VBox) contactsContainer.getChildren().get(1);

        // Get the ScrollPane inside the personal and group sections
        ScrollPane personalScrollPane = (ScrollPane) personalSection.getChildren().get(1);
        ScrollPane groupScrollPane = (ScrollPane) groupSection.getChildren().get(1);

        //get content in ScrollPane, and make sure it's VBox
        VBox personalContactsList = (VBox) personalScrollPane.getContent();
        VBox groupContactsList = (VBox) groupScrollPane.getContent();

        //call to update the contact list
        updateContactsList(personalContactsList, "Personal Contacts");
        updateContactsList(groupContactsList, "Group Contacts");

        //updateContactsList((VBox) personalSection.getChildren().get(1), "Personal Contacts");
        //updateContactsList((VBox) groupSection.getChildren().get(1), "Group Contacts");
    }

    // Create personal contact item
    private HBox createPersonalContactItem(MembersInContactList contact) {
        HBox contactItem = new HBox();
        contactItem.setAlignment(Pos.CENTER_LEFT);
        contactItem.setSpacing(10);
        contactItem.setPadding(new Insets(5, 10, 5, 10));

        Text contactName = new Text(contact.getName() + " - " + contact.getUserId());
        contactName.setStyle("-fx-font-size: 16px;");

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #95D2B3; -fx-text-fill: white;");
        deleteButton.setMinWidth(60);
        deleteButton.setVisible(false);

        contactItem.setOnMouseEntered(e -> deleteButton.setVisible(true));
        contactItem.setOnMouseExited(e -> deleteButton.setVisible(false));

        deleteButton.setOnAction(e -> {
            System.out.println("Delete button clicked for contact: " + contact.getName());
            try {
                contactController.removeContact(contactController.currentUserId(), contact.getUserId());
            } catch (Exception e1) {
                System.err.println("Remove contact error");
                e1.printStackTrace();
            }
            updateContacts();  // Refresh the contact list
        });

        //open the chat window with this friend
        contactItem.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                try {
                    String chatRoomId = contactController.existsChatRoomWith(contact.getUserId());
                    contact.setChatRoomId(chatRoomId);
                    if (chatRoomId.isEmpty()) 
                        openChatWindowView(contactController.createNewIndividualChatRoom(contact.getUserId()));
                    else
                        openChatWindowView(chatRoomId);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        contactItem.getChildren().addAll(contactName, deleteButton);
        return contactItem;
    }

    private Label makeSelectable(Label label) {
        StackPane textStack = new StackPane();
        TextField textField = new TextField(label.getText());
        textField.setEditable(false);
        textField.setStyle(
            "-fx-background-color: transparent; -fx-background-insets: 0; -fx-background-radius: 0; -fx-padding: 0;"
        );
    
        // Make sure the TextField can grow horizontally
        textField.setMaxWidth(Double.MAX_VALUE);  // Allow the TextField to expand horizontally
        textField.setPrefWidth(Region.USE_COMPUTED_SIZE);  // Compute size based on content
    
        // The invisible label is a hack to get the TextField to size like a label
        Label invisibleLabel = new Label();  
        invisibleLabel.textProperty().bind(label.textProperty());
        invisibleLabel.setVisible(false);
    
        textStack.getChildren().addAll(invisibleLabel, textField);
    
        // Bind the label's text to the TextField
        label.textProperty().bindBidirectional(textField.textProperty());
    
        // Set the label's graphic to the text stack
        label.setGraphic(textStack);
        label.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    
        return label;
    }
    

    // Create group contact item
    private HBox createGroupContactItem(MembersInContactList contact) {
        HBox contactItem = new HBox();
        contactItem.setAlignment(Pos.CENTER_LEFT);
        contactItem.setSpacing(10);
        contactItem.setPadding(new Insets(5, 10, 5, 10));

        // Text groupName = new Text(contact.getChatRoomId() + ": " + contact.getName());
        // groupName.setStyle("-fx-font-size: 16px;");
        Label groupName = new Label(contact.getName() + " - " + contact.getChatRoomId());
        groupName.setStyle("-fx-font-size: 16px;");
        makeSelectable(groupName);

        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-background-color: #95D2B3; -fx-text-fill: white;");
        exitButton.setMinWidth(60);
        exitButton.setVisible(false);

        contactItem.setOnMouseEntered(e -> exitButton.setVisible(true));
        contactItem.setOnMouseExited(e -> exitButton.setVisible(false));

        exitButton.setOnAction(e -> {
            System.out.println("Exit button clicked for group: " + contact.getName());
            try {
                contactController.removeGroupContact(contactController.currentUserId(), contact.getChatRoomId());
            } catch (Exception e1) {
                System.err.println("Exit group error");
                e1.printStackTrace();
            }
            updateContacts();  // Refresh the contact list
        });

        contactItem.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                try {
                    openChatWindowView(contact.getChatRoomId());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        contactItem.getChildren().addAll(groupName, exitButton);
        return contactItem;
    }

    // Open ChatWindowView for contact or group
    private void openChatWindowView(String chatRoomId) throws Exception {
        System.out.println("Opening chat window for: " + chatRoomId);
        ChatWindowView chatWindowView = new ChatWindowView(chatRoomId);
        CurrentChatWindowViewContext.getInstance().setChatWindowView(chatWindowView);
        // Stage chatWindowStage = new Stage();
        // chatWindowView.start(chatWindowStage);
    }

    // Create bottom bar navigation
    private HBox createBottomBar(Stage stage) {
        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setSpacing(30);
        bottomBar.setStyle("-fx-background-color: #55AD9B; -fx-padding: 10;");

        Button chatsButton = new Button("Chats");
        Button contactsButton = new Button("Contacts");
        Button settingsButton = new Button("Settings");

        // Navigate to chats view
        chatsButton.setOnAction(e -> {
            System.out.println("Navigate to Chats view");
            ChatListView chatListView = null;
            try {
                chatListView = new ChatListView();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            try {
                chatListView.start(stage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Navigate to contacts view
        contactsButton.setOnAction(e -> {
            System.out.println("Navigate to Contacts view");
            updateContacts();
        });

        // Navigate to settings view
        settingsButton.setOnAction(e -> {
            System.out.println("Navigate to Settings view");
            ProfileSettingsView settingsView = null;
            try {
                settingsView = new ProfileSettingsView();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            settingsView.start(stage);
        });

        bottomBar.getChildren().addAll(chatsButton, contactsButton, settingsButton);
        return bottomBar;
    }
}
