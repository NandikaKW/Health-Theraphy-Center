package lk.ijse.gdse.HealthTheraphyCenter.dao.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.Patient;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.Therapist;
import lk.ijse.gdse.HealthTheraphyCenter.config.FactoryConfiguration;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.TherapistDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TherapistDAOImpl implements TherapistDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();



    @Override
    public boolean save(Therapist therapist) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            // C001
            // C001
            Therapist existTherapist = session.get(Therapist.class, therapist.getId());
            if (existTherapist != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Duplicate ID");
                alert.setContentText("Program with ID " + therapist.getId() + " already exists.");
                alert.showAndWait();
                return false;
            }
            session.persist(therapist);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    @Override
    public boolean update(Therapist therapist) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(therapist);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }

    }
    @Override
    public boolean deleteByPK(String pk) throws Exception {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        Therapist therapist = session.get(Therapist.class, pk);
        if (therapist != null) {
            session.delete(therapist);
            transaction.commit();
            return true;
        }
        transaction.rollback();
        return false;



    }

    @Override
    public List<Therapist> getAll() {
        try (Session session = factoryConfiguration.getSession()) {
            Query<Therapist> query = session.createQuery("FROM Therapist", Therapist.class);
            return query.list();
        }

    }

    @Override
    public Optional<Therapist> findByPK(String pk) {
        try (Session session = factoryConfiguration.getSession()) {
            Therapist therapist = session.get(Therapist.class, pk);
            return Optional.ofNullable(therapist);
        }
    }

    @Override
    public Optional<String> getLastPK() {
        try (Session session = factoryConfiguration.getSession()) {
            Query<String> query = session.createQuery("SELECT t.id FROM Therapist t ORDER BY t.id DESC", String.class);
            query.setMaxResults(1);
            return Optional.ofNullable(query.uniqueResult());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
