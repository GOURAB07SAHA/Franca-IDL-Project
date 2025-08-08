#ifndef CALCULATOR_H
#define CALCULATOR_H

#include <string>
#include <vector>
#include <functional>

namespace org { namespace example { namespace calculator {

enum class Operation {
    ADD = 1, SUBTRACT = 2, MULTIPLY = 3, DIVIDE = 4, POWER = 5, SQRT = 6
};

struct CalculationResult {
    double result;
    std::string errorMessage;
    bool isValid;
    uint64_t timestamp;
};

class Calculator {
public:
    virtual ~Calculator() = default;
    virtual CalculationResult calculate(double leftOperand, double rightOperand, Operation operation) = 0;
};

}}} // namespace

#endif // CALCULATOR_H
