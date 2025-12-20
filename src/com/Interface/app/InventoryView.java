package src.com.Interface.app;

import src.com.Entity.app.InventoryItem;
import java.util.List;

public interface InventoryView {

    void updateInventoryTable(List<InventoryItem> items);

    void showSuccess(String message);

    void showError(String message);
}