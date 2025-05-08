package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	// The SessionFactory is created only once and is shared across the application.
    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml.
        	// The configure() method loads the configuration file and the buildSessionFactory() creates the SessionFactory.
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
        	// If there is an error during SessionFactory creation, log the error and throw an ExceptionInInitializerError
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Public method to get the SessionFactory instance, which is thread-safe and can be used by other classes
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
