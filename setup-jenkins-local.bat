@echo off
REM Jenkins Setup Script for Playwright Test Framework - Windows
REM This script sets up Jenkins with your specific URL: http://localhost:8086

echo Setting up Jenkins for Playwright Test Framework...
echo Jenkins URL: http://localhost:8086

REM Set environment variables
set JENKINS_URL=http://localhost:8086
set JENKINS_USER=admin
set JENKINS_PASS=admin

REM Check if Jenkins is running
echo Checking if Jenkins is accessible...
curl -s -o nul -w "%%{http_code}" http://localhost:8086
if %errorlevel% neq 0 (
    echo ERROR: Jenkins is not accessible at http://localhost:8086
    echo Please ensure Jenkins is running and accessible
    pause
    exit /b 1
)

echo Jenkins is accessible!

REM Download Jenkins CLI
echo Downloading Jenkins CLI...
curl -o jenkins-cli.jar http://localhost:8086/jnlpJars/jenkins-cli.jar
if %errorlevel% neq 0 (
    echo ERROR: Failed to download Jenkins CLI
    pause
    exit /b 1
)

REM Install required plugins
echo Installing required plugins...
java -jar jenkins-cli.jar -s http://localhost:8086 -auth admin:admin install-plugin workflow-aggregator
java -jar jenkins-cli.jar -s http://localhost:8086 -auth admin:admin install-plugin git
java -jar jenkins-cli.jar -s http://localhost:8086 -auth admin:admin install-plugin allure-jenkins-plugin
java -jar jenkins-cli.jar -s http://localhost:8086 -auth admin:admin install-plugin htmlpublisher
java -jar jenkins-cli.jar -s http://localhost:8086 -auth admin:admin install-plugin email-ext
java -jar jenkins-cli.jar -s http://localhost:8086 -auth admin:admin install-plugin docker-workflow
java -jar jenkins-cli.jar -s http://localhost:8086 -auth admin:admin install-plugin build-timeout
java -jar jenkins-cli.jar -s http://localhost:8086 -auth admin:admin install-plugin credentials-binding

REM Restart Jenkins
echo Restarting Jenkins...
java -jar jenkins-cli.jar -s http://localhost:8086 -auth admin:admin restart

echo Waiting for Jenkins to restart...
timeout /t 60 /nobreak > nul

REM Create the pipeline job
echo Creating Jenkins pipeline job...
java -jar jenkins-cli.jar -s http://localhost:8086 -auth admin:admin create-job "playwright-test-pipeline" < jenkins-job-config.xml

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
    echo 4. Configure your Git repository URL in the job settings
) else (
    echo ERROR: Failed to create Jenkins job
    echo Please check the job configuration and try again
)

REM Cleanup
del jenkins-cli.jar

echo.
echo Setup script completed!
pause


