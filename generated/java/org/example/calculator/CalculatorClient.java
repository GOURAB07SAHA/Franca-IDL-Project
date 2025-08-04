package org.example.calculator;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Calculator client demo
 */
public class CalculatorClient {
    
    public static void main(String[] args) {
        Calculator calculator = new CalculatorImpl();
        
        // Set up event listeners
        calculator.setCalculationCompletedListener(event -> {
            System.out.println("Calculation completed: " + event.result.result + " (Session: " + event.sessionId + ")");
        });
        
        calculator.setErrorOccurredListener(event -> {
            System.out.println("Error occurred: " + event.errorMessage + " (Code: " + event.errorCode + ")");
        });
        
        System.out.println("Calculator Client Demo");
        System.out.println("=====================");
        
        try {
            // Perform some calculations
            CompletableFuture<Calculator.CalculationResult> result1 = 
                calculator.calculate(10.0, 5.0, Calculator.Operation.ADD);
            Calculator.CalculationResult r1 = result1.get();
            if (r1.isValid) {
                System.out.println("10 + 5 = " + r1.result);
            }
            
            CompletableFuture<Calculator.CalculationResult> result2 = 
                calculator.calculate(10.0, 3.0, Calculator.Operation.DIVIDE);
            Calculator.CalculationResult r2 = result2.get();
            if (r2.isValid) {
                System.out.println("10 / 3 = " + r2.result);
            }
            
            // Test error case
            CompletableFuture<Calculator.CalculationResult> result3 = 
                calculator.calculate(10.0, 0.0, Calculator.Operation.DIVIDE);
            Calculator.CalculationResult r3 = result3.get();
            if (!r3.isValid) {
                System.out.println("Error: " + r3.errorMessage);
            }
            
            // Get statistics
            CompletableFuture<Calculator.CalculatorStats> statsResult = calculator.getStatistics();
            Calculator.CalculatorStats stats = statsResult.get();
            System.out.println("\nStatistics:");
            System.out.println("Total operations: " + stats.totalOperations);
            System.out.println("Successful operations: " + stats.successfulOperations);
            System.out.println("Errors: " + stats.errorCount);
            System.out.println("Average execution time: " + stats.averageExecutionTime + " μs");
            
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
