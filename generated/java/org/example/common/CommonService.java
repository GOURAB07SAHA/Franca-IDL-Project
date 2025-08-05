package org.example.common;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.*;
import java.time.Instant;

/**
 * CommonService interface generated from FRANCA IDL
 */
public interface CommonService {
    
    // Enumerations
    enum CommonError {
        NO_ERROR(0), INVALID_INPUT(1), TIMEOUT(2), COMMUNICATION_ERROR(3),
        AUTHENTICATION_FAILED(4), PERMISSION_DENIED(5), RESOURCE_NOT_AVAILABLE(6),
        INTERNAL_ERROR(99);
        
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
    
    // Data structures
    class Version {
        public int major;
        public int minor;
        public int patch;
        public String buildInfo;
        
        public Version() {}
        
        public Version(int major, int minor, int patch, String buildInfo) {
            this.major = major;
            this.minor = minor;
            this.patch = patch;
            this.buildInfo = buildInfo;
        }
        
        @Override
        public String toString() {
            return String.format("%d.%d.%d (%s)", major, minor, patch, buildInfo);
        }
    }
    
    class Position {
        public double latitude;
        public double longitude;
        public double altitude;
        
        public Position() {}
        
        public Position(double latitude, double longitude, double altitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.altitude = altitude;
        }
        
        @Override
        public String toString() {
            return String.format("Position(%.6f, %.6f, %.2fm)", latitude, longitude, altitude);
        }
    }
    
    class TimeInfo {
        public long timestamp;
        public String timezone;
        public String isoFormat;
        
        public TimeInfo() {
            this.timestamp = System.currentTimeMillis();
            this.timezone = "UTC";
            this.isoFormat = Instant.ofEpochMilli(timestamp).toString();
        }
        
        public TimeInfo(long timestamp, String timezone, String isoFormat) {
            this.timestamp = timestamp;
            this.timezone = timezone;
            this.isoFormat = isoFormat;
        }
    }
    
    class Response {
        public boolean success;
        public String message;
        public int errorCode;
        public long timestamp;
        
        public Response() {}
        
        public Response(boolean success, String message, int errorCode) {
            this.success = success;
            this.message = message;
            this.errorCode = errorCode;
            this.timestamp = System.currentTimeMillis();
        }
    }
    
    class ConfigItem {
        public String key;
        public String value;
        public String description;
        
        public ConfigItem() {}
        
        public ConfigItem(String key, String value, String description) {
            this.key = key;
            this.value = value;
            this.description = description;
        }
    }
    
    class ValidationResult {
        public boolean isValid;
        public String[] errors;
        public String[] warnings;
        
        public ValidationResult() {
            this.errors = new String[0];
            this.warnings = new String[0];
        }
        
        public ValidationResult(boolean isValid, String[] errors, String[] warnings) {
            this.isValid = isValid;
            this.errors = errors != null ? errors : new String[0];
            this.warnings = warnings != null ? warnings : new String[0];
        }
    }
    
    // Event classes
    class StatusChangedEvent {
        public StatusLevel newStatus;
        public String reason;
        public TimeInfo timestamp;
        
        public StatusChangedEvent(StatusLevel newStatus, String reason) {
            this.newStatus = newStatus;
            this.reason = reason;
            this.timestamp = new TimeInfo();
        }
    }
    
    class ConfigurationUpdatedEvent {
        public Map<String, ConfigItem> updatedConfig;
        public TimeInfo timestamp;
        
        public ConfigurationUpdatedEvent(Map<String, ConfigItem> updatedConfig) {
            this.updatedConfig = updatedConfig;
            this.timestamp = new TimeInfo();
        }
    }
    
    // Methods
    CompletableFuture<Version> getVersion();
    CompletableFuture<ValidationResult> validateData(String data, String[] rules);
    CompletableFuture<Position> getCurrentPosition();
    CompletableFuture<TimeInfo> getCurrentTime();
    CompletableFuture<Response> updateConfiguration(Map<String, ConfigItem> config);
    CompletableFuture<Position> processPositions(Position[] positions);
    CompletableFuture<StatusLevel> getSystemStatus();
    CompletableFuture<Map<String, String>> getStatusDetails();
    
    // Event listeners
    void setStatusChangedListener(Consumer<StatusChangedEvent> listener);
    void setConfigurationUpdatedListener(Consumer<ConfigurationUpdatedEvent> listener);
    
    // Attributes
    StatusLevel getCurrentStatus();
    long getUptime();
    boolean getDebugMode();
    void setDebugMode(boolean debugMode);
}
