package src.com.Entity.app;

import java.time.LocalDateTime;

public class AlertLog implements Comparable<AlertLog> {

    private String id;
    private String message;
    private int priorityLevel;
    private LocalDateTime timestamp;
    private boolean isAcknowledged;

    public AlertLog(String message, int priorityLevel) {
        this.message = message;
        this.priorityLevel = priorityLevel;
        this.timestamp = LocalDateTime.now();
        this.isAcknowledged = false;
        this.id = "ALR-" + System.currentTimeMillis();
    }

    @Override
    public int compareTo(AlertLog other) {
        return Integer.compare(this.priorityLevel, other.priorityLevel);
    }

    public String getMessage() { return message; }
    public int getPriorityLevel() { return priorityLevel; }
    
    public String getSeverityLabel() {
        switch (priorityLevel) {
            case 1: return "CRITICAL";
            case 2: return "HIGH";
            case 3: return "MEDIUM";
            default: return "INFO";
        }
    }

    public boolean isAcknowledged() { return isAcknowledged; }
    public void setAcknowledged(boolean acknowledged) { isAcknowledged = acknowledged; }
}
