package lk.ijse.gdse.HealthTheraphyCenter.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientTM {
    private String id;
    private String contact;
    private String email;
    private String history;
    private String name;

}
