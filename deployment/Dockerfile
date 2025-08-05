# Multi-stage Docker build for Vehicle Dashboard
# Supports Java, C++, and JavaScript implementations

# Stage 1: Java build environment
FROM openjdk:17-jdk-slim AS java-builder
WORKDIR /app
COPY generated/java ./java
RUN javac -cp java java/org/example/automotive/*.java

# Stage 2: Node.js environment  
FROM node:18-alpine AS node-builder
WORKDIR /app
COPY generated/javascript ./javascript
COPY deployment/package.json ./
RUN npm install

# Stage 3: C++ build environment
FROM gcc:latest AS cpp-builder
WORKDIR /app
COPY generated/cpp ./cpp
RUN cd cpp && g++ -std=c++17 -pthread -o vehicle-dashboard VehicleDashboard.cpp

# Final stage: Multi-runtime container
FROM ubuntu:22.04
LABEL maintainer="FRANCA IDL Vehicle Dashboard"
LABEL description="Complete Vehicle Dashboard with Java, C++, and JavaScript support"

# Install runtime dependencies
RUN apt-get update && apt-get install -y \
    openjdk-17-jre-headless \
    nodejs \
    npm \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy compiled Java classes
COPY --from=java-builder /app/java ./java

# Copy JavaScript application
COPY --from=node-builder /app/javascript ./javascript
COPY --from=node-builder /app/node_modules ./node_modules

# Copy C++ executable
COPY --from=cpp-builder /app/cpp/vehicle-dashboard ./cpp/

# Copy FIDL interface files
COPY interfaces ./interfaces

# Create startup scripts
RUN echo '#!/bin/bash\ncase "$1" in\n  java) java -cp java org.example.automotive.VehicleDashboardApp ;;\n  javascript) node javascript/vehicle-dashboard.js ;;\n  cpp) ./cpp/vehicle-dashboard ;;\n  *) echo "Usage: $0 {java|javascript|cpp}" ;;\nesac' > /app/run-dashboard.sh
RUN chmod +x /app/run-dashboard.sh

# Expose ports for web interface (if needed)
EXPOSE 8080 3000

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD pgrep -f "VehicleDashboard" > /dev/null || exit 1

# Default command
CMD ["./run-dashboard.sh", "java"]
