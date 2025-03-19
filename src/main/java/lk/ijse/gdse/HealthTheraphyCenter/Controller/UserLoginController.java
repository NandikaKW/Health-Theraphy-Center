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
import lk.ijse.gdse.HealthTheraphyCenter.Exception.UserLoginException;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.ProgramBO;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.UserBO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.UserDto;
import org.controlsfx.control.Notifications;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class UserLoginController {

    @FXML
    private TextField NameTxt;

    @FXML
    private PasswordField PasswordTxt;

    @FXML
    private AnchorPane UserAnchore;
    public static String loggedInUserName;
    UserBO userBO = BoFactory.getInstance().getBO(BoTypes.USER);

    // Method to handle user registration
    @FXML
    void RegisterBtnOnAction(ActionEvent event) throws IOException {
        // Load the Register Form in the AnchorPane
        Node node = FXMLLoader.load(getClass().getResource("/View/RegisterForm.fxml"));
        UserAnchore.getChildren().clear();
        UserAnchore.getChildren().setAll(node);
    }

    // Method to handle user login
    @FXML
    void UserLoginOnAction(ActionEvent event) throws Exception {
        try {
            validateLogin(event); // Pass event to close the login window properly
        } catch (UserLoginException e) {
            // Handle the custom exception and show an appropriate notification
            showNotification(e.getMessage(), "/Asset/icons8-close-100.png");
            clearFields();
        } catch (Exception e) {
            // Generic exception handling
            showNotification("An unexpected error occurred. Please try again.", "/Asset/icons8-close-100.png");
            clearFields();
        }
    }

    // Method to validate user login
    private void validateLogin(ActionEvent event) throws Exception {
        String username = NameTxt.getText();
        String password = PasswordTxt.getText();

        // Fetch user details from the database
        UserDto user = userBO.getUserByName(username);

        if (user == null || user.getUsername() == null) {
            throw new UserLoginException("Error: Username not found or incorrect.");
        } else {
            // Verify the entered password with the hashed password stored in the database
            try {
                if (!BCrypt.checkpw(password, user.getPassword())) {
                    throw new UserLoginException("Error: Incorrect password.");
                } else {
                    // Set the logged-in user's name
                    loggedInUserName = user.getUsername(); // Store the username

                    // Close the current login window before opening the new one
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();

                    // Open the User Dashboard
                    createDashboard();
                }
            } catch (IllegalArgumentException e) {
                // Handle invalid salt version or corrupted hash
                throw new UserLoginException("Error: Invalid password format. Please contact support.", e);
            }
        }
    }

    // Method to clear login fields
    private void clearFields() {
        NameTxt.clear();
        PasswordTxt.clear();
    }

    // Method to create and show the user dashboard
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

    // Method to show notifications
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

    // Method to handle user registration (if needed)
    @FXML
    void registerUser(ActionEvent event) throws Exception {
        String username = NameTxt.getText();
        String plainTextPassword = PasswordTxt.getText();

        // Hash the password before saving
        String hashedPassword = BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());

        // Create a new user DTO
        UserDto user = new UserDto();
        user.setUsername(username);
        user.setPassword(hashedPassword);

        // Save the user to the database
        boolean isSaved = userBO.saveUser(user);

        if (isSaved) {
            showNotification("User registered successfully!", "/Asset/success.png");
            clearFields();
        } else {
            showNotification("Error: Failed to register user.", "/Asset/icons8-close-100.png");
        }
    }

}
