package lk.ijse.gdse.HealthTheraphyCenter.dto.tm;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentTM {
    private String id;
    private Double amount;
    private String date;
    private String sessionId;
}
