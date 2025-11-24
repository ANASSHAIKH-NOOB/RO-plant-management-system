package src.com.Interface.app;

import src.com.Entity.app.SensorReading;
import src.com.Entity.app.AlertLog;

public interface DashboardView {
    void updateSensorReadings(SensorReading reading);
    void updateRejectionRate(double rate, boolean isCritical);
    void appendChartData(SensorReading reading);
    void showSystemStatus(String status, boolean isError);
    void triggerCriticalAlertPopup(AlertLog alert);
}