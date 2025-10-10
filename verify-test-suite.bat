@echo off
echo ========================================
echo Test Suite Verification Script
echo ========================================
echo.

REM Check if Java is available
echo Checking Java...
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ❌ Java not found - Please install Java 11+
    echo Run setup-environment.bat first
    pause
    exit /b 1
) else (
    echo ✅ Java is available
)

REM Check if Maven is available
echo Checking Maven...
mvn -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ⚠️ Maven not in PATH, using local installation
    set MAVEN_HOME=%~dp0apache-maven-3.9.6
    set PATH=%MAVEN_HOME%\bin;%PATH%
) else (
    echo ✅ Maven is available
)

REM Check configuration files
echo Checking configuration files...
if exist "src\test\resources\test-config-comprehensive.properties" (
    echo ✅ Comprehensive test config found
) else (
    echo ❌ Comprehensive test config missing
)

if exist "src\test\resources\test-config.properties" (
    echo ✅ Fallback test config found
) else (
    echo ❌ Fallback test config missing
)

if exist "testng.xml" (
    echo ✅ TestNG configuration found
) else (
    echo ❌ TestNG configuration missing
)

REM Check test classes
echo Checking test classes...
if exist "src\test\java\tests\ApiTestExecutor.java" (
    echo ✅ ApiTestExecutor found
) else (
    echo ❌ ApiTestExecutor missing
)

if exist "src\test\java\tests\OpUaConnectionServiceTest.java" (
    echo ✅ OpUaConnectionServiceTest found
) else (
    echo ❌ OpUaConnectionServiceTest missing
)

if exist "src\test\java\tests\ReadDataServiceTest.java" (
    echo ✅ ReadDataServiceTest found
) else (
    echo ❌ ReadDataServiceTest missing
)

if exist "src\test\java\tests\WriteDataServiceTest.java" (
    echo ✅ WriteDataServiceTest found
) else (
    echo ❌ WriteDataServiceTest missing
)

if exist "src\test\java\tests\KafkaServiceTest.java" (
    echo ✅ KafkaServiceTest found
) else (
    echo ❌ KafkaServiceTest missing
)

if exist "src\test\java\tests\IntegrationTest.java" (
    echo ✅ IntegrationTest found
) else (
    echo ❌ IntegrationTest missing
)

REM Check utility classes
echo Checking utility classes...
if exist "src\test\java\utils\UnifiedMockServer.java" (
    echo ✅ UnifiedMockServer found
) else (
    echo ❌ UnifiedMockServer missing
)

if exist "src\test\java\utils\ServiceStartupHelper.java" (
    echo ✅ ServiceStartupHelper found
) else (
    echo ❌ ServiceStartupHelper missing
)

if exist "src\test\java\utils\ExtentManager.java" (
    echo ✅ ExtentManager found
) else (
    echo ❌ ExtentManager missing
)

if exist "src\test\java\utils\TestListener.java" (
    echo ✅ TestListener found
) else (
    echo ❌ TestListener missing
)

if exist "src\test\java\utils\EnhancedTestListener.java" (
    echo ✅ EnhancedTestListener found
) else (
    echo ❌ EnhancedTestListener missing
)

REM Check base classes
echo Checking base classes...
if exist "src\test\java\base\ApiTestBase.java" (
    echo ✅ ApiTestBase found
) else (
    echo ❌ ApiTestBase missing
)

if exist "src\test\java\base\RealServiceTestBase.java" (
    echo ✅ RealServiceTestBase found
) else (
    echo ❌ RealServiceTestBase missing
)

REM Check configuration classes
echo Checking configuration classes...
if exist "src\test\java\config\TestConfigManager.java" (
    echo ✅ TestConfigManager found
) else (
    echo ❌ TestConfigManager missing
)

if exist "src\test\java\config\MicroservicesConfig.java" (
    echo ✅ MicroservicesConfig found
) else (
    echo ❌ MicroservicesConfig missing
)

echo.
echo ========================================
echo Verification Complete
echo ========================================
echo.
echo If all items show ✅, your test suite is ready!
echo.
echo Next steps:
echo 1. Run: run-all-tests.bat
echo 2. Check test reports in test-output/
echo 3. Set up Jenkins pipeline if needed
echo.
pause

