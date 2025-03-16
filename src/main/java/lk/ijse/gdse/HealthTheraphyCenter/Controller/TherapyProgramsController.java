package lk.ijse.gdse.HealthTheraphyCenter.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.BoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.PatientBO;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.ProgramBO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.ProgramDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.tm.ProgramTM;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TherapyProgramsController implements Initializable {

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<ProgramTM, String> colDescription;

    @FXML
    private TableColumn<ProgramTM, Integer> colDuration;

    @FXML
    private TableColumn<ProgramTM, String> colID;

    @FXML
    private TableColumn<ProgramTM, String> colName;

    @FXML
    private TableColumn<ProgramTM, Double> colcost;

    @FXML
    private TableView<ProgramTM> tblPrograms;

    @FXML
    private TextField txtCost;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtDuration;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtid;
    ProgramBO programBO = BoFactory.getInstance().getBO(BoTypes.THERAPY_PROGRAMS);

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
            String programId = txtid.getText();
            boolean isDeleted = programBO.deleteProgram(programId);
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Program deleted successfully").show();
                refreshTable();
                clearFields();
                GenerateNextId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete program").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }


    }

    private void refreshTable() {
        try {
            List<ProgramDTO> programs = programBO.getAllPrograms();
            tblPrograms.getItems().clear();
            for (ProgramDTO dto : programs) {
                tblPrograms.getItems().add(new ProgramTM(
                        dto.getId(),
                        dto.getName(),
                        dto.getDescription(),
                        dto.getDuration(),
                        dto.getFee()
                ));
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnRefreshOnAction(ActionEvent event) throws Exception {
        clearFields();
        refreshTable();
        GenerateNextId();

    }

    private void clearFields() {
        txtid.clear();
        txtName.clear();
        txtDescription.clear();
        txtDuration.clear();
        txtCost.clear();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        try {
            String id = txtid.getText();
            String name = txtName.getText();
            String description = txtDescription.getText();
            int duration = Integer.parseInt(txtDuration.getText());
            double fee = Double.parseDouble(txtCost.getText());

            ProgramDTO programDTO = new ProgramDTO(id, name, description, duration, fee);
            boolean isSaved = programBO.saveProgram(programDTO);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Program saved successfully").show();
                refreshTable();
                clearFields();
                GenerateNextId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save program").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            String id = txtid.getText();
            String name = txtName.getText();
            String description = txtDescription.getText();
            int duration = Integer.parseInt(txtDuration.getText());
            double fee = Double.parseDouble(txtCost.getText());

            ProgramDTO programDTO = new ProgramDTO(id, name, description, duration, fee);
            boolean isUpdated = programBO.updateProgram(programDTO);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Program updated successfully").show();
                refreshTable();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update program").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }

    }

    @FXML
    void onClickTable(MouseEvent event) {
        ProgramTM selectedProgram = tblPrograms.getSelectionModel().getSelectedItem();
        if (selectedProgram != null) {
            txtid.setText(selectedProgram.getId());
            txtName.setText(selectedProgram.getName());
            txtDescription.setText(selectedProgram.getDescription());
            txtDuration.setText(String.valueOf(selectedProgram.getDuration()));
            txtCost.setText(String.valueOf(selectedProgram.getFee()));
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colcost.setCellValueFactory(new PropertyValueFactory<>("fee"));
        try {
            refreshPage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void refreshPage() throws Exception {
        refreshTable();
        GenerateNextId();
        txtName.clear();
        txtDescription.clear();
        txtDuration.clear();
        txtCost.clear();
    }
    private void  GenerateNextId() throws Exception {
        String nextProgramID = programBO.generateNextTherapyProgramID();
        txtid.setText(nextProgramID); // Set the next program ID first
    }
}
