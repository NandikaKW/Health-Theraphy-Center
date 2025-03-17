package lk.ijse.gdse.HealthTheraphyCenter.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserTM {
    private String id;
    private String email;
    private String username;
    private String password;
    private Button removeBtn;



}
