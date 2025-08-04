# FRANCA IDL Installation Guide

## What to Download from FRANCA GitHub

### Option 1: FRANCA Core (Recommended)
Go to: https://github.com/franca/franca

**What to download:**
1. **Release Version**: Download the latest release from the "Releases" section
   - Look for files like `franca-x.x.x.zip` or `franca-x.x.x.tar.gz`
   - This contains the core FRANCA tools and Eclipse plugins

2. **Source Code**: If no pre-built release is available
   - Clone or download the entire repository
   - You'll need to build it yourself using Maven

### Option 2: FRANCA Connectors (For specific middleware)
Additional repositories you might need:

1. **CommonAPI C++ Tools**: https://github.com/COVESA/capicxx-core-tools
   - For generating C++ code with CommonAPI
   - Download: `commonapi_core_generator.zip`

2. **CommonAPI D-Bus Tools**: https://github.com/COVESA/capicxx-dbus-tools
   - For D-Bus middleware support
   - Download: `commonapi_dbus_generator.zip`

3. **CommonAPI SOME/IP Tools**: https://github.com/COVESA/capicxx-someip-tools
   - For SOME/IP middleware support (automotive)
   - Download: `commonapi_someip_generator.zip`

## Prerequisites

### Java Development Kit (JDK)
```bash
# Check if Java is installed
java -version

# If not installed, download from:
# - Oracle JDK: https://www.oracle.com/java/technologies/downloads/
# - OpenJDK: https://openjdk.org/
```

### Maven (if building from source)
```bash
# Check if Maven is installed
mvn -version

# Download from: https://maven.apache.org/download.cgi
```

### Eclipse IDE (Optional but recommended)
- Download Eclipse IDE for Java Developers
- Install FRANCA plugins from Eclipse Marketplace

## Installation Steps

### Step 1: Download FRANCA Tools
1. Go to https://github.com/franca/franca/releases
2. Download the latest release zip file
3. Extract to `C:\franca\` directory

### Step 2: Download Code Generators
For CommonAPI C++ (most common):
1. Go to https://github.com/COVESA/capicxx-core-tools/releases
2. Download `commonapi_core_generator.zip`
3. Extract to `C:\franca\generators\core\`

For D-Bus support:
1. Go to https://github.com/COVESA/capicxx-dbus-tools/releases
2. Download `commonapi_dbus_generator.zip`
3. Extract to `C:\franca\generators\dbus\`

### Step 3: Set Environment Variables
```cmd
set FRANCA_HOME=C:\franca
set GENERATOR_HOME=C:\franca\generators
set PATH=%PATH%;%FRANCA_HOME%\bin;%GENERATOR_HOME%\core;%GENERATOR_HOME%\dbus
```

### Step 4: Verify Installation
```bash
# Test FRANCA core (if available as standalone)
java -jar %FRANCA_HOME%\plugins\org.franca.core_x.x.x.jar --help

# Test CommonAPI generators
commonapi-core-generator --help
commonapi-dbus-generator --help
```

## Alternative: Using Eclipse IDE

### Step 1: Install Eclipse
1. Download Eclipse IDE for Java Developers
2. Install and launch Eclipse

### Step 2: Install FRANCA Plugins
1. Open Eclipse
2. Go to Help → Eclipse Marketplace
3. Search for "FRANCA"
4. Install "FRANCA IDL" plugin
5. Restart Eclipse

### Step 3: Import FRANCA Project
1. File → Import → General → Existing Projects into Workspace
2. Select your `franca-idl-project` directory
3. Eclipse will recognize .fidl files and provide syntax highlighting

## Troubleshooting

### Common Issues:

1. **Java not found**
   - Ensure JAVA_HOME is set correctly
   - Add Java to PATH

2. **Generator not found**
   - Check if generators are extracted correctly
   - Verify PATH includes generator directories

3. **Permission denied**
   - Run command prompt as Administrator
   - Check file permissions

### Next Steps After Installation
1. Test with provided examples
2. Generate code using our build scripts
3. Integrate with your applications

## Quick Test
Create a simple test to verify installation:
```bash
# Navigate to project directory
cd C:\Users\hp\franca-idl-project

# Run code generation (after updating paths in generate_code.cmd)
.\tools\generate_code.cmd
```
