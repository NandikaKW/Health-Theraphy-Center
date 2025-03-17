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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.ProgramBO;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.UserBO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.UserDto;
import org.controlsfx.control.Notifications;

import java.io.IOException;

public class UserLoginController {

    @FXML
    private TextField NameTxt;

    @FXML
    private PasswordField PasswordTxt;

    @FXML
    private AnchorPane UserAnchore;
    UserBO userBO = BoFactory.getInstance().getBO(BoTypes.USER);

    @FXML
    void RegisterBtnOnAction(ActionEvent event) throws IOException {
        // Load the Register Form in the AnchorPane
        Node node = FXMLLoader.load(getClass().getResource("/View/RegisterForm.fxml"));
        UserAnchore.getChildren().clear();
        UserAnchore.getChildren().setAll(node);
    }

    @FXML
    void UserLoginOnAction(ActionEvent event) throws Exception {
        validateLogin(event); // Pass event to close the login window properly
    }

    private void validateLogin(ActionEvent event) throws Exception {
        String username = NameTxt.getText();
        String password = PasswordTxt.getText();

        UserDto user = userBO.getUserByName(username);

        if (user == null || user.getUsername() == null) {
            showNotification("Error: Username not found or incorrect.", "/Asset/icons8-close-100.png");
            clearFields();
        } else if (!user.getPassword().equals(password)) {
            showNotification("Error: Incorrect password.", "/Asset/icons8-close-100.png");
            clearFields();
        } else {
            // Close the current login window before opening the new one
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Open the User Dashboard
            createDashboard();
        }
    }

    private void clearFields() {
        NameTxt.clear();
        PasswordTxt.clear();
    }

    private void createDashboard() throws IOException {
        // Load the dashboard UI
        Stage stage = new Stage();
        Parent rootNode = FXMLLoader.load(getClass().getResource("/View/UserMainForm.fxml"));
        Scene scene = new Scene(rootNode);

        stage.setTitle("User Dashboard");
        stage.setFullScreen(true);
        stage.centerOnScreen();
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();

        // Clear the login fields after login success
        NameTxt.clear();
        PasswordTxt.clear();
    }

    private void showNotification(String message, String iconPath) {
        ImageView imageView = new ImageView(new Image(iconPath));
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
