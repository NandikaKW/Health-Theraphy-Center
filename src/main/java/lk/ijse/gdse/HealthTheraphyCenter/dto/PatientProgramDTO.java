package lk.ijse.gdse.HealthTheraphyCenter.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatientProgramDTO {
    private String id;
    private String patientId;
    private String programId;
    private String enrollmentDate;
    private String status;
    private int attendance;
    private String programOutcome;

}
