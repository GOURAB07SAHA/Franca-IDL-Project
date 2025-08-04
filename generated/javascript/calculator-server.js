const EventEmitter = require('events');

/**
 * Calculator server implementation
 */
class Calculator extends EventEmitter {
    constructor() {
        super();
        this.stats = {
            totalOperations: 0,
            successfulOperations: 0,
            errorCount: 0,
            averageExecutionTime: 0.0
        };
        this.precision = 2;
    }
    
    async calculate(leftOperand, rightOperand, operation) {
        const startTime = process.hrtime.bigint();
        const result = {
            result: 0,
            errorMessage: '',
            isValid: false,
            timestamp: Date.now()
        };
        
        this.stats.totalOperations++;
        
        try {
            switch (operation) {
                case 'ADD':
                    result.result = leftOperand + rightOperand;
                    break;
                case 'SUBTRACT':
                    result.result = leftOperand - rightOperand;
                    break;
                case 'MULTIPLY':
                    result.result = leftOperand * rightOperand;
                    break;
                case 'DIVIDE':
                    if (rightOperand === 0) {
                        throw new Error('Division by zero');
                    }
                    result.result = leftOperand / rightOperand;
                    break;
                case 'POWER':
                    result.result = Math.pow(leftOperand, rightOperand);
                    break;
                case 'SQRT':
                    if (leftOperand < 0) {
                        throw new Error('Cannot take square root of negative number');
                    }
                    result.result = Math.sqrt(leftOperand);
                    break;
                default:
                    throw new Error('Invalid operation');
            }
            
            result.isValid = true;
            this.stats.successfulOperations++;
            
            this.emit('calculationCompleted', result, 12345); // sessionId
            
        } catch (error) {
            result.isValid = false;
            result.errorMessage = error.message;
            this.stats.errorCount++;
            
            this.emit('errorOccurred', result.errorMessage, 1, result.timestamp);
        }
        
        const endTime = process.hrtime.bigint();
        const executionTime = Number(endTime - startTime) / 1000; // microseconds
        
        // Update average execution time
        this.stats.averageExecutionTime = 
            (this.stats.averageExecutionTime * (this.stats.totalOperations - 1) + executionTime) / this.stats.totalOperations;
        
        return result;
    }
    
    async calculateComplex(left, right, operation) {
        const result = { real: 0, imaginary: 0 };
        
        switch (operation) {
            case 'ADD':
                result.real = left.real + right.real;
                result.imaginary = left.imaginary + right.imaginary;
                break;
            case 'SUBTRACT':
                result.real = left.real - right.real;
                result.imaginary = left.imaginary - right.imaginary;
                break;
            default:
                throw new Error('Complex operation not implemented');
        }
        
        return result;
    }
    
    async getStatistics() {
        return { ...this.stats };
    }
    
    async reset() {
        this.stats = {
            totalOperations: 0,
            successfulOperations: 0,
            errorCount: 0,
            averageExecutionTime: 0.0
        };
        return true;
    }
    
    async setPrecision(decimalPlaces) {
        if (decimalPlaces >= 0 && decimalPlaces <= 15) {
            this.precision = decimalPlaces;
            return true;
        }
        return false;
    }
}

module.exports = Calculator;
