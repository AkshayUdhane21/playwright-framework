package base;

import com.microsoft.playwright.*;
import utils.MockBackendServer;
import config.TestConfigManager;
import utils.ServiceStartupHelper;

public class ApiTestBase {
    protected Playwright playwright;
    protected APIRequestContext apiContext;
    protected static MockBackendServer mockServer;

    @org.testng.annotations.BeforeSuite
    public void setUpApiContext() {
        try {
            playwright = Playwright.create();
            apiContext = playwright.request().newContext();
            
            if (TestConfigManager.isMockMode()) {
                // Start mock backend server using singleton
                mockServer = MockBackendServer.getInstance();
                mockServer.start();
                
                // Small delay to ensure server is ready
                Thread.sleep(2000);
                System.out.println("API Context initialized with mock server on port " + TestConfigManager.getMockServerPort());
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
            MockBackendServer.stopInstance();
            System.out.println("Mock server stopped!");
        }
        System.out.println("API Context closed!");
    }
}
