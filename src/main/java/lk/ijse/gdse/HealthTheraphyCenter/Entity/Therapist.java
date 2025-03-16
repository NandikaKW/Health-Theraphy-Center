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
@Table(name = "therapists")
public class Therapist implements SuperEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id; // Primary Key


    private String name;
    private String specialization;
    private String contactInfo;

    @ManyToOne
    @JoinColumn(name = "therapy_program_id", nullable = false)
    private TherapyProgram therapyProgram;

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TherapySession> therapySessions;


    public Therapist(String id, String name, String specialization, String contactInfo, TherapyProgram therapyProgram) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.contactInfo = contactInfo;
        this.therapyProgram = therapyProgram;
    }
}
