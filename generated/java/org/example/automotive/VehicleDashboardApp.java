package org.example.automotive;

/**
 * Demo application for Vehicle Dashboard
 */
public class VehicleDashboardApp {

    public static void main(String[] args) {
        VehicleDashboardImpl dashboard = new VehicleDashboardImpl();

        // Set up event listeners
        dashboard.setVehicleDataChangedListener(event -> {
            System.out.println("Vehicle data changed:");
            System.out.println("Speed: " + event.newData.speed);
            System.out.println("RPM: " + event.newData.engineRPM);
            System.out.println("Fuel Level: " + event.newData.fuelLevel);
        });

        dashboard.setWarningStatusChangedListener(event -> {
            System.out.println("Warning status changed:");
            System.out.println("Type: " + event.warning.type);
            System.out.println("Message: " + event.warning.message);
        });

        dashboard.setFuelLevelCriticalListener(event -> {
            System.out.println("Fuel level critical: " + event.remainingFuel + "%");
            System.out.println("Estimated range: " + event.estimatedRange + " km");
        });

        // Running demo operations
        System.out.println("Vehicle Dashboard Demo");
        System.out.println("========================");

        try {
            // Retrieve and display vehicle data
            VehicleDashboard.VehicleData data = dashboard.getVehicleData().get();
            System.out.println("Current Speed: " + data.speed + " km/h");
            System.out.println("Current RPM: " + data.engineRPM);
            System.out.println("Fuel Level: " + data.fuelLevel + "%");

            // Retrieve and display active warnings
            VehicleDashboard.WarningStatus[] warnings = dashboard.getActiveWarnings().get();
            for (VehicleDashboard.WarningStatus warning : warnings) {
                System.out.println("Warning: " + warning.message);
            }

            // Simulating additional demo actions
            dashboard.simulateWarning(VehicleDashboard.WarningLight.ENGINE_CHECK, "Engine check required.");

            // Wait to see events
            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
