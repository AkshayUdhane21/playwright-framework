package utils;

import config.TestConfigManager;
import config.MicroservicesConfig;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class ServiceStartupHelper {
    private static Playwright playwright;
    private static APIRequestContext apiContext;
    
    private static void initializePlaywright() {
        if (playwright == null) {
            playwright = Playwright.create();
            apiContext = playwright.request().newContext();
        }
    }

    public static boolean waitForAllServices() {
        System.out.println("Waiting for all microservices to be ready...");
        
        initializePlaywright();
        
        String[] serviceUrls = {
            MicroservicesConfig.getServiceRegistryUrl(),
            MicroservicesConfig.getOpcUaServiceUrl(),
            MicroservicesConfig.getReadDataServiceUrl(),
            MicroservicesConfig.getKafkaServiceUrl(),
            MicroservicesConfig.getWriteDataServiceUrl()
        };
        
        int timeoutSeconds = TestConfigManager.getRealServicesWaitTimeout();
        int checkIntervalSeconds = TestConfigManager.getRealServicesCheckInterval();
        
        Instant startTime = Instant.now();
        
        while (Duration.between(startTime, Instant.now()).getSeconds() < timeoutSeconds) {
            boolean allServicesReady = true;
            
            for (String serviceUrl : serviceUrls) {
                if (!isServiceReady(serviceUrl)) {
                    allServicesReady = false;
                    break;
                }
            }
            
            if (allServicesReady) {
                System.out.println("✅ All services are ready!");
                return true;
            }
            
            try {
                TimeUnit.SECONDS.sleep(checkIntervalSeconds);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Service startup check interrupted");
                return false;
            }
        }
        
        System.err.println("ERROR: Timeout waiting for services to be ready");
        return false;
    }
    
    private static boolean isServiceReady(String serviceUrl) {
        try {
            initializePlaywright();
            
            // Try health check endpoint first
            String healthUrl = serviceUrl + "/actuator/health";
            APIResponse response = apiContext.get(healthUrl);
            
            if (response.status() == 200) {
                System.out.println("✅ Service ready: " + serviceUrl);
                return true;
            }
            
            // If health check fails, try basic connectivity
            response = apiContext.get(serviceUrl);
            if (response.status() < 500) { // Any response other than server error
                System.out.println("SUCCESS: Service reachable: " + serviceUrl);
                return true;
            }
            
        } catch (Exception e) {
            System.out.println("WAITING: Service not ready: " + serviceUrl + " - " + e.getMessage());
        }
        
        return false;
    }
    
    public static void cleanup() {
        if (apiContext != null) {
            apiContext.dispose();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}