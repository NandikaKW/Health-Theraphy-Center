package lk.ijse.gdse.HealthTheraphyCenter.dao.custom.impl;

import lk.ijse.gdse.HealthTheraphyCenter.config.FactoryConfiguration;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.QueryDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class QueryDAOImpl implements QueryDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public List<Object[]> getPatientDetails() throws Exception {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String hql = "SELECT p.id, p.name, p.email " +
                    "FROM Patient p " +
                    "WHERE (" +
                    "    SELECT COUNT(DISTINCT ppd.therapyProgram.id) " +
                    "    FROM PatientProgramDetail ppd " +
                    "    WHERE ppd.patient.id = p.id" +
                    ") = (" +
                    "    SELECT COUNT(tp.id) FROM TherapyProgram tp" +
                    ")";

            List<Object[]> result = session.createQuery(hql).list();

            transaction.commit();
            session.close();

            return result;

        } catch (Exception e) {
            transaction.rollback();
            session.close();
            throw new RuntimeException("Failed to fetch patient details", e);
        }
    }
}
