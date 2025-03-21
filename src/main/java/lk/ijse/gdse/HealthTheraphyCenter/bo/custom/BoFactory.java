package lk.ijse.gdse.HealthTheraphyCenter.bo.custom;

import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.impl.PatientBOImpl;
import lk.ijse.gdse.HealthTheraphyCenter.bo.SuperBO;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.impl.ProgramBOImpl;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.impl.TherapistBOImpl;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.impl.UserBOImpl;

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
        };
    }
}
