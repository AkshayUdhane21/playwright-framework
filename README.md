# API Automation Framework

A comprehensive API automation framework built with Playwright for Java, focusing on OPC UA, Kafka, and data operations testing.

## Features

- **OPC UA Integration**: Connection management, data reading/writing, node browsing
- **Kafka Services**: Data processing, change detection, message handling
- **API Testing**: REST API testing capabilities with comprehensive coverage
- **Reporting**: ExtentReports integration for detailed HTML reports
- **Data Management**: Excel file reading/writing support
- **Configuration**: Properties-based configuration management
- **Parallel Execution**: TestNG-based parallel test execution

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Project Structure

```
playwright-framework/
├── src/
│   ├── main/java/
│   │   └── utils/
│   │       ├── ConfigLoader.java        # Configuration management
│   │       ├── ExcelReader.java         # Excel file operations
│   │       └── ExtentManager.java       # Reporting management
│   └── test/java/
│       ├── base/
│       │   └── ApiTestBase.java         # Base class for API tests
│       ├── tests/
│       │   └── ApiTestExecutor.java     # API test executor
│       └── utils/
│           ├── TestListener.java        # TestNG listener for reporting
│           └── MockBackendServer.java   # Mock server for testing
├── src/test/resources/
│   └── testdata.xlsx                    # Sample test data file
├── config.properties                    # Configuration file
├── testng.xml                          # TestNG configuration
├── test-output/                         # Generated reports
└── pom.xml                             # Maven dependencies
```

## Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd playwright-framework
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Verify setup**
   ```bash
   mvn clean compile
   ```

## Running Tests

### Run all tests
```bash
mvn test
```

### Run with specific TestNG suite
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run with parallel execution
```bash
mvn test -Dtest.parallel.execution=true
```

## Configuration

Edit `config.properties` to customize:
- API endpoints and timeouts
- Test execution parameters
- Database connections
- Parallel execution settings

## Writing Tests

### API Tests
Extend `ApiTestBase` class for API testing:

```java
public class MyAPITest extends ApiTestBase {
    private APIRequestContext request;
    
    @BeforeClass
    public void setup() {
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("http://localhost:8081"));
    }
    
    @Test
    public void testOpcConnection() {
        APIResponse response = request.get("/api/connection/status");
        Assert.assertEquals(response.status(), 200);
    }
    
    @Test
    public void testKafkaDataProcessing() {
        String payload = "{ \"data\": \"test\" }";
        APIResponse response = request.post("/api/kafkaBrowse/processBrowseData",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(payload));
        Assert.assertEquals(response.status(), 200);
    }
}
```

## Reports

Test reports are generated in the `test-output/` directory:
- `ExtentReport.html` - Detailed HTML report with test results and logs
- Surefire reports for Maven integration
- TestNG reports for comprehensive test analysis

## Utilities

### ConfigLoader
Load configuration properties:
```java
String baseUrl = ConfigLoader.getProperty("api.base.url");
int timeout = ConfigLoader.getIntProperty("api.timeout", 10000);
```

### ExcelReader
Read test data from Excel files:
```java
ExcelReader reader = new ExcelReader("testdata.xlsx");
reader.setSheet("TestData");
List<Map<String, String>> data = reader.getDataAsMap();
```

### ExtentManager
Manage test reporting:
```java
ExtentTest test = ExtentManager.getInstance().createTest("Test Name");
test.log(Status.INFO, "Test step information");
test.log(Status.PASS, "Test passed");
```

## Dependencies

- **Playwright**: API automation and HTTP client
- **TestNG**: Test framework
- **ExtentReports**: HTML reporting
- **Apache POI**: Excel file handling
- **Jackson**: JSON processing
- **Spring Boot**: Mock server and web services

## Troubleshooting

1. **Port conflicts**: Ensure port 8081 is available for the mock server
2. **TestNG configuration**: Ensure `testng.xml` is in the project root
3. **Configuration file**: Ensure `config.properties` exists in the project root
4. **Dependencies**: Run `mvn clean install` to resolve any dependency issues

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request 