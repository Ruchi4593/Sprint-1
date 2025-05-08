package entity;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "train") // Defines the table for this entity
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the primary key
    @Column(name = "train_no") // Column for train number
    private int trainNo;

    @Column(name = "train_name", nullable = false) // Column for train name (cannot be null)
    private String trainName;

    // Mapping for compartment seats (e.g., First Class, Second Class, etc.)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "train_compartment_seats", joinColumns = @JoinColumn(name = "train_no"))
    @MapKeyColumn(name = "compartment_type") // Maps the compartment type to the seat count
    @Column(name = "seat_count") // The number of seats in each compartment
    private Map<String, Integer> compartmentSeats;

    // Mapping for compartment fares (e.g., First Class fare, Second Class fare, etc.)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "train_compartment_fares", joinColumns = @JoinColumn(name = "train_no"))
    @MapKeyColumn(name = "compartment_type") // Maps the compartment type to its fare
    @Column(name = "fare") // The fare for each compartment type
    private Map<String, Double> compartmentFares;

    @Column(name = "reservation") // Column to store number of reserved seats
    private int reservation;

    @Column(name = "rac") // Column to store number of RAC (Reservation Against Cancellation) seats
    private int rac;

    @Column(name = "waiting") // Column to store number of waiting list seats
    private int waiting;

    @Column(name = "train_half_at_station") // Column to store whether the train is half at the station
    private String trainHalfAtStation;  // Could be boolean if preferred

    @Column(name = "reservation_chart") // Column to store the reservation chart
    private String reservationChart;

    @Column(name = "source") // Column for source station
    private String source;

    @Column(name = "destination") // Column for destination station
    private String destination;

    // New fields for tracking total and available seats
    @Column(name = "total_reservation_seats") // Total reserved seats
    private Integer totalReservationSeats;

    @Column(name = "total_rac_seats") // Total RAC seats
    private Integer totalRacSeats;

    @Column(name = "total_waiting_seats") // Total waiting list seats
    private Integer totalWaitingSeats;

    @Column(name = "available_reservation_seats") // Available reserved seats
    private Integer availableReservationSeats;

    @Column(name = "available_rac_seats") // Available RAC seats
    private Integer availableRacSeats;

    @Column(name = "available_waiting_seats") // Available waiting list seats
    private Integer availableWaitingSeats;

    // Default constructor
    public Train() {}

    // Parameterized constructor for initializing the train object
    public Train(String trainName, Map<String, Integer> compartmentSeats, Map<String, Double> compartmentFares, int reservation, int rac, int waiting, String trainHalfAtStation, String reservationChart, String source, String destination) {
        this.trainName = trainName;
        this.compartmentSeats = compartmentSeats;
        this.compartmentFares = compartmentFares;
        this.reservation = reservation;
        this.rac = rac;
        this.waiting = waiting;
        this.trainHalfAtStation = trainHalfAtStation;
        this.reservationChart = reservationChart;
        this.source = source;
        this.destination = destination;
    }

    // Getters and Setters for each field

    public int getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(int trainNo) {
        this.trainNo = trainNo;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public Map<String, Integer> getCompartmentSeats() {
        return compartmentSeats;
    }

    public void setCompartmentSeats(Map<String, Integer> compartmentSeats) {
        this.compartmentSeats = compartmentSeats;
    }

    public Map<String, Double> getCompartmentFares() {
        return compartmentFares;
    }

    public void setCompartmentFares(Map<String, Double> compartmentFares) {
        this.compartmentFares = compartmentFares;
    }

    public int getReservation() {
        return reservation;
    }

    public void setReservation(int reservation) {
        this.reservation = reservation;
    }

    public int getRac() {
        return rac;
    }

    public void setRac(int rac) {
        this.rac = rac;
    }

    public int getWaiting() {
        return waiting;
    }

    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }

    public String getTrainHalfAtStation() {
        return trainHalfAtStation;
    }

    public void setTrainHalfAtStation(String trainHalfAtStation) {
        this.trainHalfAtStation = trainHalfAtStation;
    }

    public String getReservationChart() {
        return reservationChart;
    }

    public void setReservationChart(String reservationChart) {
        this.reservationChart = reservationChart;
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

    public Integer getTotalReservationSeats() {
        return totalReservationSeats;
    }

    public void setTotalReservationSeats(Integer totalReservationSeats) {
        this.totalReservationSeats = totalReservationSeats;
    }

    public Integer getTotalRacSeats() {
        return totalRacSeats;
    }

    public void setTotalRacSeats(Integer totalRacSeats) {
        this.totalRacSeats = totalRacSeats;
    }

    public Integer getTotalWaitingSeats() {
        return totalWaitingSeats;
    }

    public void setTotalWaitingSeats(Integer totalWaitingSeats) {
        this.totalWaitingSeats = totalWaitingSeats;
    }

    public Integer getAvailableReservationSeats() {
        return availableReservationSeats;
    }

    public void setAvailableReservationSeats(Integer availableReservationSeats) {
        this.availableReservationSeats = availableReservationSeats;
    }

    public Integer getAvailableRacSeats() {
        return availableRacSeats;
    }

    public void setAvailableRacSeats(Integer availableRacSeats) {
        this.availableRacSeats = availableRacSeats;
    }

    public Integer getAvailableWaitingSeats() {
        return availableWaitingSeats;
    }

    public void setAvailableWaitingSeats(Integer availableWaitingSeats) {
        this.availableWaitingSeats = availableWaitingSeats;
    }

    // Overriding toString() to provide a readable string representation of the train object
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n-------------------------\n");
        sb.append("🚆 Train No: ").append(trainNo).append("\n");
        sb.append("📛 Name: ").append(trainName).append("\n");
        sb.append("📍 Route: ").append(source).append(" ➝ ").append(destination).append("\n");
        sb.append("🕒 Half at Station: ").append(trainHalfAtStation).append("\n");
        sb.append("📜 Reservation Chart: ").append(reservationChart).append("\n\n");

        sb.append("🪑 Total Seats:\n");
        sb.append("  Reservation: ").append(totalReservationSeats).append("\n");
        sb.append("  RAC: ").append(totalRacSeats).append("\n");
        sb.append("  Waiting: ").append(totalWaitingSeats).append("\n\n");

        sb.append("✅ Available Seats:\n");
        sb.append("  Reservation: ").append(availableReservationSeats).append("\n");
        sb.append("  RAC: ").append(availableRacSeats).append("\n");
        sb.append("  Waiting: ").append(availableWaitingSeats).append("\n\n");

        sb.append("🚪 Compartment-wise Seats:\n");
        if (compartmentSeats != null && !compartmentSeats.isEmpty()) {
            for (Map.Entry<String, Integer> entry : compartmentSeats.entrySet()) {
                sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" seats\n");
            }
        } else {
            sb.append("  (No seat data)\n");
        }

        sb.append("\n💰 Compartment-wise Fares:\n");
        if (compartmentFares != null && !compartmentFares.isEmpty()) {
            for (Map.Entry<String, Double> entry : compartmentFares.entrySet()) {
                sb.append("  ").append(entry.getKey()).append(": ₹").append(entry.getValue()).append("\n");
            }
        } else {
            sb.append("  (No fare data)\n");
        }

        sb.append("-------------------------\n");
        return sb.toString();
    }
}
