package com.chatapp.chatapp.managers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonManager {
    private static JsonManager instance;

    private JsonManager() {}

    public static JsonManager getInstance() {
        if(instance == null) {
            instance = new JsonManager();
        }

        return instance;
    }

    public void writeToJson(String path, ObjectNode jsonObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(path);

        ArrayNode userList;

        if(!file.exists())  {
            try {
                file.createNewFile();
                userList = objectMapper.createArrayNode();
            }
            catch (IOException e) {
                System.out.println("Failed to create new file ");
                return;
            }
        }
        else {
            // Read existing list
            JsonNode root;
            try {
                root = objectMapper.readTree(file);
            }
            catch (IOException e) {
                System.out.println("Failed to read tree");
                return;
            }

            if (root != null && root.isArray()) {
                userList = (ArrayNode) root;
            }
            else {
                userList = objectMapper.createArrayNode(); // fallback
            }
        }

        try {
            jsonObject.put("createdAt", String.valueOf(LocalDateTime.now()));
            userList.add(jsonObject);

            // Write back to file
            objectMapper.writer().writeValue(file, userList);
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public boolean doesUsernameExist(String path, String key, String value) {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(path);

        // Read existing list
        JsonNode root;
        try {
            root = objectMapper.readTree(file);
        }
        catch (IOException e) {
            System.out.println("Failed to read tree");
            return false;
        }

        for(JsonNode node : root) {
            if (node.has(key) && node.get(key).asText().equals(value)) {
                return true;
            }
        }

        return false;
    }

    public List<Integer> getRoomPorts() {
        List<Integer> ports = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        String path = Paths.get("src", "main", "java", "com", "chatapp", "chatapp", "data", "rooms.json").toString();

        File file = new File(path);

        // Read existing list
        JsonNode root;
        try {
            root = objectMapper.readTree(file);
        }
        catch (IOException e) {
            System.out.println("Failed to read tree");
            return ports;
        }

        for(JsonNode node : root) {
            if (node.has("pinCode") ) {
                ports.add(node.get("pinCode").asInt());
            }
        }

        return ports;
    }
}
