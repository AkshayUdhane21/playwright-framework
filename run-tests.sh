#!/bin/bash

echo "========================================"
echo "Enhanced Playwright Framework"
echo "Microservices API Testing Suite"
echo "========================================"
echo

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 11 or higher"
    exit 1
fi

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6 or higher"
    exit 1
fi

echo "Checking microservices status..."
echo

# Check service status
java -cp target/test-classes utils.ServiceStartupHelper
if [ $? -ne 0 ]; then
    echo
    echo "WARNING: Some services are not ready"
    echo "Please ensure all microservices are running before proceeding"
    echo
    echo "Required services:"
    echo "- Service Registry (Port 8761)"
    echo "- OPC UA Service (Port 8081)"
    echo "- Read Data Service (Port 8082)"
    echo "- Kafka Service (Port 8083)"
    echo "- Write Data Service (Port 8084)"
    echo
    read -p "Do you want to continue anyway? (y/n): " continue
    if [[ ! "$continue" =~ ^[Yy]$ ]]; then
        echo "Test execution cancelled"
        exit 1
    fi
fi

echo
echo "Starting test execution..."
echo

# Clean and compile
echo "Cleaning and compiling..."
mvn clean compile test-compile
if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed"
    exit 1
fi

echo
echo "Running tests..."
echo

# Run tests
mvn test
if [ $? -ne 0 ]; then
    echo
    echo "WARNING: Some tests failed"
    echo "Check the reports for details"
else
    echo
    echo "All tests passed successfully!"
fi

echo
echo "========================================"
echo "Test execution completed"
echo "========================================"
echo
echo "Reports generated:"
echo "- ExtentReports: test-output/Enhanced_Test_Report_*.html"
echo "- Allure Reports: target/site/allure-maven-plugin/"
echo "- TestNG Reports: test-output/"
echo

# Open reports directory
read -p "Open reports directory? (y/n): " open
if [[ "$open" =~ ^[Yy]$ ]]; then
    if command -v xdg-open &> /dev/null; then
        xdg-open test-output
    elif command -v open &> /dev/null; then
        open test-output
    else
        echo "Please open the test-output directory manually"
    fi
fi
