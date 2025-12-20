package src.com.Interface.app;

import java.util.List;
import src.com.Entity.app.SalesTransaction;

public interface SalesView {
    
    void updateTransactionTable(List<SalesTransaction> transactions);
    
    void updateRevenueDisplay(double dailyTotal);
    
    void showTransactionSuccess(String message);
    
    void showTransactionError(String message);
    
    void clearInputFields();
}