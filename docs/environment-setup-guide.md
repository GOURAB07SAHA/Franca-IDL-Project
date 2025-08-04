# Step 3: Setting Environment Variables - Detailed Guide

## What are Environment Variables?

Environment variables are system-level variables that tell your computer where to find specific programs and tools. When you set these variables, you can run FRANCA tools from any directory without specifying the full path.

## Why We Need These Variables?

- **FRANCA_HOME**: Points to where FRANCA tools are installed
- **GENERATOR_HOME**: Points to where code generators are located  
- **PATH**: Tells Windows where to look for executable programs

## Method 1: Setting Environment Variables via GUI (Recommended)

### Step 1: Open System Properties
1. Right-click on "This PC" or "My Computer" on your desktop
2. Select "Properties"
3. Click "Advanced system settings" on the left panel
4. Click "Environment Variables..." button

### Step 2: Add New Environment Variables
Click "New..." under "System variables" section and add these one by one:

**Variable 1: FRANCA_HOME**
- Variable name: `FRANCA_HOME`
- Variable value: `C:\franca`

**Variable 2: GENERATOR_HOME**
- Variable name: `GENERATOR_HOME`
- Variable value: `C:\franca\generators`

**Variable 3: JAVA_HOME** (if not already set)
- Variable name: `JAVA_HOME`
- Variable value: `C:\Program Files\Java\jdk-11.0.x` (adjust version as needed)

### Step 3: Update PATH Variable
1. Find "Path" in the "System variables" list
2. Select it and click "Edit..."
3. Click "New" and add these entries one by one:
   - `%FRANCA_HOME%\bin`
   - `%GENERATOR_HOME%\core`
   - `%GENERATOR_HOME%\dbus`
   - `%JAVA_HOME%\bin` (if not already there)

## Method 2: Setting Environment Variables via Command Line

### Temporary (Current Session Only)
Open Command Prompt or PowerShell and run:

```cmd
# Set FRANCA_HOME
set FRANCA_HOME=C:\franca

# Set GENERATOR_HOME  
set GENERATOR_HOME=C:\franca\generators

# Set JAVA_HOME (adjust path as needed)
set JAVA_HOME=C:\Program Files\Java\jdk-11.0.x

# Update PATH
set PATH=%PATH%;%FRANCA_HOME%\bin;%GENERATOR_HOME%\core;%GENERATOR_HOME%\dbus;%JAVA_HOME%\bin
```

### Permanent (Using setx command)
Open Command Prompt as Administrator and run:

```cmd
# Set permanent environment variables
setx FRANCA_HOME "C:\franca" /M
setx GENERATOR_HOME "C:\franca\generators" /M
setx JAVA_HOME "C:\Program Files\Java\jdk-11.0.x" /M

# Update PATH permanently (be careful with this command)
setx PATH "%PATH%;%FRANCA_HOME%\bin;%GENERATOR_HOME%\core;%GENERATOR_HOME%\dbus;%JAVA_HOME%\bin" /M
```

**Note**: The `/M` flag sets system-wide variables. You need Administrator privileges.

## Method 3: Using PowerShell

### Temporary (Current Session)
```powershell
# Set environment variables for current session
$env:FRANCA_HOME = "C:\franca"
$env:GENERATOR_HOME = "C:\franca\generators"
$env:JAVA_HOME = "C:\Program Files\Java\jdk-11.0.x"
$env:PATH += ";$env:FRANCA_HOME\bin;$env:GENERATOR_HOME\core;$env:GENERATOR_HOME\dbus;$env:JAVA_HOME\bin"
```

### Permanent (System-wide)
```powershell
# Run PowerShell as Administrator and execute:
[Environment]::SetEnvironmentVariable("FRANCA_HOME", "C:\franca", "Machine")
[Environment]::SetEnvironmentVariable("GENERATOR_HOME", "C:\franca\generators", "Machine")
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-11.0.x", "Machine")

# Update PATH
$currentPath = [Environment]::GetEnvironmentVariable("PATH", "Machine")
$newPath = "$currentPath;C:\franca\bin;C:\franca\generators\core;C:\franca\generators\dbus;C:\Program Files\Java\jdk-11.0.x\bin"
[Environment]::SetEnvironmentVariable("PATH", $newPath, "Machine")
```

## Verification

After setting the environment variables, **restart your command prompt/PowerShell** and verify:

```cmd
# Check if variables are set correctly
echo %FRANCA_HOME%
echo %GENERATOR_HOME%
echo %JAVA_HOME%
echo %PATH%

# Test if Java is accessible
java -version

# Test if tools are in PATH (after installation)
where java
where commonapi-core-generator
```

## Directory Structure After Installation

Your directory structure should look like this:

```
C:\franca\
├── bin\                          # FRANCA core binaries
├── plugins\                      # Eclipse plugins
├── generators\
│   ├── core\
│   │   ├── commonapi-core-generator.exe
│   │   └── ...
│   ├── dbus\
│   │   ├── commonapi-dbus-generator.exe
│   │   └── ...
│   └── someip\
│       ├── commonapi-someip-generator.exe
│       └── ...
└── ...
```

## Troubleshooting

### Issue 1: "Command not found" or "'java' is not recognized"
**Solution**: 
- Check if JAVA_HOME is set correctly
- Ensure PATH includes `%JAVA_HOME%\bin`
- Restart command prompt after setting variables

### Issue 2: PATH too long error
**Solution**: 
- Use short paths (avoid spaces in directory names)
- Consider using `C:\franca` instead of `C:\Program Files\FRANCA`

### Issue 3: Permission denied when using setx
**Solution**: 
- Run Command Prompt as Administrator
- Or use the GUI method instead

### Issue 4: Changes not taking effect
**Solution**: 
- Restart your command prompt/PowerShell
- Log out and log back in to Windows
- Restart your computer if necessary

## Quick Setup Script

I'll create a setup script that automates this process for you.
