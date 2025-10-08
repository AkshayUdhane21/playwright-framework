#!/bin/bash

# Dependency Installation Script
# This script installs all required dependencies for the Playwright test framework

set -e

echo "Installing dependencies for Playwright Test Framework..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed. Please install Java 11 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F '.' '{print $1}')
if [ "$JAVA_VERSION" -lt 11 ]; then
    echo "ERROR: Java 11 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "Java version: $(java -version 2>&1 | head -n 1)"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

# Check Maven version
MAVEN_VERSION=$(mvn -version | head -n 1 | awk '{print $3}')
echo "Maven version: $MAVEN_VERSION"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "WARNING: Docker is not installed. Docker is required for containerized testing."
    echo "Please install Docker to use the full CI/CD pipeline."
else
    echo "Docker version: $(docker --version)"
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "WARNING: Docker Compose is not installed. Docker Compose is required for multi-container testing."
    echo "Please install Docker Compose to use the full CI/CD pipeline."
else
    echo "Docker Compose version: $(docker-compose --version)"
fi

# Install Maven dependencies
echo "Installing Maven dependencies..."
mvn clean install -DskipTests

# Install Playwright browsers
echo "Installing Playwright browsers..."
mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="install" || {
    echo "Failed to install Playwright browsers using Maven. Trying direct installation..."
    
    # Try direct Playwright installation
    if command -v npx &> /dev/null; then
        npx playwright install
    else
        echo "ERROR: Could not install Playwright browsers. Please install Node.js and npm, then run: npx playwright install"
        exit 1
    fi
}

# Install additional tools if available
if command -v npm &> /dev/null; then
    echo "Installing additional Node.js tools..."
    npm install -g allure-commandline || echo "Failed to install Allure CLI"
    npm install -g newman || echo "Failed to install Newman"
fi

# Create necessary directories
echo "Creating necessary directories..."
mkdir -p test-output allure-results reports screenshots
mkdir -p src/test/resources/wiremock/mappings
mkdir -p src/test/resources/wiremock/__files

# Set up Wiremock stubs if they don't exist
if [ ! -f "src/test/resources/wiremock/mappings/health.json" ]; then
    echo "Creating Wiremock health check stub..."
    cat > src/test/resources/wiremock/mappings/health.json << EOF
{
    "request": {
        "method": "GET",
        "url": "/health"
    },
    "response": {
        "status": 200,
        "headers": {
            "Content-Type": "application/json"
        },
        "jsonBody": {
            "status": "UP",
            "timestamp": "{{now}}"
        }
    }
}
EOF
fi

# Create sample test data
if [ ! -f "src/test/resources/testdata/sample.json" ]; then
    echo "Creating sample test data..."
    cat > src/test/resources/testdata/sample.json << EOF
{
    "testData": {
        "users": [
            {
                "id": 1,
                "name": "Test User",
                "email": "test@example.com"
            }
        ],
        "apiEndpoints": {
            "baseUrl": "http://localhost:8081",
            "healthCheck": "/health",
            "users": "/api/users"
        }
    }
}
EOF
fi

# Set up environment variables
echo "Setting up environment variables..."
if [ ! -f ".env" ]; then
    cat > .env << EOF
# Test Environment Configuration
TEST_ENV=local
BROWSER=chromium
HEADLESS=true
PARALLEL_THREADS=3

# Service URLs
MOCK_API_URL=http://localhost:8081
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
DATABASE_URL=jdbc:h2:mem:testdb

# Reporting
ALLURE_RESULTS_DIR=allure-results
TEST_OUTPUT_DIR=test-output
REPORTS_DIR=reports
EOF
fi

# Make scripts executable
echo "Making scripts executable..."
chmod +x scripts/*.sh

# Verify installation
echo "Verifying installation..."

# Test Maven build
echo "Testing Maven build..."
mvn compile -q

# Test Playwright installation
echo "Testing Playwright installation..."
mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="--version" || {
    echo "WARNING: Playwright CLI test failed"
}

echo "Dependency installation completed successfully!"
echo ""
echo "Next steps:"
echo "1. Update configuration files in the root directory"
echo "2. Run tests locally: ./scripts/run-tests-local.sh"
echo "3. Run tests in Docker: ./scripts/run-tests-docker.sh"
echo "4. Set up Jenkins: ./scripts/setup-jenkins.sh"
echo ""
echo "Configuration files to review:"
echo "- config.properties (main configuration)"
echo "- config-local.properties (local environment)"
echo "- config-staging.properties (staging environment)"
echo "- config-production.properties (production environment)"
echo "- testng.xml (test suite configuration)"


