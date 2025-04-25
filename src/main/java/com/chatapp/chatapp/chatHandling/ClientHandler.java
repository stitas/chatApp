package com.chatapp.chatapp.chatHandling;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

public class ClientHandler implements Runnable{
    public static List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList <>());
    private Socket socket;
    private Server server;
    private BufferedReader socketStreamBufferedReader;
    private BufferedWriter socketStreamBufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.server = server;
            this.socketStreamBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.socketStreamBufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUsername = socketStreamBufferedReader.readLine();
            clientHandlers.add(this);
        }
        catch (IOException e) {
            closeEverything(socket, socketStreamBufferedReader, socketStreamBufferedWriter);
        }
    }

    // Everything here is run on a seperate thread
    @Override
    public void run() {
        String messageFromClient;
        broadcastMessage("SERVER: " + clientUsername + " has joined the chat");

        while(socket.isConnected()) {
            try {
                // Wait for client to enter message
                messageFromClient = socketStreamBufferedReader.readLine();
                broadcastMessage(messageFromClient);
            }
            catch (IOException | NullPointerException | ConcurrentModificationException e) {
                closeEverything(socket, socketStreamBufferedReader, socketStreamBufferedWriter);
                break;
            }
        }
    }

    // Send client message to each client other
    private void broadcastMessage(String message) {
        String targetUsername = "";
        String privateMessage = "";
        boolean isPrivate = false;

        // Check if direct message
        String[] parts = message.split(": ", 2);
        if(parts[1].startsWith("@")){
            String[] usernameMessageParts = parts[1].split(" ", 2);
            // Remove @
            targetUsername = usernameMessageParts[0].substring(1);
            privateMessage = usernameMessageParts[1];
            isPrivate = true;
        }

        for(ClientHandler clientHandler : clientHandlers) {
            try {
                if(isPrivate && (targetUsername.equals(clientHandler.clientUsername) || clientHandler.clientUsername.equals(this.clientUsername))) {
                    clientHandler.socketStreamBufferedWriter.write("(PRIVATE MESSAGE) " + this.clientUsername + ": " + privateMessage);
                    clientHandler.socketStreamBufferedWriter.newLine();
                    clientHandler.socketStreamBufferedWriter.flush();
                }
                else if(!isPrivate){
                    clientHandler.socketStreamBufferedWriter.write(message);
                    clientHandler.socketStreamBufferedWriter.newLine();
                    clientHandler.socketStreamBufferedWriter.flush();
                }
            }
            catch (IOException | NullPointerException | ConcurrentModificationException e) {
                closeEverything(socket, socketStreamBufferedReader, socketStreamBufferedWriter);
            }
        }
    }

    private void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat");
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null) {
                socket.close();
            }
        }
        catch (IOException e) {
            System.out.println("IOEXCEPTION clientHandler closeEveything");
        }

        server.removeClient(this);
    }
}
