package org.example.calculator;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Calculator implementation
 */
public class CalculatorImpl implements Calculator {
    
    private CalculatorStats stats = new CalculatorStats(0, 0, 0, 0.0);
    private byte precision = 2;
    private Consumer<CalculationCompletedEvent> calculationCompletedListener;
    private Consumer<ErrorOccurredEvent> errorOccurredListener;
    
    @Override
    public CompletableFuture<CalculationResult> calculate(double leftOperand, double rightOperand, Operation operation) {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.nanoTime();
            CalculationResult result = new CalculationResult();
            result.timestamp = System.currentTimeMillis();
            
            stats.totalOperations++;
            
            try {
                switch (operation) {
                    case ADD:
                        result.result = leftOperand + rightOperand;
                        break;
                    case SUBTRACT:
                        result.result = leftOperand - rightOperand;
                        break;
                    case MULTIPLY:
                        result.result = leftOperand * rightOperand;
                        break;
                    case DIVIDE:
                        if (rightOperand == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        result.result = leftOperand / rightOperand;
                        break;
                    case POWER:
                        result.result = Math.pow(leftOperand, rightOperand);
                        break;
                    case SQRT:
                        if (leftOperand < 0) {
                            throw new ArithmeticException("Cannot take square root of negative number");
                        }
                        result.result = Math.sqrt(leftOperand);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operation");
                }
                
                result.isValid = true;
                stats.successfulOperations++;
                
                if (calculationCompletedListener != null) {
                    calculationCompletedListener.accept(new CalculationCompletedEvent(result, 12345L));
                }
                
            } catch (Exception e) {
                result.isValid = false;
                result.errorMessage = e.getMessage();
                stats.errorCount++;
                
                if (errorOccurredListener != null) {
                    errorOccurredListener.accept(new ErrorOccurredEvent(result.errorMessage, 1, result.timestamp));
                }
            }
            
            long endTime = System.nanoTime();
            double executionTime = (endTime - startTime) / 1000.0; // microseconds
            
            stats.averageExecutionTime = 
                (stats.averageExecutionTime * (stats.totalOperations - 1) + executionTime) / stats.totalOperations;
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<ComplexNumber> calculateComplex(ComplexNumber left, ComplexNumber right, Operation operation) {
        return CompletableFuture.supplyAsync(() -> {
            ComplexNumber result = new ComplexNumber();
            switch (operation) {
                case ADD:
                    result.real = left.real + right.real;
                    result.imaginary = left.imaginary + right.imaginary;
                    break;
                case SUBTRACT:
                    result.real = left.real - right.real;
                    result.imaginary = left.imaginary - right.imaginary;
                    break;
                default:
                    throw new IllegalArgumentException("Complex operation not implemented");
            }
            return result;
        });
    }
    
    @Override
    public CompletableFuture<CalculatorStats> getStatistics() {
        return CompletableFuture.completedFuture(stats);
    }
    
    @Override
    public CompletableFuture<Boolean> reset() {
        return CompletableFuture.supplyAsync(() -> {
            stats = new CalculatorStats(0, 0, 0, 0.0);
            return true;
        });
    }
    
    @Override
    public CompletableFuture<Boolean> setPrecision(byte decimalPlaces) {
        return CompletableFuture.supplyAsync(() -> {
            if (decimalPlaces >= 0 && decimalPlaces <= 15) {
                precision = decimalPlaces;
                return true;
            }
            return false;
        });
    }
    
    @Override
    public void setCalculationCompletedListener(Consumer<CalculationCompletedEvent> listener) {
        this.calculationCompletedListener = listener;
    }
    
    @Override
    public void setErrorOccurredListener(Consumer<ErrorOccurredEvent> listener) {
        this.errorOccurredListener = listener;
    }
}
