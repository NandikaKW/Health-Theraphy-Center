package lk.ijse.gdse.HealthTheraphyCenter.bo.custom.impl;

import lk.ijse.gdse.HealthTheraphyCenter.Entity.Patient;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.PatientProgramDetail;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.TherapyProgram;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.PatientProgramBO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.PatientDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.PatientProgramDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.ProgramDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.PatientProgramDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PatientProgramBOImpl implements PatientProgramBO {
    PatientDAO patientDAO = DaoFactory.getInstance().getDAO(DaoTypes.PATIENT);
    ProgramDAO programDAO = DaoFactory.getInstance().getDAO(DaoTypes.THERAPHY_PROGRAM);
    PatientProgramDAO patientProgramDAO = DaoFactory.getInstance().getDAO(DaoTypes.PATIENT_PROGRAM);

    @Override
    public boolean savePatientProgram(PatientProgramDTO patientProgramDTO) throws Exception {
        try{
            Patient patient = patientDAO.findById(patientProgramDTO.getPatientId());
            if (patient == null) {
                throw new IllegalArgumentException("Invalid Patient ID");
            }
            TherapyProgram therapyProgram = programDAO.findById(patientProgramDTO.getProgramId());
            if (therapyProgram == null) {
                throw new IllegalArgumentException("Invalid Program ID");
            }
            PatientProgramDetail patientProgramDetail=new PatientProgramDetail(
                    patientProgramDTO.getId(),
                    patient,
                    therapyProgram,
                    patientProgramDTO.getEnrollmentDate(),
                    patientProgramDTO.getStatus(),
                    patientProgramDTO.getAttendance(),
                    patientProgramDTO.getProgramOutcome()
            );
            return patientProgramDAO.save(patientProgramDetail);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updatePatientProgram(PatientProgramDTO patientProgramDTO) {
        try {

            Patient patient = patientDAO.findById(patientProgramDTO.getPatientId());
            if (patient == null) {
                throw new IllegalArgumentException("Invalid Patient ID");
            }

            TherapyProgram therapyProgram = programDAO.findById(patientProgramDTO.getProgramId());
            if (therapyProgram == null) {
                throw new IllegalArgumentException("Invalid Program ID");
            }


            PatientProgramDetail patientProgramDetail = new PatientProgramDetail(
                    patientProgramDTO.getId(),
                    patient,
                    therapyProgram,
                    patientProgramDTO.getEnrollmentDate(),
                    patientProgramDTO.getStatus(),
                    patientProgramDTO.getAttendance(),
                    patientProgramDTO.getProgramOutcome()
            );


            return patientProgramDAO.update(patientProgramDetail);

        } catch (Exception e) {
            throw new RuntimeException("Error updating Patient Program", e);
        }
    }


    @Override
    public boolean deletePatientProgram(String id) {
        try {
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("Invalid Patient Program ID");
            }

            return patientProgramDAO.deleteByPK(id);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public List<PatientProgramDTO> getAllPatientPrograms() {

        return patientProgramDAO.getAll().stream().map(patientProgramDetail -> new PatientProgramDTO(
                patientProgramDetail.getPatientProgramId(),
                patientProgramDetail.getPatient().getId(),
                patientProgramDetail.getTherapyProgram().getId(),
                patientProgramDetail.getEnrollmentDate(),
                patientProgramDetail.getStatus(),
                patientProgramDetail.getAttendance(),
                patientProgramDetail.getProgramOutcome()
        )).collect(Collectors.toList());
    }

    @Override
    public PatientProgramDTO findPatientProgramById(String patientId, String programId) {
       return null;
    }

    @Override
    public String getLastPatientProgramId() {
        return null;
    }
    @Override
    public String generateNextPatienProgramtID() {
        Optional<String> lastPatientProgramID = patientProgramDAO.getLastPK(); // Fetch the last PatientProgramID

        if (lastPatientProgramID.isPresent()) {
            String lastID = lastPatientProgramID.get();
            int numericPart = Integer.parseInt(lastID.substring(2));
            numericPart++;
            return String.format("PP%02d", numericPart);
        } else {
            return "PP01";
        }
    }




}
