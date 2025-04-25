package com.chatapp.chatapp.chatHandling;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    private final ServerSocket serverSocket;
    private final List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());


    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while(!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(socket, this);
                clientHandlers.add(clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }
        catch (IOException e){
            System.out.println("IOEXCEPTION in server start");
        }
    }

    public void closeServer() {
        try {
            if(serverSocket != null){
                int pinCode = serverSocket.getLocalPort();
                serverSocket.close();

                // Delete room from rooms.json

                ObjectMapper objectMapper = new ObjectMapper();
                String path = Paths.get("src", "main", "java", "com", "chatapp", "chatapp", "data", "rooms.json").toString();
                File file = new File(path);

                try {
                    if (!file.exists()) {
                        System.out.println("Rooms.json does not exist.");
                        return;
                    }

                    JsonNode root = objectMapper.readTree(file);

                    if (root != null && root.isArray()) {
                        ArrayNode userList = (ArrayNode) root;

                        // Create a copy to modify safely while iterating
                        ArrayNode updatedList = objectMapper.createArrayNode();

                        for (JsonNode user : userList) {
                            // Only keep users NOT matching the one to delete
                            if (!user.has("pinCode") || !(user.get("pinCode").asInt() == pinCode)) {
                                updatedList.add(user);
                            }
                        }

                        // Overwrite file with updated list
                        objectMapper.writer().writeValue(file, updatedList);
                    }

                } catch (IOException e) {
                    System.out.println("Failed to delete room from file.");
                }
            }
        }
        catch (IOException e) {
            System.out.println("IOEXCEPTION in server close");
        }
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);


        // Close server if 0 clients
        if (clientHandlers.isEmpty()) {
            closeServer();
        }
    }

//    public static void main(String[] args) throws IOException {
//        ServerSocket serverSocket = new ServerSocket(1234);
//        Server server = new Server(serverSocket);
//        server.startServer();
//    }

}
