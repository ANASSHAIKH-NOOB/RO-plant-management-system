package src.com.Control.app;

import src.com.Entity.app.InventoryItem;
import src.com.Entity.app.InventoryService;
import src.com.Interface.app.InventoryView;

import java.util.List;
import java.util.UUID;

public class InventoryController {

    private final InventoryView view;
    private final InventoryService service;

    public InventoryController(InventoryView view) {
        this.view = view;
        this.service = InventoryService.getInstance();
    }

    public void loadAllItems() {
        List<InventoryItem> items = service.getAllItems();
        view.updateInventoryTable(items);
    }

    public void handleSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadAllItems();
            return;
        }
        
        List<InventoryItem> results = service.searchItems(query);
        view.updateInventoryTable(results);
    }

    public void handleAddItem(String name, String category, String stockStr, String priceStr) {
        try {
            if (name.isEmpty() || category.isEmpty()) throw new IllegalArgumentException("Fields cannot be empty");
            int stock = Integer.parseInt(stockStr);
            double price = Double.parseDouble(priceStr);

            String id = "ITEM-" + UUID.randomUUID().toString().substring(0,8);
            InventoryItem newItem = new InventoryItem(id, name, category, stock, 10, price);

            service.addNewItem(newItem);
            
            view.showSuccess("Item added successfully!");
            loadAllItems();

        } catch (NumberFormatException e) {
            view.showError("Stock and Price must be valid numbers.");
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }
}