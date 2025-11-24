package src.com.Entity.app;

import src.com.Entity.app.SensorReading;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ProcessMonitorService {

    private static final int WINDOW_SIZE = 50; 
    private final Queue<SensorReading> slidingWindow;
    private final Random random;
    
    private double currentMembraneHealth = 1.0; 
    private double currentPressure = 15.0; 

    public ProcessMonitorService() {
        this.slidingWindow = new LinkedList<>();
        this.random = new Random();
    }

    public SensorReading getLatestReading() {
        SensorReading newReading = generateSimulatedData();
        
        slidingWindow.add(newReading);
        
        if (slidingWindow.size() > WINDOW_SIZE) {
            slidingWindow.poll();
        }

        return newReading;
    }

    public double calculateRejectionRate(SensorReading reading) {
        if (reading == null || reading.getTdsIn() == 0) {
            return 0.0;
        }
        return (1.0 - ((double) reading.getTdsOut() / reading.getTdsIn())) * 100.0;
    }

    public void resetSystem() {
        this.currentMembraneHealth = 1.0;
        this.currentPressure = 15.0;
        this.slidingWindow.clear();
    }

    private SensorReading generateSimulatedData() {
        int feedTds = 800 + random.nextInt(400);

        currentMembraneHealth -= 0.0001; 
        if (currentMembraneHealth < 0.7) currentMembraneHealth = 0.7; 

        double removalEfficiency = 0.98 * currentMembraneHealth;
        int permeateTds = (int) (feedTds * (1.0 - removalEfficiency));

        double ph = 6.8 + (0.4 * random.nextDouble());
        double flow = 1000.0 * currentPressure * 0.1;

        return new SensorReading(ph, feedTds, permeateTds, flow, currentPressure);
    }
}