package src.com.Entity.app;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class SalesTransaction {

    private final String transactionId;
    private final String itemId;
    private final String itemName;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal totalAmount;
    private final LocalDateTime timestamp;

    private static final DateTimeFormatter DB_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SalesTransaction(String itemId, String itemName, int quantity, double unitPriceVal) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive number.");
        }
        if (unitPriceVal < 0) {
            throw new IllegalArgumentException("Unit Price cannot be negative.");
        }

        this.transactionId = "TXN-" + UUID.randomUUID().toString();
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;

        this.unitPrice = BigDecimal.valueOf(unitPriceVal);
        this.totalAmount = this.unitPrice.multiply(BigDecimal.valueOf(quantity));
        this.timestamp = LocalDateTime.now();
    }

    public SalesTransaction(String transactionId, String itemId, String itemName, int quantity, double unitPriceVal, double totalAmountVal, String timestampStr) {
        this.transactionId = transactionId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        
        this.unitPrice = BigDecimal.valueOf(unitPriceVal);
        this.totalAmount = BigDecimal.valueOf(totalAmountVal);

        LocalDateTime parsedDate;
        try {
            parsedDate = LocalDateTime.parse(timestampStr, DB_FORMATTER);
        } catch (DateTimeParseException e) {
            try {
                parsedDate = LocalDateTime.parse(timestampStr);
            } catch (DateTimeParseException ex) {
                System.err.println("CRITICAL: Failed to parse date '" + timestampStr + "' for txn " + transactionId);
                parsedDate = LocalDateTime.now(); 
            }
        }
        this.timestamp = parsedDate;
    }

    public String getTransactionId() { return transactionId; }
    public String getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice.doubleValue(); }
    public double getTotalAmount() { return totalAmount.doubleValue(); }
    public LocalDateTime getTimestamp() { return timestamp; }

    public String getFormattedTimestamp() {
        return timestamp.format(DB_FORMATTER);
    }

    @Override
    public String toString() {
        return String.format("%s: Sold %d x %s for $%.2f", transactionId, quantity, itemName, totalAmount);
    }
}