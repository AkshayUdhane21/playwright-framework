#!/bin/bash

# Docker Test Execution Script
# This script runs Playwright tests in Docker containers

set -e

# Configuration
TEST_ENV="${TEST_ENV:-local}"
BROWSER="${BROWSER:-chromium}"
HEADLESS="${HEADLESS:-true}"
PARALLEL_THREADS="${PARALLEL_THREADS:-3}"
DOCKER_COMPOSE_FILE="${DOCKER_COMPOSE_FILE:-docker-compose.ci.yml}"

echo "Starting Playwright Test Execution - Docker Environment"
echo "Environment: $TEST_ENV"
echo "Browser: $BROWSER"
echo "Headless: $HEADLESS"
echo "Parallel Threads: $PARALLEL_THREADS"
echo "Docker Compose File: $DOCKER_COMPOSE_FILE"

# Function to cleanup on exit
cleanup() {
    echo "Cleaning up Docker containers..."
    docker-compose -f "$DOCKER_COMPOSE_FILE" down -v || true
    docker system prune -f || true
}

# Set trap for cleanup
trap cleanup EXIT

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "ERROR: Docker is not running. Please start Docker and try again."
    exit 1
fi

# Check if docker-compose file exists
if [ ! -f "$DOCKER_COMPOSE_FILE" ]; then
    echo "ERROR: Docker compose file $DOCKER_COMPOSE_FILE not found."
    exit 1
fi

# Clean up any existing containers
echo "Cleaning up existing containers..."
docker-compose -f "$DOCKER_COMPOSE_FILE" down -v || true

# Create necessary directories
mkdir -p test-output allure-results reports screenshots

# Set environment variables for Docker Compose
export TEST_ENV
export BROWSER
export HEADLESS
export PARALLEL_THREADS

# Start services and run tests
echo "Starting test services and running tests..."
docker-compose -f "$DOCKER_COMPOSE_FILE" up --build --abort-on-container-exit

# Capture exit code from the test container
TEST_EXIT_CODE=${PIPESTATUS[0]}

# Copy test results from container
echo "Copying test results from container..."
if docker ps -a --format "table {{.Names}}" | grep -q "playwright-test-runner"; then
    docker cp playwright-test-runner:/app/test-output ./test-output/ || true
    docker cp playwright-test-runner:/app/allure-results ./allure-results/ || true
    docker cp playwright-test-runner:/app/reports ./reports/ || true
fi

# Generate additional reports if needed
if [ -d "allure-results" ] && [ "$(ls -A allure-results)" ]; then
    echo "Generating Allure report..."
    # Run Allure report generation in a temporary container
    docker run --rm \
        -v "$(pwd)/allure-results:/app/allure-results" \
        -v "$(pwd)/reports:/app/reports" \
        frankescobar/allure-docker-service:latest \
        allure generate /app/allure-results -o /app/reports --clean || echo "Allure report generation failed"
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

# Display container logs for debugging
echo "Container logs:"
docker-compose -f "$DOCKER_COMPOSE_FILE" logs playwright-tests || true

# Exit with test result code
exit $TEST_EXIT_CODE


