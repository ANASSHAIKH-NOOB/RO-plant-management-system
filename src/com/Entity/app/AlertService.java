package src.com.Entity.app;
import src.com.Entity.app.AlertLog;
import java.util.PriorityQueue;
import java.util.Queue;

public class AlertService {

    private final Queue<AlertLog> alertQueue;
    private static final int MAX_QUEUE_SIZE = 100;

    public AlertService() {
        this.alertQueue = new PriorityQueue<>();
    }

    public void logAlert(String message, String severityLevel) {
        if (alertQueue.size() >= MAX_QUEUE_SIZE) {
            return; 
        }

        for (AlertLog alert : alertQueue) {
            if (alert.getMessage().equals(message)) {
                return;
            }
        }

        int priority = mapSeverityToPriority(severityLevel);
        AlertLog newAlert = new AlertLog(message, priority);
        alertQueue.add(newAlert);
    }

    public boolean hasCriticalAlerts() {
        return !alertQueue.isEmpty();
    }

    public AlertLog getHighestPriorityAlert() {
        return alertQueue.peek();
    }

    public void acknowledgeAlert() {
        alertQueue.poll();
    }

    private int mapSeverityToPriority(String severity) {
        switch (severity.toUpperCase()) {
            case "CRITICAL": return 1;
            case "HIGH": return 2;
            case "MEDIUM": return 3;
            default: return 4;
        }
    }
}
