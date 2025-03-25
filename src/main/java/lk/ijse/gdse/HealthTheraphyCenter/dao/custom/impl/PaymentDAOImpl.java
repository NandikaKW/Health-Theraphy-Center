package lk.ijse.gdse.HealthTheraphyCenter.dao.custom.impl;

import lk.ijse.gdse.HealthTheraphyCenter.Entity.Payment;
import lk.ijse.gdse.HealthTheraphyCenter.config.FactoryConfiguration;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.PaymentDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {
    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(Payment payment) {
        try (Session session = factoryConfiguration.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(payment);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Payment payment) {
        try (Session session = factoryConfiguration.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(payment);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteByPK(String pk) {
        try (Session session = factoryConfiguration.getSession()) {
            Transaction transaction = session.beginTransaction();
            Payment payment = session.get(Payment.class, pk);

            if (payment != null) {
                session.remove(payment);
                transaction.commit();
                return true;
            }
            transaction.rollback();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Payment> getAll() {
        try (Session session = factoryConfiguration.getSession()) {
            Query<Payment> query = session.createQuery("FROM Payment ", Payment.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list if something goes wrong
        }
    }

    @Override
    public Optional<Payment> findByPK(String pk) {
        Session session = factoryConfiguration.getSession();
        Payment payment = session.get(Payment.class, pk);
        session.close();
        if (payment == null) {
            return Optional.empty();
        }
        return Optional.of(payment);
    }

    @Override
    public Optional<String> getLastPK() {
        try (Session session = factoryConfiguration.getSession()) {
            String lastPk = session.createQuery("SELECT p.id FROM Payment p ORDER BY p.id DESC", String.class)
                    .setMaxResults(1)
                    .uniqueResult();
            return Optional.ofNullable(lastPk);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty(); // Handle the exception as needed
        }
    }
}
