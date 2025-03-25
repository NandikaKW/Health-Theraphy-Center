package lk.ijse.gdse.HealthTheraphyCenter.bo.custom.impl;

import lk.ijse.gdse.HealthTheraphyCenter.Entity.Patient;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.Therapist;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.TherapyProgram;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.TherapySession;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.SessionBO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.PatientDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.ProgramDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.SessionDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.TherapistDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.PatientDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.ProgramDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.SessionDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.TherapistDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionBOImpl implements SessionBO {
    SessionDAO sessionDAO = DaoFactory.getInstance().getDAO(DaoTypes.SESSION);
    PatientDAO patientDAO = DaoFactory.getInstance().getDAO(DaoTypes.PATIENT);
    TherapistDAO therapistDAO = DaoFactory.getInstance().getDAO(DaoTypes.THERAPIST);
    ProgramDAO programDAO = DaoFactory.getInstance().getDAO(DaoTypes.THERAPHY_PROGRAM);

    @Override
    public String generateNextTherapySessionID() {
        Optional<String> lastTherapyProgramID = sessionDAO.getLastPK();

        if (lastTherapyProgramID.isPresent()) {
            String lastID = lastTherapyProgramID.get(); // e.g., "TP005"
            if (lastID.startsWith("TS")) {
                int numericPart = Integer.parseInt(lastID.substring(2));
                numericPart++;
                return String.format("TS%03d", numericPart);
            } else {
                return "TS001";
            }
        } else {
            return "TS001"; // Default if no therapy programs exist
        }
    }

    @Override
    public boolean saveSession(SessionDTO sessionDTO) {
        Patient patient = null;
        Therapist therapist = null;
        TherapyProgram program = null;

        Optional<Patient> patientOpt = patientDAO.findByPK(sessionDTO.getPatientId());
        if (patientOpt.isPresent()) {
            patient = patientOpt.get();
        }

        Optional<Therapist> therapistOpt = therapistDAO.findByPK(sessionDTO.getTherapistId());
        if (therapistOpt.isPresent()) {
            therapist = therapistOpt.get();
        }

        Optional<TherapyProgram> programOpt = programDAO.findByPK(sessionDTO.getProgramId());
        if (programOpt.isPresent()) {
            program = programOpt.get();
        }

        TherapySession session = new TherapySession();
        session.setId(sessionDTO.getId());
        session.setSessionNotes(sessionDTO.getNotes());
        session.setSessionDate(sessionDTO.getDate());
        session.setStatus(sessionDTO.getStatus());
        session.setPatient(patient);
        session.setTherapist(therapist);
        session.setTherapyProgram(program);

        return sessionDAO.save(session);
    }


    @Override
    public boolean deleteSession(String sessionId) throws Exception {
        return sessionDAO.deleteByPK(sessionId);
    }

    @Override
    public boolean updateSession(SessionDTO sessionDTO) {
        Patient patient = null;
        Therapist therapist = null;
        TherapyProgram program = null;

        Optional<Patient> patientOpt = patientDAO.findByPK(sessionDTO.getPatientId());
        if (patientOpt.isPresent()) {
            patient = patientOpt.get();
        }

        Optional<Therapist> therapistOpt = therapistDAO.findByPK(sessionDTO.getTherapistId());
        if (therapistOpt.isPresent()) {
            therapist = therapistOpt.get();
        }

        Optional<TherapyProgram> programOpt = programDAO.findByPK(sessionDTO.getProgramId());
        if (programOpt.isPresent()) {
            program = programOpt.get();
        }

        TherapySession session = new TherapySession();
        session.setId(sessionDTO.getId());
        session.setSessionNotes(sessionDTO.getNotes());
        session.setSessionDate(sessionDTO.getDate());
        session.setStatus(sessionDTO.getStatus());
        session.setPatient(patient);
        session.setTherapist(therapist);
        session.setTherapyProgram(program);

        return sessionDAO.update(session);
    }

    @Override
    public List<SessionDTO> getAllSessions() throws Exception {
        List<TherapySession> all = sessionDAO.getAll();
        List<SessionDTO> sessionDTOList = new ArrayList<>();
        for (TherapySession session : all) {
            SessionDTO sessionDTO = new SessionDTO();
            sessionDTO.setId(session.getId());
            sessionDTO.setDate(session.getSessionDate());
            sessionDTO.setNotes(session.getSessionNotes());
            sessionDTO.setStatus(session.getStatus());
            sessionDTO.setProgramId(session.getTherapyProgram().getId());
            sessionDTO.setTherapistId(session.getTherapist().getId());
            sessionDTO.setPatientId(session.getPatient().getId());

            sessionDTOList.add(sessionDTO);
        }
        return sessionDTOList;
    }

    @Override
    public Optional<SessionDTO> findSessionByID(String id) throws Exception {
        Optional<TherapySession> sessionOpt = sessionDAO.findByPK(id);
        return sessionOpt.map(session -> new SessionDTO(
                session.getId(),
                session.getSessionNotes(),
                session.getStatus(),
                session.getPatient().getId(),
                session.getTherapist().getId(),
                session.getTherapyProgram().getId(),
                session.getSessionDate()
        ));
    }

    @Override
    public ArrayList<String> getAllPatientIDs() throws Exception {
        List<Patient> patients = patientDAO.getAll();
        ArrayList<String> patientIDs = new ArrayList<>();
        for (Patient patient : patients) {
            patientIDs.add(patient.getId());
        }

        return patientIDs;
    }

    @Override
    public ArrayList<String> getAllTherapistIDs() throws Exception {
        List<Therapist> therapists = therapistDAO.getAll();
        ArrayList<String> therapistIds = new ArrayList<>();
        for (Therapist therapist : therapists) {
            therapistIds.add(therapist.getId());
        }

        return therapistIds;
    }

    @Override
    public ArrayList<String> getAllProgramIDs() throws Exception {
        List<TherapyProgram> programs = programDAO.getAll();
        ArrayList<String> programIds = new ArrayList<>();
        for (TherapyProgram therapyProgram : programs) {
            programIds.add(therapyProgram.getId());
        }

        return programIds;
    }

    @Override
    public Optional<PatientDTO> findPatientByID(String id) throws Exception {
        Optional<Patient> patientOpt = patientDAO.findByPK(id);
        return patientOpt.map(patient -> new PatientDTO(
                patient.getId(),
                patient.getContactInfo(),
                patient.getEmail(),
                patient.getMedicalHistory(),
                patient.getName()
        ));
    }

    @Override
    public Optional<TherapistDTO> findTherapistByID(String id) throws Exception {
        Optional<Therapist> therapistOpt = therapistDAO.findByPK(id);
        return therapistOpt.map(therapist -> new TherapistDTO(
                therapist.getId(),
                therapist.getName(),
                therapist.getContactInfo(),
                therapist.getSpecialization(),
                therapist.getTherapyProgram().getId()
        ));
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
}
