package lk.ijse.gdse.HealthTheraphyCenter.bo.custom;

import lk.ijse.gdse.HealthTheraphyCenter.bo.SuperBO;

import java.util.ArrayList;
import java.util.List;

public interface QueryBO  extends SuperBO {
    List<Object[]> getPatientsEnrolledInAllTherapyPrograms() throws Exception;
}

