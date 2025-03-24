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
public class PatientProgramDetail implements SuperEntity {

    @Id
    @Column(name = "patient_program_id")
    private String patientProgramId; // New Primary Key

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient; // Foreign Key

    @ManyToOne
    @JoinColumn(name = "therapy_program_id", nullable = false)
    private TherapyProgram therapyProgram; // Foreign Key

    @Column(name = "enrollment_date")
    private String enrollmentDate;

    private String status; // e.g., "Active", "Completed"
    private int attendance;
    private String programOutcome;




}
