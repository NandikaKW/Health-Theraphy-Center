package lk.ijse.gdse.HealthTheraphyCenter.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;

public class AdminLoginController {

    @FXML
    private PasswordField Code;

    @FXML
    private TextField Name;
    private final String UserName = "Nandika";
    private final String Password = "1234";
    @FXML
    void loginBtnOnAction(ActionEvent event) throws IOException {
        String enteredUsername = Name.getText();
        String enteredPassword = Code.getText();

        if (UserName.equals(enteredUsername)) {
            if (Password.equals(enteredPassword)) {
                clearFields();
                createdashboard(event); // Pass event to close login window properly
            } else {
                showErrorNotification("Error: Incorrect password.");
            }
        } else {
            showErrorNotification("Error: Username not found or incorrect.");
        }
    }

    private void clearFields() {
        Name.clear();
        Code.clear();
    }

    private void createdashboard(ActionEvent event) throws IOException {
        // Load the main dashboard
        Parent rootNode = FXMLLoader.load(getClass().getResource("/View/main.fxml"));
        Scene scene = new Scene(rootNode);

        // Get the current stage from the event and close it
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Open the new stage
        Stage newStage = new Stage();
        newStage.setTitle("Main Dashboard");
        newStage.setFullScreen(true);
        newStage.centerOnScreen();
        newStage.setResizable(true);
        newStage.setScene(scene);
        newStage.show();
    }
    private void showErrorNotification(String message) {
        ImageView imageView = new ImageView(new Image("/Asset/icons8-close-100.png"));
        Notifications.create()
                .graphic(imageView)
                .text(message)
                .title("WARNING")
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT)
                .darkStyle()
                .show();
    }

}
