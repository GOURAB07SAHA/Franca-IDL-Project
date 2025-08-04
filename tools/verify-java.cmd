@echo off
echo =========================================
echo Java Installation Verification Script
echo =========================================
echo.

echo Checking JAVA_HOME environment variable...
if defined JAVA_HOME (
    echo ✓ JAVA_HOME is set to: %JAVA_HOME%
    
    echo.
    echo Checking if JAVA_HOME directory exists...
    if exist "%JAVA_HOME%" (
        echo ✓ JAVA_HOME directory exists
        
        echo.
        echo Checking if java.exe exists in JAVA_HOME\bin...
        if exist "%JAVA_HOME%\bin\java.exe" (
            echo ✓ java.exe found at: %JAVA_HOME%\bin\java.exe
        ) else (
            echo ✗ java.exe NOT found in %JAVA_HOME%\bin\
            echo   Please check your JAVA_HOME path
        )
    ) else (
        echo ✗ JAVA_HOME directory does not exist: %JAVA_HOME%
        echo   Please check your JAVA_HOME path
    )
) else (
    echo ✗ JAVA_HOME is not set
    echo   Please set JAVA_HOME environment variable
)

echo.
echo Checking if java command is available in PATH...
java -version >nul 2>&1
if %errorLevel% == 0 (
    echo ✓ Java is accessible from command line
    echo.
    echo Java version information:
    java -version
    echo.
    echo Java executable location:
    where java
) else (
    echo ✗ Java is not accessible from command line
    echo   Possible solutions:
    echo   1. Add %%JAVA_HOME%%\bin to your PATH
    echo   2. Restart your command prompt
    echo   3. Install Java if not already installed
)

echo.
echo =========================================
echo Verification Complete
echo =========================================
echo.

:: Check for common Java installation directories
echo Looking for Java installations on your system...
echo.

if exist "C:\Program Files\Java\" (
    echo Found Java directory: C:\Program Files\Java\
    dir "C:\Program Files\Java\" /B
    echo.
) else (
    echo No Java directory found at: C:\Program Files\Java\
)

if exist "C:\Program Files\OpenJDK\" (
    echo Found OpenJDK directory: C:\Program Files\OpenJDK\
    dir "C:\Program Files\OpenJDK\" /B
    echo.
)

if exist "C:\Program Files\Amazon Corretto\" (
    echo Found Amazon Corretto directory: C:\Program Files\Amazon Corretto\
    dir "C:\Program Files\Amazon Corretto\" /B
    echo.
)

echo.
echo If Java is not installed, download it from:
echo - Oracle JDK: https://www.oracle.com/java/technologies/downloads/
echo - OpenJDK: https://adoptopenjdk.net/
echo - Amazon Corretto: https://aws.amazon.com/corretto/
echo.
pause
