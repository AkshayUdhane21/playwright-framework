#!/bin/bash

echo "========================================"
echo "Playwright Framework - Mock Services Test Runner"
echo "========================================"
echo

echo "Starting mock services and running tests..."
echo

# Set test mode to mock
echo "Setting test mode to mock..."
export TEST_MODE=mock

# Clean previous builds
echo "Cleaning previous builds..."
mvn clean -q

# Compile the project
echo "Compiling project..."
mvn compile test-compile -q
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi

echo "✅ Compilation successful!"

# Run tests with mock services
echo
echo "Running tests with mock services..."
echo "========================================"

mvn test -Dtest.mode=mock -Dmock.services.enabled=true

# Check test results
if [ $? -eq 0 ]; then
    echo
    echo "========================================"
    echo "✅ ALL TESTS PASSED!"
    echo "========================================"
    echo
    echo "Test reports generated:"
    echo "- ExtentReports: target/surefire-reports/"
    echo "- Allure Reports: allure-results/"
    echo
else
    echo
    echo "========================================"
    echo "❌ SOME TESTS FAILED!"
    echo "========================================"
    echo
    echo "Check the test reports for details:"
    echo "- ExtentReports: target/surefire-reports/"
    echo "- Allure Reports: allure-results/"
    echo
fi

echo
echo "Test execution completed!"

