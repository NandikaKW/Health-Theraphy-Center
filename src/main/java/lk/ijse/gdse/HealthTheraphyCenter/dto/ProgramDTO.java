package lk.ijse.gdse.HealthTheraphyCenter.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProgramDTO {
    private String id;
    private String name;
    private String description;
    private int duration;
    private double fee;
}
