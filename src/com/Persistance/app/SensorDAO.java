package src.com.Persistance.app;

import src.com.Entity.app.SensorReading;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SensorDAO {

    public void save(SensorReading reading) {
        String sql = "INSERT INTO sensor_logs(timestamp, ph_value, tds_in, tds_out, flow_rate, pressure) VALUES(?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, reading.getTimestamp().toString());
            pstmt.setDouble(2, reading.getPhValue());
            pstmt.setInt(3, reading.getTdsIn());
            pstmt.setInt(4, reading.getTdsOut());
            pstmt.setDouble(5, reading.getFlowRate());
            pstmt.setDouble(6, reading.getPressure());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Sensor Log Error: " + e.getMessage());
        }
    }
}