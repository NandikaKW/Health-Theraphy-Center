package lk.ijse.gdse.HealthTheraphyCenter.dto;

import lombok.*;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SessionDTO {
    private String id;
    private String notes;
    private String status;
    private String patientId;
    private String therapistId;
    private String programId;
    private String date;
}
