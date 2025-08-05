/**
 * Vehicle Dashboard JavaScript Implementation
 * Generated from FRANCA IDL VehicleDashboard.fidl
 */

// Enumerations
const TransmissionState = {
    PARK: 0,
    REVERSE: 1,
    NEUTRAL: 2,
    DRIVE: 3,
    SPORT: 4,
    MANUAL: 5
};

const EngineState = {
    OFF: 0,
    STARTING: 1,
    IDLE: 2,
    RUNNING: 3,
    OVERHEATED: 4,
    ERROR: 5
};

const WarningLight = {
    ENGINE_CHECK: 1,
    OIL_PRESSURE: 2,
    BATTERY: 3,
    TEMPERATURE: 4,
    BRAKE: 5,
    ABS: 6,
    AIRBAG: 7,
    SEAT_BELT: 8,
    FUEL_LOW: 9,
    TIRE_PRESSURE: 10
};

const StatusLevel = {
    OK: 0,
    WARNING: 1,
    ERROR: 2,
    CRITICAL: 3
};

class VehicleData {
    constructor() {
        this.speed = 0;              // km/h
        this.engineRPM = 0;          // revolutions per minute
        this.fuelLevel = 0;          // percentage 0-100
        this.engineTemperature = 0;  // Celsius
        this.oilPressure = 0;        // bar
        this.odometer = 0;           // total kilometers
        this.tripMeter = 0;          // trip kilometers
        this.transmission = TransmissionState.PARK;
        this.engineState = EngineState.OFF;
    }
}

class WarningStatus {
    constructor() {
        this.type = WarningLight.ENGINE_CHECK;
        this.isActive = false;
        this.message = '';
        this.severity = StatusLevel.OK;
        this.activatedTime = 0;
    }
}

class FuelConsumption {
    constructor() {
        this.instantConsumption = 0;  // L/100km
        this.averageConsumption = 0;  // L/100km
        this.rangeEstimate = 0;       // km remaining
        this.fuelUsedTrip = 0;        // mL
    }
}

// Event classes
class VehicleDataChangedEvent {
    constructor(newData) {
        this.newData = newData;
    }
}

class WarningStatusChangedEvent {
    constructor(warning) {
        this.warning = warning;
    }
}

class FuelLevelCriticalEvent {
    constructor(remainingFuel, estimatedRange) {
        this.remainingFuel = remainingFuel;
        this.estimatedRange = estimatedRange;
    }
}

class VehicleDashboardImpl {
    constructor() {
        this.currentData = new VehicleData();
        this.activeWarnings = [];
        this.fuelConsumption = new FuelConsumption();
        this.useMetricUnits = true;
        this.displayBrightness = 75;
        this.currentSpeed = 0.0;
        this.currentRPM = 800.0;
        
        // Event listeners
        this.vehicleDataListener = null;
        this.warningStatusListener = null;
        this.fuelCriticalListener = null;
        
        this.initializeVehicleData();
        this.initializeWarnings();
        this.initializeFuelConsumption();
        this.startRealTimeSimulation();
    }
    
    initializeVehicleData() {
        this.currentData.speed = 65.5;
        this.currentData.engineRPM = 2450.0;
        this.currentData.fuelLevel = 78.5;
        this.currentData.engineTemperature = 92.3;
        this.currentData.oilPressure = 2.8;
        this.currentData.odometer = 156789;
        this.currentData.tripMeter = 423;
        this.currentData.transmission = TransmissionState.DRIVE;
        this.currentData.engineState = EngineState.RUNNING;
    }
    
    initializeWarnings() {
        const warning = new WarningStatus();
        warning.type = WarningLight.TIRE_PRESSURE;
        warning.isActive = true;
        warning.message = 'Front left tire pressure low';
        warning.severity = StatusLevel.WARNING;
        warning.activatedTime = Date.now();
        this.activeWarnings.push(warning);
    }
    
    initializeFuelConsumption() {
        this.fuelConsumption.instantConsumption = 7.8;
        this.fuelConsumption.averageConsumption = 8.2;
        this.fuelConsumption.rangeEstimate = 450.0;
        this.fuelConsumption.fuelUsedTrip = 3200;
    }
    
    startRealTimeSimulation() {
        setInterval(() => {
            this.getVehicleData().then(() => {
                this.getFuelConsumption();
            });
        }, 1000);
    }
    
    // Interface methods
    async getVehicleData() {
        return new Promise((resolve) => {
            // Simulate real-time data updates
            this.currentData.speed += (Math.random() - 0.5) * 10;
            this.currentData.engineRPM += (Math.random() - 0.5) * 200;
            this.currentData.fuelLevel -= 0.01;
            
            // Ensure realistic bounds
            this.currentData.speed = Math.max(0, Math.min(200, this.currentData.speed));
            this.currentData.engineRPM = Math.max(800, Math.min(6000, this.currentData.engineRPM));
            this.currentData.fuelLevel = Math.max(0, this.currentData.fuelLevel);
            
            // Update current attributes
            this.currentSpeed = this.currentData.speed;
            this.currentRPM = this.currentData.engineRPM;
            
            // Trigger events
            if (this.vehicleDataListener) {
                this.vehicleDataListener(new VehicleDataChangedEvent(this.currentData));
            }
            
            // Check for fuel critical level
            if (this.currentData.fuelLevel < 10.0 && this.fuelCriticalListener) {
                this.fuelCriticalListener(new FuelLevelCriticalEvent(
                    this.currentData.fuelLevel, 
                    this.fuelConsumption.rangeEstimate
                ));
            }
            
            resolve(this.currentData);
        });
    }
    
    async getActiveWarnings() {
        return Promise.resolve([...this.activeWarnings]);
    }
    
    async getFuelConsumption() {
        return new Promise((resolve) => {
            // Update fuel consumption based on current driving
            this.fuelConsumption.instantConsumption = 6.5 + (this.currentData.speed / 100.0) * 3.0;
            this.fuelConsumption.rangeEstimate = (this.currentData.fuelLevel / 100.0) * 50.0 * 
                                               (100.0 / this.fuelConsumption.averageConsumption);
            resolve(this.fuelConsumption);
        });
    }
    
    async resetTripMeter() {
        return new Promise((resolve) => {
            this.currentData.tripMeter = 0;
            this.fuelConsumption.fuelUsedTrip = 0;
            resolve(true);
        });
    }
    
    async setDisplayUnits(useMetric) {
        return new Promise((resolve) => {
            this.useMetricUnits = useMetric;
            resolve(true);
        });
    }
    
    // Event listener setters
    setVehicleDataChangedListener(listener) {
        this.vehicleDataListener = listener;
    }
    
    setWarningStatusChangedListener(listener) {
        this.warningStatusListener = listener;
    }
    
    setFuelLevelCriticalListener(listener) {
        this.fuelCriticalListener = listener;
    }
    
    // Attribute getters/setters
    getCurrentSpeed() {
        return this.currentSpeed;
    }
    
    getCurrentRPM() {
        return this.currentRPM;
    }
    
    getDisplayBrightness() {
        return this.displayBrightness;
    }
    
    setDisplayBrightness(brightness) {
        this.displayBrightness = Math.max(0, Math.min(100, brightness));
    }
    
    getUseMetricUnits() {
        return this.useMetricUnits;
    }
    
    setUseMetricUnits(useMetric) {
        this.useMetricUnits = useMetric;
    }
    
    // Simulation method
    simulateWarning(type, message) {
        const warning = new WarningStatus();
        warning.type = type;
        warning.isActive = true;
        warning.message = message;
        warning.severity = StatusLevel.ERROR;
        warning.activatedTime = Date.now();
        
        this.activeWarnings.push(warning);
        
        if (this.warningStatusListener) {
            this.warningStatusListener(new WarningStatusChangedEvent(warning));
        }
    }
}

// Export for Node.js
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        VehicleDashboardImpl,
        VehicleData,
        WarningStatus,
        FuelConsumption,
        TransmissionState,
        EngineState,
        WarningLight,
        StatusLevel,
        VehicleDataChangedEvent,
        WarningStatusChangedEvent,
        FuelLevelCriticalEvent
    };
}

// Demo application
if (typeof window === 'undefined') {
    // Node.js environment
    console.log('Vehicle Dashboard Demo');
    console.log('========================');
    
    const dashboard = new VehicleDashboardImpl();
    
    // Set up event listeners
    dashboard.setVehicleDataChangedListener((event) => {
        console.log('Vehicle data changed:');
        console.log(`Speed: ${event.newData.speed.toFixed(1)} km/h`);
        console.log(`RPM: ${event.newData.engineRPM.toFixed(1)}`);
        console.log(`Fuel Level: ${event.newData.fuelLevel.toFixed(1)}%`);
    });
    
    dashboard.setWarningStatusChangedListener((event) => {
        console.log('Warning status changed:');
        console.log(`Type: ${Object.keys(WarningLight).find(key => WarningLight[key] === event.warning.type)}`);
        console.log(`Message: ${event.warning.message}`);
    });
    
    dashboard.setFuelLevelCriticalListener((event) => {
        console.log(`Fuel level critical: ${event.remainingFuel.toFixed(1)}%`);
        console.log(`Estimated range: ${event.estimatedRange.toFixed(1)} km`);
    });
    
    // Initial data display
    setTimeout(async () => {
        const data = await dashboard.getVehicleData();
        console.log(`Current Speed: ${data.speed.toFixed(1)} km/h`);
        console.log(`Current RPM: ${data.engineRPM.toFixed(1)}`);
        console.log(`Fuel Level: ${data.fuelLevel.toFixed(1)}%`);
        
        const warnings = await dashboard.getActiveWarnings();
        warnings.forEach(warning => {
            console.log(`Warning: ${warning.message}`);
        });
        
        // Simulate additional warning
        dashboard.simulateWarning(WarningLight.ENGINE_CHECK, 'Engine check required.');
    }, 1000);
}
