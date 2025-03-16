package lk.ijse.gdse.HealthTheraphyCenter.bo.custom;

import lk.ijse.gdse.HealthTheraphyCenter.dto.PatientDTO;
import lk.ijse.gdse.HealthTheraphyCenter.bo.SuperBO;

import java.util.List;
import java.util.Optional;

public interface PatientBO extends SuperBO {
    boolean savePatient(PatientDTO dto) throws Exception;
    boolean updatePatient(PatientDTO dto) throws Exception;
    boolean deletePatient(String pk) throws Exception;
    List<PatientDTO> getAllPatients() throws Exception;
    Optional<PatientDTO> findPatientByID(String id) throws Exception;
    Optional<String> getLastPatientID() throws Exception;

}
