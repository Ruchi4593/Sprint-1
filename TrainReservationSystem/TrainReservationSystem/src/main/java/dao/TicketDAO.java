package dao;

import entity.Ticket;

import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

/**
 * DAO class to perform CRUD operations on the Ticket entity.
 */
public class TicketDAO {

    /**
     * Save a new ticket to the database.
     * @param ticket the Ticket object to be saved.
     */
    public void saveTicket(Ticket ticket) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(ticket); // Save ticket
            transaction.commit(); // Commit transaction
            System.out.println("Ticket saved successfully!");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Rollback if error
            e.printStackTrace();
        }
    }

    /**
     * Get a ticket by its ID.
     * @param id ticket ID to retrieve.
     * @return Ticket object if found.
     */
    public Ticket getTicket(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Ticket.class, id); // Get ticket by ID
        }
    }

    /**
     * Update an existing ticket in the database.
     * @param ticket the updated Ticket object.
     */
    public void updateTicket(Ticket ticket) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(ticket); // Update ticket
            transaction.commit();
            System.out.println("Ticket updated successfully!");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Delete a ticket by its ID.
     * @param id the ID of the ticket to delete.
     */
    public void deleteTicket(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Ticket ticket = session.get(Ticket.class, id);
            if (ticket != null) {
                transaction = session.beginTransaction();
                session.delete(ticket); // Delete ticket
                transaction.commit();
                System.out.println("Ticket deleted successfully!");
            } else {
                System.out.println("Ticket not found with ID: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Retrieve all tickets from the database.
     * @return list of all Ticket objects.
     */
    public List<Ticket> getAllTickets() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Ticket", Ticket.class).list(); // Get all tickets
        }
    }

    /**
     * Get all tickets booked by a specific passenger.
     * @param passengerId the passenger's ID.
     * @return list of Ticket objects booked by the passenger.
     */
    public List<Ticket> getTicketsByPassengerId(int passengerId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Ticket> tickets = session.createQuery(
                "FROM Ticket WHERE passenger.passengerId = :pid", Ticket.class)
                .setParameter("pid", passengerId)
                .getResultList();
        session.close();
        return tickets;
    }

    /**
     * Get all unpaid tickets for a specific passenger.
     * @param passengerId the passenger's ID.
     * @return list of unpaid Ticket objects.
     */
    public List<Ticket> getUnpaidTicketsByPassenger(int passengerId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Ticket> tickets = session.createQuery(
                "FROM Ticket WHERE passenger.passengerId = :pid AND paid = false", Ticket.class)
                .setParameter("pid", passengerId)
                .getResultList();
        session.close();
        return tickets;
    }

    /**
     * Mark a ticket as paid based on its ticket ID.
     * @param ticketId the ID of the ticket to mark as paid.
     * @return true if the operation is successful, otherwise false.
     */
    public boolean markAsPaid(int ticketId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Ticket ticket = session.get(Ticket.class, ticketId);
        if (ticket != null) {
            ticket.setPaid(true);     // Set paid flag to true
            session.update(ticket);   // Update ticket
            tx.commit();              // Commit transaction
            session.close();
            return true;
        }
        session.close();
        return false;
    }
}