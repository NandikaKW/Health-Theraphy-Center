package lk.ijse.gdse.HealthTheraphyCenter.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientProgramTM {
    private String id;
    private String patientId;
    private String programId;
    private String enrollmentDate;
    private String status;
    private int attendance;
    private String programOutcome;

}
