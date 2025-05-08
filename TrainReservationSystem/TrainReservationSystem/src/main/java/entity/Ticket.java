package entity;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "ticket") // Represents the 'ticket' table in the database
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary key with auto-increment
    private int ticketId;

    @Temporal(TemporalType.DATE) // Specifies that 'bookingDate' is a date type
    private Date bookingDate;

    private String seatNumber; // Seat number assigned to the ticket
    private String coachNumber; // Coach number of the train
    private String compartmentType; // Type of compartment (e.g., 1A, 2A, SL)
    private double fare; // Fare for the ticket

    // New fields based on your ER model
    private String source; // Departure station
    private String destination; // Arrival station

    @Column(name = "no_of_Passenger") // Specifies column name in the database
    private int noOfPassengers; // Number of passengers for the ticket

    @Column(name = "reservation_general") // Specifies column name in the database
    private String reservationGeneral; // Type of reservation (e.g., General, Senior)

    @Column(name = "paid", nullable = false) // Ensures 'paid' is never null
    private boolean paid = false; // Payment status of the ticket

    // Relationship with the Passenger entity (Many tickets to one passenger)
    @ManyToOne
    @JoinColumn(name = "passengerId", nullable = false) // Defines foreign key for Passenger
    private Passenger passenger;

    // Relationship with the Train entity (Many tickets to one train)
    @ManyToOne
    @JoinColumn(name = "trainId", nullable = false) // Defines foreign key for Train
    private Train train;

    private String status; // Status of the ticket (e.g., Booked, Canceled)

    // Default constructor
    public Ticket() {}

    // Parameterized constructor for creating a new Ticket instance
    public Ticket(Date bookingDate, String seatNumber, String coachNumber, String compartmentType, double fare,
                  String source, String destination, int noOfPassengers, String reservationGeneral,
                  Passenger passenger, Train train, String status) {
        this.bookingDate = bookingDate;
        this.seatNumber = seatNumber;
        this.coachNumber = coachNumber;
        this.compartmentType = compartmentType;
        this.fare = fare;
        this.source = source;
        this.destination = destination;
        this.noOfPassengers = noOfPassengers;
        this.reservationGeneral = reservationGeneral;
        this.passenger = passenger;
        this.train = train;
        this.status = status;
    }

    // Getters and Setters for all fields

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getCoachNumber() {
        return coachNumber;
    }

    public void setCoachNumber(String coachNumber) {
        this.coachNumber = coachNumber;
    }

    public String getCompartmentType() {
        return compartmentType; // Return compartment type
    }

    public void setCompartmentType(String compartmentType) {
        this.compartmentType = compartmentType; // Set compartment type
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getNoOfPassengers() {
        return noOfPassengers;
    }

    public void setNoOfPassengers(int noOfPassengers) {
        this.noOfPassengers = noOfPassengers;
    }

    public String getReservationGeneral() {
        return reservationGeneral;
    }

    public void setReservationGeneral(String reservationGeneral) {
        this.reservationGeneral = reservationGeneral;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString method to display ticket details in a formatted way
    @Override
    public String toString() {
        return "🎫 Ticket ID: " + ticketId +
        	   "\n👤 Passenger Name: " + (passenger != null ? passenger.getName() : "N/A") +
               "\n📅 Booking Date: " + bookingDate +
               "\n🚉 From: " + source +
               "\n🏁 To: " + destination +
               "\n🪑 Seat Number: " + seatNumber +
               "\n🚃 Coach Number: " + coachNumber +
               "\n🛏️ Compartment Type: " + compartmentType +
               "\n💰 Fare: ₹" + fare +
               "\n👥 No. of Passengers: " + noOfPassengers +
               "\n📝 Reservation Type: " + reservationGeneral +
               "\n✅ Paid: " + (paid ? "Yes" : "No") +
               "\n📌 Status: " + status +
               "\n🚆 Train: " + (train != null ? train.getTrainName() : "N/A");
    }
}
