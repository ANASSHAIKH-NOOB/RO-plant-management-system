package src.com.Persistance.app;


import src.com.Entity.app.SalesTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO {

    public SalesDAO() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS sales_transactions (" +
                     "transaction_id TEXT PRIMARY KEY," +
                     "item_id TEXT," +
                     "item_name TEXT," +
                     "quantity INTEGER," +
                     "unit_price TEXT," +   
                     "total_amount TEXT," + 
                     "timestamp TEXT)";
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("DB Error (Sales Table): " + e.getMessage());
        }
    }

    public void save(SalesTransaction t) throws SQLException {
        String sql = "INSERT INTO sales_transactions(transaction_id, item_id, item_name, quantity, unit_price, total_amount, timestamp) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, t.getTransactionId());
            pstmt.setString(2, t.getItemId());
            pstmt.setString(3, t.getItemName());
            pstmt.setInt(4, t.getQuantity());
            pstmt.setString(5, String.valueOf(t.getUnitPrice()));
            pstmt.setString(6, String.valueOf(t.getTotalAmount()));
            pstmt.setString(7, t.getFormattedTimestamp());
            
            pstmt.executeUpdate();
        }
    }

    public List<SalesTransaction> getTodaySales() {
        List<SalesTransaction> salesList = new ArrayList<>();
        
        String todayPrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String sql = "SELECT * FROM sales_transactions WHERE timestamp LIKE ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, todayPrefix + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    double unitPrice = Double.parseDouble(rs.getString("unit_price"));
                    double totalAmount = Double.parseDouble(rs.getString("total_amount"));

                    SalesTransaction t = new SalesTransaction(
                        rs.getString("transaction_id"),
                        rs.getString("item_id"),
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        unitPrice,
                        totalAmount,
                        rs.getString("timestamp")
                    );
                    salesList.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching sales: " + e.getMessage());
        }
        return salesList;
    }
}