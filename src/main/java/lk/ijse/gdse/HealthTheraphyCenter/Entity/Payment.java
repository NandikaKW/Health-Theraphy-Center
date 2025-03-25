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
@Table(name = "payments")
public class Payment implements SuperEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false)
    private double amount; // Fee for the therapy program

    @Column(nullable = false)
    private String paymentDate;

    @Column(nullable = false)
    private String status; // "Pending" or "Completed"

    @OneToOne
    @JoinColumn(name = "therapy_session_id", nullable = false, unique = true)
    private TherapySession therapySession;




}
