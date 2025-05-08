package service;

import dao.PassengerDAO;

import dao.TicketDAO;
import dao.TrainDAO;
import dao.PaymentDAO;

import entity.Passenger;
import entity.Ticket;
import entity.Train;
import entity.Payment;
import entity.PaymentMode;


import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class LoginService {
	

	// DAO instances for interacting with the database   
    private PassengerDAO passengerDAO = new PassengerDAO();
    private TicketDAO ticketDAO = new TicketDAO();
    private TrainDAO trainDAO = new TrainDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();

    /**
     * User login based on email & password.
     * @param email User's email
     * @param password User's password 
     * @return Passenger object if login is successful, else null.
     */
    public Passenger login(String email, String password) {
        Passenger passenger = passengerDAO.getPassengerByEmail(email);
        if (passenger != null && passenger.getPassword().equals(password)) {
            return passenger;
        } else {
            return null; // login failed
        }
    }

    /**
     * Displays the user dashboard with options to book tickets, make payments, etc.
     * @param scanner Scanner object to take user input.
     * @param passenger The logged-in passenger.
     */
    public void showUserPanel(Scanner scanner, Passenger passenger) {
        while (true) {
            System.out.println("\n===== 🎫 User Dashboard =====");
            System.out.println("1. Book Ticket");
            System.out.println("2. Make Payment");
            System.out.println("3. View My Tickets");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    bookTicket(scanner, passenger); // Book ticket
                    break;
                case 2:
                    makePayment(scanner, passenger); // Make payment
                    break;
                case 3:
                    viewTickets(passenger); // View booked tickets
                    break;
                case 4:
                    System.out.println("🔓 Logged out successfully."); // Logout
                    return;
                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }
    
    /**
     * Books a ticket for the passenger.
     * @param scanner Scanner object to take user input.
     * @param passenger The logged-in passenger.
     */

    private void bookTicket(Scanner scanner, Passenger passenger) {
        System.out.print("Enter Source Station: ");
        String source = scanner.nextLine();

        System.out.print("Enter Destination Station: ");
        String destination = scanner.nextLine();
        
        // Get available trains based on source and destination
        List<Train> availableTrains = trainDAO.getTrainByRoute(source, destination);
        
        if (availableTrains.isEmpty()) {
            System.out.println("❌ No trains found between " + source + " and " + destination);
            return;
        }

        System.out.println("🚆 Available Trains:");
        for (Train train : availableTrains) {
            System.out.println("Train Name: " + train.getTrainName() + " ➡ Compartments: " + train.getCompartmentFares());
        }
        
        // Select train and compartment type
        System.out.print("Enter Train Name to Book: ");
        String selectedTrainName = scanner.nextLine();

        Train train = trainDAO.getTrainByNameAndRoute(selectedTrainName, source, destination);
        if (train == null) {
            System.out.println("❌ Invalid train name for given route.");
            return;
        }

        System.out.println("📄 Train Selected: " + train.getTrainName());
        
        // Display available compartments and fares
        System.out.println("Available Compartments & Fares:");
        for (String type : train.getCompartmentFares().keySet()) {
            System.out.println("👉 " + type + ": ₹" + train.getCompartmentFares().get(type));
        }

        System.out.print("Enter Compartment Type (like SL, 2A, 1A): ");
        String compartmentType = scanner.nextLine();

        // Validate compartment type
        Double farePerSeat = train.getCompartmentFares().get(compartmentType);
        if (farePerSeat == null) {
            System.out.println("❌ Invalid compartment type.");
            return;
        }

        System.out.print("Enter Number of Passengers: ");
        int noOfPassengers = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Reservation Type (General/Other): ");
        String reservationType = scanner.nextLine();

        // Determine seat availability and status
        StringBuilder seatBuilder = new StringBuilder();
        String status = null;
        int seatsToBook = noOfPassengers;

        if (train.getAvailableReservationSeats() >= seatsToBook) {
            train.setAvailableReservationSeats(train.getAvailableReservationSeats() - seatsToBook);
            status = "CONFIRMED";
        } else if (train.getAvailableRacSeats() >= seatsToBook) {
            train.setAvailableRacSeats(train.getAvailableRacSeats() - seatsToBook);
            status = "RAC";
        } else if (train.getAvailableWaitingSeats() >= seatsToBook) {
            train.setAvailableWaitingSeats(train.getAvailableWaitingSeats() - seatsToBook);
            status = "WAITING";
        } else {
            System.out.println("🚫 No seats available in Reservation, RAC or Waiting list.");
            return;
        }

        // Optional: Save updated train seat availability
        trainDAO.updateTrainSeats(train);

        // Assign seat numbers (just for visual)
        String seatNumber = "";
        String coachNumber = "";
        if (!status.equals("WAITING")) {
            // Assign seat numbers (just for visual)
            for (int i = 1; i <= seatsToBook; i++) {
                seatBuilder.append((int)(Math.random() * 72 + 1)); // Random seat number
                if (i != seatsToBook) seatBuilder.append(", ");
            }
            seatNumber = seatBuilder.toString();
            coachNumber = "C" + (int)(Math.random() * 10 + 1); // Random coach number
        }
        
        // Create and save ticket
        Ticket ticket = new Ticket();
        ticket.setPassenger(passenger);
        ticket.setTrain(train);
        ticket.setSource(source);
        ticket.setDestination(destination);
        ticket.setNoOfPassengers(noOfPassengers);
        ticket.setReservationGeneral(reservationType);
        ticket.setSeatNumber(seatNumber);
        ticket.setCoachNumber(coachNumber);
        ticket.setStatus(status); // <- Add status
        ticket.setFare(farePerSeat * seatsToBook);
        ticket.setCompartmentType(compartmentType); // Assuming your Ticket entity has this field
        ticket.setBookingDate(new Date());

        ticketDAO.saveTicket(ticket);
        
        // Output ticket status
        switch (status) {
        case "CONFIRMED":
            System.out.println("🎉 Ticket booked successfully! Status: ✅ CONFIRMED");
            break;
        case "RAC":
            System.out.println("📋 Ticket booked with RAC status. You'll be moved to Confirmed if seats are freed.");
            break;
        case "WAITING":
            System.out.println("⏳ Ticket is on WAITING LIST. You’ll be updated if your status changes.");
            break;
        }
        
        // Output booking details
        System.out.println("🚆 Train: " + train.getTrainName() + " | Route: " + source + " ➡ " + destination);
        System.out.println("🛏️ Compartment: " + compartmentType);
        if (!status.equals("WAITING")) {
            System.out.println("🪑 Seat(s): " + seatNumber + " | Coach: " + coachNumber);
        } else {
            System.out.println("🪑 Seat(s): Not Assigned | Coach: Not Assigned");
        }
        System.out.println("💰 Total Fare: ₹" + ticket.getFare());


    }


    /**
     * Makes payment for a ticket.
     * @param scanner Scanner object to take user input.
     * @param passenger The logged-in passenger.
     */
    private void makePayment(Scanner scanner, Passenger passenger) {
        List<Ticket> unpaidTickets = ticketDAO.getUnpaidTicketsByPassenger(passenger.getPassengerId());

        if (unpaidTickets.isEmpty()) {
            System.out.println("✅ No pending payments.");
            return;
        }

        System.out.println("🧾 Unpaid Tickets:");
        for (Ticket t : unpaidTickets) {
            System.out.println("Ticket ID: " + t.getTicketId() + ", Fare: ₹" + t.getFare());
        }

        System.out.print("Enter Ticket ID to pay for: ");
        int ticketId = scanner.nextInt();
        scanner.nextLine();

        // Retrieve ticket for payment
        Ticket ticket = ticketDAO.getTicket(ticketId);
        if (ticket == null) {
            System.out.println("❌ Invalid Ticket ID.");
            return;
        }
        
        System.out.println("💰 Total Fare: ₹" + ticket.getFare());

        // Show payment options
        System.out.println("\nChoose a payment method:");
        System.out.println("1. UPI");
        System.out.println("2. Card");
        System.out.println("3. NetBanking");
        System.out.println("4. Wallet");
        System.out.print("Enter payment method: ");
        int paymentChoice = scanner.nextInt();
        scanner.nextLine();

        // Determine payment mode
        PaymentMode selectedMode = null;
        String paymentDetails = "";
        
        switch (paymentChoice) {
        case 1:  // UPI Payment
            selectedMode = PaymentMode.UPI;
            System.out.println("Choose UPI payment method:");
            System.out.println("1. PhonePe");
            System.out.println("2. Google Pay");
            System.out.println("3. Paytm");
            System.out.println("4. Amazon Pay");
            System.out.println("5. BHIM UPI");
            System.out.print("Enter UPI payment method: ");
            int upiChoice = scanner.nextInt();
            scanner.nextLine();

            String upiService = "";
            switch (upiChoice) {
                case 1: upiService = "PhonePe"; break;
                case 2: upiService = "Google Pay"; break;
                case 3: upiService = "Paytm"; break;
                case 4: upiService = "Amazon Pay"; break;
                case 5: upiService = "BHIM UPI"; break;
                default:
                    System.out.println("❌ Invalid choice.");
                    return;
            }
            System.out.println("You chose " + upiService);

            // Ask for UPI ID or Name
            System.out.print("Enter your " + upiService + " ID or Name: ");
            paymentDetails = scanner.nextLine();

            // Ask for the amount to pay
            System.out.print("Enter amount to pay: ₹");
            double amountToPay = scanner.nextDouble();
            scanner.nextLine();  // Consume newline

            // Ask for UPI password to confirm the payment
            System.out.print("Enter UPI password for authentication: ");
            String upiPassword = scanner.nextLine();

            // Simulate payment verification and success
            System.out.println("Processing payment via " + upiService + "...");
            System.out.println("Amount: ₹" + amountToPay);
            System.out.println("UPI payment successful using " + upiService + " for ₹" + amountToPay);

            // Now create the payment entry
            Payment payment = new Payment(ticket, amountToPay, selectedMode, paymentDetails);
            payment.setPaymentStatus("SUCCESS"); // Assuming the payment was successful
            paymentDAO.savePayment(payment);

            // Mark the ticket as paid
            ticketDAO.markAsPaid(ticketId);
            System.out.println("💳 Payment successful for Ticket ID: " + ticketId + " via " + upiService);
            break;

        case 2:  // Card Payment
            selectedMode = PaymentMode.CARD;
            System.out.println("Choose your card type:");
            System.out.println("1. Visa");
            System.out.println("2. MasterCard");
            System.out.println("3. American Express");
            System.out.print("Enter card type: ");
            int cardChoice = scanner.nextInt();
            scanner.nextLine();

            String cardType = "";
            switch (cardChoice) {
                case 1: cardType = "Visa"; break;
                case 2: cardType = "MasterCard"; break;
                case 3: cardType = "American Express"; break;
                default:
                    System.out.println("❌ Invalid card type.");
                    return;
            }
            System.out.println("You chose " + cardType);

            // Ask for card details
            System.out.print("Enter card number: ");
            String cardNumber = scanner.nextLine();
            System.out.print("Enter expiry date (MM/YY): ");
            String expiryDate = scanner.nextLine();
            System.out.print("Enter CVV: ");
            String cvv = scanner.nextLine();

            // Simulate card payment verification
            System.out.println("Processing card payment...");
            System.out.println("Card type: " + cardType);
            System.out.println("Card number: " + cardNumber); // In real-world, mask this

            // Assuming payment is successful
            System.out.println("Card payment successful for ₹" + ticket.getFare());

            // Now create the payment entry
            Payment cardPayment = new Payment(ticket, ticket.getFare(), selectedMode, cardType);
            cardPayment.setPaymentStatus("SUCCESS"); // Assuming payment success
            paymentDAO.savePayment(cardPayment);

            // Mark the ticket as paid
            ticketDAO.markAsPaid(ticketId);
            System.out.println("💳 Payment successful for Ticket ID: " + ticketId + " via " + cardType);
            break;

        case 3:  // NetBanking
            selectedMode = PaymentMode.NET_BANKING;
            System.out.println("Choose your bank:");
            System.out.println("1. ICICI Bank");
            System.out.println("2. HDFC Bank");
            System.out.println("3. SBI");
            System.out.println("4. Axis Bank");
            System.out.print("Enter bank choice: ");
            int bankChoice = scanner.nextInt();
            scanner.nextLine();

            String bankName = "";
            switch (bankChoice) {
                case 1: bankName = "ICICI Bank"; break;
                case 2: bankName = "HDFC Bank"; break;
                case 3: bankName = "SBI"; break;
                case 4: bankName = "Axis Bank"; break;
                default:
                    System.out.println("❌ Invalid bank choice.");
                    return;
            }
            System.out.println("You chose " + bankName);

            // Simulate NetBanking payment process
            System.out.print("Enter your NetBanking username: ");
            String username = scanner.nextLine();
            System.out.print("Enter your NetBanking password: ");
            String password = scanner.nextLine();
            
            System.out.print("Enter your transaction password: ");
            String transactionPassword = scanner.nextLine();

            // Simulate successful login and payment
            System.out.println("Redirecting to " + bankName + " NetBanking...");
            System.out.println("Payment successful via NetBanking for ₹" + ticket.getFare());

            // Now create the payment entry
            Payment bankPayment = new Payment(ticket, ticket.getFare(), selectedMode, bankName);
            bankPayment.setPaymentStatus("SUCCESS");
            paymentDAO.savePayment(bankPayment);

            // Mark the ticket as paid
            ticketDAO.markAsPaid(ticketId);
            System.out.println("💳 Payment successful for Ticket ID: " + ticketId + " via " + bankName);
            break;

        case 4:  // Wallet Payment
            selectedMode = PaymentMode.WALLET;
            System.out.println("Choose your wallet:");
            System.out.println("1. Paytm Wallet");
            System.out.println("2. Google Pay Wallet");
            System.out.println("3. PhonePe Wallet");
            System.out.println("4. Amazon Pay Wallet");
            System.out.print("Enter wallet choice: ");
            int walletChoice = scanner.nextInt();
            scanner.nextLine();

            String walletType = "";
            switch (walletChoice) {
                case 1: walletType = "Paytm Wallet"; break;
                case 2: walletType = "Google Pay Wallet"; break;
                case 3: walletType = "PhonePe Wallet"; break;
                case 4: walletType = "Amazon Pay Wallet"; break;
                default:
                    System.out.println("❌ Invalid wallet choice.");
                    return;
            }
            System.out.println("You chose " + walletType);

         // 1. Ask for wallet ID or phone/email
            System.out.print("Enter your " + walletType + " registered mobile/email: ");
            String walletId = scanner.nextLine();

            // 2. Confirm amount
            System.out.print("Enter amount to pay (fare is ₹" + ticket.getFare() + "): ₹");
            double amountEntered = scanner.nextDouble();
            scanner.nextLine();

            if (amountEntered != ticket.getFare()) {
                System.out.println("❌ Incorrect amount. Please enter the exact fare.");
                return;
            }

            // 3. Ask for wallet password or PIN
            System.out.print("Enter your wallet PIN/password: ");
            String walletPin = scanner.nextLine();

            // 4. Simulate payment processing
            System.out.println("Processing payment via " + walletType + "...");

            // Simulate success
            boolean paymentSuccess = true;

            if (paymentSuccess) {
                String paymentDetail = walletType + " - ID: " + walletId;
                Payment walletPayment = new Payment(ticket, ticket.getFare(), selectedMode, paymentDetail);
                walletPayment.setPaymentStatus("SUCCESS");
                paymentDAO.savePayment(walletPayment);

                ticketDAO.markAsPaid(ticketId);
                System.out.println("✅ Payment successful for Ticket ID: " + ticketId + " via " + walletType);
            } else {
                System.out.println("❌ Payment failed. Please try again.");
            }
            break;

        default:
            System.out.println("❌ Invalid payment method.");
            return;
        }
    }

        /*switch (paymentChoice) {
        case 1:
            selectedMode = PaymentMode.UPI;
            System.out.print("Enter UPI ID: ");
            paymentDetails = scanner.nextLine();
            break;
        case 2:
            selectedMode = PaymentMode.CARD;
            System.out.print("Enter Card Number: ");
            paymentDetails = scanner.nextLine();
            break;
        case 3:
            selectedMode = PaymentMode.NET_BANKING;
            System.out.print("Enter Bank Name: ");
            paymentDetails = scanner.nextLine();
            break;
        case 4:
            selectedMode = PaymentMode.WALLET;
            System.out.print("Enter Wallet Name or ID: ");
            paymentDetails = scanner.nextLine();
            break;
       
        default:
            System.out.println("❌ Invalid payment method.");
            return;
    }

        // Simulate payment process (you can integrate real payment gateways here)
        System.out.println("Processing " + selectedMode + " payment...");

        // Assuming the payment is successful
        boolean paymentSuccess = true; // Simulate success (this can be replaced with actual payment gateway logic)

        if (paymentSuccess) {
            // Create a new payment entry
        	Payment payment = new Payment(ticket, ticket.getFare(), selectedMode, paymentDetails);
            System.out.println("Enter Payment Mode (UPI / CARD / NETBANKING / CASH): ");
            String mode = scanner.nextLine().trim().toUpperCase();

            if (!(mode.equals("UPI") || mode.equals("CARD") || mode.equals("NETBANKING") || mode.equals("CASH"))) {
                System.out.println("❌ Invalid payment mode. Please try again with one of: UPI, CARD, NETBANKING, CASH.");
                return;
            }


            payment.setPaymentStatus("SUCCESS"); // Assuming the payment was successful
            paymentDAO.savePayment(payment);

            // Mark the ticket as paid
            ticketDAO.markAsPaid(ticketId);

            System.out.println("💳 Payment successful for Ticket ID: " + ticketId + " via " + selectedMode);
        } else {
            // Payment failed
            System.out.println("❌ Payment failed. Please try again.");
        }
    }*/

    /**
     * View all tickets booked by the passenger.
     * @param passenger The logged-in passenger.
     */
    private void viewTickets(Passenger passenger) {
        List<Ticket> tickets = ticketDAO.getTicketsByPassengerId(passenger.getPassengerId());

        if (tickets.isEmpty()) {
            System.out.println("📭 No tickets found.");
        } else {
            for (Ticket t : tickets) {
                System.out.println(t);
            }
        }
    }
    
 // Register a new passenger
    public boolean registerPassenger(Passenger passenger) {
        Passenger existing = passengerDAO.getPassengerByEmail(passenger.getEmail());
        if (existing != null) {
            return false; // email already registered
        }
        passengerDAO.savePassenger(passenger);
        return true;
    }

}