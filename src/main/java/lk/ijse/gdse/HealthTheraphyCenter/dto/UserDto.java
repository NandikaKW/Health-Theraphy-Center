package lk.ijse.gdse.HealthTheraphyCenter.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    private String id;
    private String email;
    private String username;
    private String password;
}
