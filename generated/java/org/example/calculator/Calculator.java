package org.example.calculator;

import java.util.function.Consumer;
import java.util.concurrent.CompletableFuture;

/**
 * Calculator interface generated from FRANCA IDL
 */
public interface Calculator {
    
    enum Operation {
        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4), POWER(5), SQRT(6);
        
        private final int value;
        Operation(int value) { this.value = value; }
        public int getValue() { return value; }
    }
    
    class CalculationResult {
        public double result;
        public String errorMessage;
        public boolean isValid;
        public long timestamp;
        
        public CalculationResult() {}
        
        public CalculationResult(double result, String errorMessage, boolean isValid, long timestamp) {
            this.result = result;
            this.errorMessage = errorMessage;
            this.isValid = isValid;
            this.timestamp = timestamp;
        }
    }
    
    class ComplexNumber {
        public double real;
        public double imaginary;
        
        public ComplexNumber() {}
        
        public ComplexNumber(double real, double imaginary) {
            this.real = real;
            this.imaginary = imaginary;
        }
    }
    
    class CalculatorStats {
        public int totalOperations;
        public int successfulOperations;
        public int errorCount;
        public double averageExecutionTime;
        
        public CalculatorStats() {}
        
        public CalculatorStats(int total, int successful, int errors, double avgTime) {
            this.totalOperations = total;
            this.successfulOperations = successful;
            this.errorCount = errors;
            this.averageExecutionTime = avgTime;
        }
    }
    
    // Methods
    CompletableFuture<CalculationResult> calculate(double leftOperand, double rightOperand, Operation operation);
    CompletableFuture<ComplexNumber> calculateComplex(ComplexNumber left, ComplexNumber right, Operation operation);
    CompletableFuture<CalculatorStats> getStatistics();
    CompletableFuture<Boolean> reset();
    CompletableFuture<Boolean> setPrecision(byte decimalPlaces);
    
    // Event listeners
    void setCalculationCompletedListener(Consumer<CalculationCompletedEvent> listener);
    void setErrorOccurredListener(Consumer<ErrorOccurredEvent> listener);
    
    // Event classes
    class CalculationCompletedEvent {
        public CalculationResult result;
        public long sessionId;
        
        public CalculationCompletedEvent(CalculationResult result, long sessionId) {
            this.result = result;
            this.sessionId = sessionId;
        }
    }
    
    class ErrorOccurredEvent {
        public String errorMessage;
        public int errorCode;
        public long timestamp;
        
        public ErrorOccurredEvent(String errorMessage, int errorCode, long timestamp) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
            this.timestamp = timestamp;
        }
    }
}
