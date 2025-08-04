# How to Set JAVA_HOME Environment Variable on Windows

## Step 1: Find Your Java Installation Directory

First, you need to find where Java is installed on your system.

### Common Java Installation Locations:
- **Oracle JDK**: `C:\Program Files\Java\jdk-17.0.x` or `C:\Program Files\Java\jdk-11.0.x`
- **OpenJDK**: `C:\Program Files\OpenJDK\jdk-17.0.x`
- **Amazon Corretto**: `C:\Program Files\Amazon Corretto\jdk17.0.x`

### How to Find Java Installation:
1. Open File Explorer
2. Navigate to `C:\Program Files\Java\`
3. Look for a folder like `jdk-17.0.2` or `jdk-11.0.15`
4. Copy the full path (e.g., `C:\Program Files\Java\jdk-17.0.2`)

## Step 2: Set JAVA_HOME Using GUI (Recommended Method)

### Method A: Through System Properties

1. **Open System Properties:**
   - Right-click on "This PC" or "My Computer" on your desktop
   - Select "Properties"
   - Click "Advanced system settings" on the left side
   - Click "Environment Variables..." button at the bottom

2. **Add JAVA_HOME Variable:**
   - In the "Environment Variables" window
   - Under "System variables" section (bottom half), click "New..."
   - Enter the following:
     - **Variable name:** `JAVA_HOME`
     - **Variable value:** `C:\Program Files\Java\jdk-17.0.2` (replace with your actual Java path)
   - Click "OK"

3. **Update PATH Variable:**
   - Still in "System variables", find and select "Path"
   - Click "Edit..."
   - Click "New"
   - Add: `%JAVA_HOME%\bin`
   - Click "OK" on all windows

### Method B: Through Windows Search

1. **Open Environment Variables:**
   - Press `Windows + R` to open Run dialog
   - Type: `sysdm.cpl` and press Enter
   - Click "Environment Variables..." button

2. **Follow the same steps as Method A above**

## Step 3: Set JAVA_HOME Using Command Line

### Using Command Prompt (Administrator required):

```cmd
# Replace with your actual Java installation path
setx JAVA_HOME "C:\Program Files\Java\jdk-17.0.2" /M

# Add Java to PATH
setx PATH "%PATH%;%JAVA_HOME%\bin" /M
```

### Using PowerShell (Administrator required):

```powershell
# Replace with your actual Java installation path
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17.0.2", "Machine")

# Add Java to PATH
$currentPath = [Environment]::GetEnvironmentVariable("PATH", "Machine")
$newPath = "$currentPath;%JAVA_HOME%\bin"
[Environment]::SetEnvironmentVariable("PATH", $newPath, "Machine")
```

## Step 4: Verify JAVA_HOME is Set Correctly

### After setting JAVA_HOME, restart your command prompt and test:

```cmd
# Check JAVA_HOME
echo %JAVA_HOME%
# Should display: C:\Program Files\Java\jdk-17.0.2

# Check if Java is accessible
java -version
# Should display Java version information

# Check where Java executable is located
where java
# Should display: C:\Program Files\Java\jdk-17.0.2\bin\java.exe
```

## Troubleshooting

### Issue 1: "java is not recognized as internal or external command"

**Possible causes:**
- JAVA_HOME is not set correctly
- PATH doesn't include `%JAVA_HOME%\bin`
- Command prompt not restarted after setting variables

**Solutions:**
1. Verify JAVA_HOME points to correct directory
2. Ensure PATH includes `%JAVA_HOME%\bin`
3. Restart command prompt or reboot computer

### Issue 2: JAVA_HOME points to wrong directory

**Solution:**
1. Navigate to `C:\Program Files\Java\` in File Explorer
2. Find the correct JDK folder (not JRE)
3. Copy the exact path
4. Update JAVA_HOME with the correct path

### Issue 3: Permission denied when using setx

**Solution:**
- Run Command Prompt as Administrator
- Right-click Command Prompt → "Run as administrator"

## Quick Verification Script

Here's a script to check if JAVA_HOME is set correctly:
