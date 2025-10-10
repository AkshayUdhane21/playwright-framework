package base;

import com.microsoft.playwright.*;
import utils.UnifiedMockServer;
import config.TestConfigManager;
import utils.ServiceStartupHelper;

public class ApiTestBase {
    protected Playwright playwright;
    protected APIRequestContext apiContext;
    protected static UnifiedMockServer mockServer;

    @org.testng.annotations.BeforeSuite
    public void setUpApiContext() {
        try {
            playwright = Playwright.create();
            apiContext = playwright.request().newContext();
            
            if (TestConfigManager.isMockMode()) {
                // Start unified mock server using singleton
                mockServer = UnifiedMockServer.getInstance();
                mockServer.start();
                
                // Small delay to ensure server is ready
                Thread.sleep(2000);
                System.out.println("API Context initialized with unified mock server on port " + UnifiedMockServer.getPort());
            } else {
                // Wait for real services to be ready
                System.out.println("API Context initialized for real services testing");
                if (TestConfigManager.isRealServicesEnabled()) {
                    boolean servicesReady = ServiceStartupHelper.waitForAllServices();
                    if (!servicesReady) {
                        System.err.println("Warning: Some real services may not be ready. Tests may fail.");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to setup API test context: " + e.getMessage(), e);
        }
    }

    @org.testng.annotations.AfterSuite
    public void tearDownApiContext() {
        if (apiContext != null) {
            apiContext.dispose();
        }
        if (playwright != null) {
            playwright.close();
        }
        if (TestConfigManager.isMockMode() && mockServer != null) {
            UnifiedMockServer.stopInstance();
            System.out.println("Unified mock server stopped!");
        }
        System.out.println("API Context closed!");
    }
}
