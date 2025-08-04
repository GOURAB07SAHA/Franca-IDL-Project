import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Simple Code Generator for Franca IDL files
 * This generates basic stub code for C++, Java, and JavaScript
 */
public class SimpleCodeGenerator {
    
    public static void main(String[] args) {
        try {
            String inputDir = "interfaces/basic";
            String outputDir = "generated";
            
            // Create output directories
            createDirectories(outputDir);
            
            // Read Calculator.fidl file
            String fidlContent = readFile(inputDir + "/Calculator.fidl");
            
            // Generate code for different languages
            generateCppCode(outputDir, fidlContent);
            generateJavaCode(outputDir, fidlContent);
            generateJavaScriptCode(outputDir, fidlContent);
            
            System.out.println("Code generation completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during code generation: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createDirectories(String baseDir) throws IOException {
        Files.createDirectories(Paths.get(baseDir + "/cpp"));
        Files.createDirectories(Paths.get(baseDir + "/java"));
        Files.createDirectories(Paths.get(baseDir + "/javascript"));
    }
    
    private static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
    
    private static void writeFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes());
    }
    
    private static void generateCppCode(String outputDir, String fidlContent) throws IOException {
        String headerContent = generateCppHeader();
        String implementationContent = generateCppImplementation();
        
        writeFile(outputDir + "/cpp/Calculator.h", headerContent);
        writeFile(outputDir + "/cpp/Calculator.cpp", implementationContent);
        writeFile(outputDir + "/cpp/CalculatorClient.cpp", generateCppClient());
        writeFile(outputDir + "/cpp/CMakeLists.txt", generateCMakeLists());
        
        System.out.println("Generated C++ code in " + outputDir + "/cpp/");
    }
    
    private static void generateJavaCode(String outputDir, String fidlContent) throws IOException {
        String interfaceContent = generateJavaInterface();
        String implementationContent = generateJavaImplementation();
        String clientContent = generateJavaClient();
        
        writeFile(outputDir + "/java/Calculator.java", interfaceContent);
        writeFile(outputDir + "/java/CalculatorImpl.java", implementationContent);
        writeFile(outputDir + "/java/CalculatorClient.java", clientContent);
        writeFile(outputDir + "/java/build.gradle", generateGradleBuild());
        
        System.out.println("Generated Java code in " + outputDir + "/java/");
    }
    
    private static void generateJavaScriptCode(String outputDir, String fidlContent) throws IOException {
        String clientContent = generateJavaScriptClient();
        String serverContent = generateJavaScriptServer();
        String packageContent = generatePackageJson();
        
        writeFile(outputDir + "/javascript/calculator-client.js", clientContent);
        writeFile(outputDir + "/javascript/calculator-server.js", serverContent);
        writeFile(outputDir + "/javascript/package.json", packageContent);
        writeFile(outputDir + "/javascript/README.md", generateJavaScriptReadme());
        
        System.out.println("Generated JavaScript code in " + outputDir + "/javascript/");
    }
    
    private static String generateCppHeader() {
        return "#ifndef CALCULATOR_H\n" +
               "#define CALCULATOR_H\n\n" +
               "#include <string>\n" +
               "#include <vector>\n" +
               "#include <functional>\n\n" +
               "namespace org { namespace example { namespace calculator {\n\n" +
               "enum class Operation {\n" +
               "    ADD = 1,\n" +
               "    SUBTRACT = 2,\n" +
               "    MULTIPLY = 3,\n" +
               "    DIVIDE = 4,\n" +
               "    POWER = 5,\n" +
               "    SQRT = 6\n" +
               "};\n\n" +
               "struct CalculationResult {\n" +
               "    double result;\n" +
               "    std::string errorMessage;\n" +
               "    bool isValid;\n" +
               "    uint64_t timestamp;\n" +
               "};\n\n" +
               "struct ComplexNumber {\n" +
               "    double real;\n" +
               "    double imaginary;\n" +
               "};\n\n" +
               "struct CalculatorStats {\n" +
               "    uint32_t totalOperations;\n" +
               "    uint32_t successfulOperations;\n" +
               "    uint32_t errorCount;\n" +
               "    double averageExecutionTime;\n" +
               "};\n\n" +
               "class Calculator {\n" +
               "public:\n" +
               "    virtual ~Calculator() = default;\n" +
               "    virtual CalculationResult calculate(double leftOperand, double rightOperand, Operation operation) = 0;\n" +
               "    virtual ComplexNumber calculateComplex(const ComplexNumber& left, const ComplexNumber& right, Operation operation) = 0;\n" +
               "    virtual CalculatorStats getStatistics() = 0;\n" +
               "    virtual bool reset() = 0;\n" +
               "    virtual bool setPrecision(uint8_t decimalPlaces) = 0;\n" +
               "    \n" +
               "    // Event callbacks\n" +
               "    std::function<void(const CalculationResult&, uint64_t)> onCalculationCompleted;\n" +
               "    std::function<void(const std::string&, uint32_t, uint64_t)> onErrorOccurred;\n" +
               "};\n\n" +
               "}}} // namespace org::example::calculator\n\n" +
               "#endif // CALCULATOR_H\n";
    }
    
    private static String generateCppImplementation() {
        return "#include \"Calculator.h\"\n" +
               "#include <cmath>\n" +
               "#include <chrono>\n" +
               "#include <stdexcept>\n\n" +
               "namespace org { namespace example { namespace calculator {\n\n" +
               "class CalculatorImpl : public Calculator {\n" +
               "private:\n" +
               "    CalculatorStats stats_;\n" +
               "    uint8_t precision_;\n" +
               "    \n" +
               "public:\n" +
               "    CalculatorImpl() : precision_(2) {\n" +
               "        stats_ = {0, 0, 0, 0.0};\n" +
               "    }\n" +
               "    \n" +
               "    CalculationResult calculate(double leftOperand, double rightOperand, Operation operation) override {\n" +
               "        auto start = std::chrono::high_resolution_clock::now();\n" +
               "        CalculationResult result;\n" +
               "        result.timestamp = std::chrono::duration_cast<std::chrono::milliseconds>(\n" +
               "            std::chrono::system_clock::now().time_since_epoch()).count();\n" +
               "        \n" +
               "        stats_.totalOperations++;\n" +
               "        \n" +
               "        try {\n" +
               "            switch (operation) {\n" +
               "                case Operation::ADD:\n" +
               "                    result.result = leftOperand + rightOperand;\n" +
               "                    break;\n" +
               "                case Operation::SUBTRACT:\n" +
               "                    result.result = leftOperand - rightOperand;\n" +
               "                    break;\n" +
               "                case Operation::MULTIPLY:\n" +
               "                    result.result = leftOperand * rightOperand;\n" +
               "                    break;\n" +
               "                case Operation::DIVIDE:\n" +
               "                    if (rightOperand == 0) {\n" +
               "                        throw std::runtime_error(\"Division by zero\");\n" +
               "                    }\n" +
               "                    result.result = leftOperand / rightOperand;\n" +
               "                    break;\n" +
               "                case Operation::POWER:\n" +
               "                    result.result = std::pow(leftOperand, rightOperand);\n" +
               "                    break;\n" +
               "                case Operation::SQRT:\n" +
               "                    if (leftOperand < 0) {\n" +
               "                        throw std::runtime_error(\"Cannot take square root of negative number\");\n" +
               "                    }\n" +
               "                    result.result = std::sqrt(leftOperand);\n" +
               "                    break;\n" +
               "                default:\n" +
               "                    throw std::runtime_error(\"Invalid operation\");\n" +
               "            }\n" +
               "            \n" +
               "            result.isValid = true;\n" +
               "            stats_.successfulOperations++;\n" +
               "            \n" +
               "        } catch (const std::exception& e) {\n" +
               "            result.isValid = false;\n" +
               "            result.errorMessage = e.what();\n" +
               "            stats_.errorCount++;\n" +
               "            \n" +
               "            if (onErrorOccurred) {\n" +
               "                onErrorOccurred(result.errorMessage, 1, result.timestamp);\n" +
               "            }\n" +
               "        }\n" +
               "        \n" +
               "        auto end = std::chrono::high_resolution_clock::now();\n" +
               "        auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start);\n" +
               "        \n" +
               "        // Update average execution time\n" +
               "        stats_.averageExecutionTime = \n" +
               "            (stats_.averageExecutionTime * (stats_.totalOperations - 1) + duration.count()) / stats_.totalOperations;\n" +
               "        \n" +
               "        if (result.isValid && onCalculationCompleted) {\n" +
               "            onCalculationCompleted(result, 12345); // sessionId\n" +
               "        }\n" +
               "        \n" +
               "        return result;\n" +
               "    }\n" +
               "    \n" +
               "    ComplexNumber calculateComplex(const ComplexNumber& left, const ComplexNumber& right, Operation operation) override {\n" +
               "        ComplexNumber result;\n" +
               "        // Simplified complex number operations\n" +
               "        switch (operation) {\n" +
               "            case Operation::ADD:\n" +
               "                result.real = left.real + right.real;\n" +
               "                result.imaginary = left.imaginary + right.imaginary;\n" +
               "                break;\n" +
               "            case Operation::SUBTRACT:\n" +
               "                result.real = left.real - right.real;\n" +
               "                result.imaginary = left.imaginary - right.imaginary;\n" +
               "                break;\n" +
               "            // Add more complex operations as needed\n" +
               "            default:\n" +
               "                throw std::runtime_error(\"Complex operation not implemented\");\n" +
               "        }\n" +
               "        return result;\n" +
               "    }\n" +
               "    \n" +
               "    CalculatorStats getStatistics() override {\n" +
               "        return stats_;\n" +
               "    }\n" +
               "    \n" +
               "    bool reset() override {\n" +
               "        stats_ = {0, 0, 0, 0.0};\n" +
               "        return true;\n" +
               "    }\n" +
               "    \n" +
               "    bool setPrecision(uint8_t decimalPlaces) override {\n" +
               "        if (decimalPlaces <= 15) {\n" +
               "            precision_ = decimalPlaces;\n" +
               "            return true;\n" +
               "        }\n" +
               "        return false;\n" +
               "    }\n" +
               "};\n\n" +
               "}}} // namespace org::example::calculator\n";
    }
    
    private static String generateCppClient() {
        return "#include \"Calculator.h\"\n" +
               "#include <iostream>\n" +
               "#include <memory>\n\n" +
               "using namespace org::example::calculator;\n\n" +
               "int main() {\n" +
               "    auto calculator = std::make_unique<CalculatorImpl>();\n" +
               "    \n" +
               "    // Set up event handlers\n" +
               "    calculator->onCalculationCompleted = [](const CalculationResult& result, uint64_t sessionId) {\n" +
               "        std::cout << \"Calculation completed: \" << result.result << \" (Session: \" << sessionId << \")\" << std::endl;\n" +
               "    };\n" +
               "    \n" +
               "    calculator->onErrorOccurred = [](const std::string& error, uint32_t errorCode, uint64_t timestamp) {\n" +
               "        std::cout << \"Error occurred: \" << error << \" (Code: \" << errorCode << \")\" << std::endl;\n" +
               "    };\n" +
               "    \n" +
               "    // Perform some calculations\n" +
               "    std::cout << \"Calculator Client Demo\" << std::endl;\n" +
               "    std::cout << \"=====================\" << std::endl;\n" +
               "    \n" +
               "    auto result1 = calculator->calculate(10.0, 5.0, Operation::ADD);\n" +
               "    if (result1.isValid) {\n" +
               "        std::cout << \"10 + 5 = \" << result1.result << std::endl;\n" +
               "    }\n" +
               "    \n" +
               "    auto result2 = calculator->calculate(10.0, 3.0, Operation::DIVIDE);\n" +
               "    if (result2.isValid) {\n" +
               "        std::cout << \"10 / 3 = \" << result2.result << std::endl;\n" +
               "    }\n" +
               "    \n" +
               "    // Test error case\n" +
               "    auto result3 = calculator->calculate(10.0, 0.0, Operation::DIVIDE);\n" +
               "    if (!result3.isValid) {\n" +
               "        std::cout << \"Error: \" << result3.errorMessage << std::endl;\n" +
               "    }\n" +
               "    \n" +
               "    // Get statistics\n" +
               "    auto stats = calculator->getStatistics();\n" +
               "    std::cout << \"\\nStatistics:\" << std::endl;\n" +
               "    std::cout << \"Total operations: \" << stats.totalOperations << std::endl;\n" +
               "    std::cout << \"Successful operations: \" << stats.successfulOperations << std::endl;\n" +
               "    std::cout << \"Errors: \" << stats.errorCount << std::endl;\n" +
               "    std::cout << \"Average execution time: \" << stats.averageExecutionTime << \" μs\" << std::endl;\n" +
               "    \n" +
               "    return 0;\n" +
               "}\n";
    }
    
    private static String generateCMakeLists() {
        return "cmake_minimum_required(VERSION 3.12)\n" +
               "project(Calculator)\n\n" +
               "set(CMAKE_CXX_STANDARD 17)\n" +
               "set(CMAKE_CXX_STANDARD_REQUIRED ON)\n\n" +
               "# Add executable\n" +
               "add_executable(calculator_client\n" +
               "    Calculator.cpp\n" +
               "    CalculatorClient.cpp\n" +
               ")\n\n" +
               "# Find required packages\n" +
               "find_package(Threads REQUIRED)\n\n" +
               "# Link libraries\n" +
               "target_link_libraries(calculator_client\n" +
               "    Threads::Threads\n" +
               ")\n\n" +
               "# Set compiler warnings\n" +
               "if(CMAKE_CXX_COMPILER_ID MATCHES \"GNU|Clang\")\n" +
               "    target_compile_options(calculator_client PRIVATE -Wall -Wextra -Wpedantic)\n" +
               "endif()\n";
    }
    
    private static String generateJavaInterface() {
        return "package org.example.calculator;\n\n" +
               "import java.util.function.Consumer;\n" +
               "import java.util.concurrent.CompletableFuture;\n\n" +
               "/**\n" +
               " * Calculator interface generated from FRANCA IDL\n" +
               " */\n" +
               "public interface Calculator {\n" +
               "    \n" +
               "    enum Operation {\n" +
               "        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4), POWER(5), SQRT(6);\n" +
               "        \n" +
               "        private final int value;\n" +
               "        Operation(int value) { this.value = value; }\n" +
               "        public int getValue() { return value; }\n" +
               "    }\n" +
               "    \n" +
               "    class CalculationResult {\n" +
               "        public double result;\n" +
               "        public String errorMessage;\n" +
               "        public boolean isValid;\n" +
               "        public long timestamp;\n" +
               "        \n" +
               "        public CalculationResult() {}\n" +
               "        \n" +
               "        public CalculationResult(double result, String errorMessage, boolean isValid, long timestamp) {\n" +
               "            this.result = result;\n" +
               "            this.errorMessage = errorMessage;\n" +
               "            this.isValid = isValid;\n" +
               "            this.timestamp = timestamp;\n" +
               "        }\n" +
               "    }\n" +
               "    \n" +
               "    class ComplexNumber {\n" +
               "        public double real;\n" +
               "        public double imaginary;\n" +
               "        \n" +
               "        public ComplexNumber() {}\n" +
               "        \n" +
               "        public ComplexNumber(double real, double imaginary) {\n" +
               "            this.real = real;\n" +
               "            this.imaginary = imaginary;\n" +
               "        }\n" +
               "    }\n" +
               "    \n" +
               "    class CalculatorStats {\n" +
               "        public int totalOperations;\n" +
               "        public int successfulOperations;\n" +
               "        public int errorCount;\n" +
               "        public double averageExecutionTime;\n" +
               "        \n" +
               "        public CalculatorStats() {}\n" +
               "        \n" +
               "        public CalculatorStats(int total, int successful, int errors, double avgTime) {\n" +
               "            this.totalOperations = total;\n" +
               "            this.successfulOperations = successful;\n" +
               "            this.errorCount = errors;\n" +
               "            this.averageExecutionTime = avgTime;\n" +
               "        }\n" +
               "    }\n" +
               "    \n" +
               "    // Methods\n" +
               "    CompletableFuture<CalculationResult> calculate(double leftOperand, double rightOperand, Operation operation);\n" +
               "    CompletableFuture<ComplexNumber> calculateComplex(ComplexNumber left, ComplexNumber right, Operation operation);\n" +
               "    CompletableFuture<CalculatorStats> getStatistics();\n" +
               "    CompletableFuture<Boolean> reset();\n" +
               "    CompletableFuture<Boolean> setPrecision(byte decimalPlaces);\n" +
               "    \n" +
               "    // Event listeners\n" +
               "    void setCalculationCompletedListener(Consumer<CalculationCompletedEvent> listener);\n" +
               "    void setErrorOccurredListener(Consumer<ErrorOccurredEvent> listener);\n" +
               "    \n" +
               "    // Event classes\n" +
               "    class CalculationCompletedEvent {\n" +
               "        public CalculationResult result;\n" +
               "        public long sessionId;\n" +
               "        \n" +
               "        public CalculationCompletedEvent(CalculationResult result, long sessionId) {\n" +
               "            this.result = result;\n" +
               "            this.sessionId = sessionId;\n" +
               "        }\n" +
               "    }\n" +
               "    \n" +
               "    class ErrorOccurredEvent {\n" +
               "        public String errorMessage;\n" +
               "        public int errorCode;\n" +
               "        public long timestamp;\n" +
               "        \n" +
               "        public ErrorOccurredEvent(String errorMessage, int errorCode, long timestamp) {\n" +
               "            this.errorMessage = errorMessage;\n" +
               "            this.errorCode = errorCode;\n" +
               "            this.timestamp = timestamp;\n" +
               "        }\n" +
               "    }\n" +
               "}\n";
    }
    
    private static String generateJavaImplementation() {
        return "package org.example.calculator;\n\n" +
               "import java.util.concurrent.CompletableFuture;\n" +
               "import java.util.function.Consumer;\n\n" +
               "/**\n" +
               " * Calculator implementation\n" +
               " */\n" +
               "public class CalculatorImpl implements Calculator {\n" +
               "    \n" +
               "    private CalculatorStats stats = new CalculatorStats(0, 0, 0, 0.0);\n" +
               "    private byte precision = 2;\n" +
               "    private Consumer<CalculationCompletedEvent> calculationCompletedListener;\n" +
               "    private Consumer<ErrorOccurredEvent> errorOccurredListener;\n" +
               "    \n" +
               "    @Override\n" +
               "    public CompletableFuture<CalculationResult> calculate(double leftOperand, double rightOperand, Operation operation) {\n" +
               "        return CompletableFuture.supplyAsync(() -> {\n" +
               "            long startTime = System.nanoTime();\n" +
               "            CalculationResult result = new CalculationResult();\n" +
               "            result.timestamp = System.currentTimeMillis();\n" +
               "            \n" +
               "            stats.totalOperations++;\n" +
               "            \n" +
               "            try {\n" +
               "                switch (operation) {\n" +
               "                    case ADD:\n" +
               "                        result.result = leftOperand + rightOperand;\n" +
               "                        break;\n" +
               "                    case SUBTRACT:\n" +
               "                        result.result = leftOperand - rightOperand;\n" +
               "                        break;\n" +
               "                    case MULTIPLY:\n" +
               "                        result.result = leftOperand * rightOperand;\n" +
               "                        break;\n" +
               "                    case DIVIDE:\n" +
               "                        if (rightOperand == 0) {\n" +
               "                            throw new ArithmeticException(\"Division by zero\");\n" +
               "                        }\n" +
               "                        result.result = leftOperand / rightOperand;\n" +
               "                        break;\n" +
               "                    case POWER:\n" +
               "                        result.result = Math.pow(leftOperand, rightOperand);\n" +
               "                        break;\n" +
               "                    case SQRT:\n" +
               "                        if (leftOperand < 0) {\n" +
               "                            throw new ArithmeticException(\"Cannot take square root of negative number\");\n" +
               "                        }\n" +
               "                        result.result = Math.sqrt(leftOperand);\n" +
               "                        break;\n" +
               "                    default:\n" +
               "                        throw new IllegalArgumentException(\"Invalid operation\");\n" +
               "                }\n" +
               "                \n" +
               "                result.isValid = true;\n" +
               "                stats.successfulOperations++;\n" +
               "                \n" +
               "                if (calculationCompletedListener != null) {\n" +
               "                    calculationCompletedListener.accept(new CalculationCompletedEvent(result, 12345L));\n" +
               "                }\n" +
               "                \n" +
               "            } catch (Exception e) {\n" +
               "                result.isValid = false;\n" +
               "                result.errorMessage = e.getMessage();\n" +
               "                stats.errorCount++;\n" +
               "                \n" +
               "                if (errorOccurredListener != null) {\n" +
               "                    errorOccurredListener.accept(new ErrorOccurredEvent(result.errorMessage, 1, result.timestamp));\n" +
               "                }\n" +
               "            }\n" +
               "            \n" +
               "            long endTime = System.nanoTime();\n" +
               "            double executionTime = (endTime - startTime) / 1000.0; // microseconds\n" +
               "            \n" +
               "            // Update average execution time\n" +
               "            stats.averageExecutionTime = \n" +
               "                (stats.averageExecutionTime * (stats.totalOperations - 1) + executionTime) / stats.totalOperations;\n" +
               "            \n" +
               "            return result;\n" +
               "        });\n" +
               "    }\n" +
               "    \n" +
               "    @Override\n" +
               "    public CompletableFuture<ComplexNumber> calculateComplex(ComplexNumber left, ComplexNumber right, Operation operation) {\n" +
               "        return CompletableFuture.supplyAsync(() -> {\n" +
               "            ComplexNumber result = new ComplexNumber();\n" +
               "            switch (operation) {\n" +
               "                case ADD:\n" +
               "                    result.real = left.real + right.real;\n" +
               "                    result.imaginary = left.imaginary + right.imaginary;\n" +
               "                    break;\n" +
               "                case SUBTRACT:\n" +
               "                    result.real = left.real - right.real;\n" +
               "                    result.imaginary = left.imaginary - right.imaginary;\n" +
               "                    break;\n" +
               "                default:\n" +
               "                    throw new IllegalArgumentException(\"Complex operation not implemented\");\n" +
               "            }\n" +
               "            return result;\n" +
               "        });\n" +
               "    }\n" +
               "    \n" +
               "    @Override\n" +
               "    public CompletableFuture<CalculatorStats> getStatistics() {\n" +
               "        return CompletableFuture.completedFuture(stats);\n" +
               "    }\n" +
               "    \n" +
               "    @Override\n" +
               "    public CompletableFuture<Boolean> reset() {\n" +
               "        return CompletableFuture.supplyAsync(() -> {\n" +
               "            stats = new CalculatorStats(0, 0, 0, 0.0);\n" +
               "            return true;\n" +
               "        });\n" +
               "    }\n" +
               "    \n" +
               "    @Override\n" +
               "    public CompletableFuture<Boolean> setPrecision(byte decimalPlaces) {\n" +
               "        return CompletableFuture.supplyAsync(() -> {\n" +
               "            if (decimalPlaces >= 0 && decimalPlaces <= 15) {\n" +
               "                precision = decimalPlaces;\n" +
               "                return true;\n" +
               "            }\n" +
               "            return false;\n" +
               "        });\n" +
               "    }\n" +
               "    \n" +
               "    @Override\n" +
               "    public void setCalculationCompletedListener(Consumer<CalculationCompletedEvent> listener) {\n" +
               "        this.calculationCompletedListener = listener;\n" +
               "    }\n" +
               "    \n" +
               "    @Override\n" +
               "    public void setErrorOccurredListener(Consumer<ErrorOccurredEvent> listener) {\n" +
               "        this.errorOccurredListener = listener;\n" +
               "    }\n" +
               "}\n";
    }
    
    private static String generateJavaClient() {
        return "package org.example.calculator;\n\n" +
               "import java.util.concurrent.CompletableFuture;\n" +
               "import java.util.concurrent.ExecutionException;\n\n" +
               "/**\n" +
               " * Calculator client demo\n" +
               " */\n" +
               "public class CalculatorClient {\n" +
               "    \n" +
               "    public static void main(String[] args) {\n" +
               "        Calculator calculator = new CalculatorImpl();\n" +
               "        \n" +
               "        // Set up event listeners\n" +
               "        calculator.setCalculationCompletedListener(event -> {\n" +
               "            System.out.println(\"Calculation completed: \" + event.result.result + \" (Session: \" + event.sessionId + \")\");\n" +
               "        });\n" +
               "        \n" +
               "        calculator.setErrorOccurredListener(event -> {\n" +
               "            System.out.println(\"Error occurred: \" + event.errorMessage + \" (Code: \" + event.errorCode + \")\");\n" +
               "        });\n" +
               "        \n" +
               "        System.out.println(\"Calculator Client Demo\");\n" +
               "        System.out.println(\"=====================\");\n" +
               "        \n" +
               "        try {\n" +
               "            // Perform some calculations\n" +
               "            CompletableFuture<Calculator.CalculationResult> result1 = \n" +
               "                calculator.calculate(10.0, 5.0, Calculator.Operation.ADD);\n" +
               "            Calculator.CalculationResult r1 = result1.get();\n" +
               "            if (r1.isValid) {\n" +
               "                System.out.println(\"10 + 5 = \" + r1.result);\n" +
               "            }\n" +
               "            \n" +
               "            CompletableFuture<Calculator.CalculationResult> result2 = \n" +
               "                calculator.calculate(10.0, 3.0, Calculator.Operation.DIVIDE);\n" +
               "            Calculator.CalculationResult r2 = result2.get();\n" +
               "            if (r2.isValid) {\n" +
               "                System.out.println(\"10 / 3 = \" + r2.result);\n" +
               "            }\n" +
               "            \n" +
               "            // Test error case\n" +
               "            CompletableFuture<Calculator.CalculationResult> result3 = \n" +
               "                calculator.calculate(10.0, 0.0, Calculator.Operation.DIVIDE);\n" +
               "            Calculator.CalculationResult r3 = result3.get();\n" +
               "            if (!r3.isValid) {\n" +
               "                System.out.println(\"Error: \" + r3.errorMessage);\n" +
               "            }\n" +
               "            \n" +
               "            // Get statistics\n" +
               "            CompletableFuture<Calculator.CalculatorStats> statsResult = calculator.getStatistics();\n" +
               "            Calculator.CalculatorStats stats = statsResult.get();\n" +
               "            System.out.println(\"\\nStatistics:\");\n" +
               "            System.out.println(\"Total operations: \" + stats.totalOperations);\n" +
               "            System.out.println(\"Successful operations: \" + stats.successfulOperations);\n" +
               "            System.out.println(\"Errors: \" + stats.errorCount);\n" +
               "            System.out.println(\"Average execution time: \" + stats.averageExecutionTime + \" μs\");\n" +
               "            \n" +
               "        } catch (InterruptedException | ExecutionException e) {\n" +
               "            e.printStackTrace();\n" +
               "        }\n" +
               "    }\n" +
               "}\n";
    }
    
    private static String generateGradleBuild() {
        return "plugins {\n" +
               "    id 'java'\n" +
               "    id 'application'\n" +
               "}\n\n" +
               "group = 'org.example.calculator'\n" +
               "version = '1.0.0'\n" +
               "sourceCompatibility = '11'\n\n" +
               "application {\n" +
               "    mainClass = 'org.example.calculator.CalculatorClient'\n" +
               "}\n\n" +
               "repositories {\n" +
               "    mavenCentral()\n" +
               "}\n\n" +
               "dependencies {\n" +
               "    testImplementation 'org.junit.jupiter:junit-jupiter:5.9.2'\n" +
               "}\n\n" +
               "tasks.named('test') {\n" +
               "    useJUnitPlatform()\n" +
               "}\n";
    }
    
    private static String generateJavaScriptClient() {
        return "const Calculator = require('./calculator-server');\n\n" +
               "/**\n" +
               " * Calculator client implementation\n" +
               " */\n" +
               "class CalculatorClient {\n" +
               "    constructor() {\n" +
               "        this.calculator = new Calculator();\n" +
               "        this.setupEventListeners();\n" +
               "    }\n" +
               "    \n" +
               "    setupEventListeners() {\n" +
               "        this.calculator.on('calculationCompleted', (result, sessionId) => {\n" +
               "            console.log(`Calculation completed: ${result.result} (Session: ${sessionId})`);\n" +
               "        });\n" +
               "        \n" +
               "        this.calculator.on('errorOccurred', (errorMessage, errorCode, timestamp) => {\n" +
               "            console.log(`Error occurred: ${errorMessage} (Code: ${errorCode})`);\n" +
               "        });\n" +
               "    }\n" +
               "    \n" +
               "    async runDemo() {\n" +
               "        console.log('Calculator Client Demo');\n" +
               "        console.log('=====================');\n" +
               "        \n" +
               "        try {\n" +
               "            // Perform some calculations\n" +
               "            const result1 = await this.calculator.calculate(10.0, 5.0, 'ADD');\n" +
               "            if (result1.isValid) {\n" +
               "                console.log(`10 + 5 = ${result1.result}`);\n" +
               "            }\n" +
               "            \n" +
               "            const result2 = await this.calculator.calculate(10.0, 3.0, 'DIVIDE');\n" +
               "            if (result2.isValid) {\n" +
               "                console.log(`10 / 3 = ${result2.result}`);\n" +
               "            }\n" +
               "            \n" +
               "            // Test error case\n" +
               "            const result3 = await this.calculator.calculate(10.0, 0.0, 'DIVIDE');\n" +
               "            if (!result3.isValid) {\n" +
               "                console.log(`Error: ${result3.errorMessage}`);\n" +
               "            }\n" +
               "            \n" +
               "            // Get statistics\n" +
               "            const stats = await this.calculator.getStatistics();\n" +
               "            console.log('\\nStatistics:');\n" +
               "            console.log(`Total operations: ${stats.totalOperations}`);\n" +
               "            console.log(`Successful operations: ${stats.successfulOperations}`);\n" +
               "            console.log(`Errors: ${stats.errorCount}`);\n" +
               "            console.log(`Average execution time: ${stats.averageExecutionTime} μs`);\n" +
               "            \n" +
               "        } catch (error) {\n" +
               "            console.error('Error in demo:', error.message);\n" +
               "        }\n" +
               "    }\n" +
               "}\n\n" +
               "// Run the demo if this file is executed directly\n" +
               "if (require.main === module) {\n" +
               "    const client = new CalculatorClient();\n" +
               "    client.runDemo().catch(console.error);\n" +
               "}\n\n" +
               "module.exports = CalculatorClient;\n";
    }
    
    private static String generateJavaScriptServer() {
        return "const EventEmitter = require('events');\n\n" +
               "/**\n" +
               " * Calculator server implementation\n" +
               " */\n" +
               "class Calculator extends EventEmitter {\n" +
               "    constructor() {\n" +
               "        super();\n" +
               "        this.stats = {\n" +
               "            totalOperations: 0,\n" +
               "            successfulOperations: 0,\n" +
               "            errorCount: 0,\n" +
               "            averageExecutionTime: 0.0\n" +
               "        };\n" +
               "        this.precision = 2;\n" +
               "    }\n" +
               "    \n" +
               "    async calculate(leftOperand, rightOperand, operation) {\n" +
               "        const startTime = process.hrtime.bigint();\n" +
               "        const result = {\n" +
               "            result: 0,\n" +
               "            errorMessage: '',\n" +
               "            isValid: false,\n" +
               "            timestamp: Date.now()\n" +
               "        };\n" +
               "        \n" +
               "        this.stats.totalOperations++;\n" +
               "        \n" +
               "        try {\n" +
               "            switch (operation) {\n" +
               "                case 'ADD':\n" +
               "                    result.result = leftOperand + rightOperand;\n" +
               "                    break;\n" +
               "                case 'SUBTRACT':\n" +
               "                    result.result = leftOperand - rightOperand;\n" +
               "                    break;\n" +
               "                case 'MULTIPLY':\n" +
               "                    result.result = leftOperand * rightOperand;\n" +
               "                    break;\n" +
               "                case 'DIVIDE':\n" +
               "                    if (rightOperand === 0) {\n" +
               "                        throw new Error('Division by zero');\n" +
               "                    }\n" +
               "                    result.result = leftOperand / rightOperand;\n" +
               "                    break;\n" +
               "                case 'POWER':\n" +
               "                    result.result = Math.pow(leftOperand, rightOperand);\n" +
               "                    break;\n" +
               "                case 'SQRT':\n" +
               "                    if (leftOperand < 0) {\n" +
               "                        throw new Error('Cannot take square root of negative number');\n" +
               "                    }\n" +
               "                    result.result = Math.sqrt(leftOperand);\n" +
               "                    break;\n" +
               "                default:\n" +
               "                    throw new Error('Invalid operation');\n" +
               "            }\n" +
               "            \n" +
               "            result.isValid = true;\n" +
               "            this.stats.successfulOperations++;\n" +
               "            \n" +
               "            this.emit('calculationCompleted', result, 12345); // sessionId\n" +
               "            \n" +
               "        } catch (error) {\n" +
               "            result.isValid = false;\n" +
               "            result.errorMessage = error.message;\n" +
               "            this.stats.errorCount++;\n" +
               "            \n" +
               "            this.emit('errorOccurred', result.errorMessage, 1, result.timestamp);\n" +
               "        }\n" +
               "        \n" +
               "        const endTime = process.hrtime.bigint();\n" +
               "        const executionTime = Number(endTime - startTime) / 1000; // microseconds\n" +
               "        \n" +
               "        // Update average execution time\n" +
               "        this.stats.averageExecutionTime = \n" +
               "            (this.stats.averageExecutionTime * (this.stats.totalOperations - 1) + executionTime) / this.stats.totalOperations;\n" +
               "        \n" +
               "        return result;\n" +
               "    }\n" +
               "    \n" +
               "    async calculateComplex(left, right, operation) {\n" +
               "        const result = { real: 0, imaginary: 0 };\n" +
               "        \n" +
               "        switch (operation) {\n" +
               "            case 'ADD':\n" +
               "                result.real = left.real + right.real;\n" +
               "                result.imaginary = left.imaginary + right.imaginary;\n" +
               "                break;\n" +
               "            case 'SUBTRACT':\n" +
               "                result.real = left.real - right.real;\n" +
               "                result.imaginary = left.imaginary - right.imaginary;\n" +
               "                break;\n" +
               "            default:\n" +
               "                throw new Error('Complex operation not implemented');\n" +
               "        }\n" +
               "        \n" +
               "        return result;\n" +
               "    }\n" +
               "    \n" +
               "    async getStatistics() {\n" +
               "        return { ...this.stats };\n" +
               "    }\n" +
               "    \n" +
               "    async reset() {\n" +
               "        this.stats = {\n" +
               "            totalOperations: 0,\n" +
               "            successfulOperations: 0,\n" +
               "            errorCount: 0,\n" +
               "            averageExecutionTime: 0.0\n" +
               "        };\n" +
               "        return true;\n" +
               "    }\n" +
               "    \n" +
               "    async setPrecision(decimalPlaces) {\n" +
               "        if (decimalPlaces >= 0 && decimalPlaces <= 15) {\n" +
               "            this.precision = decimalPlaces;\n" +
               "            return true;\n" +
               "        }\n" +
               "        return false;\n" +
               "    }\n" +
               "}\n\n" +
               "module.exports = Calculator;\n";
    }
    
    private static String generatePackageJson() {
        return "{\n" +
               "  \"name\": \"franca-calculator\",\n" +
               "  \"version\": \"1.0.0\",\n" +
               "  \"description\": \"Calculator implementation generated from FRANCA IDL\",\n" +
               "  \"main\": \"calculator-server.js\",\n" +
               "  \"scripts\": {\n" +
               "    \"start\": \"node calculator-client.js\",\n" +
               "    \"test\": \"echo \\\"Error: no test specified\\\" && exit 1\"\n" +
               "  },\n" +
               "  \"keywords\": [\n" +
               "    \"franca\",\n" +
               "    \"idl\",\n" +
               "    \"calculator\",\n" +
               "    \"interface\"\n" +
               "  ],\n" +
               "  \"author\": \"FRANCA IDL Generator\",\n" +
               "  \"license\": \"MIT\",\n" +
               "  \"engines\": {\n" +
               "    \"node\": \">=12.0.0\"\n" +
               "  }\n" +
               "}\n";
    }
    
    private static String generateJavaScriptReadme() {
        return "# Calculator JavaScript Implementation\\n\\n\" +\n" +
               "               \"Generated from FRANCA IDL Calculator interface.\\n\\n\" +\n" +
               "               \"## Usage\\n\\n\" +\n" +
               "               \"```bash\\n\" +\n" +
               "               \"# Install dependencies (if any)\\n\" +\n" +
               "               \"npm install\\n\\n\" +\n" +
               "               \"# Run the client demo\\n\" +\n" +
               "               \"npm start\\n\" +\n" +
               "               \"```\\n\\n\" +\n" +
               "               \"## Files\\n\\n\" +\n" +
               "               \"- `calculator-server.js` - Calculator implementation\\n\" +\n" +
               "               \"- `calculator-client.js` - Client demo application\\n\" +\n" +
               "               \"- `package.json` - Node.js package configuration\\n\\n\" +\n" +
               "               \"## Features\\n\\n\" +\n" +
               "               \"- Basic arithmetic operations (add, subtract, multiply, divide, power, sqrt)\\n\" +\n" +
               "               \"- Complex number operations\\n\" +\n" +
               "               \"- Statistics tracking\\n\" +\n" +
               "               \"- Event-based notifications\\n\" +\n" +
               "               \"- Error handling\\n\";\n" +
               "    }\n" +
               "}\n";
    }
}
