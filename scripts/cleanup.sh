#!/bin/bash

# Cleanup Script
# This script cleans up test artifacts, Docker containers, and temporary files

set -e

echo "Starting cleanup process..."

# Configuration
CLEAN_DOCKER="${CLEAN_DOCKER:-true}"
CLEAN_REPORTS="${CLEAN_REPORTS:-false}"
CLEAN_LOGS="${CLEAN_LOGS:-true}"
CLEAN_TARGET="${CLEAN_TARGET:-true}"

# Function to confirm action
confirm() {
    local prompt="$1"
    local default="${2:-n}"
    
    if [ "$default" = "y" ]; then
        read -p "$prompt [Y/n]: " -r
        REPLY="${REPLY:-y}"
    else
        read -p "$prompt [y/N]: " -r
        REPLY="${REPLY:-n}"
    fi
    
    [[ $REPLY =~ ^[Yy]$ ]]
}

# Clean Maven target directory
if [ "$CLEAN_TARGET" = "true" ]; then
    echo "Cleaning Maven target directory..."
    mvn clean -q || echo "Maven clean failed or target directory doesn't exist"
fi

# Clean test output directories
if [ "$CLEAN_REPORTS" = "true" ]; then
    echo "Cleaning test reports and output..."
    rm -rf test-output allure-results reports screenshots
    echo "Test output directories cleaned"
fi

# Clean logs
if [ "$CLEAN_LOGS" = "true" ]; then
    echo "Cleaning log files..."
    find . -name "*.log" -type f -delete 2>/dev/null || true
    find . -name "*.out" -type f -delete 2>/dev/null || true
    find . -name "hs_err_pid*" -type f -delete 2>/dev/null || true
    echo "Log files cleaned"
fi

# Clean Docker containers and images
if [ "$CLEAN_DOCKER" = "true" ]; then
    echo "Cleaning Docker containers and images..."
    
    # Stop and remove containers
    echo "Stopping and removing containers..."
    docker-compose down -v 2>/dev/null || true
    docker-compose -f docker-compose.ci.yml down -v 2>/dev/null || true
    
    # Remove test-related containers
    docker ps -a --filter "name=playwright-test" --format "{{.Names}}" | xargs -r docker rm -f 2>/dev/null || true
    docker ps -a --filter "name=mock-api" --format "{{.Names}}" | xargs -r docker rm -f 2>/dev/null || true
    docker ps -a --filter "name=test-" --format "{{.Names}}" | xargs -r docker rm -f 2>/dev/null || true
    
    # Remove test-related images
    echo "Removing test-related images..."
    docker images --filter "reference=playwright-framework*" --format "{{.Repository}}:{{.Tag}}" | xargs -r docker rmi -f 2>/dev/null || true
    docker images --filter "reference=*test*" --format "{{.Repository}}:{{.Tag}}" | xargs -r docker rmi -f 2>/dev/null || true
    
    # Clean up dangling images and containers
    echo "Cleaning up dangling resources..."
    docker system prune -f 2>/dev/null || true
    
    # Clean up volumes (be careful with this)
    if confirm "Do you want to remove all unused Docker volumes? (This will delete all data in unused volumes)" "n"; then
        docker volume prune -f 2>/dev/null || true
        echo "Docker volumes cleaned"
    fi
    
    echo "Docker cleanup completed"
fi

# Clean temporary files
echo "Cleaning temporary files..."
find . -name "*.tmp" -type f -delete 2>/dev/null || true
find . -name "*.temp" -type f -delete 2>/dev/null || true
find . -name ".DS_Store" -type f -delete 2>/dev/null || true
find . -name "Thumbs.db" -type f -delete 2>/dev/null || true

# Clean IDE files
echo "Cleaning IDE files..."
find . -name ".idea" -type d -exec rm -rf {} + 2>/dev/null || true
find . -name ".vscode" -type d -exec rm -rf {} + 2>/dev/null || true
find . -name "*.iml" -type f -delete 2>/dev/null || true

# Clean backup files
echo "Cleaning backup files..."
find . -name "*.bak" -type f -delete 2>/dev/null || true
find . -name "*.backup" -type f -delete 2>/dev/null || true
find . -name "*~" -type f -delete 2>/dev/null || true

# Clean Maven local repository cache (optional)
if confirm "Do you want to clean Maven local repository cache? (This will require re-downloading dependencies)" "n"; then
    echo "Cleaning Maven local repository..."
    rm -rf ~/.m2/repository/com/ats 2>/dev/null || true
    echo "Maven local repository cleaned"
fi

# Clean Jenkins workspace (if running in Jenkins)
if [ -n "$WORKSPACE" ] && [ -d "$WORKSPACE" ]; then
    echo "Cleaning Jenkins workspace..."
    find "$WORKSPACE" -name "*.jar" -type f -delete 2>/dev/null || true
    find "$WORKSPACE" -name "*.war" -type f -delete 2>/dev/null || true
    find "$WORKSPACE" -name "target" -type d -exec rm -rf {} + 2>/dev/null || true
    echo "Jenkins workspace cleaned"
fi

# Display disk usage
echo "Current disk usage:"
du -sh . 2>/dev/null || true

echo "Cleanup completed successfully!"
echo ""
echo "Summary of cleaned items:"
echo "- Maven target directory"
if [ "$CLEAN_REPORTS" = "true" ]; then
    echo "- Test reports and output"
fi
if [ "$CLEAN_LOGS" = "true" ]; then
    echo "- Log files"
fi
if [ "$CLEAN_DOCKER" = "true" ]; then
    echo "- Docker containers and images"
fi
echo "- Temporary files"
echo "- IDE files"
echo "- Backup files"


