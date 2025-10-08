package utils;

import config.MicroservicesConfig;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.APIResponse;

import java.util.concurrent.TimeUnit;

public class ServiceStartupHelper {
    private static final int MAX_WAIT_TIME = 30; // 30 seconds
    private static final int CHECK_INTERVAL = 2; // 2 seconds
    
    public static boolean waitForAllServices() {
        System.out.println("Waiting for all microservices to be ready...");
        
        String[] services = {
            "ServiceRegistry", "opcconnection", "readdata", "kafka", "writedata"
        };
        
        boolean allServicesReady = true;
        
        for (String service : services) {
            if (!waitForService(service)) {
                allServicesReady = false;
                System.err.println("Service " + service + " failed to start within " + MAX_WAIT_TIME + " seconds");
            }
        }
        
        if (allServicesReady) {
            System.out.println("All services are ready!");
        } else {
            System.err.println("Some services failed to start. Please check the service logs.");
        }
        
        return allServicesReady;
    }
    
    public static boolean waitForService(String serviceName) {
        System.out.println("Waiting for service: " + serviceName);
        
        long startTime = System.currentTimeMillis();
        long maxWaitTime = TimeUnit.SECONDS.toMillis(MAX_WAIT_TIME);
        
        while (System.currentTimeMillis() - startTime < maxWaitTime) {
            if (isServiceReady(serviceName)) {
                long duration = System.currentTimeMillis() - startTime;
                System.out.println("Service " + serviceName + " is ready! (took " + duration + "ms)");
                return true;
            }
            
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(CHECK_INTERVAL));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        
        return false;
    }
    
    public static boolean isServiceReady(String serviceName) {
        try (Playwright playwright = Playwright.create()) {
            APIRequestContext context = playwright.request().newContext();
            
            String healthUrl = getServiceHealthUrl(serviceName);
            APIResponse response = context.get(healthUrl);
            
            return response.status() == 200;
        } catch (Exception e) {
            return false;
        }
    }
    
    private static String getServiceHealthUrl(String serviceName) {
        switch (serviceName.toLowerCase()) {
            case "serviceregistry":
                return MicroservicesConfig.getServiceRegistryUrl() + "/actuator/health";
            case "opcconnection":
                return MicroservicesConfig.getOpcUaServiceUrl() + "/actuator/health";
            case "readdata":
                return MicroservicesConfig.getReadDataServiceUrl() + "/actuator/health";
            case "kafka":
                return MicroservicesConfig.getKafkaServiceUrl() + "/actuator/health";
            case "writedata":
                return MicroservicesConfig.getWriteDataServiceUrl() + "/actuator/health";
            default:
                throw new IllegalArgumentException("Unknown service: " + serviceName);
        }
    }
    
    public static void printServiceStatus() {
        System.out.println("\n=== Microservices Status ===");
        
        String[] services = {
            "ServiceRegistry", "opcconnection", "readdata", "kafka", "writedata"
        };
        
        for (String service : services) {
            boolean isReady = isServiceReady(service);
            String status = isReady ? "UP" : "DOWN";
            String url = getServiceUrl(service);
            
            System.out.println(service + ": " + status + " (" + url + ")");
        }
        
        System.out.println("===========================\n");
    }
    
    private static String getServiceUrl(String serviceName) {
        switch (serviceName.toLowerCase()) {
            case "serviceregistry":
                return MicroservicesConfig.getServiceRegistryUrl();
            case "opcconnection":
                return MicroservicesConfig.getOpcUaServiceUrl();
            case "readdata":
                return MicroservicesConfig.getReadDataServiceUrl();
            case "kafka":
                return MicroservicesConfig.getKafkaServiceUrl();
            case "writedata":
                return MicroservicesConfig.getWriteDataServiceUrl();
            default:
                return "Unknown";
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Microservices Startup Helper");
        System.out.println("============================");
        
        printServiceStatus();
        
        if (waitForAllServices()) {
            System.out.println("All services are ready for testing!");
            System.exit(0);
        } else {
            System.err.println("Some services are not ready. Please check the logs.");
            System.exit(1);
        }
    }
}
