# Microservices API Test Suite

This project contains a comprehensive test suite for microservices APIs using Playwright, TestNG, and Spring Boot mock services.

## 🚀 Quick Start

### Prerequisites

1. **Java 11 or higher** - Download from [Adoptium](https://adoptium.net/)
2. **Maven 3.6+** - Included in the project (`apache-maven-3.9.6/`)
3. **Windows PowerShell** - For running batch scripts

### Setup

1. **Run the setup script:**
   ```bash
   setup-environment.bat
   ```

2. **Run all tests:**
   ```bash
   run-all-tests.bat
   ```

## 📋 Test Structure

### Test Classes

- **`ApiTestExecutor`** - Legacy API tests using mock server
- **`OpUaConnectionServiceTest`** - OPC UA connection service tests
- **`ReadDataServiceTest`** - Read data service tests
- **`WriteDataServiceTest`** - Write data service tests
- **`KafkaServiceTest`** - Kafka service tests
- **`IntegrationTest`** - Integration tests across services

### Test Configuration

- **Mock Mode (Default)** - Tests run against mock services
- **Real Mode** - Tests run against real microservices
- **Parallel Execution** - Tests run in parallel for faster execution
- **Comprehensive Reporting** - ExtentReports and Allure integration

## 🔧 Configuration Files

### Test Configuration
- `src/test/resources/test-config-comprehensive.properties` - Main test configuration
- `src/test/resources/test-config.properties` - Fallback configuration
- `src/test/resources/microservices-config.properties` - Service URLs and settings

### TestNG Configuration
- `testng.xml` - Test suite configuration with parallel execution

## 🏃‍♂️ Running Tests

### Option 1: Batch Script (Recommended)
```bash
run-all-tests.bat
```

### Option 2: Maven Commands
```bash
# Compile only
mvn clean compile test-compile -DskipTests

# Run tests with mock services
mvn test -Dtest.mode=mock -Dmock.services.enabled=true

# Run tests with real services
mvn test -Dtest.mode=real -Dreal.services.enabled=true
```

### Option 3: Individual Test Classes
```bash
# Run specific test class
mvn test -Dtest=OpUaConnectionServiceTest

# Run specific test method
mvn test -Dtest=OpUaConnectionServiceTest#testOpcUaConnectionStatus
```

## 📊 Test Reports

### ExtentReports
- **Location:** `test-output/Enhanced_Test_Report_*.html`
- **Features:** Detailed test results, screenshots, performance metrics
- **Access:** Open HTML file in browser

### Allure Reports
- **Location:** `allure-results/`
- **Features:** Interactive test reports with trends and analytics
- **Access:** Use Allure command line tool

### TestNG Reports
- **Location:** `test-output/testng-results.xml`
- **Features:** Standard TestNG XML results
- **Access:** Jenkins integration

## 🐳 Jenkins CI/CD

### Jenkinsfile Features
- **Multi-stage Pipeline** - Clean, compile, test, report
- **Mock Service Testing** - Default test execution
- **Real Service Testing** - Optional real service tests
- **Report Publishing** - HTML reports, artifacts, notifications
- **Email Notifications** - Success/failure notifications

### Jenkins Setup
1. Create new Pipeline job
2. Point to repository with Jenkinsfile
3. Configure Maven and JDK tools
4. Run pipeline

## 🔍 Mock Services

### UnifiedMockServer
- **Port:** 8080
- **Services:** All microservices on single port
- **Endpoints:** Complete API coverage for all services

### Service Endpoints
- **Service Registry:** `/eureka/*`
- **OPC UA Service:** `/opcua/*`
- **Read Data Service:** `/read/*`
- **Kafka Service:** `/kafka/*`
- **Write Data Service:** `/write/*`

## 🛠️ Troubleshooting

### Common Issues

1. **Java not found**
   - Install Java 11+ from Adoptium
   - Set JAVA_HOME environment variable
   - Add %JAVA_HOME%\bin to PATH

2. **Maven not found**
   - Use local Maven: `apache-maven-3.9.6\bin\mvn.cmd`
   - Or install Maven globally

3. **Tests being skipped**
   - Check configuration files
   - Ensure mock services are enabled
   - Verify test dependencies

4. **Port conflicts**
   - Mock server uses port 8080
   - Ensure port is available
   - Check for other running services

### Debug Mode
```bash
# Run with debug output
mvn test -Dtest.mode=mock -X

# Run specific test with debug
mvn test -Dtest=ApiTestExecutor -Dtest.mode=mock -X
```

## 📈 Performance Testing

### Configuration
- **Parallel Threads:** 3 (configurable)
- **Timeout:** 30 seconds per test
- **Retry Count:** 3 attempts
- **Performance Metrics:** Response time, memory usage

### Performance Reports
- Response time tracking
- Memory usage monitoring
- Concurrent execution metrics

## 🔐 Security

### Test Data
- No sensitive data in tests
- Mock responses for all endpoints
- Safe test node IDs and values

### Environment Isolation
- Tests run in isolated environment
- No impact on production services
- Cleanup after test execution

## 📝 Contributing

### Adding New Tests
1. Extend appropriate base class (`RealServiceTestBase` or `ApiTestBase`)
2. Add test methods with `@Test` annotation
3. Use utility methods for assertions
4. Update testng.xml if needed

### Test Best Practices
- Use descriptive test names
- Add proper assertions
- Include performance checks
- Clean up test data
- Use data providers for multiple scenarios

## 📞 Support

### Issues
- Check console output for errors
- Review test reports for failures
- Verify configuration files
- Check service availability

### Logs
- Console output shows test progress
- ExtentReports show detailed results
- Allure reports show trends and analytics

---

## 🎯 Success Criteria

When all tests pass, you should see:
- ✅ All 83 tests executed
- ✅ 0 failures, 0 skips
- ✅ Test reports generated
- ✅ Mock services working correctly
- ✅ Jenkins pipeline successful

The test suite is designed to be robust, comprehensive, and easy to maintain. All tests use mock services by default, ensuring reliable execution regardless of external service availability.

