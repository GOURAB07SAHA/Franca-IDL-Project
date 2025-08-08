const Calculator = require('./calculator-server');

/**
 * Calculator client implementation
 */
class CalculatorClient {
    constructor() {
        this.calculator = new Calculator();
        this.setupEventListeners();
    }
    
    setupEventListeners() {
        this.calculator.on('calculationCompleted', (result, sessionId) => {
            console.log(`Calculation completed: ${result.result} (Session: ${sessionId})`);
        });
        
        this.calculator.on('errorOccurred', (errorMessage, errorCode, timestamp) => {
            console.log(`Error occurred: ${errorMessage} (Code: ${errorCode})`);
        });
    }
    
    async runDemo() {
        console.log('Calculator Client Demo');
        console.log('=====================');
        
        try {
            const result1 = await this.calculator.calculate(10.0, 5.0, 'ADD');
            if (result1.isValid) {
                console.log(`10 + 5 = ${result1.result}`);
            }
            
            const result2 = await this.calculator.calculate(10.0, 3.0, 'DIVIDE');
            if (result2.isValid) {
                console.log(`10 / 3 = ${result2.result}`);
            }
            
            const result3 = await this.calculator.calculate(10.0, 0.0, 'DIVIDE');
            if (!result3.isValid) {
                console.log(`Error: ${result3.errorMessage}`);
            }
            
            const stats = await this.calculator.getStatistics();
            console.log('\nStatistics:');
            console.log(`Total operations: ${stats.totalOperations}`);
            console.log(`Successful operations: ${stats.successfulOperations}`);
            console.log(`Errors: ${stats.errorCount}`);
            console.log(`Average execution time: ${stats.averageExecutionTime} μs`);
            
        } catch (error) {
            console.error('Error in demo:', error.message);
        }
    }
}

if (require.main === module) {
    const client = new CalculatorClient();
    client.runDemo().catch(console.error);
}

module.exports = CalculatorClient;
