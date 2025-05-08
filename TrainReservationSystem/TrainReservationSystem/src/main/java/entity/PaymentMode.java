package entity;

/**
 * Enum representing various modes of payment available in the Train Reservation System.
 */
public enum PaymentMode {
    
    // Payment made through UPI (e.g., Google Pay, PhonePe)
    UPI,
    
    // Payment made using Net Banking (online bank transfers)
    NET_BANKING,
    
    // Payment made using Credit/Debit Card
    CARD,
    
    // Payment made through digital wallets (e.g., Paytm, Amazon Pay)
    WALLET
}

