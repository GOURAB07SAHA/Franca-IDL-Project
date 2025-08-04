@echo off
echo =========================================
echo FRANCA IDL Environment Setup Script
echo =========================================
echo.

:: Check if running as Administrator
net session >nul 2>&1
if %errorLevel% == 0 (
    echo Running as Administrator... Good!
    echo.
) else (
    echo ERROR: This script must be run as Administrator
    echo Right-click on this file and select "Run as administrator"
    echo.
    pause
    exit /b 1
)

:: Set default installation paths
set DEFAULT_FRANCA_HOME=C:\franca
set DEFAULT_GENERATOR_HOME=C:\franca\generators
set DEFAULT_JAVA_HOME=C:\Program Files\Java\jdk-11

echo Setting up environment variables...
echo.

:: Set FRANCA_HOME
echo Setting FRANCA_HOME=%DEFAULT_FRANCA_HOME%
setx FRANCA_HOME "%DEFAULT_FRANCA_HOME%" /M
if %errorLevel% == 0 (
    echo ✓ FRANCA_HOME set successfully
) else (
    echo ✗ Failed to set FRANCA_HOME
)

:: Set GENERATOR_HOME
echo Setting GENERATOR_HOME=%DEFAULT_GENERATOR_HOME%
setx GENERATOR_HOME "%DEFAULT_GENERATOR_HOME%" /M
if %errorLevel% == 0 (
    echo ✓ GENERATOR_HOME set successfully
) else (
    echo ✗ Failed to set GENERATOR_HOME
)

:: Check if JAVA_HOME is already set
if defined JAVA_HOME (
    echo JAVA_HOME is already set to: %JAVA_HOME%
    echo Skipping JAVA_HOME setup...
) else (
    echo Setting JAVA_HOME=%DEFAULT_JAVA_HOME%
    setx JAVA_HOME "%DEFAULT_JAVA_HOME%" /M
    if %errorLevel% == 0 (
        echo ✓ JAVA_HOME set successfully
    ) else (
        echo ✗ Failed to set JAVA_HOME
        echo Please set JAVA_HOME manually to your Java installation directory
    )
)

:: Update PATH
echo.
echo Updating PATH variable...
for /f "tokens=2*" %%A in ('reg query "HKLM\SYSTEM\CurrentControlSet\Control\Session Manager\Environment" /v PATH') do set CURRENT_PATH=%%B

:: Check if paths are already in PATH
echo %CURRENT_PATH% | find "%DEFAULT_FRANCA_HOME%\bin" >nul
if %errorLevel% == 0 (
    echo FRANCA bin path already in PATH
) else (
    echo Adding FRANCA bin to PATH...
    setx PATH "%CURRENT_PATH%;%DEFAULT_FRANCA_HOME%\bin" /M
)

echo %CURRENT_PATH% | find "%DEFAULT_GENERATOR_HOME%\core" >nul
if %errorLevel% == 0 (
    echo Generator core path already in PATH
) else (
    echo Adding generator core to PATH...
    for /f "tokens=2*" %%A in ('reg query "HKLM\SYSTEM\CurrentControlSet\Control\Session Manager\Environment" /v PATH') do set CURRENT_PATH=%%B
    setx PATH "%CURRENT_PATH%;%DEFAULT_GENERATOR_HOME%\core" /M
)

echo %CURRENT_PATH% | find "%DEFAULT_GENERATOR_HOME%\dbus" >nul
if %errorLevel% == 0 (
    echo Generator dbus path already in PATH
) else (
    echo Adding generator dbus to PATH...
    for /f "tokens=2*" %%A in ('reg query "HKLM\SYSTEM\CurrentControlSet\Control\Session Manager\Environment" /v PATH') do set CURRENT_PATH=%%B
    setx PATH "%CURRENT_PATH%;%DEFAULT_GENERATOR_HOME%\dbus" /M
)

echo.
echo =========================================
echo Environment Setup Complete!
echo =========================================
echo.
echo The following environment variables have been set:
echo - FRANCA_HOME: %DEFAULT_FRANCA_HOME%
echo - GENERATOR_HOME: %DEFAULT_GENERATOR_HOME%
echo - JAVA_HOME: %JAVA_HOME%
echo.
echo PATH has been updated to include generator directories.
echo.
echo IMPORTANT: You must restart your command prompt/PowerShell
echo for the changes to take effect.
echo.
echo Next steps:
echo 1. Download and extract FRANCA tools to %DEFAULT_FRANCA_HOME%
echo 2. Download and extract generators to %DEFAULT_GENERATOR_HOME%
echo 3. Restart your command prompt
echo 4. Test with: java -version
echo.
pause
