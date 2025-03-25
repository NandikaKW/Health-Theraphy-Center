package lk.ijse.gdse.HealthTheraphyCenter.bo.custom.impl;

import lk.ijse.gdse.HealthTheraphyCenter.Entity.Payment;
import lk.ijse.gdse.HealthTheraphyCenter.Entity.TherapySession;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.PaymentBO;
import lk.ijse.gdse.HealthTheraphyCenter.config.FactoryConfiguration;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.PaymentDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.SessionDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.PaymentDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.SessionDTO;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentBOImpl implements PaymentBO {
    PaymentDAO paymentDAO = DaoFactory.getInstance().getDAO(DaoTypes.PAYMENT);
    SessionDAO sessionDAO = DaoFactory.getInstance().getDAO(DaoTypes.SESSION);
    FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public String generateNextTherapyPaymentID() {
        Optional<String> lastPaymentId = paymentDAO.getLastPK();

        if (lastPaymentId.isPresent()) {
            String lastID = lastPaymentId.get(); // e.g., "TP005"
            if (lastID.startsWith("P")) {
                int numericPart = Integer.parseInt(lastID.substring(1));
                numericPart++;
                return String.format("P%03d", numericPart);
            } else {
                return "P001";
            }
        } else {
            return "P001"; // Default if no therapy programs exist
        }
    }

    @Override
    public boolean savePayment(PaymentDTO paymentDTO) {
        Transaction transaction = null;
        try (Session session = factoryConfiguration.getSession()) {
            transaction = session.beginTransaction();

            // Validate session ID
            Optional<TherapySession> byPK = sessionDAO.findByPK(paymentDTO.getSessionId());
            if (!byPK.isPresent()) {
                System.err.println("Error: Invalid Therapy Session ID: " + paymentDTO.getSessionId());
                return false;
            }

            TherapySession therapySession = byPK.get();
            Payment payment = new Payment(paymentDTO.getId(), paymentDTO.getAmount(), paymentDTO.getDate(), "Pending", therapySession);


            session.save(payment); // Use session.save() directly
            therapySession.setStatus("PAID");
            session.merge(therapySession);

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deletePayment(String paymentId) throws Exception {
        return paymentDAO.deleteByPK(paymentId);
    }

    @Override
    public boolean updatePayment(PaymentDTO paymentDTO) {
        Optional<Payment> paymentOpt = paymentDAO.findByPK(paymentDTO.getId());

        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setAmount(paymentDTO.getAmount());
            payment.setPaymentDate(paymentDTO.getDate());

            return paymentDAO.update(payment);
        }
        return false;
    }

    @Override
    public List<PaymentDTO> getAllPayments() throws Exception {
        List<Payment> payments = paymentDAO.getAll();
        List<PaymentDTO> paymentDTOs = new ArrayList<>();
        for (Payment payment : payments) {
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setId(payment.getId());
            paymentDTO.setAmount(payment.getAmount());
            paymentDTO.setDate(payment.getPaymentDate());
            paymentDTO.setSessionId(payment.getTherapySession() != null ? payment.getTherapySession().getId() : null);
            paymentDTOs.add(paymentDTO);
        }

        return paymentDTOs;
    }

    @Override
    public Optional<PaymentDTO> findPaymentByID(String id) throws Exception {
        Optional<Payment> paymentOpt = paymentDAO.findByPK(id);
        return paymentOpt.map(payment -> new PaymentDTO(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getTherapySession().getId()
        ));
    }

    @Override
    public ArrayList<String> getAllSessionIDs() throws Exception {
        ArrayList<String> sessionIDs = new ArrayList<>();
        List<TherapySession> all = sessionDAO.getAll();
        for (TherapySession therapySession : all) {
            if (therapySession.getStatus().equals("UNPAID")) {
                sessionIDs.add(therapySession.getId());
            }
        }
        return sessionIDs;
    }

    @Override
    public Optional<SessionDTO> findSessionByID(String id) throws Exception {
        Optional<TherapySession> sessionOpt = sessionDAO.findByPK(id);
        return sessionOpt.map(session -> new SessionDTO(
                session.getId(),
                session.getSessionNotes(),
                session.getStatus(),
                session.getPatient().getId(),
                session.getTherapist().getId(),
                session.getTherapyProgram().getId(),
                session.getSessionDate()
        ));
    }
}
