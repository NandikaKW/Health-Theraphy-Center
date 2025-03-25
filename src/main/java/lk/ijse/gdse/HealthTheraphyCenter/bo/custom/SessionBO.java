package lk.ijse.gdse.HealthTheraphyCenter.bo.custom;

import lk.ijse.gdse.HealthTheraphyCenter.bo.SuperBO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.PatientDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.ProgramDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.SessionDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.TherapistDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SessionBO extends SuperBO {
    String generateNextTherapySessionID();
    boolean saveSession(SessionDTO sessionDTO);
    boolean deleteSession(String sessionId) throws Exception;
    boolean updateSession(SessionDTO sessionDTO);
    List<SessionDTO> getAllSessions() throws Exception;
    Optional<SessionDTO> findSessionByID(String id) throws Exception;
    ArrayList<String> getAllPatientIDs() throws Exception;
    ArrayList<String> getAllTherapistIDs() throws Exception;
    ArrayList<String> getAllProgramIDs() throws Exception;
    Optional<PatientDTO> findPatientByID(String id) throws Exception;
    Optional<TherapistDTO> findTherapistByID(String id) throws Exception;
    Optional<ProgramDTO> findProgramByID(String id) throws Exception;
}
