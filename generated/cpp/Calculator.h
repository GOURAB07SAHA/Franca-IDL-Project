#ifndef CALCULATOR_H
#define CALCULATOR_H

#include <string>
#include <vector>
#include <functional>

namespace org { namespace example { namespace calculator {

enum class Operation {
    ADD = 1,
    SUBTRACT = 2,
    MULTIPLY = 3,
    DIVIDE = 4,
    POWER = 5,
    SQRT = 6
};

struct CalculationResult {
    double result;
    std::string errorMessage;
    bool isValid;
    uint64_t timestamp;
};

struct ComplexNumber {
    double real;
    double imaginary;
};

struct CalculatorStats {
    uint32_t totalOperations;
    uint32_t successfulOperations;
    uint32_t errorCount;
    double averageExecutionTime;
};

class Calculator {
public:
    virtual ~Calculator() = default;
    virtual CalculationResult calculate(double leftOperand, double rightOperand, Operation operation) = 0;
    virtual ComplexNumber calculateComplex(const ComplexNumber& left, const ComplexNumber& right, Operation operation) = 0;
    virtual CalculatorStats getStatistics() = 0;
    virtual bool reset() = 0;
    virtual bool setPrecision(uint8_t decimalPlaces) = 0;
    
    // Event callbacks
    std::function<void(const CalculationResult&, uint64_t)> onCalculationCompleted;
    std::function<void(const std::string&, uint32_t, uint64_t)> onErrorOccurred;
};

}}} // namespace org::example::calculator

#endif // CALCULATOR_H
