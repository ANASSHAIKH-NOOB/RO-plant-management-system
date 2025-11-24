package src.com.Entity.app;

import java.time.LocalDateTime;

public class SensorReading {
    
    private final LocalDateTime timestamp;
    private final double phValue;
    private final int tdsIn;
    private final int tdsOut;
    private final double flowRate;
    private final double pressure;

    public SensorReading(double phValue, int tdsIn, int tdsOut, double flowRate, double pressure) {
        this.timestamp = LocalDateTime.now();
        this.phValue = phValue;
        this.tdsIn = tdsIn;
        this.tdsOut = tdsOut;
        this.flowRate = flowRate;
        this.pressure = pressure;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public double getPhValue() { return phValue; }
    public int getTdsIn() { return tdsIn; }
    public int getTdsOut() { return tdsOut; }
    public double getFlowRate() { return flowRate; }
    public double getPressure() { return pressure; }

    @Override
    public String toString() {
        return "Reading[" + timestamp + "]: TDS In=" + tdsIn + ", Out=" + tdsOut;
    }
}