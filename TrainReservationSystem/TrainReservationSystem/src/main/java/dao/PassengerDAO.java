package dao;

import entity.Passenger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

/**
 * DAO class for handling CRUD operations for Passenger entity.
 */

public class PassengerDAO {

	/**
     * Save a new Passenger to the database.
     * @param passenger the Passenger object to save.
     */
    public void savePassenger(Passenger passenger) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(passenger); // Save passenger object
            transaction.commit();    // Commit transaction
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Rollback on error
            e.printStackTrace(); 
        }
    }

    /**
     * Update an existing Passenger record.
     * @param passenger the Passenger object with updated data.
     */
    public void updatePassenger(Passenger passenger) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(passenger); // Update existing passenger
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Delete a Passenger by ID.
     * @param id the ID of the Passenger to delete.
     */
    public void deletePassenger(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Passenger passenger = session.get(Passenger.class, id); // Fetch passenger by ID
            if (passenger != null) {
                session.delete(passenger); // Delete if exists
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Retrieve a Passenger by ID.
     * @param id the ID of the Passenger.
     * @return the Passenger object if found, otherwise null.
     */
    public Passenger getPassengerById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Passenger.class, id); // Get passenger using session
        }
    }

    /**
     * Retrieve all Passengers from the database.
     * @return list of all Passenger objects.
     */
    public List<Passenger> getAllPassengers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Passenger> query = session.createQuery("FROM Passenger", Passenger.class);
            return query.list(); // Return list of all passengers
        }
    }

    /**
     * Get a Passenger by email address.
     * @param email the email of the Passenger.
     * @return the Passenger object if found, otherwise null.
     */
    public Passenger getPassengerByEmail(String email) {
        Transaction transaction = null;
        Passenger passenger = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            passenger = session.createQuery("FROM Passenger WHERE email = :email", Passenger.class)
                               .setParameter("email", email)
                               .uniqueResult(); // Get passenger by unique email
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }

        return passenger;
    }
}