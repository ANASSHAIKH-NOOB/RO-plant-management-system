package src.com.Persistance.app;

import src.com.Entity.app.InventoryItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    public InventoryDAO() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS inventory (" +
                     "id TEXT PRIMARY KEY," +
                     "name TEXT NOT NULL," +
                     "category TEXT," +
                     "stock INTEGER," +
                     "threshold INTEGER," +
                     "price TEXT)"; 
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("DB Error (Inventory Table): " + e.getMessage());
        }
    }

    public void addItem(InventoryItem item) throws SQLException {
        String sql = "INSERT INTO inventory(id, name, category, stock, threshold, price) VALUES(?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, item.getId());
            pstmt.setString(2, item.getName());
            pstmt.setString(3, item.getCategory());
            pstmt.setInt(4, item.getCurrentStock());
            pstmt.setInt(5, item.getMinThreshold());
            pstmt.setString(6, String.valueOf(item.getUnitPrice()));
            
            pstmt.executeUpdate();
        }
    }

    public List<InventoryItem> getAllItems() {
        List<InventoryItem> list = new ArrayList<>();
        String sql = "SELECT * FROM inventory";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                InventoryItem item = new InventoryItem(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getInt("stock"),
                    rs.getInt("threshold"),
                    Double.parseDouble(rs.getString("price"))
                );
                list.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error loading inventory: " + e.getMessage());
        }
        return list;
    }

    public void updateStockCount(String itemId, int newCount) throws SQLException {
        String sql = "UPDATE inventory SET stock = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newCount);
            pstmt.setString(2, itemId);
            pstmt.executeUpdate();
        }
    }
}