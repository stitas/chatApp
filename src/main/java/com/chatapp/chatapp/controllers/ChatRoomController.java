package com.chatapp.chatapp.controllers;

import com.chatapp.chatapp.chatHandling.Client;
import com.chatapp.chatapp.managers.UsernameManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class ChatRoomController {
    @FXML
    private Button exitBtn, sendBtn;

    @FXML
    private Text pinCodeText;

    @FXML
    private TextArea messageArea;

    @FXML
    private TextField messageInputField;

    private Client client;

    public void initData(int pinCode) {
        pinCodeText.textProperty().setValue(String.valueOf(pinCode));

        try {
            Socket socket = new Socket("localhost", pinCode);
            client = new Client(socket, UsernameManager.getInstance().getUsername());
        }
        catch (IOException e) {
            messageArea.textProperty().setValue("Error has occured");
            return;
        }

        // On message received display it in text area
        client.listenForMessage(message -> {
            Platform.runLater(() -> {
                messageArea.appendText(message + "\n");
            });
        });

        // On send btn click
        sendBtn.setOnAction(e -> {
            String msg = messageInputField.getText();
            client.sendMessage(msg);
            messageInputField.clear();
        });
    }

    public void onExitBtnClick() {
        // Close socket
        client.closeEverything();

        // Load index screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chatApp/chatApp/index.fxml"));
            Parent attendanceScene = loader.load();

            // Get current stage
            Stage currentStage = (Stage) exitBtn.getScene().getWindow();

            currentStage.setScene(new Scene(attendanceScene));
        }
        catch (IOException e) {
            messageArea.textProperty().setValue("Error has occured");
        }
    }
}
