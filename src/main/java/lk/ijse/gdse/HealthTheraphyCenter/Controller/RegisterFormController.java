package lk.ijse.gdse.HealthTheraphyCenter.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import lk.ijse.gdse.HealthTheraphyCenter.Exception.UserRegistrationException;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.UserBO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.impl.UserDAOImpl;
import lk.ijse.gdse.HealthTheraphyCenter.dto.UserDto;
import org.controlsfx.control.Notifications;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterFormController {

    @FXML
    private TextField EmailTxt;

    @FXML
    private TextField NameTxt;

    @FXML
    private PasswordField PasswordTxt;
    UserBO userBO = BoFactory.getInstance().getBO(BoTypes.USER);

    @FXML
    void RegisterBtnOnAction(ActionEvent event) {
        String name = NameTxt.getText();
        String plainTextPassword = PasswordTxt.getText();
        String email = EmailTxt.getText();


        String hashedPassword = BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());

        try {

            UserDto userDto = new UserDto(null, email, name, hashedPassword);


            boolean isSaved = userBO.saveUser(userDto);

            if (isSaved) {
                ImageView imageView = new ImageView(new Image("/Asset/icons8-done-96.png"));
                Notifications.create()
                        .graphic(imageView)
                        .text("User Added Successfully!")
                        .title("Successful")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.TOP_RIGHT)
                        .darkStyle()
                        .show();
                clearFields();
            } else {
                throw new UserRegistrationException("Failed to save user.");
            }
        } catch (UserRegistrationException e) {
            e.printStackTrace();
            ImageView imageView = new ImageView(new Image("/Asset/icons8-close-100.png"));
            Notifications.create()
                    .graphic(imageView)
                    .text("User Not Added. Please try again.")
                    .title("WARNING")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.TOP_RIGHT)
                    .darkStyle()
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
            ImageView imageView = new ImageView(new Image("/Asset/icons8-close-100.png"));
            Notifications.create()
                    .graphic(imageView)
                    .text("An unexpected error occurred. Please try again later.")
                    .title("ERROR")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.TOP_RIGHT)
                    .darkStyle()
                    .show();
        }
    }

    private void clearFields() {
        NameTxt.clear();
        PasswordTxt.clear();
        EmailTxt.clear();
    }

}


