@echo off
REM Code generation script for FRANCA IDL

REM Set up environment variables
SET JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.8.9-hotspot
SET FRANCA_TOOL_DIR=C:\Franca\tools

REM Define input and output directories
SET FIDL_SRC_DIR=interfaces\basic
SET OUTPUT_DIR=generated

REM Define command for code generation
SET FRANCA_CMD=java -jar %FRANCA_TOOL_DIR%\franca-generator.jar

REM Check if Franca tools exist
if not exist "%FRANCA_TOOL_DIR%\franca-generator.jar" (
    echo Error: Franca generator not found at %FRANCA_TOOL_DIR%\franca-generator.jar
    echo Please install Franca IDL tools first.
    pause
    exit /b 1
)

REM Run code generation
echo Generating C++ code...
%FRANCA_CMD% -i %FIDL_SRC_DIR% -o %OUTPUT_DIR% --language=cpp

echo Generating Java code...
%FRANCA_CMD% -i %FIDL_SRC_DIR% -o %OUTPUT_DIR% --language=java

echo Generating JavaScript code...
%FRANCA_CMD% -i %FIDL_SRC_DIR% -o %OUTPUT_DIR% --language=js

REM Notify user
echo Code generation complete.
pause

