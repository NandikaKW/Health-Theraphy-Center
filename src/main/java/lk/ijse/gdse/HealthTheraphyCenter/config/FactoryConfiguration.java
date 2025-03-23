package lk.ijse.gdse.HealthTheraphyCenter.config;

import lk.ijse.gdse.HealthTheraphyCenter.Entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Properties;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private final SessionFactory sessionFactory;

    private FactoryConfiguration() throws IOException {
        Configuration configuration = new Configuration();
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("hibernate.properties"));
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(Patient.class).addAnnotatedClass(PatientProgramDetail.class).addAnnotatedClass(Payment.class).addAnnotatedClass(Therapist.class).addAnnotatedClass(TherapyProgram.class).addAnnotatedClass(TherapySession.class).addAnnotatedClass(User.class);
        this.sessionFactory = configuration.buildSessionFactory();
    }

    public static FactoryConfiguration getInstance() {
        try {
            return factoryConfiguration == null ? (factoryConfiguration = new FactoryConfiguration()) : factoryConfiguration;
        } catch (Exception var1) {
            Exception e = var1;
            throw new RuntimeException(e);
        }
    }

    public Session getSession() {
        return this.sessionFactory.openSession();
    }
}
