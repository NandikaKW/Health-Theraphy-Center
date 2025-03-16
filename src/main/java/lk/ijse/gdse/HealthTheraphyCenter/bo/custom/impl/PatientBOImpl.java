package lk.ijse.gdse.HealthTheraphyCenter.bo.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.Patient;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.PatientDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.PatientDTO;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.PatientBO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PatientBOImpl implements PatientBO {
    PatientDAO patientDAO = DaoFactory.getInstance().getDAO(DaoTypes.PATIENT);
    @Override
    public boolean savePatient(PatientDTO dto) {
        try {
            // Convert DTO to Entity
            Patient patient = new Patient(
                    dto.getId(),
                    dto.getContact(),
                    dto.getEmail(),
                    dto.getHistory(),
                    dto.getName()
            );

            // Call DAO to save the patient
            return patientDAO.save(patient);

        } catch (Exception e) {
            showErrorAlert("Error while saving patient: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updatePatient(PatientDTO dto) {
        try {
            patientDAO.update(new Patient(
                    dto.getId(),
                    dto.getContact(),
                    dto.getEmail(),
                    dto.getHistory(),
                    dto.getName()
            ));
            return true;
        } catch (Exception e) {
            showErrorAlert("Error while updating patient: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deletePatient(String id) {
        try {
            return patientDAO.deleteByPK(id);
        } catch (Exception e) {
            showErrorAlert("Error while deleting patient: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return patientDAO.getAll().stream()
                .map(patient -> new PatientDTO(
                        patient.getId(),
                        patient.getContactInfo(),
                        patient.getEmail(),
                        patient.getMedicalHistory(),
                        patient.getName()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PatientDTO> findPatientByID(String id) {
        return patientDAO.findByPK(id)
                .map(patient -> new PatientDTO(
                        patient.getId(),
                        patient.getContactInfo(),
                        patient.getEmail(),
                        patient.getMedicalHistory(),
                        patient.getName()
                ));
    }

    @Override
    public Optional<String> getLastPatientID() {
        return patientDAO.getLastPK();
    }

    // Helper method to show error alerts
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
