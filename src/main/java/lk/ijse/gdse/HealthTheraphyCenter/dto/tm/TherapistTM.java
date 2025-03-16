package lk.ijse.gdse.HealthTheraphyCenter.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TherapistTM {
    private String id;
    private String name;
    private String contactInfo;
    private String specialization;
    private String therapyProgramId;
}
