# Selenium UI Automation Framework

This is a comprehensive Selenium WebDriver-based UI automation framework for testing the Master Module functionality.

## Project Structure

```
selenium-ui-automation/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── config/
│   │   │       └── ConfigManager.java
│   │   └── resources/
│   │       ├── config.properties
│   │       ├── config-local.properties
│   │       └── config-staging.properties
│   └── test/
│       ├── java/
│       │   ├── base/
│       │   │   └── BaseTest.java
│       │   ├── pages/
│       │   │   ├── HomePage.java
│       │   │   └── MasterProductVariantPage.java
│       │   ├── tests/
│       │   │   └── MasterProductVariantTest.java
│       │   └── utils/
│       │       ├── SeleniumUtils.java
│       │       ├── ExtentReportManager.java
│       │       └── TestListener.java
│       └── resources/
├── pom.xml
├── testng.xml
├── run-tests.bat
├── run-tests.sh
└── README.md
```

## Features

- **Page Object Model**: Clean separation of test logic and page elements
- **Multiple Browser Support**: Chrome, Firefox, Edge
- **Comprehensive Reporting**: ExtentReports with screenshots
- **Configuration Management**: Environment-specific configurations
- **Retry Mechanism**: Built-in retry logic for flaky tests
- **Screenshot Capture**: Automatic screenshots on test failures
- **Data-Driven Testing**: Support for multiple test data sets
- **Parallel Execution**: Configurable parallel test execution

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Chrome, Firefox, or Edge browser
- Internet connection (for WebDriverManager)

## Installation

1. Clone or download the project
2. Navigate to the project directory
3. Install dependencies:
   ```bash
   mvn clean install
   ```

## Configuration

### Environment Configuration

The framework supports multiple environments through configuration files:

- `config.properties` - Default configuration
- `config-local.properties` - Local environment
- `config-staging.properties` - Staging environment

### Key Configuration Properties

```properties
# Application URLs
app.base.url=http://localhost:5173/

# Browser Configuration
browser.name=chrome
browser.headless=false
browser.window.maximize=true

# Test Configuration
test.timeout=30
test.retry.count=2
test.parallel.threads=1

# Screenshot Configuration
screenshot.on.failure=true
screenshot.path=screenshots/
```

## Running Tests

### Using Maven

```bash
# Run all tests
mvn clean test

# Run with specific environment
mvn clean test -Denv=local

# Run specific test class
mvn clean test -Dtest=MasterProductVariantTest

# Run with specific browser
mvn clean test -Dbrowser.name=firefox
```

### Using TestNG XML

```bash
# Run specific test suite
mvn clean test -DsuiteXmlFile=testng.xml
```

### Using Batch/Script Files

**Windows:**
```cmd
run-tests.bat
```

**Linux/Mac:**
```bash
./run-tests.sh
```

## Test Suites

The framework includes multiple test suites:

1. **Master Product Variant Tests** - Complete test suite
2. **Smoke Tests** - Critical functionality tests
3. **Regression Tests** - Full regression test suite

## Test Scenarios

### Master Product Variant Tests

1. **Navigation Test**
   - Navigate to Master Product Variant Details page
   - Verify page elements are visible

2. **Create Button Test**
   - Click Create button
   - Verify form is displayed

3. **Form Validation Tests**
   - Test required field validation
   - Test data format validation
   - Test invalid data handling

4. **Product Creation Test**
   - Fill form with valid data
   - Submit form
   - Verify success/error messages

5. **Complete Workflow Test**
   - End-to-end test from navigation to creation

## Test Data

Test data is configured in `config.properties`:

```properties
master.product.code=6100100
master.product.name=HL ASSY RH MRE
master.trolley.type=NA
master.storage.capacity=6
```

## Reporting

### ExtentReports

- HTML reports generated in `test-output/` directory
- Screenshots attached to failed tests
- Detailed test execution logs
- Test summary and statistics

### Allure Reports

- Generate Allure reports:
  ```bash
  mvn allure:report
  ```

## Screenshots

Screenshots are automatically captured:
- On test failures
- At key test steps
- Stored in `screenshots/` directory

## Troubleshooting

### Common Issues

1. **WebDriver Issues**
   - Ensure browser is installed
   - Check WebDriverManager logs
   - Verify browser version compatibility

2. **Element Not Found**
   - Check if application is running
   - Verify XPath selectors
   - Check page load timing

3. **Test Failures**
   - Check screenshots in `screenshots/` directory
   - Review ExtentReports for detailed logs
   - Verify test data configuration

### Debug Mode

Run tests in debug mode:
```bash
mvn clean test -Ddebug=true
```

## Best Practices

1. **Page Object Model**: Keep page elements and actions in page classes
2. **Wait Strategies**: Use explicit waits instead of thread.sleep()
3. **Test Data**: Use configuration files for test data
4. **Screenshots**: Take screenshots at key test steps
5. **Reporting**: Use descriptive test names and descriptions
6. **Error Handling**: Implement proper exception handling

## Contributing

1. Follow the existing code structure
2. Add proper documentation
3. Include test cases for new features
4. Update configuration files as needed

## Support

For issues and questions:
1. Check the troubleshooting section
2. Review test reports and logs
3. Verify configuration settings
4. Check browser and WebDriver compatibility







