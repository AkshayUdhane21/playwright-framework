package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MicroservicesConfig {
    private static Properties config;
    
    static {
        config = new Properties();
        try (InputStream input = MicroservicesConfig.class.getClassLoader().getResourceAsStream("microservices-config.properties")) {
            if (input != null) {
                config.load(input);
            } else {
                System.err.println("Warning: microservices-config.properties not found in resources. Using default values.");
            }
        } catch (IOException e) {
            System.err.println("Warning: Error loading microservices-config.properties: " + e.getMessage() + ". Using default values.");
        }
    }
    
    // Helper method to safely parse integer properties
    private static int getIntProperty(String key, int defaultValue) {
        try {
            String value = config.getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            System.err.println("Warning: Invalid integer value for key '" + key + "', using default: " + defaultValue);
            return defaultValue;
        }
    }
    
    // Helper method to safely parse boolean properties
    private static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = config.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    // Service Registry Configuration
    public static String getServiceRegistryUrl() {
        // Check if we're in mock mode and use unified mock server
        if (TestConfigManager.isMockMode()) {
            return "http://localhost:8085/eureka";
        }
        return config.getProperty("service.registry.url", "http://localhost:8761");
    }
    
    public static int getServiceRegistryPort() {
        return getIntProperty("service.registry.port", 8761);
    }
    
    // OPC UA Service Configuration
    public static String getOpcUaServiceUrl() {
        // Check if we're in mock mode and use unified mock server
        if (TestConfigManager.isMockMode()) {
            return "http://localhost:8085/opcua";
        }
        return config.getProperty("opcua.service.url", "http://localhost:8081");
    }
    
    public static int getOpcUaServicePort() {
        return getIntProperty("opcua.service.port", 8081);
    }
    
    public static String getOpcUaServerUrl() {
        return config.getProperty("opcua.server.url", "opc.tcp://192.168.103.24/4840");
    }
    
    public static String getOpcUaSecurityPolicy() {
        return config.getProperty("opcua.security.policy", "None");
    }
    
    public static String getOpcUaUsername() {
        return config.getProperty("opcua.username", "");
    }
    
    public static String getOpcUaPassword() {
        return config.getProperty("opcua.password", "");
    }
    
    public static int getOpcUaConnectionTimeout() {
        return getIntProperty("opcua.connection.timeout", 5000);
    }
    
    public static int getOpcUaMaxReconnectAttempts() {
        return getIntProperty("opcua.max.reconnect.attempts", 3);
    }
    
    public static int getOpcUaReconnectDelay() {
        return getIntProperty("opcua.reconnect.delay", 5000);
    }
    
    // Read Data Service Configuration
    public static String getReadDataServiceUrl() {
        // Check if we're in mock mode and use unified mock server
        if (TestConfigManager.isMockMode()) {
            return "http://localhost:8085/read";
        }
        return config.getProperty("readdata.service.url", "http://localhost:8082");
    }
    
    public static int getReadDataServicePort() {
        return getIntProperty("readdata.service.port", 8082);
    }
    
    // Kafka Service Configuration
    public static String getKafkaServiceUrl() {
        // Check if we're in mock mode and use unified mock server
        if (TestConfigManager.isMockMode()) {
            return "http://localhost:8085/kafka";
        }
        return config.getProperty("kafka.service.url", "http://localhost:8083");
    }
    
    public static int getKafkaServicePort() {
        return getIntProperty("kafka.service.port", 8083);
    }
    
    public static String getKafkaBootstrapServers() {
        return config.getProperty("kafka.bootstrap.servers", "localhost:9092");
    }
    
    public static String getKafkaBrowseTopic() {
        return config.getProperty("kafka.browse.topic", "plc-browse-updates");
    }
    
    // Write Data Service Configuration
    public static String getWriteDataServiceUrl() {
        // Check if we're in mock mode and use unified mock server
        if (TestConfigManager.isMockMode()) {
            return "http://localhost:8085/write";
        }
        return config.getProperty("writedata.service.url", "http://localhost:8084");
    }
    
    public static int getWriteDataServicePort() {
        return getIntProperty("writedata.service.port", 8084);
    }
    
    // Test Configuration
    public static String getTestEnvironment() {
        return config.getProperty("test.environment", "real");
    }
    
    public static int getTestTimeout() {
        return getIntProperty("test.timeout", 30000);
    }
    
    public static int getTestRetryCount() {
        return getIntProperty("test.retry.count", 3);
    }
    
    public static int getTestParallelThreads() {
        return getIntProperty("test.parallel.threads", 5);
    }
    
    // Performance Testing Configuration
    public static boolean isPerformanceTestEnabled() {
        return getBooleanProperty("performance.test.enabled", true);
    }
    
    public static int getPerformanceTestDuration() {
        return getIntProperty("performance.test.duration", 300);
    }
    
    public static int getPerformanceTestUsers() {
        return getIntProperty("performance.test.users", 10);
    }
    
    public static int getPerformanceTestRampUp() {
        return getIntProperty("performance.test.ramp.up", 60);
    }
    
    // Validation Configuration
    public static boolean isSchemaValidationEnabled() {
        return getBooleanProperty("validation.schema.enabled", true);
    }
    
    public static boolean isResponseTimeValidationEnabled() {
        return getBooleanProperty("validation.response.time.enabled", true);
    }
    
    public static boolean isResponseSizeValidationEnabled() {
        return getBooleanProperty("validation.response.size.enabled", true);
    }
}
