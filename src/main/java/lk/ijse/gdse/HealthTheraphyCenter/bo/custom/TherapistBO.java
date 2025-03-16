package lk.ijse.gdse.HealthTheraphyCenter.bo.custom;

import lk.ijse.gdse.HealthTheraphyCenter.bo.SuperBO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.TherapistDTO;

import java.util.List;

public interface TherapistBO extends SuperBO {
    boolean addTherapist(TherapistDTO therapistDTO) throws Exception;
    boolean updateTherapist(TherapistDTO therapistDTO) throws Exception;
    boolean deleteTherapist(String therapistId) throws Exception;
    TherapistDTO getTherapistById(String therapistId) throws Exception;
    List<TherapistDTO> getAllTherapists() throws Exception;
    String getLastTherapistId() throws Exception;
    String generateNextTherapistID() throws Exception;
}
