package lk.ijse.gdse.HealthTheraphyCenter.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PatientsProgramsIDS {
    @Column(name = "patient_id")
    private String patientId;

    @Column(name = "therapy_program_id")
    private String therapyProgramId;


}
