package lk.ijse.gdse.HealthTheraphyCenter.dao.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.Patient;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.User;
import lk.ijse.gdse.HealthTheraphyCenter.config.FactoryConfiguration;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.UserDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(User user) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            User exisUser = session.get(User.class, user.getId());
            if (exisUser != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Duplicate ID");
                alert.setContentText("User with ID " + user.getId() + " already exists.");
                alert.showAndWait();
                return false;
            }
            session.persist(user);
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
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean deleteByPK(String pk) throws Exception {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = session.get(User.class, pk);
            if (user == null) {
                return false; // Just return false instead of showing an alert here
            }
            session.remove(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace(); // Log the error for debugging
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<User> getAll() {
        Session session = factoryConfiguration.getSession();
        Query<User> query = session.createQuery("FROM User", User.class);
        return query.list();
    }

    @Override
    public Optional<User> findByPK(String pk) {
        return Optional.empty();
    }

    @Override
    public Optional<String> getLastPK() {
        return Optional.empty();
    }

    @Override
    public int getUserCount() throws Exception {
        Session session = factoryConfiguration.getSession();
        try {
            // Create a query to count the total number of users
            Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u", Long.class);
            Long count = query.uniqueResult(); // Execute the query and get the result
            return count != null ? count.intValue() : 0; // Convert the result to int
        } catch (Exception e) {
            throw new Exception("Failed to fetch user count", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Session session = factoryConfiguration.getSession();
        try {
            // Create a query to find the user by username
            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            User user = query.uniqueResult(); // Execute the query and get the result
            return Optional.ofNullable(user); // Return the user wrapped in an Optional
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty(); // Return empty Optional if an error occurs
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public String getLastUserId() throws Exception {
        Session session = factoryConfiguration.getSession();
        try {
            Query<String> query = session.createQuery("SELECT u.id FROM User u ORDER BY u.id DESC", String.class);
            query.setMaxResults(1);
            return query.uniqueResult(); // Fetch last inserted user ID
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
}
