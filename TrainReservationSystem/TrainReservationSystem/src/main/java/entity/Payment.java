package entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity class representing a Payment for a booked ticket.
 */
@Entity
public class Payment {

    /** Unique identifier for the payment (Primary Key) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    /** Associated ticket for which the payment is made (One-to-One relationship) */
    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    /** Total amount paid */
    private double amount;

    /** Date of payment */
    @Temporal(TemporalType.DATE)
    private Date paymentDate;

    /** Mode of payment (UPI, CARD, NETBANKING, etc.) */
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    /** Status of the payment (e.g., Success, Failed, Pending) */
    private String paymentStatus;

    /** Additional details such as UPI ID, Card number, etc. */
    private String paymentDetails;

    // ===================== Getters and Setters =====================

    /**
     * @return Payment ID
     */
    public int getPaymentId() {
        return paymentId;
    }

    /**
     * @param paymentId ID to be set
     */
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * @return Associated ticket
     */
    public Ticket getTicket() {
        return ticket;
    }

    /**
     * @param ticket Ticket to associate
     */
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    /**
     * @return Payment amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount Amount to be set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return Date of payment
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param paymentDate Date to be set
     */
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * @return Mode of payment
     */
    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    /**
     * Converts a string to the PaymentMode enum and sets it. Defaults to UPI if invalid.
     * 
     * @param mode Mode of payment as string
     */
    public void setPaymentMode(String mode) {
        try {
            this.paymentMode = PaymentMode.valueOf(mode);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Invalid payment method: " + mode);
            this.paymentMode = PaymentMode.UPI;
        }
    }

    /**
     * @return Payment status
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * @param paymentStatus Status to be set
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * @return Additional payment details
     */
    public String getPaymentDetails() {
        return paymentDetails;
    }

    /**
     * @param paymentDetails Details to be set
     */
    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    // ===================== Constructors =====================

    /**
     * Constructor with essential values
     *
     * @param ticket         Associated ticket
     * @param amount         Amount paid
     * @param paymentMode    Mode of payment
     * @param paymentDetails Additional payment details
     */
    public Payment(Ticket ticket, double amount, PaymentMode paymentMode, String paymentDetails) {
        this.ticket = ticket;
        this.amount = amount;
        this.paymentDate = new Date();
        this.paymentMode = paymentMode;
        this.paymentStatus = "Pending";
        this.paymentDetails = paymentDetails;
    }

    /**
     * Constructor with default values (UPI as mode, pending status)
     *
     * @param ticket Associated ticket
     * @param amount Amount paid
     */
    public Payment(Ticket ticket, double amount) {
        this.ticket = ticket;
        this.amount = amount;
        this.paymentDate = new Date();
        this.paymentMode = PaymentMode.UPI;
        this.paymentStatus = "Pending";
        this.paymentDetails = "";
    }

    // ===================== toString =====================

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", ticket=" + (ticket != null ? ticket.getTicketId() : "null") +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", paymentMode=" + paymentMode +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentDetails='" + paymentDetails + '\'' +
                '}';
    }
}
