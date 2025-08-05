package org.example.common;

import org.example.common.*;

public interface CommonService {

    public static class CommonTypesImpl implements CommonService {
        public Version getVersion() {
            return new Version(1, 2, 3, "Build 2025-08-02");
        }

        public void validateData(String data, String[] rules) {
            // Implementation detail
        }

        public Position getCurrentPosition() {
            return new Position(40.7128, -74.0060, 15.0);
        }

        public void updateConfiguration(ConfigMap config) {
            // Implementation detail
        }

        public Position processPositions(PositionArray positions) {
            // Implementation detail
            return new Position();
        }

        public StatusLevel getSystemStatus() {
            return StatusLevel.OK;
        }
    }

    class Version {
        private final int major;
        private final int minor;
        private final int patch;
        private final String buildInfo;

        public Version(int major, int minor, int patch, String buildInfo) {
            this.major = major;
            this.minor = minor;
            this.patch = patch;
            this.buildInfo = buildInfo;
        }

        public int getMajor() { return major; }
        public int getMinor() { return minor; }
        public int getPatch() { return patch; }
        public String getBuildInfo() { return buildInfo; }
    }

    class Position {
        private final double latitude;
        private final double longitude;
        private final double altitude;

        public Position(double latitude, double longitude, double altitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.altitude = altitude;
        }

        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
        public double getAltitude() { return altitude; }
    }

    // Other types similar to those above
}
