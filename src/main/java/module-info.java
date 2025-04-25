module com.chatapp.chatapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.chatapp.chatapp to javafx.fxml;
    exports com.chatapp.chatapp;

    opens com.chatapp.chatapp.controllers to javafx.fxml;
    exports com.chatapp.chatapp.controllers;
}