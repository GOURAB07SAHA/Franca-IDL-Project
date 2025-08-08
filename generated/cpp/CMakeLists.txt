cmake_minimum_required(VERSION 3.12)
project(FrancaComprehensive)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Add executables
add_executable(calculator_client
    Calculator.cpp
    CalculatorClient.cpp
)

add_executable(vehicle_dashboard
    VehicleDashboard.cpp
)

# Find required packages
find_package(Threads REQUIRED)

# Link libraries
target_link_libraries(calculator_client Threads::Threads)
target_link_libraries(vehicle_dashboard Threads::Threads)
