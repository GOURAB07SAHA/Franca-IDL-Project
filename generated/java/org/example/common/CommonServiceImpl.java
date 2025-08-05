package org.example.common;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.time.Instant;

public class CommonServiceImpl implements CommonService {

    private String statusReason;
    private Instant lastStatusChange;
    private Map<String, ConfigItem> configuration = new HashMap<>();
    private Consumer<StatusChangedEvent> statusChangedListener;
    private Consumer<ConfigurationUpdatedEvent> configUpdatedListener;

    public CommonServiceImpl() {
        this.statusReason = "System initialized";
        this.lastStatusChange = Instant.now();
    }

    @Override
    public CompletableFuture<Version> getVersion() {
        return CompletableFuture.supplyAsync(() -> {
            return new Version(1, 0, 0, "Initial release");
        });
    }

    @Override
    public CompletableFuture<ValidationResult> validateData(String data, String[] rules) {
        return CompletableFuture.supplyAsync(() -> {
            boolean valid = true;
            List<String> errors = new ArrayList<>();
            List<String> warnings = new ArrayList<>();

            if (data == null || data.isEmpty()) {
                valid = false;
                errors.add("Data is empty");
            }

            if (rules != null) {
                for (String rule : rules) {
                    if (rule.equals("no-numbers") && data.matches(".*\\d.*")) {
                        valid = false;
                        errors.add("No numbers allowed");
                    }
                }
            }

            return new ValidationResult(valid, errors.toArray(new String[0]), warnings.toArray(new String[0]));
        });
    }

    @Override
    public CompletableFuture<Position> getCurrentPosition() {
        return CompletableFuture.supplyAsync(() -> {
            return new Position(52.5163, 13.3777, 34.0);
        });
    }

    @Override
    public CompletableFuture<TimeInfo> getCurrentTime() {
        return CompletableFuture.supplyAsync(() -> new TimeInfo());
    }

    @Override
    public CompletableFuture<Response> updateConfiguration(Map<String, ConfigItem> config) {
        return CompletableFuture.supplyAsync(() -> {
            configuration.putAll(config);
            if (configUpdatedListener != null) {
                configUpdatedListener.accept(new ConfigurationUpdatedEvent(config));
            }
            return new Response(true, "Configuration updated successfully", 0);
        });
    }

    @Override
    public CompletableFuture<Position> processPositions(Position[] positions) {
        return CompletableFuture.supplyAsync(() -> {
            if (positions == null || positions.length == 0) {
                return new Position(0, 0, 0);
            }
            double avgLat = Arrays.stream(positions).mapToDouble(p -> p.latitude).average().orElse(0);
            double avgLon = Arrays.stream(positions).mapToDouble(p -> p.longitude).average().orElse(0);
            double avgAlt = Arrays.stream(positions).mapToDouble(p -> p.altitude).average().orElse(0);
            return new Position(avgLat, avgLon, avgAlt);
        });
    }

    @Override
    public CompletableFuture<StatusLevel> getSystemStatus() {
        return CompletableFuture.supplyAsync(() -> StatusLevel.OK);
    }

    @Override
    public CompletableFuture<Map<String, String>> getStatusDetails() {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> details = new HashMap<>();
            details.put("system", "operational");
            details.put("lastCheck", lastStatusChange.toString());
            details.put("reason", statusReason);
            return details;
        });
    }

    @Override
    public void setStatusChangedListener(Consumer<StatusChangedEvent> listener) {
        this.statusChangedListener = listener;
    }

    @Override
    public void setConfigurationUpdatedListener(Consumer<ConfigurationUpdatedEvent> listener) {
        this.configUpdatedListener = listener;
    }

    @Override
    public StatusLevel getCurrentStatus() {
        return StatusLevel.OK;
    }

    @Override
    public long getUptime() {
        return Instant.now().getEpochSecond() - lastStatusChange.getEpochSecond();
    }

    @Override
    public boolean getDebugMode() {
        return true;
    }

    @Override
    public void setDebugMode(boolean debugMode) {
        // implementation can vary
    }
}
