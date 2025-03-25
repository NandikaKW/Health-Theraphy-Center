package lk.ijse.gdse.HealthTheraphyCenter.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDTO {
    private String id;
    private Double amount;
    private String date;
    private String sessionId;
}
