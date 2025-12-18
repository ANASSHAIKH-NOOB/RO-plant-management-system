package src.com.Entity.app;

import src.com.Persistance.app.SalesDAO;
import src.com.Entity.app.SalesTransaction;
import src.com.Entity.app.InventoryItem;

import java.util.List;

public class SalesService {

    private final SalesDAO salesDAO;
    private final InventoryService inventoryService;

    public SalesService() {
        this.salesDAO = new SalesDAO();
        this.inventoryService = InventoryService.getInstance(); 
    }

    public void processSale(String itemName, int quantity) throws IllegalArgumentException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        InventoryItem item = inventoryService.getItemByName(itemName);

        if (item == null) {
            throw new IllegalArgumentException("Item not found: " + itemName);
        }

        if (item.getCurrentStock() < quantity) {
            throw new IllegalArgumentException("Insufficient Stock! Only " + item.getCurrentStock() + " remaining.");
        }

        inventoryService.updateStock(item.getName(), -quantity); 

        SalesTransaction transaction = new SalesTransaction(
            item.getId(),
            item.getName(),
            quantity,
            item.getUnitPrice()
        );

        try {
            salesDAO.save(transaction);
        } catch (Exception e) {
            System.err.println("CRITICAL: DB Save failed. Rolling back inventory.");
            inventoryService.updateStock(item.getName(), quantity); 
            throw new RuntimeException("Transaction failed. Database Error: " + e.getMessage());
        }
    }

    public double getDailyRevenue() {
        List<SalesTransaction> todaySales = salesDAO.getTodaySales();
        double total = 0.0;
        for (SalesTransaction t : todaySales) {
            total += t.getTotalAmount();
        }
        return total;
    }

    public List<SalesTransaction> getRecentSales() {
        return salesDAO.getTodaySales();
    }
}
