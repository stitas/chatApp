package com.chatapp.chatapp.chatHandling;

import com.chatapp.chatapp.managers.JsonManager;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.function.Consumer;

public class Client {
    private Socket socket;
    private BufferedReader socketStreamBufferedReader;
    private BufferedWriter socketStreambufferedWriter;
    private String username;
    private boolean running;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.socketStreamBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.socketStreambufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
            this.running = true;

            // Send username to clientHandler
            socketStreambufferedWriter.write(this.username);
            socketStreambufferedWriter.newLine();
            socketStreambufferedWriter.flush();
        }
        catch (IOException e) {
            closeEverything();
        }
    }

    public void sendMessage(String message) {
        try {
            if(socket.isConnected() && !message.isEmpty()) {
                socketStreambufferedWriter.write(username + ": " + message);
                socketStreambufferedWriter.newLine();
                socketStreambufferedWriter.flush();

                // Create json object
                ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();
                jsonObject.put("username", username);
                jsonObject.put("message", message);

                String path = Paths.get("src", "main", "java", "com", "chatapp", "chatapp", "data", "messages", "room" + socket.getPort() + ".json").toString();

                // Write json object to file
                JsonManager.getInstance().writeToJson(path, jsonObject);
            }
        }
        catch (IOException e){
            closeEverything();
        }
    }

    public void listenForMessage(Consumer<String> onMessageReceived) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromChat;

                while(running && socket.isConnected()) {
                    try {
                        messageFromChat = socketStreamBufferedReader.readLine();

                        // If stream closed break
                        if (messageFromChat == null) {
                            break;
                        }

                        onMessageReceived.accept(messageFromChat);
                    }
                    catch (IOException e) {
                        closeEverything();
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything() {
        running = false;

        try {
            if(socket != null) {
                socket.close();
            }

            if(socketStreamBufferedReader != null) {
                socketStreamBufferedReader.close();
            }

            if(socketStreambufferedWriter != null) {
                socketStreambufferedWriter.close();
            }
        }
        catch (IOException e) {
            System.out.println("IOEXCEPTION clientHandler close everything");
        }
    }

//    public static void main(String[] args) throws IOException {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter your username: ");
//        String username = scanner.nextLine();
//
//        Socket socket = new Socket("localhost", 1234);
//        Client client = new Client(socket, username);
//        client.listenForMessage();
//        client.sendMessage();
//    }

}
