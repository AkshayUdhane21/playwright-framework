@echo off
echo Starting Microservices for API Testing...

echo.
echo Make sure you have the following services running:
echo 1. Service Registry on port 8761
echo 2. OPC UA Connection Service on port 8081
echo 3. Read Data Service on port 8082
echo 4. Kafka Service on port 8083
echo 5. Write Data Service on port 8084
echo 6. Kafka Server on port 9092
echo.

echo Checking if services are running...
netstat -an | findstr :8761
if %errorlevel% neq 0 (
    echo WARNING: Service Registry not running on port 8761
)

netstat -an | findstr :8081
if %errorlevel% neq 0 (
    echo WARNING: OPC UA Connection Service not running on port 8081
)

netstat -an | findstr :8082
if %errorlevel% neq 0 (
    echo WARNING: Read Data Service not running on port 8082
)

netstat -an | findstr :8083
if %errorlevel% neq 0 (
    echo WARNING: Kafka Service not running on port 8083
)

netstat -an | findstr :8084
if %errorlevel% neq 0 (
    echo WARNING: Write Data Service not running on port 8084
)

netstat -an | findstr :9092
if %errorlevel% neq 0 (
    echo WARNING: Kafka Server not running on port 9092
)

echo.
echo Services check complete. Run your tests now.
pause

