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
        // Get values from UI
        String id = txtid.getText().trim();
        String patientId = cmbPatient.getValue();
        String therapistId = cmbTherapist.getValue();
        String programId = cmbProgram.getValue();
        String notes = txtNotes.getText().trim();
        LocalDate selectedDateValue = datePicker.getValue();

        // Regex patterns
        String idPattern = "^TS\\d{3}$"; // Session ID: TS001
        String patientPattern = "^P\\d{3}$"; // Patient ID: P001
        String therapistPattern = "^T\\d{3}$"; // Therapist ID: T001
        String programPattern = "^TP\\d{3}$"; // Program ID: TP001
        String notesPattern = "^[\\w\\s.,!?'-]{0,200}$"; // Notes: Alphanumeric and punctuation
        boolean isValid = true;

        // Validate Session ID
        if (!id.matches(idPattern)) {
            txtid.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Session ID. Format must be 'TSxxx'.");
            isValid = false;
        } else {
            txtid.setStyle(null);
        }

        // Validate Patient ID
        if (patientId == null || !patientId.matches(patientPattern)) {
            cmbPatient.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Patient ID. Format must be 'Pxxx'.");
            isValid = false;
        } else {
            cmbPatient.setStyle(null);
        }

        // Validate Therapist ID
        if (therapistId == null || !therapistId.matches(therapistPattern)) {
            cmbTherapist.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Therapist ID. Format must be 'Txxx'.");
            isValid = false;
        } else {
            cmbTherapist.setStyle(null);
        }

        // Validate Program ID
        if (programId == null || !programId.matches(programPattern)) {
            cmbProgram.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Program ID. Format must be 'TPxxx'.");
            isValid = false;
        } else {
            cmbProgram.setStyle(null);
        }

        // Validate Notes
        if (!notes.matches(notesPattern)) {
            txtNotes.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid notes. Only 200 characters max including letters, numbers, and punctuation.");
            isValid = false;
        } else {
            txtNotes.setStyle(null);
        }

        // Validate Date
        if (selectedDateValue == null) {
            datePicker.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Please select a session date.");
            isValid = false;
        } else {
            datePicker.setStyle(null);
        }

        if (!isValid) return;

        // Prepare date and DTO
        Date selectedDate = Date.from(selectedDateValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(selectedDate);

        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setId(id); // id → therapy_sessions.id
        sessionDTO.setPatientId(patientId); // patient_id
        sessionDTO.setTherapistId(therapistId); // therapist_id
        sessionDTO.setProgramId(programId); // therapy_program_id
        sessionDTO.setNotes(notes); // sessionNotes
        sessionDTO.setDate(formattedDate); // sessionDate
        sessionDTO.setStatus("UNPAID"); // status

        // Save session
        try {
            boolean saved = sessionBO.saveSession(sessionDTO);
            if (saved) {
                showAlert(Alert.AlertType.INFORMATION, "Success", null, "Session saved successfully.");
                refreshPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", null, "Failed to save session.");
            }
        } catch (Exception e) {
            showErrorAlert("Error: " + e.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) throws Exception {
        // Get values from UI
        String id = txtid.getText().trim();
        String patientId = cmbPatient.getValue();
        String therapistId = cmbTherapist.getValue();
        String programId = cmbProgram.getValue();
        String notes = txtNotes.getText().trim();
        LocalDate selectedDateValue = datePicker.getValue();

        // Regex patterns
        String idPattern = "^TS\\d{3}$"; // Session ID
        String patientPattern = "^P\\d{3}$"; // Patient ID
        String therapistPattern = "^T\\d{3}$"; // Therapist ID
        String programPattern = "^TP\\d{3}$"; // Program ID
        String notesPattern = "^[\\w\\s.,!?'-]{0,200}$"; // Notes

        boolean isValid = true;

        // Validate Session ID
        if (!id.matches(idPattern)) {
            txtid.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Session ID. Format must be 'TSxxx'.");
            isValid = false;
        } else {
            txtid.setStyle(null);
        }

        // Validate Patient ID
        if (patientId == null || !patientId.matches(patientPattern)) {
            cmbPatient.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Patient ID. Format must be 'Pxxx'.");
            isValid = false;
        } else {
            cmbPatient.setStyle(null);
        }

        // Validate Therapist ID
        if (therapistId == null || !therapistId.matches(therapistPattern)) {
            cmbTherapist.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Therapist ID. Format must be 'Txxx'.");
            isValid = false;
        } else {
            cmbTherapist.setStyle(null);
        }

        // Validate Program ID
        if (programId == null || !programId.matches(programPattern)) {
            cmbProgram.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Program ID. Format must be 'TPxxx'.");
            isValid = false;
        } else {
            cmbProgram.setStyle(null);
        }

        // Validate Notes
        if (!notes.matches(notesPattern)) {
            txtNotes.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid notes. Only 200 characters max including letters, numbers, and punctuation.");
            isValid = false;
        } else {
            txtNotes.setStyle(null);
        }

        // Validate Date
        if (selectedDateValue == null) {
            datePicker.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Please select a session date.");
            isValid = false;
        } else {
            datePicker.setStyle(null);
        }

        if (!isValid) return;

        // Prepare formatted date
        Date selectedDate = Date.from(selectedDateValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(selectedDate);

        // Create DTO
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setId(id);
        sessionDTO.setPatientId(patientId);
        sessionDTO.setTherapistId(therapistId);
        sessionDTO.setProgramId(programId);
        sessionDTO.setNotes(notes);
        sessionDTO.setDate(formattedDate);

        // Retain original status
        Optional<SessionDTO> sessionByID = sessionBO.findSessionByID(id);
        sessionByID.ifPresent(dto -> sessionDTO.setStatus(dto.getStatus()));

        try {
            boolean updated = sessionBO.updateSession(sessionDTO);

            if (updated) {
                showAlert(Alert.AlertType.INFORMATION, "Success", null, "Session updated successfully.");
                refreshPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", null, "Failed to update session.");
            }
        } catch (Exception e) {
            showErrorAlert("Update Error: " + e.getMessage());
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
