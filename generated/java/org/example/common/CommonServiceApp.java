package org.example.common;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.Map;
import java.util.HashMap;

/**
 * Common Service demo application
 */
public class CommonServiceApp {
    
    public static void main(String[] args) {
        CommonService commonService = new CommonServiceImpl();
        
        // Set up event listeners
        commonService.setStatusChangedListener(event -> {
            System.out.println("Status changed to: " + event.newStatus + " (Reason: " + event.reason + ")");
        });
        
        commonService.setConfigurationUpdatedListener(event -> {
            System.out.println("Configuration updated: " + event.updatedConfig.size() + " items");
        });
        
        System.out.println("Common Service Demo");
        System.out.println("===================");
        
        try {
            // Get service version
            CompletableFuture<CommonService.Version> versionFuture = commonService.getVersion();
            CommonService.Version version = versionFuture.get();
            
            System.out.println("Service Information:");
            System.out.println("  Version: " + version.major + "." + version.minor + "." + version.patch);
            System.out.println("  Build: " + version.buildInfo);
            
            // Get current position
            CompletableFuture<CommonService.Position> positionFuture = commonService.getCurrentPosition();
            CommonService.Position position = positionFuture.get();
            System.out.println("Current Position:");
            System.out.println("  Latitude: " + position.latitude + "°");
            System.out.println("  Longitude: " + position.longitude + "°");
            System.out.println("  Altitude: " + position.altitude + " m");
            
            // Get system status
            CompletableFuture<CommonService.StatusLevel> statusFuture = commonService.getSystemStatus();
            CommonService.StatusLevel status = statusFuture.get();
            System.out.println("System Status: " + status);
            
            // Test data validation
            System.out.println("\nTesting Data Validation:");
            String[] validationRules = {"required", "min_length:5", "max_length:50"};
            
            CompletableFuture<CommonService.ValidationResult> result1 = 
                commonService.validateData("Hello World!", validationRules);
            CommonService.ValidationResult validation1 = result1.get();
            System.out.println("  \"Hello World!\" validation: " + 
                (validation1.isValid ? "✅ Valid" : "❌ Invalid - " + 
                (validation1.errors.length > 0 ? validation1.errors[0] : "Unknown error")));
            
            CompletableFuture<CommonService.ValidationResult> result2 = 
                commonService.validateData("Hi", validationRules);
            CommonService.ValidationResult validation2 = result2.get();
            System.out.println("  \"Hi\" validation: " + 
                (validation2.isValid ? "✅ Valid" : "❌ Invalid - " + 
                (validation2.errors.length > 0 ? validation2.errors[0] : "Unknown error")));
            
            // Test configuration operations
            System.out.println("\nTesting Configuration:");
            
            CommonService.ConfigItem newConfig = new CommonService.ConfigItem();
            newConfig.key = "api_timeout";
            newConfig.value = "60000";
            newConfig.description = "API timeout in milliseconds";
            
            Map<String, CommonService.ConfigItem> configMap = new HashMap<>();
            configMap.put(newConfig.key, newConfig);
            
            CompletableFuture<CommonService.Response> configResult = 
                commonService.updateConfiguration(configMap);
            CommonService.Response configResponse = configResult.get();
            
            if (configResponse.success) {
                System.out.println("✅ Configuration updated: " + configResponse.message);
            } else {
                System.out.println("❌ Configuration update failed: " + configResponse.message);
            }
            
            // Test event simulation
            if (commonService instanceof CommonServiceImpl) {
                System.out.println("\nSimulating Events:");
                CommonServiceImpl impl = (CommonServiceImpl) commonService;
                
                impl.simulateStatusChange(CommonService.StatusLevel.WARNING, "System load high");
                impl.updatePosition(40.7128, -74.0060, 10.0); // New York coordinates
                impl.simulateStatusChange(CommonService.StatusLevel.OK, "System load normalized");
            }
            
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
