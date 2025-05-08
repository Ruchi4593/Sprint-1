package dao;

import entity.Admin;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

/**
 * DAO class for performing operations related to Admin entity.
 */

public class AdminDAO {
	
	/**
     * Save a new Admin to the database.
     * @param admin the Admin object to save.
     */

    public void saveAdmin(Admin admin) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(admin);
        tx.commit();
        session.close();
    }
    
    /**
     * Get Admin by email.
     * @param email the email to search by.
     * @return the Admin object if found, otherwise null.
     */

    public Admin getAdminByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Admin admin = session.createQuery("FROM Admin WHERE email = :email", Admin.class)
                             .setParameter("email", email)
                             .uniqueResult();
        session.close();
        return admin;
    }
    
    /**
     * Get all Admin records.
     * @return a list of all Admins.
     */

    public List<Admin> getAllAdmins() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Admin> list = session.createQuery("FROM Admin", Admin.class).list();
        session.close();
        return list;
    }
}
