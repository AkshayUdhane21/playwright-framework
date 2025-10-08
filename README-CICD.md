# CI/CD Setup for Playwright Test Framework

This document provides comprehensive instructions for setting up Continuous Integration and Continuous Deployment (CI/CD) for the Playwright Test Framework using Jenkins.

## 📋 Table of Contents

- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Jenkins Setup](#jenkins-setup)
- [Pipeline Configuration](#pipeline-configuration)
- [Environment Configuration](#environment-configuration)
- [Docker Configuration](#docker-configuration)
- [Running Tests](#running-tests)
- [Monitoring and Reporting](#monitoring-and-reporting)
- [Troubleshooting](#troubleshooting)

## 🚀 Prerequisites

### System Requirements
- **Java 11+** - Required for running the test framework
- **Maven 3.6+** - For building and dependency management
- **Docker & Docker Compose** - For containerized testing
- **Jenkins 2.400+** - For CI/CD pipeline
- **Git** - For version control

### Jenkins Plugins Required
- Pipeline
- Git
- Allure Jenkins Plugin
- HTML Publisher
- Email Extension
- Docker Pipeline
- Build Timeout
- Credentials Binding

## 🏃 Quick Start

### 1. Install Dependencies
```bash
# Run the dependency installation script
./scripts/install-dependencies.sh
```

### 2. Set Up Jenkins
```bash
# Configure Jenkins with required plugins and job
./scripts/setup-jenkins.sh
```

### 3. Run Tests Locally
```bash
# Run tests in local environment
./scripts/run-tests-local.sh

# Or run tests in Docker
./scripts/run-tests-docker.sh
```

## 🔧 Jenkins Setup

### Manual Jenkins Configuration

1. **Install Required Plugins:**
   - Go to Jenkins → Manage Jenkins → Manage Plugins
   - Install the following plugins:
     - Pipeline
     - Git
     - Allure Jenkins Plugin
     - HTML Publisher
     - Email Extension
     - Docker Pipeline
     - Build Timeout
     - Credentials Binding

2. **Configure Global Tools:**
   - Go to Jenkins → Manage Jenkins → Global Tool Configuration
   - Set up JDK 11
   - Set up Maven 3.9.6

3. **Create Pipeline Job:**
   - Create new item → Pipeline
   - Name: `playwright-test-pipeline`
   - Copy content from `jenkins-job-config.xml`

### Automated Setup
```bash
# Set environment variables
export JENKINS_URL="http://localhost:8086"
export JENKINS_USER="admin"
export JENKINS_PASS="admin"

# Run setup script
./scripts/setup-jenkins.sh
```

## 🔄 Pipeline Configuration

### Pipeline Stages

The Jenkins pipeline includes the following stages:

1. **Preparation** - Clean workspace and setup
2. **Checkout** - Get source code from repository
3. **Environment Setup** - Configure environment-specific settings
4. **Dependencies** - Install Playwright browsers
5. **Build** - Compile the application
6. **Code Quality** - Run linting and security scans
7. **Test Execution** - Run Playwright tests
8. **Test Reports** - Generate and archive reports
9. **Package** - Create deployment artifacts
10. **Deploy** - Deploy to target environment

### Pipeline Parameters

| Parameter | Description | Default | Options |
|-----------|-------------|---------|---------|
| `TEST_ENV` | Test environment | local | local, staging, production |
| `BROWSER` | Browser for testing | chromium | chromium, firefox, webkit |
| `HEADLESS` | Run in headless mode | true | true, false |
| `PARALLEL_THREADS` | Number of parallel threads | 3 | 1-10 |
| `SKIP_TESTS` | Skip test execution | false | true, false |
| `CLEAN_WORKSPACE` | Clean workspace before build | true | true, false |

## 🌍 Environment Configuration

### Local Environment (`config-local.properties`)
```properties
api.base.url=http://localhost:8081
test.parallel.execution=true
test.thread.count=3
browser=chromium
headless=true
```

### Staging Environment (`config-staging.properties`)
```properties
api.base.url=http://staging-api:8081
test.parallel.execution=true
test.thread.count=5
browser=chromium
headless=true
```

### Production Environment (`config-production.properties`)
```properties
api.base.url=http://prod-api:8081
test.parallel.execution=true
test.thread.count=3
browser=chromium
headless=true
```

## 🐳 Docker Configuration

### Dockerfile
The `Dockerfile` creates a containerized environment with:
- OpenJDK 11
- Maven 3.9.6
- Playwright browsers (Chrome, Firefox, Safari)
- Xvfb for headless testing
- All necessary dependencies

### Docker Compose (`docker-compose.ci.yml`)
Services included:
- **playwright-tests** - Main test runner
- **mock-api** - WireMock server for API mocking
- **test-database** - PostgreSQL database
- **kafka** - Apache Kafka for message testing
- **allure-server** - Allure report server
- **test-dashboard** - Nginx server for test reports

## 🧪 Running Tests

### Local Execution
```bash
# Basic test execution
./scripts/run-tests-local.sh

# With custom parameters
TEST_ENV=staging BROWSER=firefox HEADLESS=false ./scripts/run-tests-local.sh
```

### Docker Execution
```bash
# Run tests in Docker
./scripts/run-tests-docker.sh

# With custom parameters
TEST_ENV=staging BROWSER=webkit PARALLEL_THREADS=5 ./scripts/run-tests-docker.sh
```

### Maven Execution
```bash
# Run specific test suite
mvn test -Dtest=ApiTestExecutor

# Run with custom configuration
mvn test -Dtest.parallel.execution=true -Dtest.thread.count=5 -Dbrowser=firefox
```

## 📊 Monitoring and Reporting

### Test Reports
- **Extent Reports** - HTML reports in `test-output/ExtentReport.html`
- **Allure Reports** - Interactive reports at `reports/index.html`
- **TestNG Reports** - XML reports in `target/surefire-reports/`

### Jenkins Integration
- **Build History** - Track build success/failure
- **Test Results** - View test execution results
- **Artifacts** - Download test reports and logs
- **Email Notifications** - Get notified of build results

### Monitoring URLs
- **Jenkins Dashboard** - `http://localhost:8086`
- **Test Reports** - `http://localhost:8086/job/playwright-test-pipeline/`
- **Allure Reports** - `http://localhost:5050` (when using Docker)

## 🔧 Troubleshooting

### Common Issues

#### 1. Playwright Browsers Not Installing
```bash
# Manual browser installation
mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="install"
```

#### 2. Docker Permission Issues
```bash
# Add user to docker group
sudo usermod -aG docker $USER
# Logout and login again
```

#### 3. Jenkins Plugin Installation Failed
- Check Jenkins logs: `tail -f /var/log/jenkins/jenkins.log`
- Restart Jenkins: `sudo systemctl restart jenkins`
- Install plugins manually through Jenkins UI

#### 4. Test Execution Timeout
- Increase timeout in `config.properties`
- Reduce parallel threads
- Check service availability

#### 5. Memory Issues
```bash
# Increase Maven memory
export MAVEN_OPTS="-Xmx2048m -XX:MaxPermSize=512m"

# Increase Docker memory
# In Docker Desktop settings, increase memory allocation
```

### Debug Commands

```bash
# Check service status
docker-compose ps

# View container logs
docker-compose logs playwright-tests

# Check Jenkins job status
curl -u admin:admin http://localhost:8086/job/playwright-test-pipeline/lastBuild/api/json

# Test API connectivity
curl http://localhost:8081/health
```

### Log Locations
- **Jenkins Logs** - `/var/log/jenkins/jenkins.log`
- **Test Logs** - `test-output/`
- **Docker Logs** - `docker-compose logs`
- **Maven Logs** - `target/surefire-reports/`

## 📚 Additional Resources

- [Jenkins Pipeline Documentation](https://www.jenkins.io/doc/book/pipeline/)
- [Playwright Documentation](https://playwright.dev/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [TestNG Documentation](https://testng.org/doc/)
- [Allure Reporting](https://docs.qameta.io/allure/)

## 🤝 Support

For issues and questions:
1. Check the troubleshooting section
2. Review Jenkins and Docker logs
3. Check the test output directory for detailed error messages
4. Ensure all prerequisites are installed correctly

## 📝 License

This CI/CD setup is part of the Playwright Test Framework project.
