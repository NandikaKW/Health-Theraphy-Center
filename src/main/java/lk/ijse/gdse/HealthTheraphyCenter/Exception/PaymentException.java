package lk.ijse.gdse.HealthTheraphyCenter.Exception;

public class PaymentException extends Exception{
    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
