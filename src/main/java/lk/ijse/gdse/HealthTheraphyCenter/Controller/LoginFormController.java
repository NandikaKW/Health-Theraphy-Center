package lk.ijse.gdse.HealthTheraphyCenter.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {

    @FXML
    private AnchorPane loginFuntions;
    @FXML
    public void initialize() throws SQLException, ClassNotFoundException, IOException {
        Node node = (Node) FXMLLoader.load(getClass().getResource("/View/UserLogin.fxml"));
        loginFuntions.getChildren().clear();
        loginFuntions.getChildren().setAll(node);
    }

    @FXML
    void AdminBtnOnAction(ActionEvent event) throws IOException {
        Node node = (Node) FXMLLoader.load(getClass().getResource("/View/AdminLogin.fxml"));
        loginFuntions.getChildren().clear();
        loginFuntions.getChildren().setAll(node);

    }

    @FXML
    void UserBtnOnAction(ActionEvent event) throws IOException {
        Node node = (Node) FXMLLoader.load(getClass().getResource("/View/UserLogin.fxml"));
        loginFuntions.getChildren().clear();
        loginFuntions.getChildren().setAll(node);

    }

}
