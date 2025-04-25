package com.chatapp.chatapp.managers;

import com.chatapp.chatapp.chatHandling.Server;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoomManager {
    private static RoomManager instance;
    private final List<Server> rooms;

    private RoomManager() {
        rooms = new ArrayList<>();
    }

    public static RoomManager getInstance() {
        if(instance == null){
            instance = new RoomManager();
        }

        return instance;
    }

    // Returns created room port
    public int createRoom() throws IOException {
        ServerSocket serverSocket = new ServerSocket(0);
        Server server = new Server(serverSocket);

        // Start server on new thread to not freeze the program
        new Thread(server::startServer).start();
        rooms.add(server);

        int port = server.getPort();

        String path = Paths.get("src", "main", "java", "com", "chatapp", "chatapp", "data", "rooms.json").toString();

        // Create json object with username
        ObjectNode jsonObject = JsonNodeFactory.instance.objectNode().put("pinCode", port);

        // Write json object to file
        JsonManager.getInstance().writeToJson(path, jsonObject);

        return port;
    }
}
