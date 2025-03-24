package lk.ijse.gdse.HealthTheraphyCenter.bo.custom;

import lk.ijse.gdse.HealthTheraphyCenter.bo.SuperBO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.PatientProgramDTO;

import java.util.List;

public interface PatientProgramBO extends SuperBO {
    boolean savePatientProgram(PatientProgramDTO patientProgramDTO) throws Exception;
    boolean updatePatientProgram(PatientProgramDTO patientProgramDTO) throws Exception;
    boolean deletePatientProgram(String id) throws Exception;
    PatientProgramDTO findPatientProgramById(String patientId, String programId) throws Exception;
    String getLastPatientProgramId() throws Exception;
    List<PatientProgramDTO> getAllPatientPrograms() throws Exception;
    String generateNextPatienProgramtID() throws Exception;
}
