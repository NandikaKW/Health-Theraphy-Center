package lk.ijse.gdse.HealthTheraphyCenter.dao.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.Patient;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.TherapyProgram;
import lk.ijse.gdse.HealthTheraphyCenter.config.FactoryConfiguration;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.ProgramDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProgramDAOImpl implements ProgramDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();
    @Override
    public boolean save(TherapyProgram therapyProgram) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            // C001
            // C001
            Patient existsPatient = session.get(Patient.class, therapyProgram.getId());
            if (existsPatient != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Duplicate ID");
                alert.setContentText("Program with ID " + therapyProgram.getId() + " already exists.");
                alert.showAndWait();
                return false;
            }

            session.persist(therapyProgram);
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
    public boolean update(TherapyProgram therapyProgram) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            System.out.println(therapyProgram);
            session.update(therapyProgram);
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
    public boolean deleteByPK(String pk) throws Exception {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            TherapyProgram program = session.get(TherapyProgram.class, pk);
            if (program == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Program not found");
                alert.setContentText("Program with ID " + pk + " not found.");
                alert.showAndWait();
                return false;
            }
            // customer have order
            // In use

            session.remove(program);
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
    public List<TherapyProgram> getAll() {
        try (Session session = factoryConfiguration.getSession()) {
            Query<TherapyProgram> query = session.createQuery("FROM TherapyProgram", TherapyProgram.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list if something goes wrong
        }
    }

    @Override
    public Optional<TherapyProgram> findByPK(String pk) {
        Session session = factoryConfiguration.getSession();
        TherapyProgram program = session.get(TherapyProgram.class, pk);
        session.close();
        if (program == null) {
            return Optional.empty();
        }
        return Optional.of(program);
    }

    @Override
    public Optional<String> getLastPK() {
        try (Session session = factoryConfiguration.getSession()) {
            String lastPk = session.createQuery("SELECT t.id FROM TherapyProgram t ORDER BY t.id DESC", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
            return Optional.ofNullable(lastPk);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty(); // Handle the exception as needed
        }
    }

    @Override
    public TherapyProgram findById(String programId) throws Exception {
        Session session = factoryConfiguration.getSession();
        try {
            // Retrieve the patient by ID from the database
            TherapyProgram program = session.get(TherapyProgram.class, programId);

            if (program == null) {
                throw new Exception("Program with ID " + programId + " not found.");
            }

            return program;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error while retrieving patient by ID.", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
