package lk.ijse.gdse.HealthTheraphyCenter.dao;

import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.impl.PatientDAOImpl;

public class DaoFactory {
    private static DaoFactory daoFactory;

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        return daoFactory == null ? (daoFactory = new DaoFactory()) : daoFactory;
    }

    //    SuperDAO
    @SuppressWarnings("unchecked")
    // prevent compiler warning about unchecked type casting
    public <T extends SuperDAO> T getDAO(DaoTypes daoTypes) {
        return switch (daoTypes) {
            case PATIENT -> (T) new PatientDAOImpl();

        };
    }
}
