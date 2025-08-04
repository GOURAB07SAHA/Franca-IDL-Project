#include "Calculator.h"
#include <cmath>
#include <chrono>
#include <stdexcept>

namespace org { namespace example { namespace calculator {

class CalculatorImpl : public Calculator {
private:
    CalculatorStats stats_;
    uint8_t precision_;
    
public:
    CalculatorImpl() : precision_(2) {
        stats_ = {0, 0, 0, 0.0};
    }
    
    CalculationResult calculate(double leftOperand, double rightOperand, Operation operation) override {
        auto start = std::chrono::high_resolution_clock::now();
        CalculationResult result;
        result.timestamp = std::chrono::duration_cast<std::chrono::milliseconds>(
            std::chrono::system_clock::now().time_since_epoch()).count();
        
        stats_.totalOperations++;
        
        try {
            switch (operation) {
                case Operation::ADD:
                    result.result = leftOperand + rightOperand;
                    break;
                case Operation::SUBTRACT:
                    result.result = leftOperand - rightOperand;
                    break;
                case Operation::MULTIPLY:
                    result.result = leftOperand * rightOperand;
                    break;
                case Operation::DIVIDE:
                    if (rightOperand == 0) {
                        throw std::runtime_error("Division by zero");
                    }
                    result.result = leftOperand / rightOperand;
                    break;
                case Operation::POWER:
                    result.result = std::pow(leftOperand, rightOperand);
                    break;
                case Operation::SQRT:
                    if (leftOperand < 0) {
                        throw std::runtime_error("Cannot take square root of negative number");
                    }
                    result.result = std::sqrt(leftOperand);
                    break;
                default:
                    throw std::runtime_error("Invalid operation");
            }
            
            result.isValid = true;
            stats_.successfulOperations++;
            
        } catch (const std::exception& e) {
            result.isValid = false;
            result.errorMessage = e.what();
            stats_.errorCount++;
            
            if (onErrorOccurred) {
                onErrorOccurred(result.errorMessage, 1, result.timestamp);
            }
        }
        
        auto end = std::chrono::high_resolution_clock::now();
        auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start);
        
        // Update average execution time
        stats_.averageExecutionTime = 
            (stats_.averageExecutionTime * (stats_.totalOperations - 1) + duration.count()) / stats_.totalOperations;
        
        if (result.isValid && onCalculationCompleted) {
            onCalculationCompleted(result, 12345); // sessionId
        }
        
        return result;
    }
    
    ComplexNumber calculateComplex(const ComplexNumber& left, const ComplexNumber& right, Operation operation) override {
        ComplexNumber result;
        // Simplified complex number operations
        switch (operation) {
            case Operation::ADD:
                result.real = left.real + right.real;
                result.imaginary = left.imaginary + right.imaginary;
                break;
            case Operation::SUBTRACT:
                result.real = left.real - right.real;
                result.imaginary = left.imaginary - right.imaginary;
                break;
            // Add more complex operations as needed
            default:
                throw std::runtime_error("Complex operation not implemented");
        }
        return result;
    }
    
    CalculatorStats getStatistics() override {
        return stats_;
    }
    
    bool reset() override {
        stats_ = {0, 0, 0, 0.0};
        return true;
    }
    
    bool setPrecision(uint8_t decimalPlaces) override {
        if (decimalPlaces <= 15) {
            precision_ = decimalPlaces;
            return true;
        }
        return false;
    }
};

}}} // namespace org::example::calculator
