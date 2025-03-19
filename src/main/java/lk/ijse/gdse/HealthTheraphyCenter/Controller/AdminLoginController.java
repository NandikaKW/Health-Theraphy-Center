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
import lk.ijse.gdse.HealthTheraphyCenter.Exception.LoginException;
import org.controlsfx.control.Notifications;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class AdminLoginController {

    @FXML
    private PasswordField Code;

    @FXML
    private TextField Name;
    // Storing a hashed password
    private final String UserName = "Nandika";
    private final String PasswordHash = BCrypt.hashpw("1234", BCrypt.gensalt()); // Hash password during storage

    @FXML
    void loginBtnOnAction(ActionEvent event) throws IOException {
        String enteredUsername = Name.getText();
        String enteredPassword = Code.getText();

        try {
            if (UserName.equals(enteredUsername)) {
                // Verify the entered password with the hashed one
                if (BCrypt.checkpw(enteredPassword, PasswordHash)) {
                    clearFields();
                    createdashboard(event); // Pass event to close login window properly
                } else {
                    throw new LoginException("Error: Incorrect password.");
                }
            } else {
                throw new LoginException("Error: Username not found or incorrect.");
            }
        } catch (LoginException e) {
            showErrorNotification(e.getMessage());
        } catch (Exception e) {
            showErrorNotification("An unexpected error occurred. Please try again later.");
        }
    }

    private void clearFields() {
        Name.clear();
        Code.clear();
    }

    private void createdashboard(ActionEvent event) throws IOException {
        try {
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
        } catch (IOException e) {
            showErrorNotification("Error: Unable to load the dashboard.");
        }
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
