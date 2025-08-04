#include "Calculator.h"
#include <iostream>
#include <memory>

using namespace org::example::calculator;

int main() {
    auto calculator = std::make_unique<CalculatorImpl>();
    
    // Set up event handlers
    calculator->onCalculationCompleted = [](const CalculationResult& result, uint64_t sessionId) {
        std::cout << "Calculation completed: " << result.result << " (Session: " << sessionId << ")" << std::endl;
    };
    
    calculator->onErrorOccurred = [](const std::string& error, uint32_t errorCode, uint64_t timestamp) {
        std::cout << "Error occurred: " << error << " (Code: " << errorCode << ")" << std::endl;
    };
    
    // Perform some calculations
    std::cout << "Calculator Client Demo" << std::endl;
    std::cout << "=====================" << std::endl;
    
    auto result1 = calculator->calculate(10.0, 5.0, Operation::ADD);
    if (result1.isValid) {
        std::cout << "10 + 5 = " << result1.result << std::endl;
    }
    
    auto result2 = calculator->calculate(10.0, 3.0, Operation::DIVIDE);
    if (result2.isValid) {
        std::cout << "10 / 3 = " << result2.result << std::endl;
    }
    
    // Test error case
    auto result3 = calculator->calculate(10.0, 0.0, Operation::DIVIDE);
    if (!result3.isValid) {
        std::cout << "Error: " << result3.errorMessage << std::endl;
    }
    
    // Get statistics
    auto stats = calculator->getStatistics();
    std::cout << "\nStatistics:" << std::endl;
    std::cout << "Total operations: " << stats.totalOperations << std::endl;
    std::cout << "Successful operations: " << stats.successfulOperations << std::endl;
    std::cout << "Errors: " << stats.errorCount << std::endl;
    std::cout << "Average execution time: " << stats.averageExecutionTime << " μs" << std::endl;
    
    return 0;
}
