package org.example.automotive;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Complete Vehicle Dashboard implementation for automotive demo
 * Features: Real-time simulation, advanced warnings, maintenance tracking
 */
public class VehicleDashboardImpl implements VehicleDashboard {
    
    private VehicleData currentData;
    private List<WarningStatus> activeWarnings;
    private FuelConsumption fuelConsumption;
    private boolean useMetricUnits = true;
    private Random random = new Random();
    private ScheduledExecutorService scheduler;
    private Timer maintenanceTimer;
    
    // Attributes from FIDL interface
    private float currentSpeed = 0.0f;
    private float currentRPM = 800.0f; // Idle RPM
    private int displayBrightness = 75; // 0-100
    
    // Enhanced state tracking
    private boolean engineRunning = false;
    private long engineStartTime = 0;
    private Map<WarningLight, Long> warningHistory = new HashMap<>();
    private float totalFuelUsed = 0.0f;
    private long lastMaintenanceOdometer = 150000; // km
    
    private Consumer<VehicleDataChangedEvent> vehicleDataListener;
    private Consumer<WarningStatusChangedEvent> warningStatusListener;
    private Consumer<FuelLevelCriticalEvent> fuelCriticalListener;
    
    public VehicleDashboardImpl() {
        initializeVehicleData();
        initializeWarnings();
        initializeFuelConsumption();
        startRealTimeSimulation();
        setupMaintenanceTracking();
    }
    
    private void startRealTimeSimulation() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                getVehicleData().get();
                getFuelConsumption().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
    
    private void setupMaintenanceTracking() {
        maintenanceTimer = new Timer(true);
        maintenanceTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkMaintenanceStatus();
            }
        }, 0, 24 * 60 * 60 * 1000); // Check daily
    }
    
    private void checkMaintenanceStatus() {
        if (currentData.odometer >= lastMaintenanceOdometer + 10000) {
            simulateWarning(WarningLight.ENGINE_CHECK, "Time for maintenance check.");
        }
    }
    
    private void initializeVehicleData() {
        currentData = new VehicleData(
            65.5f,                        // speed km/h
            2450.0f,                      // engine RPM
            78.5f,                        // fuel level %
            92.3f,                        // engine temperature °C
            2.8f,                         // oil pressure bar
            156789,                       // odometer km
            423,                          // trip meter km
            TransmissionState.DRIVE,      // transmission
            EngineState.RUNNING           // engine state
        );
    }
    
    private void initializeWarnings() {
        activeWarnings = new ArrayList<>();
        // Add some example warnings
        activeWarnings.add(new WarningStatus(
            WarningLight.TIRE_PRESSURE, 
            true, 
            "Front left tire pressure low", 
            StatusLevel.WARNING, 
            System.currentTimeMillis()
        ));
    }
    
    private void initializeFuelConsumption() {
        fuelConsumption = new FuelConsumption(
            7.8f,    // instant L/100km
            8.2f,    // average L/100km
            450.0f,  // range estimate km
            3200     // fuel used on trip mL
        );
    }
    
    @Override
    public CompletableFuture<VehicleData> getVehicleData() {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate real-time data updates
            currentData.speed += (random.nextFloat() - 0.5f) * 10;
            currentData.engineRPM += (random.nextFloat() - 0.5f) * 200;
            currentData.fuelLevel -= 0.01f; // Gradually decrease fuel
            
            // Ensure realistic bounds
            currentData.speed = Math.max(0, Math.min(200, currentData.speed));
            currentData.engineRPM = Math.max(800, Math.min(6000, currentData.engineRPM));
            currentData.fuelLevel = Math.max(0, currentData.fuelLevel);
            
            // Trigger events if needed
            if (vehicleDataListener != null) {
                vehicleDataListener.accept(new VehicleDataChangedEvent(currentData));
            }
            
            // Check for fuel critical level
            if (currentData.fuelLevel < 10.0f && fuelCriticalListener != null) {
                fuelCriticalListener.accept(new FuelLevelCriticalEvent(
                    currentData.fuelLevel, 
                    fuelConsumption.rangeEstimate
                ));
            }
            
            return currentData;
        });
    }
    
    @Override
    public CompletableFuture<WarningStatus[]> getActiveWarnings() {
        return CompletableFuture.completedFuture(
            activeWarnings.toArray(new WarningStatus[0])
        );
    }
    
    @Override
    public CompletableFuture<FuelConsumption> getFuelConsumption() {
        return CompletableFuture.supplyAsync(() -> {
            // Update fuel consumption based on current driving
            fuelConsumption.instantConsumption = 6.5f + (currentData.speed / 100.0f) * 3.0f;
            fuelConsumption.rangeEstimate = (currentData.fuelLevel / 100.0f) * 50.0f * 
                                          (100.0f / fuelConsumption.averageConsumption);
            
            return fuelConsumption;
        });
    }
    
    @Override
    public CompletableFuture<Boolean> resetTripMeter() {
        return CompletableFuture.supplyAsync(() -> {
            currentData.tripMeter = 0;
            fuelConsumption.fuelUsedTrip = 0;
            return true;
        });
    }
    
    @Override
    public CompletableFuture<Boolean> setDisplayUnits(boolean useMetric) {
        return CompletableFuture.supplyAsync(() -> {
            this.useMetricUnits = useMetric;
            return true;
        });
    }
    
    @Override
    public void setVehicleDataChangedListener(Consumer<VehicleDataChangedEvent> listener) {
        this.vehicleDataListener = listener;
    }
    
    @Override
    public void setWarningStatusChangedListener(Consumer<WarningStatusChangedEvent> listener) {
        this.warningStatusListener = listener;
    }
    
    @Override
    public void setFuelLevelCriticalListener(Consumer<FuelLevelCriticalEvent> listener) {
        this.fuelCriticalListener = listener;
    }
    
    // Simulate warning activation
    public void simulateWarning(WarningLight type, String message) {
        WarningStatus warning = new WarningStatus(
            type, true, message, StatusLevel.ERROR, System.currentTimeMillis()
        );
        activeWarnings.add(warning);
        
        if (warningStatusListener != null) {
            warningStatusListener.accept(new WarningStatusChangedEvent(warning));
        }
    }
}
