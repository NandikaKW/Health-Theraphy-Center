package lk.ijse.gdse.HealthTheraphyCenter.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.PatientBO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.PatientDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.tm.PatientTM;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PatientsFormController implements Initializable {

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<PatientTM, String> colContact;

    @FXML
    private TableColumn<PatientTM, String> colEmail;

    @FXML
    private TableColumn<PatientTM, String> colHistory;

    @FXML
    private TableColumn<PatientTM, String> colID;

    @FXML
    private TableColumn<PatientTM, String> colName;

    @FXML
    private TableView<PatientTM> tablePatients;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtHistory;

    @FXML
    private TextField txtID;
    @FXML
    private JFXButton btnReport;

    @FXML
    private TextField txtName;
    PatientBO patientBO = BoFactory.getInstance().getBO(BoTypes.PATIENTS);

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtID.getText();
        if (id.isEmpty()) {
            showErrorAlert("Select a patient to delete.");
            return;
        }

        try {
            boolean isDeleted = patientBO.deletePatient(id);
            if (isDeleted) {
                showSuccessAlert("Patient deleted successfully!");
                refreshTable();
                clearFields();
                GenerateNextPatientID();
            } else {
                showErrorAlert("Failed to delete patient.");
            }
        } catch (Exception e) {
            showErrorAlert("Error: " + e.getMessage());
        }

    }

    @FXML
    void btnRefreshOnAction(ActionEvent event) throws Exception {
        refreshTable();
        clearFields();
        GenerateNextPatientID();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtID.getText();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String email = txtEmail.getText();
        String history = txtHistory.getText();

        if (id.isEmpty() || name.isEmpty()) {
            showErrorAlert("Patient ID and Name are required.");
            return;
        }

        PatientDTO patientDTO = new PatientDTO(id, contact, email, history, name);
        try {
            boolean isSaved = patientBO.savePatient(patientDTO);
            if (isSaved) {
                showSuccessAlert("Patient saved successfully!");
                refreshTable();
                clearFields();
                GenerateNextPatientID();
            } else {
                showErrorAlert("Failed to save patient.");
            }
        } catch (Exception e) {
            showErrorAlert("Error: " + e.getMessage());
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtID.getText();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String email = txtEmail.getText();
        String history = txtHistory.getText();

//        if (id.isEmpty()) {
//            showErrorAlert("Select a patient to update.");
//            return;
//        }
//
//        PatientDTO patientDTO = new PatientDTO(id, contact, email, history, name);
//        try {
//            boolean isUpdated = patientBO.updatePatient(patientDTO);
//            if (isUpdated) {
//                showSuccessAlert("Patient updated successfully!");
//                refreshPage();
//                clearFields();
//            } else {
//                showErrorAlert("Failed to update patient.");
//            }
//        } catch (Exception e) {
//            showErrorAlert("Error: " + e.getMessage());
//        }

            PatientDTO patientDTO = new PatientDTO(id, contact, email, history, name);
            try {
                boolean isUpdated = patientBO.updatePatient(patientDTO);
                if (isUpdated) {
                    showSuccessAlert("Patient updated successfully!");
                    refreshPage();
                    clearFields();
                } else {
                    showErrorAlert("Failed to update patient.");
                }
            } catch (Exception e) {
                showErrorAlert("Error: " + e.getMessage());
            }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colHistory.setCellValueFactory(new PropertyValueFactory<>("history"));
        try {
            refreshPage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private void refreshPage() throws Exception {
        refreshTable();
        String nextPatientID=patientBO.generateNextPatientID();
        txtID.setText(nextPatientID);

        txtContact.setText("");
        txtEmail.setText("");
        txtHistory.setText("");
//        txtID.setText("");
        txtName.setText("");

    }

    private void refreshTable() throws Exception {
        try {
            List<PatientDTO> allPatients = patientBO.getAllPatients();
            ObservableList<PatientTM> patientList = FXCollections.observableArrayList(
                    allPatients.stream().map(patient -> new PatientTM(
                            patient.getId(),
                            patient.getContact(),
                            patient.getEmail(),
                            patient.getHistory(),
                            patient.getName()
                    )).collect(Collectors.toList())
            );
            tablePatients.setItems(patientList);
        } catch (Exception e) {
            showErrorAlert("Error loading patients: " + e.getMessage());
        }



    }


    public void onClickTable(MouseEvent mouseEvent) {
        PatientTM selectedPatient = tablePatients.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            txtID.setText(selectedPatient.getId());
            txtName.setText(selectedPatient.getName());
            txtContact.setText(selectedPatient.getContact());
            txtEmail.setText(selectedPatient.getEmail());
            txtHistory.setText(selectedPatient.getHistory());
        }

    }
    private void clearFields() {
        txtID.clear();
        txtName.clear();
        txtContact.clear();
        txtEmail.clear();
        txtHistory.clear();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void GenerateNextPatientID() throws Exception {
        String nextPatientID = patientBO.generateNextPatientID();
        txtID.setText(nextPatientID);
    }
    @FXML
    void openSendMailModel(ActionEvent event) {
        PatientTM selectedPatient = tablePatients.getSelectionModel().getSelectedItem();

        if (selectedPatient == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a patient to send an email.").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Email.fxml"));
            Parent load = loader.load();

            EmailController emailController = loader.getController();
            emailController.setCustomerEmail(selectedPatient.getEmail());

            Stage stage = new Stage();
            stage.setScene(new Scene(load));
            stage.setTitle("Send Email");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imges/icons8-open-email-24.png")));
            stage.initModality(Modality.APPLICATION_MODAL);

            Window parentWindow = ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.initOwner(parentWindow);
            stage.showAndWait();

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load the email sending interface. Please try again later.").show();
            e.printStackTrace();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
            e.printStackTrace();
        }

    }
    @FXML
    void btnReportOnAction(ActionEvent event) {

    }


}
