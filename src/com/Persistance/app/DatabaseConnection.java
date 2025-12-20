package src.com.Persistance.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:ro_plant.db";
    
    private static Connection instance = null;

    private DatabaseConnection() {}

    public static synchronized Connection connect() throws SQLException {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection(URL);
        }
        return instance;
    }

    public static void initialize() {
        String sqlSensor = "CREATE TABLE IF NOT EXISTS sensor_logs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "timestamp TEXT," +
                "ph_value REAL," +
                "tds_in INTEGER," +
                "tds_out INTEGER," +
                "flow_rate REAL," +
                "pressure REAL);";

        String sqlAlerts = "CREATE TABLE IF NOT EXISTS alert_logs (" +
                "id TEXT PRIMARY KEY," +
                "timestamp TEXT," +
                "message TEXT," +
                "priority INTEGER," +
                "acknowledged INTEGER);";

        try (Connection conn = connect();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sqlSensor);
            stmt.execute(sqlAlerts);
        } catch (SQLException e) {
            System.out.println("DB Init Error: " + e.getMessage());
        }
    }
}
