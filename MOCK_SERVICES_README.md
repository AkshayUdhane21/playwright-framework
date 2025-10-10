# Mock Services Setup for Playwright Framework

## Overview

This document describes how to run the Playwright framework tests using mock services instead of real microservices. This approach eliminates the need for external service dependencies and ensures consistent test results.

## Mock Services Architecture

The framework now includes a comprehensive mock service manager that simulates all required microservices:

### Services Mocked

1. **Service Registry** (Port 8761) - Eureka Server simulation
2. **OPC UA Connection Service** (Port 8081) - OPC UA connectivity management
3. **Read Data Service** (Port 8082) - Data reading operations
4. **Kafka Service** (Port 8083) - Message processing and value conversion
5. **Write Data Service** (Port 8084) - Data writing operations

### Mock Service Manager

The `MockServiceManager` class automatically starts all required mock services on their respective ports and provides realistic API responses for all test scenarios.

## Configuration

### Test Configuration

The test mode is controlled by the `test-config.properties` file:

```properties
# Set to 'mock' for mock server testing, 'real' for real service testing
test.mode=mock

# Mock Server Configuration
mock.server.port=8081
mock.server.enabled=true
mock.services.enabled=true
```

### Service URLs

In mock mode, the framework automatically uses the correct mock service ports:
- Service Registry: `http://localhost:8761`
- OPC UA Service: `http://localhost:8081`
- Read Data Service: `http://localhost:8082`
- Kafka Service: `http://localhost:8083`
- Write Data Service: `http://localhost:8084`

## Running Tests

### Option 1: Using Test Runner Scripts

#### Windows
```bash
run-tests-with-mock-services.bat
```

#### Linux/Unix
```bash
./run-tests-with-mock-services.sh
```

### Option 2: Using Maven Directly

```bash
# Set test mode to mock
mvn test -Dtest.mode=mock -Dmock.services.enabled=true
```

### Option 3: Using Jenkins Pipeline

The Jenkins pipeline has been updated to automatically use mock services:

```groovy
stage('Test') {
    steps {
        echo "Running Playwright tests with Mock Services..."
        bat 'mvn test -Dtest.mode=mock -Dmock.services.enabled=true'
    }
}
```

## Mock API Endpoints

### Service Registry (Port 8761)
- `GET /actuator/health` - Service health check
- `GET /eureka/apps` - Registered services list

### OPC UA Service (Port 8081)
- `GET /api/connection/status` - Connection status
- `GET /api/connection/init` - Initialize connection
- `GET /api/connection/connect` - Establish connection

### Read Data Service (Port 8082)
- `GET /api/read/browse` - Browse tags/nodes
- `GET /api/read/readValue` - Read node values
- `GET /api/read/subscribeToData` - Subscribe to data
- `GET /api/read/read-node` - Read node data
- `GET /api/read/read-node2` - Read node data (alternative)
- `GET /api/read/readTagValuesSimplified` - Read simplified tag values

### Write Data Service (Port 8084)
- `POST /api/write/write-node` - Write node values

### Kafka Service (Port 8083)
- `POST /api/kafkaBrowse/processBrowseData` - Process browse data
- `POST /api/kafkaBrowse/hasChanged` - Check for changes
- `POST /api/opcUaValueConverter/convertValue` - Convert values
- `POST /api/opcUaValueConverter/convertDataValue` - Convert data values

## Test Execution Flow

1. **Setup Phase**: Mock services are automatically started before test execution
2. **Test Execution**: Tests run against mock services with realistic responses
3. **Cleanup Phase**: Mock services are automatically stopped after test completion

## Benefits of Mock Services

1. **No External Dependencies**: Tests run without requiring real microservices
2. **Consistent Results**: Mock responses are predictable and reliable
3. **Faster Execution**: No network latency or service startup delays
4. **Isolated Testing**: Tests are not affected by external service issues
5. **CI/CD Ready**: Perfect for automated pipelines and Jenkins

## Error Handling

The mock services include comprehensive error handling:
- Invalid node IDs return appropriate error responses
- Service health checks always return "UP" status
- All endpoints return realistic JSON responses
- Proper HTTP status codes are maintained

## Troubleshooting

### Common Issues

1. **Port Conflicts**: Ensure ports 8761, 8081-8084 are available
2. **Test Mode**: Verify `test.mode=mock` in configuration
3. **Service Startup**: Check console output for service startup messages

### Debug Mode

To enable debug logging for mock services:

```properties
logging.level.org.springframework=DEBUG
logging.level.utils.MockServiceManager=DEBUG
```

## Migration from Real Services

To switch from real services to mock services:

1. Update `test-config.properties`:
   ```properties
   test.mode=mock
   mock.services.enabled=true
   ```

2. Run tests using the new configuration
3. Verify all tests pass with mock services

## Future Enhancements

- Configurable mock responses
- Service-specific mock behaviors
- Performance testing with mock services
- Integration with service virtualization tools

## Support

For issues or questions regarding mock services setup, please refer to the test logs and ensure all configuration files are properly set up.

