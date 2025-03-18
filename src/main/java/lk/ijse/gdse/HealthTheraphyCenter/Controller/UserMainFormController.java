package lk.ijse.gdse.HealthTheraphyCenter.Controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserMainFormController {

    @FXML
    private AnchorPane CommonAnchore;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;
    @FXML
    public void initialize() throws SQLException, ClassNotFoundException, IOException {
        Node node = (Node) FXMLLoader.load(getClass().getResource("/View/Dashboard.fxml"));
        CommonAnchore.getChildren().clear();
        CommonAnchore.getChildren().setAll(node);
        loadDateAndTime();
    }

    private void loadDateAndTime() {
        if (lblTime == null || lblDate == null) {
            System.out.println("lblTime is null, check FXML binding.");
            return;
        }


        Timeline time = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    lblDate.setText(LocalDateTime.now().format(dateFormatter));
                    lblTime.setText(LocalDateTime.now().format(timeFormatter));
                }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();

    }

    @FXML
    void DashboardBtnOnAction(ActionEvent event) throws IOException {
        Node node = (Node) FXMLLoader.load(getClass().getResource("/View/Dashboard.fxml"));
        CommonAnchore.getChildren().clear();
        CommonAnchore.getChildren().setAll(node);

    }
    @FXML
    void patientsbtnOnaction(ActionEvent event) throws IOException {
        Node node = (Node) FXMLLoader.load(getClass().getResource("/View/Patients.fxml"));
        CommonAnchore.getChildren().clear();
        CommonAnchore.getChildren().setAll(node);

    }
    @FXML
    void BackbtnOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LoginForm.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("User Login"); // Set window title
        stage.show();

        // Optionally close the current window
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

    }


}
