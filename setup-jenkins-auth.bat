@echo off
REM Jenkins Setup Script with Authentication Fix
REM This script helps you set up Jenkins with proper authentication

echo Setting up Jenkins for Playwright Test Framework...
echo Jenkins URL: http://localhost:8086

REM Check if Jenkins is accessible
echo Checking if Jenkins is accessible...
curl -s -o nul -w "%%{http_code}" http://localhost:8086
if %errorlevel% neq 0 (
    echo ERROR: Jenkins is not accessible at http://localhost:8086
    echo Please ensure Jenkins is running and accessible
    pause
    exit /b 1
)

echo Jenkins is accessible!

REM Get Jenkins credentials from user
echo.
echo Jenkins Authentication Required
echo ================================
echo The default credentials (admin/admin) are not working.
echo Please provide your Jenkins credentials:
echo.

set /p JENKINS_USER="Enter Jenkins Username: "
set /p JENKINS_PASS="Enter Jenkins Password: "

if "%JENKINS_USER%"=="" (
    echo ERROR: Username cannot be empty
    pause
    exit /b 1
)

if "%JENKINS_PASS%"=="" (
    echo ERROR: Password cannot be empty
    pause
    exit /b 1
)

echo.
echo Testing authentication...
curl -s -u %JENKINS_USER%:%JENKINS_PASS% http://localhost:8086/api/json > nul
if %errorlevel% neq 0 (
    echo ERROR: Authentication failed with provided credentials
    echo Please check your username and password
    pause
    exit /b 1
)

echo Authentication successful!

REM Download Jenkins CLI
echo Downloading Jenkins CLI...
curl -o jenkins-cli.jar http://localhost:8086/jnlpJars/jenkins-cli.jar
if %errorlevel% neq 0 (
    echo ERROR: Failed to download Jenkins CLI
    pause
    exit /b 1
)

REM Test CLI authentication
echo Testing CLI authentication...
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% who-am-i
if %errorlevel% neq 0 (
    echo ERROR: CLI authentication failed
    echo Please check your credentials and try again
    del jenkins-cli.jar
    pause
    exit /b 1
)

echo CLI authentication successful!

REM Install required plugins
echo Installing required plugins...
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin workflow-aggregator
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin git
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin allure-jenkins-plugin
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin htmlpublisher
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin email-ext
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin docker-workflow
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin build-timeout
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% install-plugin credentials-binding

echo Plugins installation completed!

REM Restart Jenkins
echo Restarting Jenkins...
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% restart

echo Waiting for Jenkins to restart...
timeout /t 60 /nobreak > nul

REM Test connection after restart
echo Testing connection after restart...
curl -s -u %JENKINS_USER%:%JENKINS_PASS% http://localhost:8086/api/json > nul
if %errorlevel% neq 0 (
    echo WARNING: Jenkins may still be restarting. Please wait a moment and try again.
)

REM Create the pipeline job
echo Creating Jenkins pipeline job...
java -jar jenkins-cli.jar -s http://localhost:8086 -auth %JENKINS_USER%:%JENKINS_PASS% create-job "playwright-test-pipeline" < jenkins-job-config.xml

if %errorlevel% equ 0 (
    echo SUCCESS: Jenkins setup completed!
    echo.
    echo Access Jenkins at: http://localhost:8086
    echo Job created: playwright-test-pipeline
    echo.
    echo Next steps:
    echo 1. Go to http://localhost:8086
    echo 2. Navigate to the playwright-test-pipeline job
    echo 3. Click "Build with Parameters" to run tests
    echo 4. Git repository is already configured: https://github.com/AkshayUdhane21/playwright-framework.git
    echo.
    echo Your credentials:
    echo Username: %JENKINS_USER%
    echo Password: [hidden]
) else (
    echo ERROR: Failed to create Jenkins job
    echo Please check the job configuration and try again
)

REM Cleanup
del jenkins-cli.jar

echo.
echo Setup script completed!
pause

