package lk.ijse.gdse.HealthTheraphyCenter.bo.custom.impl;

import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.QueryBO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.ProgramDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.QueryDAO;

import java.util.ArrayList;
import java.util.List;

public class QueryBOImpl implements QueryBO {
    QueryDAO queryDAO = DaoFactory.getInstance().getDAO(DaoTypes.QUERY);

    @Override
    public List<Object[]> getPatientsEnrolledInAllTherapyPrograms() throws Exception {
        return queryDAO.getPatientDetails();
    }
}
