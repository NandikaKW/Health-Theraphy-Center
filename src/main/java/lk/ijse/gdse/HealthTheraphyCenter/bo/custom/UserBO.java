package lk.ijse.gdse.HealthTheraphyCenter.bo.custom;

import lk.ijse.gdse.HealthTheraphyCenter.bo.SuperBO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.UserDto;

import java.util.List;

public interface UserBO extends SuperBO {
    boolean saveUser(UserDto userDto) throws Exception;
    boolean updateUser(UserDto userDto) throws Exception;
    boolean deleteUser(String id) throws Exception;
    List<UserDto> getAllUsers() throws Exception;
    UserDto getUserById(String id) throws Exception;
    int getUserCount() throws Exception;
    UserDto getUserByName(String username) throws Exception;
}
