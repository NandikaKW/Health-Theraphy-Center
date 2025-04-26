package lk.ijse.gdse.HealthTheraphyCenter.dao.custom;

import lk.ijse.gdse.HealthTheraphyCenter.dao.SuperDAO;

import java.util.List;

public interface QueryDAO extends SuperDAO {
     List<Object[]> getPatientDetails() throws Exception;
}
