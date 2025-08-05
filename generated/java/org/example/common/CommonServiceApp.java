package org.example.common;

/**
 * Demo application for CommonService
 */
public class CommonServiceApp {

    public static void main(String[] args) {
        CommonServiceImpl commonService = new CommonServiceImpl();
        
        // Set up event listeners
        commonService.setStatusChangedListener(event -> {
            System.out.println("Status changed:");
            System.out.println("New Status: " + event.newStatus);
            System.out.println("Reason: " + event.reason);
            System.out.println("Timestamp: " + event.timestamp.isoFormat);
        });
        
        commonService.setConfigurationUpdatedListener(event -> {
            System.out.println("Configuration updated:");
            System.out.println("Updated config items: " + event.updatedConfig.size());
            System.out.println("Timestamp: " + event.timestamp.isoFormat);
        });
        
        // Running demo operations
        System.out.println("Common Service Demo");
        System.out.println("===================");
        
        try {
            // Get version
            CommonService.Version version = commonService.getVersion().get();
            System.out.println("System Version: " + version);
            
            // Get current position
            CommonService.Position position = commonService.getCurrentPosition().get();
            System.out.println("Current Position: " + position);
            
            // Get current time
            CommonService.TimeInfo timeInfo = commonService.getCurrentTime().get();
            System.out.println("Current Time: " + timeInfo.isoFormat + " (" + timeInfo.timezone + ")");
            
            // Test data validation
            String[] rules = {"no-numbers"};
            CommonService.ValidationResult result = commonService.validateData("hello123", rules).get();
            System.out.println("Validation Result: " + result.isValid);
            if (result.errors.length > 0) {
                System.out.println("Errors: " + String.join(", ", result.errors));
            }
            
            // Process positions
            CommonService.Position[] positions = {
                new CommonService.Position(52.0, 13.0, 30.0),
                new CommonService.Position(53.0, 14.0, 40.0),
                new CommonService.Position(51.0, 12.0, 20.0)
            };
            CommonService.Position average = commonService.processPositions(positions).get();
            System.out.println("Average Position: " + average);
            
            // Update configuration
            java.util.Map<String, CommonService.ConfigItem> config = new java.util.HashMap<>();
            config.put("debug", new CommonService.ConfigItem("debug", "true", "Enable debug mode"));
            config.put("timeout", new CommonService.ConfigItem("timeout", "30", "Request timeout in seconds"));
            
            CommonService.Response response = commonService.updateConfiguration(config).get();
            System.out.println("Configuration Update: " + response.message);
            
            // Get system status
            CommonService.StatusLevel status = commonService.getSystemStatus().get();
            System.out.println("System Status: " + status);
            
            // Get status details
            java.util.Map<String, String> details = commonService.getStatusDetails().get();
            System.out.println("Status Details:");
            details.forEach((key, value) -> System.out.println("  " + key + ": " + value));
            
            // Get attributes
            System.out.println("Current Status: " + commonService.getCurrentStatus());
            System.out.println("Uptime: " + commonService.getUptime() + " seconds");
            System.out.println("Debug Mode: " + commonService.getDebugMode());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
