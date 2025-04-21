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
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.PaymentBO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.PaymentDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.tm.PaymentTM;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    @FXML
    public Label lblDate;

    @FXML
    private TextField txtid;

    @FXML
    private TextField txtAmount;

    @FXML
    private ComboBox<String> cmbSession;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private TableView<PaymentTM> tblPayments;

    @FXML
    private TableColumn<PaymentTM, String> colID;

    @FXML
    private TableColumn<PaymentTM, Date> colDate;

    @FXML
    private TableColumn<PaymentTM, Double> colAmount;

    @FXML
    private TableColumn<PaymentTM, String> colSessionID;

    PaymentBO paymentBO = BoFactory.getInstance().getBO(BoTypes.PAYMENT);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colSessionID.setCellValueFactory(new PropertyValueFactory<>("sessionId"));

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
        String nextProgramID = paymentBO.generateNextTherapyPaymentID();
        txtid.setText(nextProgramID);
    }

    private void refreshTable() {
        try {
            List<PaymentDTO> paymentDTOS = paymentBO.getAllPayments();
            ObservableList<PaymentTM> paymentTMS = FXCollections.observableArrayList();
            for (PaymentDTO dto : paymentDTOS) {
                paymentTMS.add(new PaymentTM(dto.getId(), dto.getAmount(), dto.getDate(), dto.getSessionId()));
            }
            tblPayments.setItems(paymentTMS);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    private void clearFields() throws Exception {
        txtAmount.clear();
        cmbSession.getItems().clear();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        lblDate.setText(LocalDateTime.now().format(dateFormatter));
        loadCmbSession();
    }

    private void loadCmbSession() throws Exception {
        ArrayList<String> sessionIDs = paymentBO.getAllSessionIDs();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(sessionIDs);
        cmbSession.setItems(observableList);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        // Get inputs
        String id = txtid.getText().trim();
        String amountText = txtAmount.getText().trim();
        String sessionId = cmbSession.getValue();
        String date = lblDate.getText();

        // Regex patterns
        String idPattern = "^P\\d{3}$"; // e.g., P001
        String sessionPattern = "^TS\\d{3}$"; // Session ID e.g., TS001
        String amountPattern = "^[0-9]+(\\.[0-9]{1,2})?$"; // Decimal or whole number

        boolean isValid = true;

        // Validate Payment ID
        if (!id.matches(idPattern)) {
            txtid.setStyle("-fx-border-color:  #005656;");
            showErrorAlert("Invalid Payment ID. Format must be 'PYxxx'.");
            isValid = false;
        } else {
            txtid.setStyle(null);
        }

        // Validate Amount
        if (!amountText.matches(amountPattern)) {
            txtAmount.setStyle("-fx-border-color:  #005656;");
            showErrorAlert("Invalid amount. Enter a valid number (e.g., 100.00).");
            isValid = false;
        } else {
            txtAmount.setStyle(null);
        }

        // Validate Session ID
        if (sessionId == null || !sessionId.matches(sessionPattern)) {
            cmbSession.setStyle("-fx-border-color:  #005656;");
            showErrorAlert("Invalid Session ID. Format must be 'TSxxx'.");
            isValid = false;
        } else {
            cmbSession.setStyle(null);
        }

        if (!isValid) return;

        try {
            double amount = Double.parseDouble(amountText);
            PaymentDTO paymentDTO = new PaymentDTO(id, amount, date, sessionId);
            boolean isSaved = paymentBO.savePayment(paymentDTO);

            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Payment Saved", "Payment record saved successfully!");
                refreshPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Save Failed", "Payment record could not be saved.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Exception Occurred", e.getMessage());
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
    void btnDeleteOnAction(ActionEvent event) {
        PaymentTM selectedPayment = tblPayments.getSelectionModel().getSelectedItem();

        if (selectedPayment == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "No Selection", "Please select a payment to delete.");
            return;
        }

        try {
            boolean isDeleted = paymentBO.deletePayment(selectedPayment.getId());

            if (isDeleted) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Payment Deleted", "Payment record deleted successfully!");
                refreshPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Deletion Failed", "Could not delete the payment.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Exception Occurred", e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        PaymentTM selectedPayment = tblPayments.getSelectionModel().getSelectedItem();

        if (selectedPayment == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "No Selection", "Please select a payment to update.");
            return;
        }

        String id = txtid.getText().trim();
        String amountText = txtAmount.getText().trim();
        String sessionId = cmbSession.getValue();
        String date = lblDate.getText();

        // Updated regex patterns
        String idPattern = "^P\\d{3}$";        // Payment ID: P001
        String sessionPattern = "^TS\\d{3}$";  // Session ID: TS001
        String amountPattern = "^[0-9]+(\\.[0-9]{1,2})?$";

        boolean isValid = true;

        if (!id.matches(idPattern)) {
            txtid.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Payment ID. Format must be 'Pxxx'.");
            isValid = false;
        } else {
            txtid.setStyle(null);
        }

        if (!amountText.matches(amountPattern)) {
            txtAmount.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid amount. Use a valid number like 250 or 99.99.");
            isValid = false;
        } else {
            txtAmount.setStyle(null);
        }

        if (sessionId == null || !sessionId.matches(sessionPattern)) {
            cmbSession.setStyle("-fx-border-color: #005656;");
            showErrorAlert("Invalid Session ID. Format must be 'TSxxx'.");
            isValid = false;
        } else {
            cmbSession.setStyle(null);
        }

        if (!isValid) return;

        try {
            double amount = Double.parseDouble(amountText);
            PaymentDTO paymentDTO = new PaymentDTO(id, amount, date, sessionId);
            boolean isUpdated = paymentBO.updatePayment(paymentDTO);

            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Payment Updated", "Payment record updated successfully!");
                refreshPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Update Failed", "Could not update the payment.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Exception Occurred", e.getMessage());
        }
    }


    @FXML
    void onClickTable(MouseEvent event) {
        PaymentTM selectedPayment = tblPayments.getSelectionModel().getSelectedItem();

        if (selectedPayment != null) {
            txtid.setText(selectedPayment.getId());
            txtAmount.setText(String.valueOf(selectedPayment.getAmount()));
            cmbSession.setValue(selectedPayment.getSessionId());
            lblDate.setText(selectedPayment.getDate().toString());
        }
    }

    private boolean validateFields() {
        if (txtid.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Validation Error", "Payment ID cannot be empty.");
            return false;
        }

        if (cmbSession.getValue().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Validation Error", "Session ID cannot be empty.");
            return false;
        }

        if (txtAmount.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Validation Error", "Amount cannot be empty.");
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

    @FXML
    void btnRefreshOnAction(ActionEvent event) throws Exception {
        refreshPage();
    }

}
