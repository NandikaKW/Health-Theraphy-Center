package lk.ijse.gdse.HealthTheraphyCenter.dao.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.TherapySession;
import lk.ijse.gdse.HealthTheraphyCenter.config.FactoryConfiguration;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.SessionDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SessionDAOImpl implements SessionDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(TherapySession therapySession) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            TherapySession existsSession = session.get(TherapySession.class, therapySession.getId());
            if (existsSession != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Duplicate ID");
                alert.setContentText("Program with ID " + therapySession.getId() + " already exists.");
                alert.showAndWait();
                return false;
            }

            session.persist(therapySession);
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
    public boolean update(TherapySession therapySession) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            System.out.println("Updating session: " + therapySession);
            session.update(therapySession);  // Using update() instead of merge()
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();  // Log the exception for debugging
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean deleteByPK(String pk){
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            TherapySession therapySession = session.get(TherapySession.class, pk);
            if (therapySession == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Session not found");
                alert.setContentText("Session with ID " + pk + " not found.");
                alert.showAndWait();
                return false;
            }

            session.remove(therapySession);
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
    public List<TherapySession> getAll() {
        try (Session session = factoryConfiguration.getSession()) {
            Query<TherapySession> query = session.createQuery("FROM TherapySession ", TherapySession.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list if something goes wrong
        }
    }

    @Override
    public Optional<TherapySession> findByPK(String pk) {
        Session session = factoryConfiguration.getSession();
        TherapySession therapySession = session.get(TherapySession.class, pk);
        session.close();
        if (therapySession == null) {
            return Optional.empty();
        }
        return Optional.of(therapySession);
    }

    @Override
    public Optional<String> getLastPK() {
        try (Session session = factoryConfiguration.getSession()) {
            String lastPk = session.createQuery("SELECT t.id FROM TherapySession t ORDER BY t.id DESC", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
            return Optional.ofNullable(lastPk);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty(); // Handle the exception as needed
        }
    }
}
