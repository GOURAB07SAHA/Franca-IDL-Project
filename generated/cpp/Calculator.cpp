#include "Calculator.h"
#include <cmath>
#include <stdexcept>

namespace org { namespace example { namespace calculator {

class CalculatorImpl : public Calculator {
public:
    CalculationResult calculate(double leftOperand, double rightOperand, Operation operation) override {
        CalculationResult result;
        result.timestamp = std::chrono::duration_cast<std::chrono::milliseconds>(
            std::chrono::system_clock::now().time_since_epoch()).count();
        
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
                default:
                    throw std::runtime_error("Invalid operation");
            }
            result.isValid = true;
        } catch (const std::exception& e) {
            result.isValid = false;
            result.errorMessage = e.what();
        }
        
        return result;
    }
};

}}} // namespace
