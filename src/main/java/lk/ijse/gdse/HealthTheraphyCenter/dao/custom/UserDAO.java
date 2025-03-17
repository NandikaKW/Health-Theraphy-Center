package lk.ijse.gdse.HealthTheraphyCenter.dao.custom;

import lk.ijse.gdse.HealthTheraphyCenter.Entity.Therapist;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.User;
import lk.ijse.gdse.HealthTheraphyCenter.dao.CrudDAO;

import java.util.Optional;

public interface UserDAO extends CrudDAO<User,String> {
    int getUserCount() throws Exception;
    Optional<User> findByUsername(String username);
    String getLastUserId() throws Exception;
}
