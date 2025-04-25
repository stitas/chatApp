package com.chatapp.chatapp.controllers;

import com.chatapp.chatapp.managers.UsernameManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class startController {
    @FXML
    private TextField nameField;

    @FXML
    private Button enterBtn;

    @FXML
    private Text errorText;

    public void onEnterBtnClick() {
        String username = nameField.textProperty().getValue();

        if(username == null){
            errorText.setText("Please enter your name !");
            errorText.setVisible(true);
            return;
        }

        // Set username of user
        UsernameManager.getInstance().setUsername(username);

        // Switch to index screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chatApp/chatApp/index.fxml"));
            Parent attendanceScene = loader.load();

            // Get current stage
            Stage currentStage = (Stage) enterBtn.getScene().getWindow();

            currentStage.setScene(new Scene(attendanceScene));
        }
        catch (IOException e){
            errorText.setText("Error occured");
            errorText.setVisible(true);
        }

    }
}
