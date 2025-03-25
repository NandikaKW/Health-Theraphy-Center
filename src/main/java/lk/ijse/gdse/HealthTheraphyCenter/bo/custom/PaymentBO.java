package lk.ijse.gdse.HealthTheraphyCenter.bo.custom;

import lk.ijse.gdse.HealthTheraphyCenter.bo.SuperBO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.PaymentDTO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.SessionDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PaymentBO extends SuperBO {
    String generateNextTherapyPaymentID();
    boolean savePayment(PaymentDTO paymentDTO);
    boolean deletePayment(String paymentId) throws Exception;
    boolean updatePayment(PaymentDTO paymentDTO);
    List<PaymentDTO> getAllPayments() throws Exception;
    Optional<PaymentDTO> findPaymentByID(String id) throws Exception;
    ArrayList<String> getAllSessionIDs() throws Exception;
    Optional<SessionDTO> findSessionByID(String id) throws Exception;
}
