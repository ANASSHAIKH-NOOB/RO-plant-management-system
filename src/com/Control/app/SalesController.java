package src.com.Control.app;

import src.com.Entity.app.SalesService;
import src.com.Interface.app.SalesView;
import src.com.Entity.app.SalesTransaction;

import java.util.List;

public class SalesController {

    private final SalesView view;
    private final SalesService salesService;

    public SalesController(SalesView view) {
        this.view = view;
        this.salesService = new SalesService();
    }

    public void loadDailyData() {
        refreshTable();
        refreshRevenue();
    }

    public void handleSaleRequest(String itemName, String quantityStr) {
        if (itemName == null || itemName.trim().isEmpty()) {
            view.showTransactionError("Please select or type an item name.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            view.showTransactionError("Quantity must be a valid number.");
            return;
        }

        try {
            salesService.processSale(itemName, quantity);
            
            view.showTransactionSuccess("Sold " + quantity + " x " + itemName);
            view.clearInputFields();
            
            refreshTable();
            refreshRevenue();

        } catch (IllegalArgumentException e) {
            view.showTransactionError(e.getMessage());
        } catch (RuntimeException e) {
            view.showTransactionError("System Error: " + e.getMessage());
        }
    }

    private void refreshTable() {
        List<SalesTransaction> recentSales = salesService.getRecentSales();
        view.updateTransactionTable(recentSales);
    }

    private void refreshRevenue() {
        double revenue = salesService.getDailyRevenue();
        view.updateRevenueDisplay(revenue);
    }
}