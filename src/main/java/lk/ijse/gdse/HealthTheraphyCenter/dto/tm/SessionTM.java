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
public class SessionTM {
    private String id;
    private String notes;
    private String status;
    private String date;
    private String patientId;
    private String therapistId;
    private String programId;
}
