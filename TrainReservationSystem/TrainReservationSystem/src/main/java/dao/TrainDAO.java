package dao;

import entity.Train;

import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * DAO class for managing Train-related operations.
 */
public class TrainDAO {

    /**
     * Save a new train to the database.
     * @param train the Train object to be saved.
     */
    public void saveTrain(Train train) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(train);
            transaction.commit();
            System.out.println("✅ Train saved successfully!");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Retrieve a train by its train number.
     * @param trainNo the train number (primary key).
     * @return the Train object if found, otherwise null.
     */
    public Train getTrainById(int trainNo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Train.class, trainNo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieve all trains from the database.
     * @return a list of all Train objects.
     */
    public List<Train> getAllTrains() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Train", Train.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Update an existing train in the database.
     * @param train the Train object with updated values.
     */
    public void updateTrain(Train train) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(train);
            transaction.commit();
            System.out.println("✏️ Train updated successfully!");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Delete a train by its train number.
     * @param trainNo the train number to delete.
     */
    public void deleteTrain(int trainNo) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Train train = session.get(Train.class, trainNo);
            if (train != null) {
                transaction = session.beginTransaction();
                session.delete(train);
                transaction.commit();
                System.out.println("❌ Train deleted successfully!");
            } else {
                System.out.println("Train not found with Train No: " + trainNo);
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Get a list of trains running between a source and destination.
     * @param source the starting station.
     * @param destination the ending station.
     * @return list of matching Train objects.
     */
    public List<Train> getTrainByRoute(String source, String destination) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Train WHERE source = :source AND destination = :destination", Train.class)
                    .setParameter("source", source)
                    .setParameter("destination", destination)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieve a train based on its name and route.
     * @param trainName the name of the train.
     * @param source the source station.
     * @param destination the destination station.
     * @return a single Train object if found.
     */
    public Train getTrainByNameAndRoute(String trainName, String source, String destination) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Train WHERE trainName = :trainName AND source = :source AND destination = :destination", Train.class)
                    .setParameter("trainName", trainName)
                    .setParameter("source", source)
                    .setParameter("destination", destination)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Establish and return a JDBC connection (used for raw SQL).
     * @return JDBC Connection object.
     * @throws SQLException if a database access error occurs.
     */
    public Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/trainreservation"; // Update DB name as needed
        String user = "root"; // Replace with your DB username
        String password = "ruchi"; // Replace with your DB password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC Driver not found", e);
        }
    }

    /**
     * Update seat availability values (reservation, RAC, and waiting list) using JDBC.
     * @param train the Train object with updated seat counts.
     */
    public void updateTrainSeats(Train train) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE train SET available_reservation_seats = ?, available_rac_seats = ?, available_waiting_seats = ? WHERE train_name = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, train.getAvailableReservationSeats());
            ps.setInt(2, train.getAvailableRacSeats());
            ps.setInt(3, train.getAvailableWaitingSeats());
            ps.setString(4, train.getTrainName());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}