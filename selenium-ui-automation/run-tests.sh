#!/bin/bash

echo "Starting Selenium UI Automation Tests..."

# Set environment
export ENV=local

# Clean previous test results
rm -rf test-output
rm -rf screenshots
rm -rf allure-results

# Create directories
mkdir -p test-output
mkdir -p screenshots
mkdir -p allure-results

# Run tests
echo "Running tests with environment: $ENV"
mvn clean test -Denv=$ENV -DsuiteXmlFile=testng.xml

# Generate Allure report
echo "Generating Allure report..."
mvn allure:report

# Open report (Linux/Mac)
echo "Test execution completed!"
echo "Report generated in test-output directory"

# Make script executable
chmod +x run-tests.sh







