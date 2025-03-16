package lk.ijse.gdse.HealthTheraphyCenter.bo.custom;

import lk.ijse.gdse.HealthTheraphyCenter.bo.SuperBO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.PatientDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.ProgramDTO;

import java.util.List;
import java.util.Optional;

public interface ProgramBO extends SuperBO {
    boolean saveProgram(ProgramDTO dto) throws Exception;
    boolean updateProgram(ProgramDTO dto) throws Exception;
    boolean deleteProgram(String pk) throws Exception;
    List<ProgramDTO> getAllPrograms() throws Exception;
    Optional<ProgramDTO> findProgramByID(String id) throws Exception;
    Optional<String> getLastProgramID() throws Exception;
    String generateNextTherapyProgramID() throws Exception;
    String loadCurrentProgramID() throws Exception;

}
