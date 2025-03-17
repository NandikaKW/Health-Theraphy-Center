package lk.ijse.gdse.HealthTheraphyCenter.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.UserBO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.impl.UserDAOImpl;
import lk.ijse.gdse.HealthTheraphyCenter.dto.UserDto;
import org.controlsfx.control.Notifications;

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
        String Name=NameTxt.getText();
        String Password=PasswordTxt.getText();
        String Email=EmailTxt.getText();


        try {
            UserDto userDto = new UserDto(null, Email, Name, Password);
            userBO.saveUser(userDto);
            ImageView imageView = new ImageView(new Image("/Asset/icons8-done-96.png"));
            Notifications.create()
                    .graphic(imageView)
                    .text(" User Added. ")
                    .title("Successful")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.TOP_RIGHT)
                    .darkStyle()
                    .show();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            ImageView imageView = new ImageView(new Image("/Asset/icons8-close-100.png"));
            Notifications.create()
                    .graphic(imageView)
                    .text("User Not Added. ")
                    .title("WARNING")
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


