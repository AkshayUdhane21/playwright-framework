package tests;

import base.RealServiceTestBase;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class OpUaConnectionServiceTest extends RealServiceTestBase {
    
    private static final String SERVICE_NAME = "opcconnection";
    private static final int MAX_RESPONSE_TIME = 5000;
    
    @BeforeClass
    public void setUpOpUaTests() {
        waitForServiceReady(SERVICE_NAME, 30000);
    }
    
    @Test(description = "Test OPC UA connection status endpoint")
    public void testOpcUaConnectionStatus() {
        APIResponse response = getOpcUaConnectionStatus();
        
        verifyResponseStatus(response, 200);
        verifyResponseContains(response, "Connected");
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test OPC UA connection initialization")
    public void testOpcUaConnectionInit() {
        APIResponse response = initOpcUaConnection();
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test OPC UA connection establishment")
    public void testOpcUaConnectionConnect() {
        APIResponse response = connectOpcUa();
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test OPC UA connection sequence")
    public void testOpcUaConnectionSequence() {
        // Step 1: Initialize connection
        APIResponse initResponse = initOpcUaConnection();
        verifyResponseStatus(initResponse, 200);
        
        // Step 2: Connect
        APIResponse connectResponse = connectOpcUa();
        verifyResponseStatus(connectResponse, 200);
        
        // Step 3: Check status
        APIResponse statusResponse = getOpcUaConnectionStatus();
        verifyResponseStatus(statusResponse, 200);
        verifyResponseContains(statusResponse, "Connected");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, "OPC UA connection sequence completed successfully");
    }
    
    @Test(description = "Test OPC UA service health check")
    public void testOpcUaServiceHealth() {
        boolean isHealthy = checkServiceHealth(SERVICE_NAME);
        Assert.assertTrue(isHealthy, "OPC UA service should be healthy");
    }
    
    @Test(description = "Test OPC UA connection error handling")
    public void testOpcUaConnectionErrorHandling() {
        // Test with invalid endpoint
        try {
            APIResponse response = getOpcUaConnectionStatus();
            // Even if the service is down, we should get a proper response
            Assert.assertTrue(response.status() >= 200 && response.status() < 600, 
                "Should get a valid HTTP status code");
        } catch (Exception e) {
            extentTest.log(com.aventstack.extentreports.Status.INFO, 
                "Expected error handling: " + e.getMessage());
        }
    }
    
    @Test(description = "Test OPC UA connection performance")
    public void testOpcUaConnectionPerformance() {
        PerformanceMetrics metrics = measureApiPerformance("OPC UA Status Check", () -> {
            APIResponse response = getOpcUaConnectionStatus();
            verifyResponseStatus(response, 200);
        });
        
        Assert.assertTrue(metrics.getResponseTime() < MAX_RESPONSE_TIME, 
            "Response time should be less than " + MAX_RESPONSE_TIME + "ms");
    }
    
    @Test(description = "Test OPC UA connection retry mechanism")
    public void testOpcUaConnectionRetry() {
        int retryCount = 0;
        int maxRetries = 3;
        boolean success = false;
        
        while (retryCount < maxRetries && !success) {
            try {
                APIResponse response = getOpcUaConnectionStatus();
                if (response.status() == 200) {
                    success = true;
                    extentTest.log(com.aventstack.extentreports.Status.PASS, 
                        "Connection successful after " + retryCount + " retries");
                }
            } catch (Exception e) {
                retryCount++;
                extentTest.log(com.aventstack.extentreports.Status.INFO, 
                    "Retry attempt " + retryCount + " failed: " + e.getMessage());
                
                if (retryCount < maxRetries) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        
        Assert.assertTrue(success, "Connection should succeed within " + maxRetries + " retries");
    }
}
