package org.example.common;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.Map;

/**
 * CommonService interface generated from FRANCA IDL
 */
public interface CommonService {
    
    enum CommonError {
        NO_ERROR(0), INVALID_INPUT(1), TIMEOUT(2), COMMUNICATION_ERROR(3),
        AUTHENTICATION_FAILED(4), PERMISSION_DENIED(5), RESOURCE_NOT_AVAILABLE(6), INTERNAL_ERROR(99);
        
        private final int value;
        CommonError(int value) { this.value = value; }
        public int getValue() { return value; }
    }
    
    enum StatusLevel {
        OK(0), WARNING(1), ERROR(2), CRITICAL(3);
        
        private final int value;
        StatusLevel(int value) { this.value = value; }
        public int getValue() { return value; }
    }
    
    class Response {
        public boolean success;
        public String message;
        public int errorCode;
        public long timestamp;
    }
    
    class Position {
        public double latitude;
        public double longitude;
        public double altitude;
    }
    
    class TimeInfo {
        public long timestamp;
        public String timezone;
        public String isoFormat;
    }
    
    class Version {
        public int major;
        public int minor;
        public int patch;
        public String buildInfo;
    }
    
    class ConfigItem {
        public String key;
        public String value;
        public String description;
    }
    
    class ValidationResult {
        public boolean isValid;
        public String[] errors;
        public String[] warnings;
    }
    
    // Methods
    CompletableFuture<Version> getVersion();
    CompletableFuture<ValidationResult> validateData(String data, String[] rules);
    CompletableFuture<Position> getCurrentPosition();
    CompletableFuture<Response> updateConfiguration(Map<String, ConfigItem> config);
    CompletableFuture<StatusLevel> getSystemStatus();
    
    // Event listeners
    void setStatusChangedListener(Consumer<StatusChangedEvent> listener);
    void setConfigurationUpdatedListener(Consumer<ConfigurationUpdatedEvent> listener);
    
    // Event classes
    class StatusChangedEvent {
        public StatusLevel newStatus;
        public String reason;
        public TimeInfo timestamp;
    }
    
    class ConfigurationUpdatedEvent {
        public Map<String, ConfigItem> updatedConfig;
        public TimeInfo timestamp;
    }
}
