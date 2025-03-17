package lk.ijse.gdse.HealthTheraphyCenter.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.Duration;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.PatientBO;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.UserBO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.tm.UserTM;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UsersFormController implements Initializable {

    @FXML
    private TableColumn<UserTM, String> Actioncolumn;

    @FXML
    private TableColumn<UserTM, String> EmailColumn;

    @FXML
    private TableColumn<UserTM, String> IDColumn;

    @FXML
    private TableColumn<UserTM, String> NameColumn;

    @FXML
    private TableView<UserTM> tblUser;
    UserBO userBO = BoFactory.getInstance().getBO(BoTypes.USER);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind table columns to UserTM properties
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        EmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Create a button for each row in the "Actioncolumn"
        Actioncolumn.setCellFactory(new Callback<TableColumn<UserTM, String>, TableCell<UserTM, String>>() {
            @Override
            public TableCell<UserTM, String> call(TableColumn<UserTM, String> param) {
                return new TableCell<UserTM, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            Button removeBtn = new Button("Remove");
                            removeBtn.setOnAction(event -> {
                                // Action for removing the user
                                UserTM user = getTableRow().getItem();
                                try {
                                    removeUser(user);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            setGraphic(removeBtn);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        try {
            loadUserData();
        } catch (Exception e) {
            e.printStackTrace(); // Handle any exceptions (you can log or show an error message)
        }
    }


    private void loadUserData() throws Exception {
        // Retrieve all users from the BO layer
        List<UserTM> userTMList = userBO.getAllUsers().stream()
                .map(userDto -> new UserTM(
                        userDto.getId(),
                        userDto.getEmail(),
                        userDto.getUsername(),
                        userDto.getPassword(),
                        new Button("Remove") // Add a button for each row
                ))
                .toList();

        // Add users to the table
        tblUser.getItems().addAll(userTMList);
    }

    private void removeUser(UserTM user) throws Exception {
        try {
            boolean isDeleted = userBO.deleteUser(user.getId());

            if (isDeleted) {
                // Remove the user from the table
                tblUser.getItems().remove(user);

                // Show success notification
                ImageView imageView = new ImageView(new Image("/Asset/icons8-done-96.png"));
                Notifications.create()
                        .graphic(imageView)
                        .text("User deleted successfully.")
                        .title("Successful")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.TOP_RIGHT)
                        .darkStyle()
                        .show();
            } else {
                // Show warning notification if deletion was unsuccessful
                ImageView imageView = new ImageView(new Image("/Asset/icons8-close-100.png"));
                Notifications.create()
                        .graphic(imageView)
                        .text("User could not be deleted. User not found.")
                        .title("Error")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.TOP_RIGHT)
                        .darkStyle()
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();

            // Show error notification
            ImageView imageView = new ImageView(new Image("/Asset/icons8-close-100.png"));
            Notifications.create()
                    .graphic(imageView)
                    .text("An error occurred while deleting the user.")
                    .title("ERROR")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.TOP_RIGHT)
                    .darkStyle()
                    .show();
        }
    }
    }

