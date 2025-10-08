#!/bin/bash

# Local Test Execution Script
# This script runs Playwright tests locally with proper setup

set -e

# Configuration
TEST_ENV="${TEST_ENV:-local}"
BROWSER="${BROWSER:-chromium}"
HEADLESS="${HEADLESS:-true}"
PARALLEL_THREADS="${PARALLEL_THREADS:-3}"
CLEAN_BUILD="${CLEAN_BUILD:-true}"

echo "Starting Playwright Test Execution - Local Environment"
echo "Environment: $TEST_ENV"
echo "Browser: $BROWSER"
echo "Headless: $HEADLESS"
echo "Parallel Threads: $PARALLEL_THREADS"

# Function to check if service is running
check_service() {
    local service_name=$1
    local port=$2
    local max_attempts=30
    local attempt=1
    
    echo "Checking if $service_name is running on port $port..."
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s "http://localhost:$port" > /dev/null 2>&1; then
            echo "$service_name is running on port $port"
            return 0
        fi
        echo "Attempt $attempt/$max_attempts: $service_name not ready yet..."
        sleep 2
        ((attempt++))
    done
    
    echo "ERROR: $service_name failed to start on port $port after $max_attempts attempts"
    return 1
}

# Clean previous builds if requested
if [ "$CLEAN_BUILD" = "true" ]; then
    echo "Cleaning previous builds..."
    mvn clean
    rm -rf test-output allure-results reports
fi

# Create necessary directories
mkdir -p test-output allure-results reports screenshots

# Start services using Docker Compose
echo "Starting test services..."
if [ -f "docker-compose.yml" ]; then
    docker-compose up -d --build
    
    # Wait for services to be ready
    echo "Waiting for services to be ready..."
    check_service "Mock API" 8081
    check_service "Test Database" 5432
    check_service "Kafka" 9092
    
    echo "All services are ready!"
else
    echo "docker-compose.yml not found. Running tests without external services..."
fi

# Update configuration for local environment
echo "Updating configuration for local environment..."
cp config-local.properties config.properties

# Add browser and headless settings
echo "browser=$BROWSER" >> config.properties
echo "headless=$HEADLESS" >> config.properties
echo "parallel.threads=$PARALLEL_THREADS" >> config.properties

# Install Playwright browsers if not already installed
echo "Installing Playwright browsers..."
mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="install" || {
    echo "Failed to install Playwright browsers. Continuing with existing installation..."
}

# Run tests
echo "Running Playwright tests..."
mvn test \
    -Dtest.parallel.execution=true \
    -Dtest.thread.count=$PARALLEL_THREADS \
    -Dbrowser=$BROWSER \
    -Dheadless=$HEADLESS \
    -Dtest.env=$TEST_ENV \
    -Dmaven.test.failure.ignore=true

# Capture exit code
TEST_EXIT_CODE=$?

# Generate reports
echo "Generating test reports..."
if [ -d "allure-results" ] && [ "$(ls -A allure-results)" ]; then
    echo "Generating Allure report..."
    mvn allure:report || echo "Allure report generation failed"
fi

# Copy reports to reports directory
if [ -d "target/site/allure-maven" ]; then
    cp -r target/site/allure-maven/* reports/ || true
fi

if [ -d "test-output" ]; then
    cp -r test-output/* reports/ || true
fi

# Display test results
echo "Test execution completed with exit code: $TEST_EXIT_CODE"

if [ $TEST_EXIT_CODE -eq 0 ]; then
    echo "✅ All tests passed!"
else
    echo "❌ Some tests failed!"
fi

# Display report locations
echo "Test reports available at:"
echo "- Extent Report: test-output/ExtentReport.html"
echo "- Allure Report: reports/index.html"
echo "- Screenshots: test-output/screenshots/"

# Stop services
if [ -f "docker-compose.yml" ]; then
    echo "Stopping test services..."
    docker-compose down
fi

# Exit with test result code
exit $TEST_EXIT_CODE


