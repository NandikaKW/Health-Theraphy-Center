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
@Table(name = "therapy_sessions")
public class TherapySession {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private Date sessionDate;
    private String sessionNotes;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "therapy_program_id", nullable = false)
    private TherapyProgram therapyProgram;

    @ManyToOne
    @JoinColumn(name = "therapist_id", nullable = false)
    private Therapist therapist;


    @OneToOne(mappedBy = "therapySession", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;





}
