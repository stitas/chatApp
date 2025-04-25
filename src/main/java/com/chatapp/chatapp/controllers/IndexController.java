package com.chatapp.chatapp.controllers;

import com.chatapp.chatapp.managers.JsonManager;
import com.chatapp.chatapp.managers.RoomManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class IndexController {
    @FXML
    private Button createRoomBtn, joinRoomBtn, dmBtn;

    @FXML
    private Text errorText;

    RoomManager roomManager;

    public IndexController() {
        roomManager = RoomManager.getInstance();
    }

    public void onCreateRoomBtnClick() {
        int roomPort = 0;

        // Create new server
        try {
            roomPort = roomManager.createRoom();
        }
        catch (IOException e) {
            errorText.setText("Error occured. Unable to create room !");
            errorText.setVisible(true);
        }

        // Switch screen
        try {
            loadChatRoomScreen(roomPort);
        }
        catch (IOException e){
            errorText.setText("Error occured");
            errorText.setVisible(true);
        }

    }

    public void onJoinRoomBtnClick() {
        List<Integer> availablePorts = JsonManager.getInstance().getRoomPorts();

        // Show dialog to enter room pin code
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter group pin code");
        dialog.setHeaderText("Enter group pin code");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(res -> {
            try {
                // Parse pin code
                int pinCode = Integer.parseInt(res);

                // Check if pin code exists
                if(availablePorts.contains(pinCode)){
                    // Switch screen
                    try {
                        loadChatRoomScreen(pinCode);
                    }
                    catch (IOException e){
                        errorText.setText("Error occured");
                        errorText.setVisible(true);
                    }
                }
                else {
                    errorText.setText("Room with pin code " + pinCode + " does not exist");
                    errorText.setVisible(true);
                }
            }
            catch (NumberFormatException e){
                errorText.setText("Please enter a valid pin code");
                errorText.setVisible(true);
            }
        });
    }

    private void loadChatRoomScreen(int roomPort) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chatApp/chatApp/chatRoom.fxml"));
        Parent attendanceScene = loader.load();

        ChatRoomController controller = loader.getController();
        controller.initData(roomPort);

        // Get current stage
        Stage currentStage = (Stage) createRoomBtn.getScene().getWindow();

        currentStage.setScene(new Scene(attendanceScene));
    }
}

