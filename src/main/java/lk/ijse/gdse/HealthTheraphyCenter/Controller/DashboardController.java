package lk.ijse.gdse.HealthTheraphyCenter.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.*;

import java.io.IOException;
import java.sql.SQLException;

public class DashboardController {

    @FXML
    private Label NameLabel;

    @FXML
    private Label ProgramCount;
    @FXML
    private Label PatientsCount;

    @FXML
    private Label UserCount;
    private String name;

    private final UserBO userBO = BoFactory.getInstance().getBO(BoTypes.USER);
    private final ProgramBO programBO = BoFactory.getInstance().getBO(BoTypes.THERAPY_PROGRAMS);
    private final PatientBO patientBO = BoFactory.getInstance().getBO(BoTypes.PATIENTS);

    public void initialize() throws Exception {
        try {

            String name = UserLoginController.loggedInUserName; // Access the static variable
            NameLabel.setText(name);


            setProgramCount();


            setUserCount();

            setPatientCount();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();

        }

    }

    private void setPatientCount() throws Exception {
        int count = patientBO.getAllPatients().size();
        PatientsCount.setText(String.valueOf(count));
    }

    private void setProgramCount() throws Exception {
        int count = programBO.getAllPrograms().size();
        ProgramCount.setText(String.valueOf(count));
    }

    private void setUserCount() throws Exception {
        int count = userBO.getUserCount();
        UserCount.setText(String.valueOf(count));
    }


}
