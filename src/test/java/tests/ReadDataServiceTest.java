package tests;

import base.RealServiceTestBase;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import java.util.Map;
import java.util.HashMap;

public class ReadDataServiceTest extends RealServiceTestBase {
    
    private static final String SERVICE_NAME = "readdata";
    private static final int MAX_RESPONSE_TIME = 10000;
    
    @BeforeClass
    public void setUpReadDataTests() {
        waitForServiceReady(SERVICE_NAME, 30000);
    }
    
    @Test(description = "Test browse tags endpoint")
    public void testBrowseTags() {
        APIResponse response = browseTags("ns=3;s=\"WMS TO PLC\"");
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test browse tags with default node")
    public void testBrowseTagsWithDefaultNode() {
        APIResponse response = browseTags(null);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test read value endpoint")
    public void testReadValue() {
        String nodeId = "ns=3;s=\"PLC_To_WMS\"";
        APIResponse response = readValue(nodeId);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test read value with different node IDs", dataProvider = "nodeIdProvider")
    public void testReadValueWithDifferentNodes(String nodeId, String description) {
        APIResponse response = readValue(nodeId);
        
        // Accept both 200 and 404 as valid responses for different node IDs
        Assert.assertTrue(response.status() == 200 || response.status() == 404, 
            "Should get valid response for node: " + nodeId);
        
        if (response.status() == 200) {
            verifyJsonResponse(response);
        }
        
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        logApiResponse(response);
    }
    
    @Test(description = "Test subscribe to data endpoint")
    public void testSubscribeToData() {
        APIResponse response = subscribeToData();
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test read node endpoint")
    public void testReadNode() {
        String nodeId = "ns=3;s=DataBlocksGlobal";
        APIResponse response = readNode(nodeId);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test read node2 endpoint")
    public void testReadNode2() {
        String nodeId = "MCOM";
        APIResponse response = readNode2(nodeId);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test read tag values simplified")
    public void testReadTagValuesSimplified() {
        String startingNode = "ns=3;s=\"WMS TO PLC\"";
        APIResponse response = readTagValuesSimplified(startingNode);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test read data service health")
    public void testReadDataServiceHealth() {
        boolean isHealthy = checkServiceHealth(SERVICE_NAME);
        Assert.assertTrue(isHealthy, "Read Data service should be healthy");
    }
    
    @Test(description = "Test read data performance under load")
    public void testReadDataPerformance() {
        PerformanceMetrics metrics = measureApiPerformance("Read Data Performance", () -> {
            APIResponse response = browseTags("ns=3;s=\"WMS TO PLC\"");
            verifyResponseStatus(response, 200);
        });
        
        Assert.assertTrue(metrics.getResponseTime() < MAX_RESPONSE_TIME, 
            "Response time should be less than " + MAX_RESPONSE_TIME + "ms");
    }
    
    @Test(description = "Test read data error handling")
    public void testReadDataErrorHandling() {
        // Test with invalid node ID
        String invalidNodeId = "ns=999;s=InvalidNode";
        APIResponse response = readValue(invalidNodeId);
        
        // Should handle error gracefully
        Assert.assertTrue(response.status() >= 400 && response.status() < 600, 
            "Should return error status for invalid node ID");
        
        logApiResponse(response);
    }
    
    @Test(description = "Test concurrent read operations")
    public void testConcurrentReadOperations() {
        int numberOfThreads = 5;
        Thread[] threads = new Thread[numberOfThreads];
        boolean[] results = new boolean[numberOfThreads];
        
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                try {
                    APIResponse response = browseTags("ns=3;s=\"WMS TO PLC\"");
                    results[threadIndex] = response.status() == 200;
                } catch (Exception e) {
                    results[threadIndex] = false;
                }
            });
            threads[i].start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Check results
        int successCount = 0;
        for (boolean result : results) {
            if (result) successCount++;
        }
        
        Assert.assertTrue(successCount >= numberOfThreads * 0.8, 
            "At least 80% of concurrent operations should succeed");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Concurrent read operations completed: " + successCount + "/" + numberOfThreads + " successful");
    }
    
    @DataProvider(name = "nodeIdProvider")
    public Object[][] nodeIdProvider() {
        return new Object[][] {
            {"ns=3;s=\"PLC_To_WMS\"", "PLC to WMS communication node"},
            {"ns=3;s=\"WMS_To_PLC\"", "WMS to PLC communication node"},
            {"ns=3;s=DataBlocksGlobal", "Global data blocks node"},
            {"ns=0;i=2253", "Numeric node ID"},
            {"ns=2;i=5001", "Another numeric node ID"},
            {"MCOM", "Communication node"}
        };
    }
}
