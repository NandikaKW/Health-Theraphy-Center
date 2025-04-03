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
    private JFXButton btnUpdate;
    PatientProgramBO patientProgramBO = BoFactory.getInstance().getBO(BoTypes.PATIENT_PROGRAM);
    PatientBO patientBO = BoFactory.getInstance().getBO(BoTypes.PATIENTS);
    ProgramBO programBO = BoFactory.getInstance().getBO(BoTypes.THERAPY_PROGRAMS);

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
            txtStatus.setText(selectedItem.getStatus());


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
    void btnSaveOnAction(ActionEvent event) throws Exception {
        try{
            String id=txtPatientProgramid.getText();
            String patientId=ComboPatientID.getValue();
            String programId=ComboProgramID.getValue();
            int attendance=Integer.parseInt(txtAttendance.getText().trim());
            String programOutcome=txtOutCome.getText().trim();
            String status=txtStatus.getText().trim();

            if (ComboYear.getValue()==null || CombMonth.getValue()==null || ComboDay.getValue()==null){
                new Alert(Alert.AlertType.WARNING,"Please select a valid date!").show();
                return;
            }
            String enrollmentDate=ComboYear.getValue()+"-"+CombMonth.getValue()+"-"+ComboDay.getValue();
            PatientProgramDTO patientProgramDTO=new PatientProgramDTO(id,patientId,programId,enrollmentDate,status,attendance,programOutcome);

            boolean isSaved = patientProgramBO.savePatientProgram(patientProgramDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Patient Program Saved Successfully!").show();
                loadAllPatientPrograms();
                clearFields();
                GenerateNextPatientProgramId();
            }else{
                new Alert(Alert.AlertType.ERROR,"Failed to Save Patient Program!").show();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter valid values for Attendance and Program Outcome.").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred while saving the Patient Program. Please try again later.").show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            String id = colPatientProgramID.getText();
            String patientId = ComboPatientID.getValue();
            String programId = ComboProgramID.getValue();
            int attendance = Integer.parseInt(txtAttendance.getText().trim());
            String programOutcome = txtOutCome.getText().trim();
            String status = txtStatus.getText().trim();


            if (ComboYear.getValue() == null || CombMonth.getValue() == null || ComboDay.getValue() == null) {
                new Alert(Alert.AlertType.WARNING, "Please select a valid date!").show();
                return;
            }


            String enrollmentDate = ComboYear.getValue() + "-" + CombMonth.getValue() + "-" + ComboDay.getValue();


            PatientProgramDTO patientProgramDTO = new PatientProgramDTO(
                    id,
                    patientId, programId, enrollmentDate, status, attendance, programOutcome
            );


            boolean isUpdated = patientProgramBO.updatePatientProgram(patientProgramDTO);


            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Patient Program Updated Successfully!").show();
                loadAllPatientPrograms();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to Update Patient Program!").show();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid attendance input. Please enter a number!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage()).show();
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
        txtStatus.clear();
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
