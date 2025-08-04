cmake_minimum_required(VERSION 3.12)
project(Calculator)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Add executable
add_executable(calculator_client
    Calculator.cpp
    CalculatorClient.cpp
)

# Find required packages
find_package(Threads REQUIRED)

# Link libraries
target_link_libraries(calculator_client
    Threads::Threads
)

# Set compiler warnings
if(CMAKE_CXX_COMPILER_ID MATCHES "GNU|Clang")
    target_compile_options(calculator_client PRIVATE -Wall -Wextra -Wpedantic)
endif()
