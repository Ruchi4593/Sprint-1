package dao;

import entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

/**
 * DAO class for handling CRUD operations for Payment entity.
 */
public class PaymentDAO {

    /**
     * Save a new Payment to the database.
     * @param payment the Payment object to be saved.
     */
    public void savePayment(Payment payment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(payment);      // Save payment object
            transaction.commit();       // Commit transaction
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Rollback if error occurs
            e.printStackTrace();
        }
    }

    /**
     * Get a Payment by its ID.
     * @param id the payment ID.
     * @return Payment object if found, otherwise null.
     */
    public Payment getPaymentById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Payment.class, id); // Fetch payment by ID
        }
    }

    /**
     * Update an existing Payment.
     * @param payment the Payment object with updated values.
     */
    public void updatePayment(Payment payment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(payment);    // Update payment record
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Rollback on error
            e.printStackTrace();
        }
    }

    /**
     * Delete a Payment by its ID.
     * @param id the ID of the Payment to delete.
     */
    public void deletePayment(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Payment payment = session.get(Payment.class, id); // Get payment object
            if (payment != null) {
                transaction = session.beginTransaction();
                session.delete(payment); // Delete payment object
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Rollback on failure
            e.printStackTrace();
        }
    }

    /**
     * Get all Payment records from the database.
     * @return list of all Payment objects.
     */
    public List<Payment> getAllPayments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Payment", Payment.class).list(); // Fetch all payments
        }
    }

    /**
     * Get list of Payments by associated Ticket ID.
     * @param ticketId the Ticket ID whose payments to fetch.
     * @return list of matching Payment objects.
     */
    public List<Payment> getPaymentsByTicketId(int ticketId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Payment where ticket.ticketId = :ticketId", Payment.class)
                          .setParameter("ticketId", ticketId)
                          .list();
        }
    }

    /**
     * Get list of Payments by Payment Mode (e.g., UPI, CARD, etc.).
     * @param paymentMode the mode of payment to search by.
     * @return list of Payment objects with matching mode.
     */
    public List<Payment> getPaymentsByPaymentMode(String paymentMode) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Payment where paymentMode = :paymentMode", Payment.class)
                          .setParameter("paymentMode", paymentMode)
                          .list();
        }
    }
}