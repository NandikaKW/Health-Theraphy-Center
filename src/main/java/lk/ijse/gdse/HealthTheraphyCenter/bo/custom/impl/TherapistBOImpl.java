package lk.ijse.gdse.HealthTheraphyCenter.bo.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.Therapist;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.TherapyProgram;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.TherapistBO;
import lk.ijse.gdse.HealthTheraphyCenter.config.FactoryConfiguration;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.ProgramDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.TherapistDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.TherapistDTO;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TherapistBOImpl implements TherapistBO {
    TherapistDAO therapistDAO = DaoFactory.getInstance().getDAO(DaoTypes.THERAPIST);
    ProgramDAO programDAO = DaoFactory.getInstance().getDAO(DaoTypes.THERAPHY_PROGRAM);

    @Override
    public boolean addTherapist(TherapistDTO therapistDTO) {
        try {
            // Fetch the TherapyProgram entity using the provided ID
            Optional<TherapyProgram> therapyProgramOpt = programDAO.findByPK(therapistDTO.getTherapyProgramId());

            if (!therapyProgramOpt.isPresent()) {
                showErrorAlert("Error: Therapy Program with ID " + therapistDTO.getTherapyProgramId() + " not found.");
                return false;
            }

            Therapist therapist = new Therapist(
                    therapistDTO.getId(),
                    therapistDTO.getName(),
                    therapistDTO.getSpecialization(),
                    therapistDTO.getContactInfo(),
                    therapyProgramOpt.get()  // Assign the fetched TherapyProgram entity
            );

            return therapistDAO.save(therapist);
        } catch (Exception e) {
            showErrorAlert("Error while saving therapist: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateTherapist(TherapistDTO therapistDTO) {
        try {
            // Fetch the Therapist entity using the provided ID
            Optional<Therapist> optionalTherapist = therapistDAO.findByPK(therapistDTO.getId());

            if (!optionalTherapist.isPresent()) {
                showErrorAlert("Error: Therapist with ID " + therapistDTO.getId() + " not found.");
                return false;
            }

            // Fetch the TherapyProgram entity using the provided therapy program ID
            Optional<TherapyProgram> therapyProgramOpt = programDAO.findByPK(therapistDTO.getTherapyProgramId());

            if (!therapyProgramOpt.isPresent()) {
                showErrorAlert("Error: Therapy Program with ID " + therapistDTO.getTherapyProgramId() + " not found.");
                return false;
            }

            // Retrieve the existing therapist
            Therapist therapist = optionalTherapist.get();

            // Update the therapist fields
            therapist.setName(therapistDTO.getName());
            therapist.setSpecialization(therapistDTO.getSpecialization());
            therapist.setContactInfo(therapistDTO.getContactInfo());
            therapist.setTherapyProgram(therapyProgramOpt.get());  // Set the new therapy program

            // Save the updated therapist entity
            return therapistDAO.update(therapist);
        } catch (Exception e) {
            showErrorAlert("Error while updating therapist: " + e.getMessage());
            return false;
        }

    }

    @Override
    public boolean deleteTherapist(String therapistId) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.beginTransaction();
            boolean result = therapistDAO.deleteByPK(therapistId);
            transaction.commit();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public TherapistDTO getTherapistById(String therapistId) {
        Optional<Therapist> therapist = therapistDAO.findByPK(therapistId);
        return therapist.map(t -> new TherapistDTO(
                t.getId(),
                t.getName(),
                t.getContactInfo(),
                t.getSpecialization(),
                (t.getTherapyProgram() != null) ? t.getTherapyProgram().getId() : null
        )).orElse(null);
    }

    @Override
    public List<TherapistDTO> getAllTherapists() {
        List<Therapist> therapistList = therapistDAO.getAll();
        return therapistList.stream().map(t -> new TherapistDTO(
                t.getId(),
                t.getName(),
                t.getContactInfo(),
                t.getSpecialization(),
                (t.getTherapyProgram() != null) ? t.getTherapyProgram().getId() : null
        )).collect(Collectors.toList());
    }

    @Override
    public String getLastTherapistId() {
        Optional<String> lastPK = therapistDAO.getLastPK();
        return lastPK.orElse(null);
    }
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
    @Override
    public String generateNextTherapistID() {
        String lastTherapistId = getLastTherapistId();

        if (lastTherapistId == null || lastTherapistId.isEmpty()) {
            return "T001"; // Start from T001 if no records exist
        }

        // Extract the numeric part and increment
        int numericPart = Integer.parseInt(lastTherapistId.substring(1));
        numericPart++;

        // Format the new ID with leading zeros (e.g., T002, T010, T100)
        return String.format("T%03d", numericPart);
    }

}
