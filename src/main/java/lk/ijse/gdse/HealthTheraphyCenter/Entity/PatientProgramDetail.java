package lk.ijse.gdse.HealthTheraphyCenter.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "patient_program_details")
public class PatientProgramDetail {

    @EmbeddedId
    private PatientsProgramsIDS patientsProgramsIDS;



    @ManyToOne
    @MapsId("patientId")
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @MapsId("therapyProgramId")
    @JoinColumn(name = "therapy_program_id", nullable = false)
    private TherapyProgram therapyProgram;

    private Date enrollmentDate;
    private String status; // e.g., "Active", "Completed"

}
