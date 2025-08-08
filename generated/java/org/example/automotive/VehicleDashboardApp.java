package org.example.automotive;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Vehicle Dashboard demo application
 */
public class VehicleDashboardApp {
    
    public static void main(String[] args) {
        VehicleDashboard dashboard = new VehicleDashboardImpl();
        
        // Set up event listeners
        dashboard.setVehicleDataChangedListener(event -> {
            System.out.println("Vehicle data updated - Speed: " + event.newData.speed + " km/h");
        });
        
        dashboard.setWarningStatusChangedListener(event -> {
            System.out.println("Warning: " + event.warning.type + " - " + event.warning.message);
        });
        
        dashboard.setFuelLevelCriticalListener(event -> {
            System.out.println("FUEL CRITICAL! Remaining: " + event.remainingFuel + "L, Range: " + event.estimatedRange + "km");
        });
        
        System.out.println("Vehicle Dashboard Demo");
        System.out.println("========================");
        
        try {
            // Get current vehicle data
            CompletableFuture<VehicleDashboard.VehicleData> dataFuture = dashboard.getVehicleData();
            VehicleDashboard.VehicleData data = dataFuture.get();
            
            System.out.println("Current Vehicle Status:");
            System.out.println("  Speed: " + data.speed + " km/h");
            System.out.println("  Engine RPM: " + data.engineRPM);
            System.out.println("  Fuel Level: " + data.fuelLevel + "%");
            System.out.println("  Engine Temp: " + data.engineTemperature + "°C");
            System.out.println("  Oil Pressure: " + data.oilPressure + " PSI");
            System.out.println("  Odometer: " + data.odometer + " km");
            System.out.println("  Trip Meter: " + data.tripMeter + " km");
            System.out.println("  Transmission: " + data.transmission);
            System.out.println("  Engine State: " + data.engineState);
            
            // Get active warnings
            CompletableFuture<VehicleDashboard.WarningStatus[]> warningsFuture = dashboard.getActiveWarnings();
            VehicleDashboard.WarningStatus[] warnings = warningsFuture.get();
            System.out.println("Active Warnings: " + warnings.length);
            
            // Get fuel consumption
            CompletableFuture<VehicleDashboard.FuelConsumption> fuelFuture = dashboard.getFuelConsumption();
            VehicleDashboard.FuelConsumption fuel = fuelFuture.get();
            System.out.println("Fuel Consumption:");
            System.out.println("  Instant: " + fuel.instantConsumption + " L/100km");
            System.out.println("  Average: " + fuel.averageConsumption + " L/100km");
            System.out.println("  Range: " + fuel.rangeEstimate + " km");
            System.out.println("  Trip Fuel Used: " + fuel.fuelUsedTrip + " L");
            
            // Test operations
            System.out.println("\nTesting Operations:");
            
            // Reset trip meter
            CompletableFuture<Boolean> resetResult = dashboard.resetTripMeter();
            if (resetResult.get()) {
                System.out.println("✅ Trip meter reset successfully");
            }
            
            // Set display units
            CompletableFuture<Boolean> unitsResult = dashboard.setDisplayUnits(true);
            if (unitsResult.get()) {
                System.out.println("✅ Display units set to metric");
            }
            
            // Test event simulation (if implementation supports it)
            if (dashboard instanceof VehicleDashboardImpl) {
                System.out.println("\nSimulating Events:");
                VehicleDashboardImpl impl = (VehicleDashboardImpl) dashboard;
                
                impl.simulateVehicleDataChange();
                impl.simulateFuelLevelCritical();
            }
            
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
