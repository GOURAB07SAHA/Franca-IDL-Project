package org.example.common;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.Map;
import java.util.HashMap;

/**
 * CommonService implementation with all required methods
 */
public class CommonServiceImpl implements CommonService {
    
    // Event listeners
    private Consumer<StatusChangedEvent> statusChangedListener;
    private Consumer<ConfigurationUpdatedEvent> configurationUpdatedListener;
    
    // Current service state
    private StatusLevel currentStatus = StatusLevel.OK;
    private Version serviceVersion;
    private Position currentPosition;
    private Map<String, ConfigItem> configuration;
    
    public CommonServiceImpl() {
        initializeDefaultData();
    }
    
    private void initializeDefaultData() {
        serviceVersion = new Version();
        serviceVersion.major = 1;
        serviceVersion.minor = 0;
        serviceVersion.patch = 0;
        serviceVersion.buildInfo = "Build-001";
        
        currentPosition = new Position();
        currentPosition.latitude = 37.7749;
        currentPosition.longitude = -122.4194;
        currentPosition.altitude = 52.0;
        
        configuration = new HashMap<>();
        ConfigItem defaultConfig = new ConfigItem();
        defaultConfig.key = "default_timeout";
        defaultConfig.value = "30000";
        defaultConfig.description = "Default timeout in milliseconds";
        configuration.put("default_timeout", defaultConfig);
    }
    
    @Override
    public CompletableFuture<Version> getVersion() {
        return CompletableFuture.completedFuture(serviceVersion);
    }
    
    @Override
    public CompletableFuture<ValidationResult> validateData(String data, String[] rules) {
        return CompletableFuture.supplyAsync(() -> {
            ValidationResult result = new ValidationResult();
            
            if (data == null || data.trim().isEmpty()) {
                result.isValid = false;
                result.errors = new String[]{"Data cannot be null or empty"};
                result.warnings = new String[0];
            } else {
                result.isValid = true;
                result.errors = new String[0];
                result.warnings = new String[0];
            }
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<Position> getCurrentPosition() {
        return CompletableFuture.completedFuture(currentPosition);
    }
    
    @Override
    public CompletableFuture<Response> updateConfiguration(Map<String, ConfigItem> config) {
        return CompletableFuture.supplyAsync(() -> {
            Response response = new Response();
            response.timestamp = System.currentTimeMillis();
            
            try {
                if (config != null) {
                    configuration.putAll(config);
                    response.success = true;
                    response.message = "Configuration updated successfully";
                    response.errorCode = 0;
                    
                    // Trigger configuration updated event
                    if (configurationUpdatedListener != null) {
                        ConfigurationUpdatedEvent event = new ConfigurationUpdatedEvent();
                        event.updatedConfig = new HashMap<>(configuration);
                        event.timestamp = new TimeInfo();
                        event.timestamp.timestamp = System.currentTimeMillis();
                        event.timestamp.timezone = "UTC";
                        event.timestamp.isoFormat = new java.util.Date().toString();
                        configurationUpdatedListener.accept(event);
                    }
                } else {
                    response.success = false;
                    response.message = "Configuration cannot be null";
                    response.errorCode = 1;
                }
            } catch (Exception e) {
                response.success = false;
                response.message = "Error updating configuration: " + e.getMessage();
                response.errorCode = 99;
            }
            
            return response;
        });
    }
    
    @Override
    public CompletableFuture<StatusLevel> getSystemStatus() {
        return CompletableFuture.completedFuture(currentStatus);
    }
    
    @Override
    public void setStatusChangedListener(Consumer<StatusChangedEvent> listener) {
        this.statusChangedListener = listener;
    }
    
    @Override
    public void setConfigurationUpdatedListener(Consumer<ConfigurationUpdatedEvent> listener) {
        this.configurationUpdatedListener = listener;
    }
    
    // Utility methods for testing
    public void simulateStatusChange(StatusLevel newStatus, String reason) {
        StatusLevel oldStatus = currentStatus;
        currentStatus = newStatus;
        
        if (statusChangedListener != null && oldStatus != newStatus) {
            StatusChangedEvent event = new StatusChangedEvent();
            event.newStatus = newStatus;
            event.reason = reason;
            event.timestamp = new TimeInfo();
            event.timestamp.timestamp = System.currentTimeMillis();
            event.timestamp.timezone = "UTC";
            event.timestamp.isoFormat = new java.util.Date().toString();
            statusChangedListener.accept(event);
        }
    }
    
    public void updatePosition(double latitude, double longitude, double altitude) {
        currentPosition.latitude = latitude;
        currentPosition.longitude = longitude;
        currentPosition.altitude = altitude;
    }
}
