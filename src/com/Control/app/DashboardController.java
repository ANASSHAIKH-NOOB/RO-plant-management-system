package src.com.Control.app;

import src.com.Interface.app.DashboardView;
import src.com.Entity.app.ProcessMonitorService;
import src.com.Entity.app.AlertService;
import src.com.Entity.app.SensorReading;
import src.com.Entity.app.AlertLog;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DashboardController {

    private final DashboardView view;
    private final ProcessMonitorService monitorService;
    private final AlertService alertService;
    
    private ScheduledExecutorService scheduler;

    public DashboardController(DashboardView view) {
        this.view = view;
        this.monitorService = new ProcessMonitorService();
        this.alertService = new AlertService();
    }

    public void startSystem() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::onTick, 0, 1, TimeUnit.SECONDS);
    }

    public void stopSystem() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    public void triggerReset() {
        monitorService.resetSystem();
        view.showSystemStatus("SYSTEM RESET COMPLETE", false);
    }

    private void onTick() {
        SensorReading reading = monitorService.getLatestReading();
        
        if (reading == null) return;

        double rejectionRate = monitorService.calculateRejectionRate(reading);
        boolean isCritical = rejectionRate < 95.0;

        view.updateSensorReadings(reading);
        view.updateRejectionRate(rejectionRate, isCritical);
        view.appendChartData(reading);

        if (isCritical) {
            view.showSystemStatus("CRITICAL: MEMBRANE DEGRADING", true);
            alertService.logAlert("Low Rejection Rate: " + String.format("%.1f", rejectionRate) + "%", "HIGH");
        } else {
            view.showSystemStatus("SYSTEM OPTIMAL", false);
        }

        checkAlerts();
    }

    private void checkAlerts() {
        if (alertService.hasCriticalAlerts()) {
            AlertLog topAlert = alertService.getHighestPriorityAlert();
            view.triggerCriticalAlertPopup(topAlert);
            alertService.acknowledgeAlert();
        }
    }
}
