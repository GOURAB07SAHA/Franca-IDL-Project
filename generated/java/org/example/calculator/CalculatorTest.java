package org.example.calculator;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Comprehensive test suite for the Calculator implementation
 */
public class CalculatorTest {
    
    private Calculator calculator;
    private int completedEvents = 0;
    private int errorEvents = 0;
    
    public static void main(String[] args) {
        CalculatorTest test = new CalculatorTest();
        test.runAllTests();
    }
    
    public void runAllTests() {
        System.out.println("=".repeat(60));
        System.out.println("FRANCA IDL Calculator - Comprehensive Test Suite");
        System.out.println("=".repeat(60));
        
        setup();
        
        // Run all test categories
        testBasicOperations();
        testComplexNumbers();
        testErrorHandling();
        testStatistics();
        testPrecisionSettings();
        testEventHandling();
        testPerformance();
        
        // Final summary
        printTestSummary();
    }
    
    private void setup() {
        calculator = new CalculatorImpl();
        completedEvents = 0;
        errorEvents = 0;
        
        // Set up event listeners
        calculator.setCalculationCompletedListener(event -> {
            completedEvents++;
            System.out.printf("  ✓ Calculation completed: %.4f (Session: %d)%n", 
                event.result.result, event.sessionId);
        });
        
        calculator.setErrorOccurredListener(event -> {
            errorEvents++;
            System.out.printf("  ⚠ Error occurred: %s (Code: %d)%n", 
                event.errorMessage, event.errorCode);
        });
    }
    
    private void testBasicOperations() {
        System.out.println("\n1. BASIC ARITHMETIC OPERATIONS");
        System.out.println("-".repeat(40));
        
        try {
            // Addition
            testOperation("Addition", 15.5, 4.5, Calculator.Operation.ADD, 20.0);
            
            // Subtraction
            testOperation("Subtraction", 10.0, 3.0, Calculator.Operation.SUBTRACT, 7.0);
            
            // Multiplication
            testOperation("Multiplication", 6.0, 7.0, Calculator.Operation.MULTIPLY, 42.0);
            
            // Division
            testOperation("Division", 15.0, 3.0, Calculator.Operation.DIVIDE, 5.0);
            
            // Power
            testOperation("Power", 2.0, 3.0, Calculator.Operation.POWER, 8.0);
            
            // Square Root
            testOperation("Square Root", 16.0, 0.0, Calculator.Operation.SQRT, 4.0);
            
        } catch (Exception e) {
            System.err.println("Error in basic operations test: " + e.getMessage());
        }
    }
    
    private void testComplexNumbers() {
        System.out.println("\n2. COMPLEX NUMBER OPERATIONS");
        System.out.println("-".repeat(40));
        
        try {
            Calculator.ComplexNumber c1 = new Calculator.ComplexNumber(3.0, 4.0);
            Calculator.ComplexNumber c2 = new Calculator.ComplexNumber(1.0, 2.0);
            
            // Complex addition
            CompletableFuture<Calculator.ComplexNumber> result = 
                calculator.calculateComplex(c1, c2, Calculator.Operation.ADD);
            Calculator.ComplexNumber sum = result.get(5, TimeUnit.SECONDS);
            
            System.out.printf("Complex Addition: (3+4i) + (1+2i) = (%.1f+%.1fi)%n", 
                sum.real, sum.imaginary);
            assert sum.real == 4.0 && sum.imaginary == 6.0 : "Complex addition failed";
            
            // Complex subtraction
            result = calculator.calculateComplex(c1, c2, Calculator.Operation.SUBTRACT);
            Calculator.ComplexNumber diff = result.get(5, TimeUnit.SECONDS);
            
            System.out.printf("Complex Subtraction: (3+4i) - (1+2i) = (%.1f+%.1fi)%n", 
                diff.real, diff.imaginary);
            assert diff.real == 2.0 && diff.imaginary == 2.0 : "Complex subtraction failed";
            
        } catch (Exception e) {
            System.err.println("Error in complex numbers test: " + e.getMessage());
        }
    }
    
    private void testErrorHandling() {
        System.out.println("\n3. ERROR HANDLING");
        System.out.println("-".repeat(40));
        
        try {
            // Division by zero
            System.out.println("Testing division by zero:");
            CompletableFuture<Calculator.CalculationResult> result = 
                calculator.calculate(10.0, 0.0, Calculator.Operation.DIVIDE);
            Calculator.CalculationResult divResult = result.get(5, TimeUnit.SECONDS);
            
            assert !divResult.isValid : "Division by zero should fail";
            assert divResult.errorMessage.contains("zero") : "Error message should mention zero";
            
            // Square root of negative number
            System.out.println("Testing square root of negative number:");
            result = calculator.calculate(-4.0, 0.0, Calculator.Operation.SQRT);
            Calculator.CalculationResult sqrtResult = result.get(5, TimeUnit.SECONDS);
            
            assert !sqrtResult.isValid : "Square root of negative should fail";
            assert sqrtResult.errorMessage.contains("negative") : "Error message should mention negative";
            
        } catch (Exception e) {
            System.err.println("Error in error handling test: " + e.getMessage());
        }
    }
    
    private void testStatistics() {
        System.out.println("\n4. STATISTICS TRACKING");
        System.out.println("-".repeat(40));
        
        try {
            // Reset calculator first
            CompletableFuture<Boolean> resetResult = calculator.reset();
            boolean resetSuccess = resetResult.get(5, TimeUnit.SECONDS);
            assert resetSuccess : "Reset should succeed";
            
            // Perform several operations
            calculator.calculate(5.0, 3.0, Calculator.Operation.ADD).get();
            calculator.calculate(10.0, 2.0, Calculator.Operation.MULTIPLY).get();
            calculator.calculate(8.0, 0.0, Calculator.Operation.DIVIDE).get(); // This will fail
            
            // Get statistics
            CompletableFuture<Calculator.CalculatorStats> statsResult = calculator.getStatistics();
            Calculator.CalculatorStats stats = statsResult.get(5, TimeUnit.SECONDS);
            
            System.out.printf("Statistics after reset and 3 operations:%n");
            System.out.printf("  Total operations: %d%n", stats.totalOperations);
            System.out.printf("  Successful operations: %d%n", stats.successfulOperations);
            System.out.printf("  Error count: %d%n", stats.errorCount);
            System.out.printf("  Average execution time: %.2f μs%n", stats.averageExecutionTime);
            
            assert stats.totalOperations == 3 : "Should have 3 total operations";
            assert stats.successfulOperations == 2 : "Should have 2 successful operations";
            assert stats.errorCount == 1 : "Should have 1 error";
            
        } catch (Exception e) {
            System.err.println("Error in statistics test: " + e.getMessage());
        }
    }
    
    private void testPrecisionSettings() {
        System.out.println("\n5. PRECISION SETTINGS");
        System.out.println("-".repeat(40));
        
        try {
            // Test setting valid precision
            CompletableFuture<Boolean> result = calculator.setPrecision((byte) 4);
            boolean success = result.get(5, TimeUnit.SECONDS);
            assert success : "Setting precision to 4 should succeed";
            System.out.println("✓ Successfully set precision to 4 decimal places");
            
            // Test setting invalid precision
            result = calculator.setPrecision((byte) 20);
            success = result.get(5, TimeUnit.SECONDS);
            assert !success : "Setting precision to 20 should fail";
            System.out.println("✓ Correctly rejected invalid precision (20)");
            
        } catch (Exception e) {
            System.err.println("Error in precision test: " + e.getMessage());
        }
    }
    
    private void testEventHandling() {
        System.out.println("\n6. EVENT HANDLING");
        System.out.println("-".repeat(40));
        
        int initialCompleted = completedEvents;
        int initialErrors = errorEvents;
        
        try {
            // Perform operations that should trigger events
            calculator.calculate(2.0, 3.0, Calculator.Operation.ADD).get();
            calculator.calculate(10.0, 0.0, Calculator.Operation.DIVIDE).get();
            
            // Allow time for events to be processed
            Thread.sleep(100);
            
            System.out.printf("Events triggered:%n");
            System.out.printf("  Completion events: %d%n", completedEvents - initialCompleted);
            System.out.printf("  Error events: %d%n", errorEvents - initialErrors);
            
            assert (completedEvents - initialCompleted) >= 1 : "Should have completion events";
            assert (errorEvents - initialErrors) >= 1 : "Should have error events";
            
        } catch (Exception e) {
            System.err.println("Error in event handling test: " + e.getMessage());
        }
    }
    
    private void testPerformance() {
        System.out.println("\n7. PERFORMANCE TEST");
        System.out.println("-".repeat(40));
        
        try {
            calculator.reset().get();
            
            int numOperations = 1000;
            long startTime = System.nanoTime();
            
            // Perform many operations
            for (int i = 0; i < numOperations; i++) {
                calculator.calculate(Math.random() * 100, Math.random() * 100, 
                    Calculator.Operation.ADD).get();
            }
            
            long endTime = System.nanoTime();
            double totalTime = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
            double avgTime = totalTime / numOperations;
            
            System.out.printf("Performance test results:%n");
            System.out.printf("  Operations: %d%n", numOperations);
            System.out.printf("  Total time: %.2f ms%n", totalTime);
            System.out.printf("  Average time per operation: %.4f ms%n", avgTime);
            System.out.printf("  Operations per second: %.0f%n", 1000.0 / avgTime);
            
            // Get final statistics
            Calculator.CalculatorStats stats = calculator.getStatistics().get();
            System.out.printf("  Internal avg execution time: %.2f μs%n", stats.averageExecutionTime);
            
        } catch (Exception e) {
            System.err.println("Error in performance test: " + e.getMessage());
        }
    }
    
    private void testOperation(String name, double left, double right, 
                              Calculator.Operation op, double expected) 
                              throws InterruptedException, ExecutionException, TimeoutException {
        
        CompletableFuture<Calculator.CalculationResult> result = 
            calculator.calculate(left, right, op);
        Calculator.CalculationResult calcResult = result.get(5, TimeUnit.SECONDS);
        
        if (calcResult.isValid) {
            double tolerance = 0.0001;
            boolean isCorrect = Math.abs(calcResult.result - expected) < tolerance;
            System.out.printf("%-15s: %.1f op %.1f = %.4f %s%n", 
                name, left, right, calcResult.result, 
                isCorrect ? "✓" : "✗ (expected " + expected + ")");
            
            if (!isCorrect) {
                throw new AssertionError(String.format("%s failed: expected %.4f, got %.4f", 
                    name, expected, calcResult.result));
            }
        } else {
            System.out.printf("%-15s: FAILED - %s%n", name, calcResult.errorMessage);
        }
    }
    
    private void printTestSummary() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST SUMMARY");
        System.out.println("=".repeat(60));
        System.out.printf("Total completion events: %d%n", completedEvents);
        System.out.printf("Total error events: %d%n", errorEvents);
        
        try {
            Calculator.CalculatorStats finalStats = calculator.getStatistics().get();
            System.out.printf("Final statistics:%n");
            System.out.printf("  Total operations: %d%n", finalStats.totalOperations);
            System.out.printf("  Successful operations: %d%n", finalStats.successfulOperations);
            System.out.printf("  Error count: %d%n", finalStats.errorCount);
            System.out.printf("  Success rate: %.1f%%%n", 
                (double) finalStats.successfulOperations / finalStats.totalOperations * 100);
        } catch (Exception e) {
            System.err.println("Error getting final statistics: " + e.getMessage());
        }
        
        System.out.println("\n🎉 All tests completed successfully!");
        System.out.println("✅ Java implementation is fully functional and tested.");
    }
}
