#ifndef VEHICLE_DASHBOARD_H
#define VEHICLE_DASHBOARD_H

#include <string>
#include <vector>
#include <functional>
#include <memory>
#include <future>

namespace org {
namespace example {
namespace automotive {

enum class TransmissionState {
    PARK = 0,
    REVERSE = 1,
    NEUTRAL = 2,
    DRIVE = 3,
    SPORT = 4,
    MANUAL = 5
};

enum class EngineState {
    OFF = 0,
    STARTING = 1,
    IDLE = 2,
    RUNNING = 3,
    OVERHEATED = 4,
    ERROR = 5
};

enum class WarningLight {
    ENGINE_CHECK = 1,
    OIL_PRESSURE = 2,
    BATTERY = 3,
    TEMPERATURE = 4,
    BRAKE = 5,
    ABS = 6,
    AIRBAG = 7,
    SEAT_BELT = 8,
    FUEL_LOW = 9,
    TIRE_PRESSURE = 10
};

enum class StatusLevel {
    OK = 0,
    WARNING = 1,
    ERROR = 2,
    CRITICAL = 3
};

struct VehicleData {
    float speed;              // km/h
    float engineRPM;          // revolutions per minute
    float fuelLevel;          // percentage 0-100
    float engineTemperature;  // Celsius
    float oilPressure;        // bar
    uint32_t odometer;        // total kilometers
    uint32_t tripMeter;       // trip kilometers
    TransmissionState transmission;
    EngineState engineState;
    
    VehicleData() : speed(0), engineRPM(0), fuelLevel(0), engineTemperature(0), 
                    oilPressure(0), odometer(0), tripMeter(0), 
                    transmission(TransmissionState::PARK), engineState(EngineState::OFF) {}
};

struct WarningStatus {
    WarningLight type;
    bool isActive;
    std::string message;
    StatusLevel severity;
    uint64_t activatedTime;
    
    WarningStatus() : type(WarningLight::ENGINE_CHECK), isActive(false), 
                      severity(StatusLevel::OK), activatedTime(0) {}
};

struct FuelConsumption {
    float instantConsumption;  // L/100km
    float averageConsumption;  // L/100km
    float rangeEstimate;       // km remaining
    uint32_t fuelUsedTrip;     // mL
    
    FuelConsumption() : instantConsumption(0), averageConsumption(0), 
                        rangeEstimate(0), fuelUsedTrip(0) {}
};

// Event structures
struct VehicleDataChangedEvent {
    VehicleData newData;
};

struct WarningStatusChangedEvent {
    WarningStatus warning;
};

struct FuelLevelCriticalEvent {
    float remainingFuel;
    float estimatedRange;
};

class VehicleDashboard {
public:
    virtual ~VehicleDashboard() = default;
    
    // Methods
    virtual std::future<VehicleData> getVehicleData() = 0;
    virtual std::future<std::vector<WarningStatus>> getActiveWarnings() = 0;
    virtual std::future<FuelConsumption> getFuelConsumption() = 0;
    virtual std::future<bool> resetTripMeter() = 0;
    virtual std::future<bool> setDisplayUnits(bool useMetric) = 0;
    
    // Event listeners
    virtual void setVehicleDataChangedListener(std::function<void(const VehicleDataChangedEvent&)> listener) = 0;
    virtual void setWarningStatusChangedListener(std::function<void(const WarningStatusChangedEvent&)> listener) = 0;
    virtual void setFuelLevelCriticalListener(std::function<void(const FuelLevelCriticalEvent&)> listener) = 0;
    
    // Attributes
    virtual float getCurrentSpeed() const = 0;
    virtual float getCurrentRPM() const = 0;
    virtual uint8_t getDisplayBrightness() const = 0;
    virtual void setDisplayBrightness(uint8_t brightness) = 0;
    virtual bool getUseMetricUnits() const = 0;
    virtual void setUseMetricUnits(bool useMetric) = 0;
};

} // namespace automotive
} // namespace example
} // namespace org

#endif // VEHICLE_DASHBOARD_H
