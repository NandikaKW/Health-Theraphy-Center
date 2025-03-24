package lk.ijse.gdse.HealthTheraphyCenter.bo.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.Patient;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.TherapyProgram;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.ProgramBO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.PatientDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.ProgramDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.PatientDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.ProgramDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProgramBOImpl implements ProgramBO {
    ProgramDAO programDAO = DaoFactory.getInstance().getDAO(DaoTypes.THERAPHY_PROGRAM);
    @Override
    public boolean saveProgram(ProgramDTO dto) throws Exception {
        try {
            // Convert DTO to Entity
            TherapyProgram therapyProgram = new TherapyProgram(
                    dto.getId(),
                    dto.getName(),
                    dto.getDuration(),
                    dto.getFee(),
                    dto.getDescription()
            );

            // Save the therapy program
            return programDAO.save(therapyProgram);

        } catch (Exception e) {
            showErrorAlert("Error while saving program: " + e.getMessage());
            return false;
        }
    }


    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public boolean updateProgram(ProgramDTO dto) throws Exception {
        try {

            TherapyProgram therapyProgram = new TherapyProgram(
                    dto.getId(),
                    dto.getName(),
                    dto.getDuration(),
                    dto.getFee(),
                    dto.getDescription()
            );


            return programDAO.update(therapyProgram);
        } catch (Exception e) {
            showErrorAlert("Error while updating program: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteProgram(String id) throws Exception {
        try {
            return programDAO.deleteByPK(id);
        } catch (Exception e) {
            showErrorAlert("Error while deleting program: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<ProgramDTO> getAllPrograms() throws Exception {
        return programDAO.getAll().stream()
                .map(program -> new ProgramDTO(
                        program.getId(),
                        program.getName(),
                        program.getDescription(),
                        program.getDuration(),
                        program.getFee()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProgramDTO> findProgramByID(String id) throws Exception {
        Optional<TherapyProgram> programOpt = programDAO.findByPK(id);
        return programOpt.map(program -> new ProgramDTO(
                program.getId(),
                program.getName(),
                program.getDescription(),
                program.getDuration(),
                program.getFee()
        ));
    }

    @Override
    public Optional<String> getLastProgramID() throws Exception {
        return programDAO.getLastPK();
    }
    @Override
    public String generateNextTherapyProgramID() {
        Optional<String> lastTherapyProgramID = programDAO.getLastPK();

        if (lastTherapyProgramID.isPresent()) {
            String lastID = lastTherapyProgramID.get(); // e.g., "TP005"
            int numericPart = Integer.parseInt(lastID.substring(2)); // Extract numeric part (005 -> 5)
            numericPart++; // Increment the numeric part
            return String.format("TP%03d", numericPart); // Format back to TP001, TP002, etc.
        } else {
            return "TP001"; // Default if no therapy programs exist
        }
    }
    @Override
    public String loadCurrentProgramID() {
        Optional<String> lastProgramID = programDAO.getLastPK();
        return lastProgramID.orElse(null); // Return the last existing program ID, or null if none exists
    }


}
