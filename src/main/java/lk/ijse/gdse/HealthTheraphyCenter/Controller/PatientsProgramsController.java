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
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.*;
import lk.ijse.gdse.HealthTheraphyCenter.dto.PatientDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.PatientProgramDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.ProgramDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.tm.PatientProgramTM;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;


public class PatientsProgramsController implements Initializable {

    @FXML
    private ComboBox<Integer> CombMonth;

    @FXML
    private ComboBox<Integer> ComboDay;

    @FXML
    private ComboBox<String> ComboPatientID;

    @FXML
    private ComboBox<String> ComboProgramID;

    @FXML
    private ComboBox<Integer> ComboYear;

    @FXML
    private TableColumn<PatientProgramTM, String> colPatientid;

    @FXML
    private TableColumn<PatientProgramTM, String> colProgramid;

    @FXML
    private TableColumn<PatientProgramTM, Integer> colattendance;

    @FXML
    private TableColumn<PatientProgramTM, String> coldate;

    @FXML
    private TableColumn<PatientProgramTM, String> coloutcome;

    @FXML
    private TableColumn<PatientProgramTM, String> colstatus;

    @FXML
    private TableView<PatientProgramTM> tblPatientProgram;
    @FXML
    private TableColumn<PatientProgramTM, String> colPatientProgramID;
    @FXML
    private TextField txtPatientProgramid;


    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtAttendance;

    @FXML
    private TextField txtOutCome;

    @FXML
    private TextField txtStatus;
    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private JFXButton btnSave;

    @FXML
    private ComboBox<String> comboStatus;

    @FXML
    private JFXButton btnUpdate;
    PatientProgramBO patientProgramBO = BoFactory.getInstance().getBO(BoTypes.PATIENT_PROGRAM);
    PatientBO patientBO = BoFactory.getInstance().getBO(BoTypes.PATIENTS);
    ProgramBO programBO = BoFactory.getInstance().getBO(BoTypes.THERAPY_PROGRAMS);
    @FXML
    private void handleComboBoxAction(ActionEvent event) {
        String selectedStatus = comboStatus.getValue();

    }
    @FXML
    void ComboDayOnAction(ActionEvent event) {
        showSelectedDate();

    }

    @FXML
    void ComboMonthOnAction(ActionEvent event) {
        updateDays();
    }
    private void updateDays(){
        try{
            Integer year=ComboYear.getValue();
            Integer month=CombMonth.getValue();
            if (year!=null && month!=null){
                int daysInMonth= YearMonth.of(year,month).lengthOfMonth();
                ComboDay.setItems(FXCollections.observableArrayList(
                        IntStream.rangeClosed(1,daysInMonth).boxed().toList()
                ));
                ComboDay.getSelectionModel().selectFirst();
                showSelectedDate();

            }else{
                System.out.println("Year or Month ComboBox is null");
            }

        }catch (Exception e){
            e.printStackTrace();

        }
    }
    private void showSelectedDate(){
        Integer year=ComboYear.getValue();
        Integer month=CombMonth.getValue();
        Integer day=ComboDay.getValue();
        if (year!=null && month!=null && day!=null){
            txtDate.setText(String.format("%04d-%02d-%02d",year,month,day));
        }

    }

    @FXML
    void ComboPatientOnAction(ActionEvent event) {
        String selectedPatientID = ComboPatientID.getSelectionModel().getSelectedItem();
        if (selectedPatientID != null) {
            System.out.println("Selected Patient ID: " + selectedPatientID);
        }



    }

    @FXML
    void ComboProgramOnAction(ActionEvent event) {
        String selectedProgramID = ComboProgramID.getSelectionModel().getSelectedItem();
        if (selectedProgramID != null) {
            System.out.println("Selected Program ID: " + selectedProgramID);
        }

    }

    @FXML
    void ComboYearOnAction(ActionEvent event) {
        updateDays();

    }


    @FXML
    void onClickTable(MouseEvent event) {
        // Handle table row selection
        PatientProgramTM selectedItem = tblPatientProgram.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            colPatientProgramID.setText(selectedItem.getId());
            ComboPatientID.setValue(selectedItem.getPatientId());
            ComboProgramID.setValue(selectedItem.getProgramId());
            txtAttendance.setText(String.valueOf(selectedItem.getAttendance()));
            txtOutCome.setText(selectedItem.getProgramOutcome());
            comboStatus.setValue(selectedItem.getStatus());


            String enrollmentDate = selectedItem.getEnrollmentDate();
            String[] dateParts = enrollmentDate.split("-");

            if (dateParts.length == 3) {
                ComboYear.setValue(Integer.parseInt(dateParts[0]));
                CombMonth.setValue(Integer.parseInt(dateParts[1]));
                ComboDay.setValue(Integer.parseInt(dateParts[2]));
            }
        }

    }
    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
            PatientProgramTM selectedPatientProgram = tblPatientProgram.getSelectionModel().getSelectedItem();

            if (selectedPatientProgram == null) {
                new Alert(Alert.AlertType.WARNING, "Please Select a Patient Program to Delete!").show();
                return;
            }

            boolean isDeleted = patientProgramBO.deletePatientProgram(selectedPatientProgram.getId());

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Patient Program Deleted Successfully!").show();
                loadAllPatientPrograms();
                clearFields();
                GenerateNextPatientProgramId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to Delete Patient Program!").show();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred while deleting the Patient Program. Please try again later.").show();
            e.printStackTrace();
        }

    }


    @FXML
    void btnRefreshOnAction(ActionEvent event) throws Exception {
        clearFields();
        loadAllPatientPrograms();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtPatientProgramid.getText();
        String patientId = ComboPatientID.getValue();
        String programId = ComboProgramID.getValue();
        String attendanceStr = txtAttendance.getText().trim();
        String programOutcome = txtOutCome.getText().trim();
        String status = comboStatus.getValue();

        // Regex patterns
        String idPattern = "^PP\\d{2,}$"; // Example: PP01
        String patientIdPattern = "^P\\d{3}$"; // Example: P001
        String programIdPattern = "^TP\\d{3}$"; // Example: TP001
        String attendancePattern = "^\\d{1,3}$"; // Max 3-digit attendance
        String outcomePattern = "^[\\w\\s,.#-]{0,255}$"; // Optional but limited to 255 chars
        String statusPattern = "^(Incomplete|Complete|Pending)$"; // Adjust as per allowed statuses

        // Validations
        boolean isValidId = id != null && id.matches(idPattern);
        boolean isValidPatientId = patientId != null && patientId.matches(patientIdPattern);
        boolean isValidProgramId = programId != null && programId.matches(programIdPattern);
        boolean isValidAttendance = attendanceStr.matches(attendancePattern);
        boolean isValidOutcome = programOutcome.matches(outcomePattern);
        boolean isValidStatus = status != null && status.matches(statusPattern);

        resetFieldStyles();

        if (!isValidId) {
            txtPatientProgramid.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Program ID (e.g., PP01).");
        }
        if (!isValidPatientId) {
            ComboPatientID.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Patient ID (e.g., P001).");
        }
        if (!isValidProgramId) {
            ComboProgramID.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Therapy Program ID (e.g., TP001).");
        }
        if (!isValidAttendance) {
            txtAttendance.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Attendance (should be a number between 0-999).");
        }
        if (!isValidOutcome) {
            txtOutCome.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Program Outcome.");
        }
        if (!isValidStatus) {
            comboStatus.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Status. Must be Complete, Incomplete, or Pending.");
        }

        if (!isValidId || !isValidPatientId || !isValidProgramId || !isValidAttendance || !isValidOutcome || !isValidStatus)
            return;

        if (ComboYear.getValue() == null || CombMonth.getValue() == null || ComboDay.getValue() == null) {
            showErrorAlert("Please select a valid date.");
            return;
        }

        String enrollmentDate = ComboYear.getValue() + "-" + CombMonth.getValue() + "-" + ComboDay.getValue();

        try {
            int attendance = Integer.parseInt(attendanceStr);
            PatientProgramDTO patientProgramDTO = new PatientProgramDTO(id, patientId, programId, enrollmentDate, status, attendance, programOutcome);

            boolean isSaved = patientProgramBO.savePatientProgram(patientProgramDTO);
            if (isSaved) {
                showSuccessAlert("Patient Program Saved Successfully!");
                loadAllPatientPrograms();
                clearFields();
                GenerateNextPatientProgramId();
            } else {
                showErrorAlert("Failed to Save Patient Program.");
            }
        } catch (Exception e) {
            showErrorAlert("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void resetFieldStyles() {
        txtPatientProgramid.setStyle(null);
        ComboPatientID.setStyle(null);
        ComboProgramID.setStyle(null);
        txtAttendance.setStyle(null);
        txtOutCome.setStyle(null);
        comboStatus.setStyle(null);
    }
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            String id = colPatientProgramID.getText();
            String patientId = ComboPatientID.getValue();
            String programId = ComboProgramID.getValue();
            String attendanceStr = txtAttendance.getText().trim();
            String programOutcome = txtOutCome.getText().trim();
            String status = comboStatus.getValue();

            // Regex for attendance: must be a positive integer (e.g., 1 to 999)
            String attendancePattern = "^\\d{1,3}$";

            if (!attendanceStr.matches(attendancePattern)) {
                txtAttendance.setStyle("-fx-border-color: #005656;");
                showErrorAlert("Invalid Attendance. Please enter a number between 0 and 999.");
                return;
            } else {
                txtAttendance.setStyle(null); // Reset if valid
            }

            int attendance = Integer.parseInt(attendanceStr);

            if (ComboYear.getValue() == null || CombMonth.getValue() == null || ComboDay.getValue() == null) {
                showErrorAlert("Please select a valid date!");
                return;
            }

            String enrollmentDate = ComboYear.getValue() + "-" + CombMonth.getValue() + "-" + ComboDay.getValue();

            PatientProgramDTO patientProgramDTO = new PatientProgramDTO(
                    id, patientId, programId, enrollmentDate, status, attendance, programOutcome
            );

            boolean isUpdated = patientProgramBO.updatePatientProgram(patientProgramDTO);

            if (isUpdated) {
                showSuccessAlert("Patient Program Updated Successfully!");
                loadAllPatientPrograms();
                clearFields();
            } else {
                showErrorAlert("Failed to Update Patient Program!");
            }

        } catch (Exception e) {
            showErrorAlert("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colPatientProgramID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatientid.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colProgramid.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colattendance.setCellValueFactory(new PropertyValueFactory<>("attendance"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("enrollmentDate"));
        coloutcome.setCellValueFactory(new PropertyValueFactory<>("programOutcome"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        comboStatus.getItems().addAll("Complete", "Incomplete");
        initializeDateCombos();


        try {
            GenerateNextPatientProgramId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        try {
            loadAllPatientPrograms();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            loadPatientIDs();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            loadProgramIDs();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private void initializeDateCombos() {
        ComboYear.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1970, YearMonth.now().getYear()).boxed().toList()
        ));
        ComboYear.getSelectionModel().selectLast();
        CombMonth.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 12).boxed().toList()
        ));
        CombMonth.getSelectionModel().selectFirst();
        updateDays();
    }

    private void loadAllPatientPrograms() throws Exception {

        List<PatientProgramDTO> patientProgramDTOs = patientProgramBO.getAllPatientPrograms();


        ObservableList<PatientProgramTM> patientProgramTMs = FXCollections.observableArrayList();

        // Loop through the DTOs and convert each into a TableModel (TM)
        for (PatientProgramDTO dto : patientProgramDTOs) {
            patientProgramTMs.add(new PatientProgramTM(
                    dto.getId(),
                    dto.getPatientId(),
                    dto.getProgramId(),
                    dto.getEnrollmentDate(),
                    dto.getStatus(),
                    dto.getAttendance(),
                    dto.getProgramOutcome()
            ));
        }


        tblPatientProgram.setItems(patientProgramTMs);
    }
    private void clearFields() throws Exception {
        txtPatientProgramid.clear();
        ComboPatientID.setValue(null);
        ComboProgramID.setValue(null);
        txtAttendance.clear();
        txtOutCome.clear();
        comboStatus.setValue(null);
        ComboYear.setValue(null);
        CombMonth.setValue(null);
        ComboDay.setValue(null);

        GenerateNextPatientProgramId();
    }
    private void loadPatientIDs() throws Exception {
        List<PatientDTO> patientList = patientBO.getAllPatients();
        ComboPatientID.getItems().clear();
        for (PatientDTO patient : patientList) {
            ComboPatientID.getItems().add(patient.getId());  // Add only patient IDs
        }
    }

    private void loadProgramIDs() throws Exception {
        List<ProgramDTO> programList = programBO.getAllPrograms();
        ComboProgramID.getItems().clear();
        for (ProgramDTO program : programList) {
            ComboProgramID.getItems().add(program.getId());  // Add only program IDs
        }
    }
   private  void GenerateNextPatientProgramId() throws Exception {
       String nextPatientID=patientProgramBO.generateNextPatienProgramtID();
       txtPatientProgramid.setText(nextPatientID);
    }



}
