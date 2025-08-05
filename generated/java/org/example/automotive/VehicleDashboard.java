package org.example.automotive;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Vehicle Dashboard interface generated from FRANCA IDL
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
        ENGINE_CHECK(1), OIL_PRESSURE(2), BATTERY(3), TEMPERATURE(4), 
        BRAKE(5), ABS(6), AIRBAG(7), SEAT_BELT(8), FUEL_LOW(9), TIRE_PRESSURE(10);
        
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
        public float speed;              // km/h
        public float engineRPM;          // revolutions per minute
        public float fuelLevel;          // percentage 0-100
        public float engineTemperature;  // Celsius
        public float oilPressure;        // bar
        public int odometer;             // total kilometers
        public int tripMeter;            // trip kilometers
        public TransmissionState transmission;
        public EngineState engineState;
        
        public VehicleData() {}
        
        public VehicleData(float speed, float rpm, float fuel, float temp, float oil, 
                          int odometer, int trip, TransmissionState trans, EngineState engine) {
            this.speed = speed;
            this.engineRPM = rpm;
            this.fuelLevel = fuel;
            this.engineTemperature = temp;
            this.oilPressure = oil;
            this.odometer = odometer;
            this.tripMeter = trip;
            this.transmission = trans;
            this.engineState = engine;
        }
    }
    
    class WarningStatus {
        public WarningLight type;
        public boolean isActive;
        public String message;
        public StatusLevel severity;
        public long activatedTime;
        
        public WarningStatus() {}
        
        public WarningStatus(WarningLight type, boolean active, String msg, StatusLevel sev, long time) {
            this.type = type;
            this.isActive = active;
            this.message = msg;
            this.severity = sev;
            this.activatedTime = time;
        }
    }
    
    class FuelConsumption {
        public float instantConsumption;  // L/100km
        public float averageConsumption;  // L/100km
        public float rangeEstimate;       // km remaining
        public int fuelUsedTrip;          // mL
        
        public FuelConsumption() {}
        
        public FuelConsumption(float instant, float average, float range, int used) {
            this.instantConsumption = instant;
            this.averageConsumption = average;
            this.rangeEstimate = range;
            this.fuelUsedTrip = used;
        }
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
        
        public VehicleDataChangedEvent(VehicleData data) {
            this.newData = data;
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
        
        public FuelLevelCriticalEvent(float fuel, float range) {
            this.remainingFuel = fuel;
            this.estimatedRange = range;
        }
    }
}
