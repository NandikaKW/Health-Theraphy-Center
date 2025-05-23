package lk.ijse.gdse.HealthTheraphyCenter.bo.custom;

import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.impl.*;
import lk.ijse.gdse.HealthTheraphyCenter.bo.SuperBO;

public class BoFactory {
    public static BoFactory boFactory;

    private BoFactory() {
    }

    public static BoFactory getInstance() {
        return boFactory == null ? boFactory = new BoFactory() : boFactory;
    }

    @SuppressWarnings("unchecked")
    public <T extends SuperBO>T getBO(BoTypes boTypes){
        return  switch (boTypes){
            case PATIENTS -> (T) new PatientBOImpl();
            case THERAPY_PROGRAMS -> (T) new ProgramBOImpl();
            case THERAPISTS -> (T) new TherapistBOImpl();
            case USER -> (T) new UserBOImpl();
            case PATIENT_PROGRAM -> (T) new PatientProgramBOImpl();
            case SESSION -> (T) new SessionBOImpl();
            case PAYMENT -> (T) new PaymentBOImpl();
            case QUERY -> (T) new QueryBOImpl();
        };
    }
}
