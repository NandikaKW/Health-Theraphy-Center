package lk.ijse.gdse.HealthTheraphyCenter.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UserLoginController {

    @FXML
    private TextField NameTxt;

    @FXML
    private PasswordField PasswordTxt;

    @FXML
    private AnchorPane UserAnchore;

    @FXML
    void RegisterBtnOnAction(ActionEvent event) throws IOException {
        Node node = (Node) FXMLLoader.load(getClass().getResource("/View/RegisterForm.fxml"));
        UserAnchore.getChildren().clear();
        UserAnchore.getChildren().setAll(node);

    }

    @FXML
    void UserLoginOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(getClass().getResource("/View/UserMainForm.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.setTitle("Main");
        stage.setFullScreen(true);
        stage.centerOnScreen();
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();

    }

}
