package lk.ijse.gdse.HealthTheraphyCenter.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.SessionBO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.PatientDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.ProgramDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.SessionDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.TherapistDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.tm.SessionTM;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TherapySessionsController implements Initializable {

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private ComboBox<String> cmbPatient;

    @FXML
    private ComboBox<String> cmbProgram;

    @FXML
    private ComboBox<String> cmbTherapist;

    @FXML
    private TableColumn<SessionTM, String> colDate;

    @FXML
    private TableColumn<SessionTM, String> colID;

    @FXML
    private TableColumn<SessionTM, String> colNotes;

    @FXML
    private TableColumn<SessionTM, String> colPatientID;

    @FXML
    private TableColumn<SessionTM, String> colProgramID;

    @FXML
    private TableColumn<SessionTM, String> colStatus;

    @FXML
    private TableColumn<SessionTM, String> colTherapistID;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<SessionTM> tblSessions;

    @FXML
    private TextField txtNotes;

    @FXML
    private TextField txtid;
    @FXML
    private Label lblPatient;

    @FXML
    private Label lblTherapist;

    @FXML
    private Label lblProgram;
    SessionBO sessionBO = BoFactory.getInstance().getBO(BoTypes.SESSION);

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String sessionId = txtid.getText();

        try {
            boolean deleted = sessionBO.deleteSession(sessionId);

            if (deleted) {
                showAlert(Alert.AlertType.INFORMATION, "Error", "Success", "Session deleted successfully.");
                refreshPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed", "Failed to delete session.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error", "An error occurred while deleting the session.");
        }

    }

    @FXML
    void btnRefreshOnAction(ActionEvent event) throws Exception {
        refreshTable();
        GenerateNextId();
        clearFields();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (validateFields()) {
            SessionDTO sessionDTO = new SessionDTO();
            sessionDTO.setId(txtid.getText());
            sessionDTO.setPatientId(cmbPatient.getValue());
            sessionDTO.setTherapistId(cmbTherapist.getValue());
            sessionDTO.setProgramId(cmbProgram.getValue());
            sessionDTO.setNotes(txtNotes.getText());


            Date selectedDate = Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(selectedDate);

            sessionDTO.setDate(formattedDate);  // Set formatted date string
            sessionDTO.setStatus("UNPAID");

            try {
                boolean saved = sessionBO.saveSession(sessionDTO);

                if (saved) {
                    showAlert(Alert.AlertType.INFORMATION, "Error", "Success", "Session saved successfully.");
                    refreshPage();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed", "Failed to save session.");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws Exception {
        if (validateFields()) {
            SessionDTO sessionDTO = new SessionDTO();
            sessionDTO.setId(txtid.getText());
            sessionDTO.setPatientId(cmbPatient.getValue());
            sessionDTO.setTherapistId(cmbTherapist.getValue());
            sessionDTO.setProgramId(cmbProgram.getValue());
            sessionDTO.setNotes(txtNotes.getText());


            Date selectedDate = Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(selectedDate);

            sessionDTO.setDate(formattedDate); // Set formatted date string

            Optional<SessionDTO> sessionByID = sessionBO.findSessionByID(txtid.getText());
            sessionByID.ifPresent(dto -> sessionDTO.setStatus(dto.getStatus()));

            try {
                boolean updated = sessionBO.updateSession(sessionDTO);

                if (updated) {
                    showAlert(Alert.AlertType.INFORMATION, "Error", "Success", "Session updated successfully.");
                    refreshPage();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed", "Failed to update session.");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    void cmbPatientOnAction(ActionEvent event) throws Exception {
        if (cmbPatient.getValue() == null) return;

        Optional<PatientDTO> patientDTO = sessionBO.findPatientByID(cmbPatient.getValue());

        if (patientDTO.isPresent()) {
            lblPatient.setText(patientDTO.get().getName());
        }

    }

    @FXML
    void cmbProgramOnAction(ActionEvent event) throws Exception {
        if (cmbProgram.getValue() == null) return;

        Optional<ProgramDTO> programDTO = sessionBO.findProgramByID(cmbProgram.getValue());

        if (programDTO.isPresent()) {
            lblProgram.setText(programDTO.get().getName());
        }

    }

    @FXML
    void cmbTherapistOnAction(ActionEvent event) throws Exception {
        if (cmbTherapist.getValue() == null) return;

        Optional<TherapistDTO> therapistDTO = sessionBO.findTherapistByID(cmbTherapist.getValue());

        if (therapistDTO.isPresent()) {
            lblTherapist.setText(therapistDTO.get().getName());
        }

    }

    @FXML
    void onClickTable(MouseEvent event) {
        SessionTM selectedSession = tblSessions.getSelectionModel().getSelectedItem();
        if (selectedSession != null) {
            txtid.setText(selectedSession.getId());
            txtNotes.setText(selectedSession.getNotes());


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedDate = LocalDate.parse(selectedSession.getDate(), formatter);

            datePicker.setValue(parsedDate);
            cmbPatient.setValue(selectedSession.getPatientId());
            cmbTherapist.setValue(selectedSession.getTherapistId());
            cmbProgram.setValue(selectedSession.getProgramId());
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPatientID.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colTherapistID.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colProgramID.setCellValueFactory(new PropertyValueFactory<>("programId"));

        try {
            refreshPage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    private void refreshPage() throws Exception {
        refreshTable();
        GenerateNextId();
        clearFields();
    }

    private void  GenerateNextId() throws Exception {
        String nextProgramID = sessionBO.generateNextTherapySessionID();
        txtid.setText(nextProgramID);
    }
    private void refreshTable() {
        try {
            List<SessionDTO> sessionDTOS = sessionBO.getAllSessions();
            ObservableList<SessionTM> sessionList = FXCollections.observableArrayList();
            for (SessionDTO dto : sessionDTOS) {
                sessionList.add(new SessionTM(dto.getId(), dto.getNotes(), dto.getStatus(), dto.getDate(), dto.getPatientId(), dto.getTherapistId(), dto.getProgramId()));
            }
            tblSessions.setItems(sessionList);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    private void clearFields() throws Exception {
        txtNotes.clear();
        datePicker.setValue(null);

        cmbPatient.getItems().clear();
        cmbTherapist.getItems().clear();
        cmbProgram.getItems().clear();

        lblPatient.setText("");
        lblProgram.setText("");
        lblTherapist.setText("");

        loadCmbPatient();
        loadCmbTherapist();
        loadCmbProgram();
    }
    private void loadCmbPatient() throws Exception {
        ArrayList<String> allPatientIDs = sessionBO.getAllPatientIDs();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(allPatientIDs);
        cmbPatient.setItems(observableList);
    }

    private void loadCmbTherapist() throws Exception {
        ArrayList<String> allTherapistIDs = sessionBO.getAllTherapistIDs();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(allTherapistIDs);
        cmbTherapist.setItems(observableList);
    }

    private void loadCmbProgram() throws Exception {
        ArrayList<String> allProgramIDs = sessionBO.getAllProgramIDs();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(allProgramIDs);
        cmbProgram.setItems(observableList);
    }
    private boolean validateFields() {
        if (txtid.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Validation Error", "Session ID cannot be empty.");
            return false;
        }

        if (cmbPatient.getValue().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Validation Error", "Patient ID cannot be empty.");
            return false;
        }

        if (cmbTherapist.getValue().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Validation Error", "Therapist ID cannot be empty.");
            return false;
        }

        if (cmbProgram.getValue().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Validation Error", "Program ID cannot be empty.");
            return false;
        }

        if (txtNotes.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Validation Error", "Notes cannot be empty.");
            return false;
        }

        if (datePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Validation Error", "Session date cannot be empty.");
            return false;
        }

        return true;
    }
    public static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
