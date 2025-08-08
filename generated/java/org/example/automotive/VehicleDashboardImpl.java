package org.example.automotive;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.List;

/**
 * VehicleDashboard implementation with all required methods
 */
public class VehicleDashboardImpl implements VehicleDashboard {
    
    // Event listeners
    private Consumer<VehicleDataChangedEvent> vehicleDataChangedListener;
    private Consumer<WarningStatusChangedEvent> warningStatusChangedListener;
    private Consumer<FuelLevelCriticalEvent> fuelLevelCriticalListener;
    
    // Current vehicle state
    private VehicleData currentVehicleData;
    private List<WarningStatus> activeWarnings;
    private FuelConsumption currentFuelConsumption;
    private boolean useMetricUnits = true;
    
    public VehicleDashboardImpl() {
        // Initialize with default data
        initializeDefaultData();
    }
    
    private void initializeDefaultData() {
        currentVehicleData = new VehicleData();
        currentVehicleData.speed = 0.0f;
        currentVehicleData.engineRPM = 800.0f;
        currentVehicleData.fuelLevel = 50.0f;
        currentVehicleData.engineTemperature = 90.0f;
        currentVehicleData.oilPressure = 45.0f;
        currentVehicleData.odometer = 12345;
        currentVehicleData.tripMeter = 0;
        currentVehicleData.transmission = TransmissionState.PARK;
        currentVehicleData.engineState = EngineState.IDLE;
        
        activeWarnings = new ArrayList<>();
        
        currentFuelConsumption = new FuelConsumption();
        currentFuelConsumption.instantConsumption = 8.5f;
        currentFuelConsumption.averageConsumption = 7.2f;
        currentFuelConsumption.rangeEstimate = 450.0f;
        currentFuelConsumption.fuelUsedTrip = 25;
    }
    
    @Override
    public CompletableFuture<VehicleData> getVehicleData() {
        return CompletableFuture.completedFuture(currentVehicleData);
    }
    
    @Override
    public CompletableFuture<WarningStatus[]> getActiveWarnings() {
        WarningStatus[] warnings = activeWarnings.toArray(new WarningStatus[0]);
        return CompletableFuture.completedFuture(warnings);
    }
    
    @Override
    public CompletableFuture<FuelConsumption> getFuelConsumption() {
        return CompletableFuture.completedFuture(currentFuelConsumption);
    }
    
    @Override
    public CompletableFuture<Boolean> resetTripMeter() {
        currentVehicleData.tripMeter = 0;
        currentFuelConsumption.fuelUsedTrip = 0;
        return CompletableFuture.completedFuture(true);
    }
    
    @Override
    public CompletableFuture<Boolean> setDisplayUnits(boolean useMetric) {
        this.useMetricUnits = useMetric;
        return CompletableFuture.completedFuture(true);
    }
    
    @Override
    public void setVehicleDataChangedListener(Consumer<VehicleDataChangedEvent> listener) {
        this.vehicleDataChangedListener = listener;
    }
    
    @Override
    public void setWarningStatusChangedListener(Consumer<WarningStatusChangedEvent> listener) {
        this.warningStatusChangedListener = listener;
    }
    
    @Override
    public void setFuelLevelCriticalListener(Consumer<FuelLevelCriticalEvent> listener) {
        this.fuelLevelCriticalListener = listener;
    }
    
    // Simulation methods to trigger events (for testing)
    public void simulateVehicleDataChange() {
        if (vehicleDataChangedListener != null) {
            vehicleDataChangedListener.accept(new VehicleDataChangedEvent(currentVehicleData));
        }
    }
    
    public void simulateWarningStatusChange(WarningStatus warning) {
        if (warningStatusChangedListener != null) {
            warningStatusChangedListener.accept(new WarningStatusChangedEvent(warning));
        }
    }
    
    public void simulateFuelLevelCritical() {
        if (fuelLevelCriticalListener != null) {
            fuelLevelCriticalListener.accept(
                new FuelLevelCriticalEvent(currentVehicleData.fuelLevel, currentFuelConsumption.rangeEstimate)
            );
        }
    }
}
