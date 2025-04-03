package lk.ijse.gdse.HealthTheraphyCenter.dao.custom.impl;

import lk.ijse.gdse.HealthTheraphyCenter.Entity.PatientProgramDetail;
import lk.ijse.gdse.HealthTheraphyCenter.config.FactoryConfiguration;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.PatientProgramDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class PatientProgramDAOImpl implements PatientProgramDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();
    @Override
    public boolean save(PatientProgramDetail patientProgramDetail) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(patientProgramDetail);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }

    }
    @Override
    public boolean update(PatientProgramDetail patientProgramDetail) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(patientProgramDetail);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteByPK(String pk) throws Exception {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            PatientProgramDetail patientProgramDetail = session.get(PatientProgramDetail.class, pk);
            if (patientProgramDetail != null) {
                session.delete(patientProgramDetail);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<PatientProgramDetail> getAll() {
        Session session = factoryConfiguration.getSession();
        try {
            Query<PatientProgramDetail> query = session.createQuery("FROM PatientProgramDetail", PatientProgramDetail.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<PatientProgramDetail> findByPK(String pk) {
        return Optional.empty();
    }

    @Override
    public Optional<String> getLastPK() {
        Session session = factoryConfiguration.getSession();

        String lastPk = session
                .createQuery("SELECT pp.id FROM PatientProgramDetail pp ORDER BY pp.id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();

        return Optional.ofNullable(lastPk);
    }
}
