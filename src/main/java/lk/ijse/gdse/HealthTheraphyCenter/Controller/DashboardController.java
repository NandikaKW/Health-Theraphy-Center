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
            // Set the name of the logged-in user
            String name = UserLoginController.loggedInUserName; // Access the static variable
            NameLabel.setText(name);

            // Set the program count
            setProgramCount();

            // Set the user count
            setUserCount();

            setPatientCount();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message to the user)
        }

    }

    private void setPatientCount() throws Exception {
        int count = patientBO.getAllPatients().size(); // Assuming getAllPrograms() returns a list of programs
        PatientsCount.setText(String.valueOf(count));
    }

    private void setProgramCount() throws Exception {
        int count = programBO.getAllPrograms().size(); // Assuming getAllPrograms() returns a list of programs
        ProgramCount.setText(String.valueOf(count));
    }

    private void setUserCount() throws Exception {
        int count = userBO.getUserCount(); // Assuming getUserCount() returns the total number of users
        UserCount.setText(String.valueOf(count));
    }


}
