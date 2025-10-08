package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfigManager {
    private static Properties config;
    
    static {
        config = new Properties();
        try (InputStream input = TestConfigManager.class.getClassLoader().getResourceAsStream("test-config.properties")) {
            if (input != null) {
                config.load(input);
            } else {
                System.err.println("Warning: test-config.properties not found in resources. Using default values.");
            }
        } catch (IOException e) {
            System.err.println("Warning: Error loading test-config.properties: " + e.getMessage() + ". Using default values.");
        }
    }
    
    public static String getTestMode() {
        return config.getProperty("test.mode", "real");
    }
    
    public static boolean isMockMode() {
        return "mock".equalsIgnoreCase(getTestMode());
    }
    
    public static boolean isRealMode() {
        return "real".equalsIgnoreCase(getTestMode());
    }
    
    public static int getMockServerPort() {
        return Integer.parseInt(config.getProperty("mock.server.port", "8081"));
    }
    
    public static boolean isMockServerEnabled() {
        return Boolean.parseBoolean(config.getProperty("mock.server.enabled", "true"));
    }
    
    public static boolean isRealServicesEnabled() {
        return Boolean.parseBoolean(config.getProperty("real.services.enabled", "true"));
    }
    
    public static int getRealServicesWaitTimeout() {
        return Integer.parseInt(config.getProperty("real.services.wait.timeout", "300"));
    }
    
    public static int getRealServicesCheckInterval() {
        return Integer.parseInt(config.getProperty("real.services.check.interval", "5"));
    }
    
    public static boolean isHealthCheckEnabled() {
        return Boolean.parseBoolean(config.getProperty("health.check.enabled", "true"));
    }
    
    public static int getHealthCheckTimeout() {
        return Integer.parseInt(config.getProperty("health.check.timeout", "30"));
    }
    
    public static int getHealthCheckRetryCount() {
        return Integer.parseInt(config.getProperty("health.check.retry.count", "3"));
    }
    
    public static boolean isTestParallelEnabled() {
        return Boolean.parseBoolean(config.getProperty("test.parallel.enabled", "true"));
    }
    
    public static int getTestParallelThreads() {
        return Integer.parseInt(config.getProperty("test.parallel.threads", "3"));
    }
    
    public static int getTestTimeout() {
        return Integer.parseInt(config.getProperty("test.timeout", "30000"));
    }
    
    public static int getTestRetryCount() {
        return Integer.parseInt(config.getProperty("test.retry.count", "3"));
    }
    
    public static boolean isConsoleReportEnabled() {
        return Boolean.parseBoolean(config.getProperty("report.console.enabled", "true"));
    }
    
    public static boolean isExtentReportEnabled() {
        return Boolean.parseBoolean(config.getProperty("report.extent.enabled", "true"));
    }
    
    public static boolean isAllureReportEnabled() {
        return Boolean.parseBoolean(config.getProperty("report.allure.enabled", "true"));
    }
}

