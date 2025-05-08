package service;

import dao.TrainDAO;
import dao.TicketDAO;
import dao.PassengerDAO;
import entity.Train;
import entity.Ticket;
import entity.Passenger;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;

public class AdminService {

    // Admin credentials
    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "admin123";

    // DAO instances for interacting with the database
    private TrainDAO trainDAO = new TrainDAO();
    private TicketDAO ticketDAO = new TicketDAO();
    private PassengerDAO passengerDAO = new PassengerDAO();

    // Method for admin login
    public boolean login(String username, String password) {
        // Validates the admin credentials
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }

    // Method to display the admin dashboard
    public void showAdminPanel(Scanner scanner) {
        int choice;

        do {
            // Display admin menu options
            System.out.println("\n===== Admin Dashboard =====");
            System.out.println("1. Add Train");
            System.out.println("2. View All Trains");
            System.out.println("3. View All Tickets");
            System.out.println("4. View Passenger Details");
            System.out.println("5. View Available Tickets");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            // Execute corresponding actions based on user choice
            switch (choice) {
                case 1:
                    addTrain(scanner); // Adds a new train
                    break;
                case 2:
                    viewAllTrains(); // Views all trains
                    break;
                case 3:
                    viewAllTickets(); // Views all booked tickets
                    break;
                case 4:
                    viewAllPassengers(); // Views all passenger details
                    break;
                case 5:
                    viewAvailableTickets(); // <-- New method
                    break;
                case 6:
                    System.out.println("🔒 Logging out from Admin panel.");
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }

        } while (choice != 6); // Continue the loop until the admin logs out
    }

    // Method to add a new train
    private void addTrain(Scanner scanner) {
        System.out.print("Enter Train Name: ");
        String trainName = scanner.nextLine();

        System.out.print("How many compartments to add (e.g., 1A, 2A, SL, etc.)? ");
        int compartmentCount = scanner.nextInt();
        scanner.nextLine(); // consume newline

        // Maps to store compartment data
        Map<String, Integer> compartmentSeats = new HashMap<>();
        Map<String, Double> compartmentFares = new HashMap<>();

        // Loop to add compartment details
        for (int i = 1; i <= compartmentCount; i++) {
            System.out.println("Enter details for Compartment #" + i);

            System.out.print("Compartment Type (e.g., SL, 2A, 3A): ");
            String type = scanner.nextLine();

            System.out.print("Total Seats in " + type + ": ");
            int seats = scanner.nextInt();

            System.out.print("Fare for " + type + ": ");
            double typeFare = scanner.nextDouble();
            scanner.nextLine(); // consume newline

            compartmentSeats.put(type, seats);
            compartmentFares.put(type, typeFare);
        }

        // Additional train details
        System.out.print("Enter Reservation Seats: ");
        int reservation = scanner.nextInt();

        System.out.print("Enter RAC Seats: ");
        int rac = scanner.nextInt();

        System.out.print("Enter Waiting Seats: ");
        int waiting = scanner.nextInt();
        scanner.nextLine(); // clear newline

        System.out.print("Is Train Half at Station? (yes/no): ");
        String trainHalfAtStation = scanner.nextLine();

        System.out.print("Enter Reservation Chart Status (e.g., Prepared/Not Prepared): ");
        String reservationChart = scanner.nextLine();

        System.out.print("Enter Source Station: ");
        String source = scanner.nextLine();

        System.out.print("Enter Destination Station: ");
        String destination = scanner.nextLine();

        // Create a new Train object with provided details
        Train train = new Train(trainName, compartmentSeats, compartmentFares, reservation, rac, waiting, trainHalfAtStation, reservationChart, source, destination);
        
        // Set the total and available seats
        train.setTotalReservationSeats(reservation);
        train.setAvailableReservationSeats(reservation);
        train.setTotalRacSeats(rac);
        train.setAvailableRacSeats(rac);
        train.setTotalWaitingSeats(waiting);
        train.setAvailableWaitingSeats(waiting);
        train.setTrainHalfAtStation(trainHalfAtStation);
        train.setReservationChart(reservationChart);
        train.setSource(source);
        train.setDestination(destination);

        // Save the train to the database using the TrainDAO
        trainDAO.saveTrain(train);
        System.out.println("✅ Train added successfully!");
    }

    // Method to view all trains
    private void viewAllTrains() {
        List<Train> trains = trainDAO.getAllTrains();

        if (trains.isEmpty()) {
            System.out.println("🚫 No trains found.");
            return;
        }

        // Display all trains
        System.out.println("\n===== All Trains =====");
        for (Train train : trains) {
            System.out.println(train); // Uses toString() from Train entity
        }
    }

    // Method to view all booked tickets
    private void viewAllTickets() {
        List<Ticket> tickets = ticketDAO.getAllTickets();

        if (tickets.isEmpty()) {
            System.out.println("🚫 No tickets found.");
            return;
        }

        // Display all tickets
        System.out.println("\n===== All Booked Tickets =====");
        for (Ticket ticket : tickets) {
            System.out.println(ticket); // Make sure Ticket entity has a proper toString()
            System.out.println("------------------------------");
        }
    }

    // Method to view all passenger details
    private void viewAllPassengers() {
        List<Passenger> passengers = passengerDAO.getAllPassengers();
        
        // Check if there are any passengers
        if (passengers.isEmpty()) {
            System.out.println("🚫 No passengers found.");
        } else {
            // Display passenger details
            System.out.println("\n👤 Passenger Details:");
            for (Passenger passenger : passengers) {
                System.out.println("ID: " + passenger.getPassengerId() +
                        ", Name: " + passenger.getName() +
                        ", Email: " + passenger.getEmail() +
                        ", Phone: " + passenger.getPhone() +
                        ", Age: " + passenger.getAge() +
                        ", Gender: " + passenger.getGender());
            }
        }
    }
        
     // Method to view available tickets
        private void viewAvailableTickets() {
            List<Train> trains = trainDAO.getAllTrains();

            if (trains.isEmpty()) {
                System.out.println("🚫 No trains found.");
                return;
            }

            System.out.println("\n===== Available Tickets in Each Train =====");
            for (Train train : trains) {
                System.out.println("Train No: " + train.getTrainNo());
                System.out.println("Train Name: " + train.getTrainName());
                System.out.println("Available Reservation Seats: " + train.getAvailableReservationSeats());
                System.out.println("Available RAC Seats: " + train.getAvailableRacSeats());
                System.out.println("Available Waiting Seats: " + train.getAvailableWaitingSeats());
                System.out.println("-------------------------------------------");
            }
        }
}

