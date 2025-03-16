package lk.ijse.gdse.HealthTheraphyCenter.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "therapy_programs")
public class TherapyProgram implements SuperEntity{
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id; // Primary Key

    @Column(nullable = false)
    private String name; // e.g., Cognitive Behavioral Therapy

    @Column(nullable = false)
    private int duration; // in weeks or months

    @Column(nullable = false)
    private double fee; // in LKR

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "therapyProgram", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Therapist> therapists;

    @OneToMany(mappedBy = "therapyProgram", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TherapySession> therapySessions;

    @OneToMany(mappedBy = "therapyProgram", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientProgramDetail> patientProgramDetails;


    public TherapyProgram(String id, String name, int duration, double fee, String description) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.fee = fee;
        this.description = description;
    }
}
