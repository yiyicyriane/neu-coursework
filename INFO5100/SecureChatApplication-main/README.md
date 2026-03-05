# SecureChatApplication

A secure, real-time desktop messaging application built with JavaFX and Spring WebSocket. This project was developed as the final project for INFO 5100.

## 🚀 Features

- **Secure Authentication**: User registration and login system with password hashing (SHA-256) and dynamic salting to ensure credential security.
- **Real-time Messaging**: Full-duplex communication using WebSocket and STOMP protocol for instantaneous message delivery.
- **Contact & Group Management**: 
  - Search and add friends via User ID.
  - Real-time friend request notifications.
  - Create and manage group chats with multiple members.
- **Rich User Interface**:
  - Modern desktop UI built with JavaFX.
  - Interactive chat windows with message history.
  - Profile customization including circular avatar cropping and updates.
- **Message Control**: Users can delete their own sent messages, which synchronizes across the server.

## 🛠️ Technology Stack

- **Java**: Version 20
- **Framework**: JavaFX 23 (UI), Spring Framework 6.2 (Messaging/WebSocket)
- **Networking**: Java HttpClient (REST), STOMP over WebSocket
- **Security**: Apache Commons Codec (SHA-256 Hashing)
- **Data Handling**: Jackson (JSON processing)
- **Utilities**: Project Lombok, Maven

## 📂 Project Structure

- `com.chat.view`: JavaFX UI components (Auth, Chat, Contacts, Settings).
- `com.chat.controller`: Handles UI logic and coordinates between services and views.
- `com.chat.service`: Communicates with the backend server via REST and WebSocket.
- `com.chat.model`: Data entities and transfer objects.
- `com.chat.util`: Helper classes for hashing, image processing, and context management.

## ⚙️ Setup & Installation

1. **Prerequisites**: Ensure you have JDK 20 and Maven installed.
2. **Configuration**: The server URL is configured in `src/main/resources/serverUrl.csv`.
3. **Build**:
   ```bash
   mvn clean package
4. **Run**:
   ```bash
   mvn javafx:run

## Security Implementation
This application prioritizes data integrity. All passwords are encrypted on the client-side before transmission using SHA-256 combined with a unique salt retrieved from the server during the login handshake.
