package src.com.Entity.app;

import java.math.BigDecimal;

public class InventoryItem {

    private final String id;
    private final String name;
    private final String category;
    private int currentStock;
    private final int minThreshold;
    private final BigDecimal unitPrice;

    public InventoryItem(String id, String name, String category, int currentStock, int minThreshold, double unitPrice) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.currentStock = currentStock;
        this.minThreshold = minThreshold;
        this.unitPrice = BigDecimal.valueOf(unitPrice);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getCurrentStock() { return currentStock; }
    public int getMinThreshold() { return minThreshold; }
    public double getUnitPrice() { return unitPrice.doubleValue(); }

    public boolean isLowStock() {
        return currentStock <= minThreshold;
    }

    public void adjustStock(int quantityChange) {
        int newStock = this.currentStock + quantityChange;
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.currentStock = newStock;
    }
    
    @Override
    public String toString() {
        return name + " (" + currentStock + ")";
    }
}