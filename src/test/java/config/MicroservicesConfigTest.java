package config;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MicroservicesConfigTest {
    
    @Test
    public void testServiceRegistryConfiguration() {
        // Test that the methods return values without throwing exceptions
        assertNotNull(MicroservicesConfig.getServiceRegistryUrl());
        assertTrue(MicroservicesConfig.getServiceRegistryPort() > 0);
    }
    
    @Test
    public void testOpcUaConfiguration() {
        // Test OPC UA configuration methods
        assertNotNull(MicroservicesConfig.getOpcUaServiceUrl());
        assertTrue(MicroservicesConfig.getOpcUaServicePort() > 0);
        assertNotNull(MicroservicesConfig.getOpcUaServerUrl());
        assertNotNull(MicroservicesConfig.getOpcUaSecurityPolicy());
        assertTrue(MicroservicesConfig.getOpcUaConnectionTimeout() > 0);
        assertTrue(MicroservicesConfig.getOpcUaMaxReconnectAttempts() >= 0);
        assertTrue(MicroservicesConfig.getOpcUaReconnectDelay() > 0);
    }
    
    @Test
    public void testReadDataConfiguration() {
        assertNotNull(MicroservicesConfig.getReadDataServiceUrl());
        assertTrue(MicroservicesConfig.getReadDataServicePort() > 0);
    }
    
    @Test
    public void testKafkaConfiguration() {
        assertNotNull(MicroservicesConfig.getKafkaServiceUrl());
        assertTrue(MicroservicesConfig.getKafkaServicePort() > 0);
        assertNotNull(MicroservicesConfig.getKafkaBootstrapServers());
        assertNotNull(MicroservicesConfig.getKafkaBrowseTopic());
    }
    
    @Test
    public void testWriteDataConfiguration() {
        assertNotNull(MicroservicesConfig.getWriteDataServiceUrl());
        assertTrue(MicroservicesConfig.getWriteDataServicePort() > 0);
    }
    
    @Test
    public void testTestConfiguration() {
        assertNotNull(MicroservicesConfig.getTestEnvironment());
        assertTrue(MicroservicesConfig.getTestTimeout() > 0);
        assertTrue(MicroservicesConfig.getTestRetryCount() >= 0);
        assertTrue(MicroservicesConfig.getTestParallelThreads() > 0);
    }
    
    @Test
    public void testPerformanceConfiguration() {
        // These should return boolean values without throwing exceptions
        boolean performanceEnabled = MicroservicesConfig.isPerformanceTestEnabled();
        assertTrue(MicroservicesConfig.getPerformanceTestDuration() > 0);
        assertTrue(MicroservicesConfig.getPerformanceTestUsers() > 0);
        assertTrue(MicroservicesConfig.getPerformanceTestRampUp() > 0);
    }
    
    @Test
    public void testValidationConfiguration() {
        // These should return boolean values without throwing exceptions
        boolean schemaValidation = MicroservicesConfig.isSchemaValidationEnabled();
        boolean responseTimeValidation = MicroservicesConfig.isResponseTimeValidationEnabled();
        boolean responseSizeValidation = MicroservicesConfig.isResponseSizeValidationEnabled();
        
        // Just verify they don't throw exceptions - the actual values depend on config
        assertTrue(true); // If we get here, no exceptions were thrown
    }
}

