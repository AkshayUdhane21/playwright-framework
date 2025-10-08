@echo off
REM Quick Setup Script for Akshay's Playwright Framework Repository
REM Repository: https://github.com/AkshayUdhane21/playwright-framework.git
REM Jenkins URL: http://localhost:8086

echo ========================================
echo Playwright Framework CI/CD Setup
echo Repository: https://github.com/AkshayUdhane21/playwright-framework.git
echo Jenkins URL: http://localhost:8086
echo ========================================
echo.

REM Check if Jenkins is accessible
echo [1/6] Checking Jenkins accessibility...
curl -s -o nul -w "%%{http_code}" http://localhost:8086
if %errorlevel% neq 0 (
    echo ❌ ERROR: Jenkins is not accessible at http://localhost:8086
    echo Please ensure Jenkins is running and accessible
    pause
    exit /b 1
)
echo ✅ Jenkins is accessible!

REM Get Jenkins credentials
echo.
echo [2/6] Jenkins Authentication Required
echo =====================================
echo Please provide your Jenkins credentials:
echo.
set /p JENKINS_USER="Enter Jenkins Username: "
set /p JENKINS_PASS="Enter Jenkins Password: "

if "%JENKINS_USER%"=="" (
    echo ❌ ERROR: Username cannot be empty
    pause
    exit /b 1
)

if "%JENKINS_PASS%"=="" (
    echo ❌ ERROR: Password cannot be empty
    pause
    exit /b 1
)

echo.
echo [3/6] Testing authentication...
curl -s -u %JENKINS_USER%:%JENKINS_PASS% http://localhost:8086/api/json > nul
if %errorlevel% neq 0 (
    echo ❌ ERROR: Authentication failed with provided credentials
    echo Please check your username and password
    pause
    exit /b 1
)
echo ✅ Authentication successful!

REM Download Jenkins CLI
echo.
echo [4/6] Downloading Jenkins CLI...
curl -o jenkins-cli.jar http://localhost:8086/jnlpJars/jenkins-cli.jar
if %errorlevel% neq 0 (
    echo ❌ ERROR: Failed to download Jenkins CLI
    pause
    exit /b 1
)
echo ✅ Jenkins CLI downloaded!

REM Test CLI authentication
echo.
echo [5/6] Testing CLI authentication...
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% who-am-i
if %errorlevel% neq 0 (
    echo ❌ ERROR: CLI authentication failed
    echo Please check your credentials and try again
    del jenkins-cli.jar
    pause
    exit /b 1
)
echo ✅ CLI authentication successful!

REM Install essential plugins
echo.
echo [6/6] Installing essential plugins...
echo Installing Pipeline plugin...
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin workflow-aggregator -restart

echo Waiting for Jenkins to restart...
timeout /t 30 /nobreak > nul

echo Installing additional plugins...
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin git
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin allure-jenkins-plugin
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin htmlpublisher
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin email-ext
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin docker-workflow
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin build-timeout
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin credentials-binding

echo ✅ Plugins installation completed!

REM Create the pipeline job
echo.
echo Creating Jenkins pipeline job...
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% create-job "playwright-test-pipeline" < jenkins-job-config.xml

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo ✅ SUCCESS: Jenkins setup completed!
    echo ========================================
    echo.
    echo 📋 Your Jenkins Configuration:
    echo    URL: http://localhost:8086
    echo    Job: playwright-test-pipeline
    echo    Repository: https://github.com/AkshayUdhane21/playwright-framework.git
    echo.
    echo 🚀 Next Steps:
    echo    1. Go to http://localhost:8086
    echo    2. Login with your credentials
    echo    3. Navigate to 'playwright-test-pipeline' job
    echo    4. Click 'Build with Parameters' to run tests
    echo.
    echo 📊 Test Parameters Available:
    echo    - TEST_ENV: local, staging, production
    echo    - BROWSER: chromium, firefox, webkit
    echo    - HEADLESS: true, false
    echo    - PARALLEL_THREADS: 1-10
    echo.
    echo 🎯 Your credentials:
    echo    Username: %JENKINS_USER%
    echo    Password: [hidden]
    echo.
) else (
    echo ❌ ERROR: Failed to create Jenkins job
    echo Please check the job configuration and try again
    echo.
    echo Manual setup instructions:
    echo 1. Go to http://localhost:8086
    echo 2. Create new Pipeline job named 'playwright-test-pipeline'
    echo 3. Use the Jenkinsfile from your repository
    echo 4. Set repository URL to: https://github.com/AkshayUdhane21/playwright-framework.git
)

REM Cleanup
del jenkins-cli.jar

echo.
echo Setup script completed!
echo Press any key to exit...
pause > nul
