package lk.ijse.gdse.HealthTheraphyCenter.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TherapistDTO {
    private String id;
    private String name;
    private String contactInfo;
    private String specialization;
    private String therapyProgramId;
}
