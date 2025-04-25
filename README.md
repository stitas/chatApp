# CHAT APP

## 🛠️ Features

### ✅ 1. Client-Server Communication
The system enables clients to exchange messages via a server. The server uses `ServerSocket`, and clients use `Socket` to ensure reliable TCP communication.

### 🏠 2. Room Creation
Users can create chat rooms, which other users can join.

### 🗣️ 3. Group Messaging in Rooms
Users can send messages to all other users in the same room, enabling group chats.

### 🧑‍🤝‍🧑 4. Private Messaging
Users can also send private messages to individual users.

### 💾 5. Data Storage in Files
All data — messages, rooms, and users — are saved to a file

### 🧵 6. Multithreading Support
Each user is handled in a separate thread, allowing the server to serve multiple clients simultaneously. Java `Thread` class is used for this purpose.

## 🎨 Graphical User Interface (GUI)
A user-friendly interface is recommended, built using **JavaFX** to enhance the user experience.

---

## 🖥️ Technologies Used
- Java
- ServerSocket / Socket
- Threads (Multithreading)
- JavaFX (GUI)
- File-based data storage (`json`)

---

![](https://raw.githubusercontent.com/stitas/chatApp/refs/heads/master/preview_images/image0.png)
![](https://raw.githubusercontent.com/stitas/chatApp/refs/heads/master/preview_images/image1.png)
![](https://raw.githubusercontent.com/stitas/chatApp/refs/heads/master/preview_images/image2.png)
