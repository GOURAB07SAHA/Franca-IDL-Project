package org.example.automotive;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.example.common.*;

/**
 * VehicleDashboard interface generated from FRANCA IDL
 */
public interface VehicleDashboard {
    
    enum TransmissionState {
        PARK(0), REVERSE(1), NEUTRAL(2), DRIVE(3), SPORT(4), MANUAL(5);
        
        private final int value;
        TransmissionState(int value) { this.value = value; }
        public int getValue() { return value; }
    }
    
    enum EngineState {
        OFF(0), STARTING(1), IDLE(2), RUNNING(3), OVERHEATED(4), ERROR(5);
        
        private final int value;
        EngineState(int value) { this.value = value; }
        public int getValue() { return value; }
    }
    
    enum WarningLight {
        ENGINE_CHECK(1), OIL_PRESSURE(2), BATTERY(3), TEMPERATURE(4), BRAKE(5),
        ABS(6), AIRBAG(7), SEAT_BELT(8), FUEL_LOW(9), TIRE_PRESSURE(10);
        
        private final int value;
        WarningLight(int value) { this.value = value; }
        public int getValue() { return value; }
    }
    
    enum StatusLevel {
        OK(0), WARNING(1), ERROR(2), CRITICAL(3);
        
        private final int value;
        StatusLevel(int value) { this.value = value; }
        public int getValue() { return value; }
    }
    
    class VehicleData {
        public float speed;
        public float engineRPM;
        public float fuelLevel;
        public float engineTemperature;
        public float oilPressure;
        public int odometer;
        public int tripMeter;
        public TransmissionState transmission;
        public EngineState engineState;
    }
    
    class WarningStatus {
        public WarningLight type;
        public boolean isActive;
        public String message;
        public StatusLevel severity;
        public long activatedTime;
    }
    
    class FuelConsumption {
        public float instantConsumption;
        public float averageConsumption;
        public float rangeEstimate;
        public int fuelUsedTrip;
    }
    
    // Methods
    CompletableFuture<VehicleData> getVehicleData();
    CompletableFuture<WarningStatus[]> getActiveWarnings();
    CompletableFuture<FuelConsumption> getFuelConsumption();
    CompletableFuture<Boolean> resetTripMeter();
    CompletableFuture<Boolean> setDisplayUnits(boolean useMetric);
    
    // Event listeners
    void setVehicleDataChangedListener(Consumer<VehicleDataChangedEvent> listener);
    void setWarningStatusChangedListener(Consumer<WarningStatusChangedEvent> listener);
    void setFuelLevelCriticalListener(Consumer<FuelLevelCriticalEvent> listener);
    
    // Event classes
    class VehicleDataChangedEvent {
        public VehicleData newData;
        
        public VehicleDataChangedEvent(VehicleData newData) {
            this.newData = newData;
        }
    }
    
    class WarningStatusChangedEvent {
        public WarningStatus warning;
        
        public WarningStatusChangedEvent(WarningStatus warning) {
            this.warning = warning;
        }
    }
    
    class FuelLevelCriticalEvent {
        public float remainingFuel;
        public float estimatedRange;
        
        public FuelLevelCriticalEvent(float remainingFuel, float estimatedRange) {
            this.remainingFuel = remainingFuel;
            this.estimatedRange = estimatedRange;
        }
    }
}
