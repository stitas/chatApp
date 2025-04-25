package com.chatapp.chatapp.managers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class UsernameManager {
    private static UsernameManager instance;
    private String username;

    private UsernameManager() {}

    public static UsernameManager getInstance() {
        if(instance == null) {
            instance = new UsernameManager();
        }

        return instance;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;

        String path = Paths.get("src", "main", "java", "com", "chatapp", "chatapp", "data", "users.json").toString();

        if(!JsonManager.getInstance().doesUsernameExist(path, "username", username)){
            // Create json object with username
            ObjectNode jsonObject = JsonNodeFactory.instance.objectNode().put("username", username);

            // Write json object to file
            JsonManager.getInstance().writeToJson(path, jsonObject);
        }

    }
}
