# Enhanced Playwright Framework for Microservices API Testing

## Overview

This enhanced Playwright framework is designed to test real microservices APIs with comprehensive reporting and advanced testing capabilities. It supports testing of OPC UA, Kafka, Read Data, Write Data, and Service Registry microservices.

## Architecture

### Microservices Tested
- **Service Registry** (Port 8761) - Eureka Server for service discovery
- **OPC UA Connection** (Port 8081) - OPC UA connectivity management
- **Read Data** (Port 8082) - Data reading operations from OPC UA
- **Kafka Service** (Port 8083) - Kafka message processing and value conversion
- **Write Data** (Port 8084) - Data writing operations to OPC UA

### Framework Components
- **Real Service Testing** - Tests against actual running microservices
- **Comprehensive Reporting** - ExtentReports + Allure integration
- **Performance Testing** - Response time and memory usage monitoring
- **Concurrency Testing** - Multi-threaded operation testing
- **Integration Testing** - End-to-end workflow testing
- **Error Handling** - Robust error detection and reporting

## Prerequisites

### Software Requirements
- Java 11 or higher
- Maven 3.6 or higher
- Docker (for TestContainers)
- Kafka (for real service testing)
- OPC UA Server (for real service testing)

### Service Dependencies
- All microservices must be running before executing tests
- OPC UA server must be accessible at `opc.tcp://192.168.103.24/4840`
- Kafka must be running on `localhost:9092`
- Service Registry must be running on `localhost:8761`

## Configuration

### Microservices Configuration
Edit `src/test/resources/microservices-config.properties`:

```properties
# Service URLs
opcua.service.url=http://localhost:8081
readdata.service.url=http://localhost:8082
kafka.service.url=http://localhost:8083
writedata.service.url=http://localhost:8084
service.registry.url=http://localhost:8761

# OPC UA Configuration
opcua.server.url=opc.tcp://192.168.103.24/4840
opcua.security.policy=None
opcua.username=
opcua.password=

# Test Configuration
test.environment=real
test.timeout=30000
test.retry.count=3
test.parallel.threads=5
```

## Running Tests

### Prerequisites Check
Before running tests, ensure all services are running:

```bash
# Check service status
java -cp target/test-classes utils.ServiceStartupHelper

# Or manually check each service
curl http://localhost:8761/actuator/health  # Service Registry
curl http://localhost:8081/actuator/health  # OPC UA Service
curl http://localhost:8082/actuator/health  # Read Data Service
curl http://localhost:8083/actuator/health  # Kafka Service
curl http://localhost:8084/actuator/health  # Write Data Service
```

### Running All Tests
```bash
mvn clean test
```

### Running Specific Test Suites
```bash
# OPC UA Connection Tests
mvn test -Dtest=OpUaConnectionServiceTest

# Read Data Service Tests
mvn test -Dtest=ReadDataServiceTest

# Write Data Service Tests
mvn test -Dtest=WriteDataServiceTest

# Kafka Service Tests
mvn test -Dtest=KafkaServiceTest

# Integration Tests
mvn test -Dtest=IntegrationTest
```

### Running by Test Groups
```bash
# OPC UA related tests
mvn test -Dgroups="opcua,connection"

# Data operations tests
mvn test -Dgroups="data-reading,data-writing"

# Integration tests
mvn test -Dgroups="integration,end-to-end"

# Performance tests
mvn test -Dgroups="performance"
```

## Test Structure

### Test Classes

#### 1. OpUaConnectionServiceTest
Tests OPC UA connection management:
- Connection status checking
- Connection initialization
- Connection establishment
- Error handling and retry mechanisms
- Performance testing

#### 2. ReadDataServiceTest
Tests data reading operations:
- Tag browsing
- Value reading
- Data subscription
- Node reading
- Concurrency testing

#### 3. WriteDataServiceTest
Tests data writing operations:
- Node value writing
- Different data type handling
- Error handling
- Validation testing

#### 4. KafkaServiceTest
Tests Kafka message processing:
- Browse data processing
- Change detection
- Value conversion
- Message validation

#### 5. IntegrationTest
Tests end-to-end workflows:
- Complete data flow
- Read-write cycles
- Service discovery
- Concurrent operations
- Error propagation

### Test Features

#### Real Service Testing
- Tests against actual running microservices
- No mock servers for production-like testing
- Real OPC UA server integration
- Real Kafka message processing

#### Comprehensive Reporting
- **ExtentReports**: Detailed HTML reports with screenshots
- **Allure**: Modern test reporting with trends
- **Performance Metrics**: Response time and memory usage
- **Service Health**: Real-time service status monitoring

#### Advanced Testing Capabilities
- **Concurrency Testing**: Multi-threaded operation testing
- **Performance Testing**: Response time and load testing
- **Error Handling**: Robust error detection and recovery
- **Data Validation**: Schema and content validation
- **Integration Testing**: End-to-end workflow testing

## Reports

### ExtentReports
- Location: `test-output/Enhanced_Test_Report_*.html`
- Features: Dashboard, test details, categories, performance metrics
- Includes: Screenshots, API responses, error details

### Allure Reports
- Generate: `mvn allure:report`
- Location: `target/site/allure-maven-plugin/`
- Features: Trends, categories, detailed test information

## Performance Testing

### Response Time Monitoring
- Automatic response time measurement
- Configurable timeout thresholds
- Performance regression detection

### Memory Usage Monitoring
- Memory consumption tracking
- Memory leak detection
- Resource usage optimization

### Load Testing
- Concurrent operation testing
- Stress testing capabilities
- Scalability validation

## Error Handling

### Service Health Monitoring
- Real-time service status checking
- Automatic retry mechanisms
- Graceful degradation handling

### Error Recovery
- Automatic retry on failures
- Circuit breaker patterns
- Fallback mechanisms

## Best Practices

### Test Design
1. **Independent Tests**: Each test should be independent
2. **Data Cleanup**: Clean up test data after execution
3. **Error Handling**: Test both success and failure scenarios
4. **Performance**: Monitor response times and resource usage

### Service Management
1. **Health Checks**: Always verify service health before testing
2. **Retry Logic**: Implement retry mechanisms for flaky operations
3. **Timeout Handling**: Use appropriate timeouts for different operations
4. **Resource Management**: Properly dispose of resources

### Reporting
1. **Detailed Logging**: Log important test information
2. **Screenshot Capture**: Capture screenshots for failures
3. **Performance Metrics**: Record and analyze performance data
4. **Error Details**: Include detailed error information

## Troubleshooting

### Common Issues

#### Services Not Starting
```bash
# Check service logs
docker logs <service-container>

# Verify port availability
netstat -an | grep :8081
netstat -an | grep :8082
netstat -an | grep :8083
netstat -an | grep :8084
netstat -an | grep :8761
```

#### OPC UA Connection Issues
- Verify OPC UA server is running
- Check network connectivity
- Validate security policy settings
- Check authentication credentials

#### Kafka Issues
- Verify Kafka is running
- Check topic configuration
- Validate message serialization
- Check consumer group settings

#### Test Failures
- Check service health status
- Verify test data setup
- Review error logs
- Check network connectivity

### Debug Mode
Enable debug logging by setting:
```properties
logging.level=DEBUG
```

## Contributing

### Adding New Tests
1. Create test class extending `RealServiceTestBase`
2. Add appropriate test groups
3. Include comprehensive error handling
4. Add performance monitoring
5. Update test suite configuration

### Adding New Services
1. Update `MicroservicesConfig` class
2. Add service configuration properties
3. Create service-specific test client
4. Add health check endpoints
5. Update integration tests

## Support

For issues and questions:
1. Check the troubleshooting section
2. Review test logs and reports
3. Verify service configurations
4. Check network connectivity
5. Contact the development team

## License

This framework is part of the ATS Playwright Testing Suite.
