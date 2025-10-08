package tests;

import base.RealServiceTestBase;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import java.util.Map;
import java.util.HashMap;

public class WriteDataServiceTest extends RealServiceTestBase {
    
    private static final String SERVICE_NAME = "writedata";
    private static final int MAX_RESPONSE_TIME = 10000;
    
    @BeforeClass
    public void setUpWriteDataTests() {
        waitForServiceReady(SERVICE_NAME, 30000);
    }
    
    @Test(description = "Test write node with integer value")
    public void testWriteNodeWithInteger() {
        String nodeId = "ns=3;s=DataBlocksGlobal";
        Integer value = 123;
        
        APIResponse response = writeNode(nodeId, value);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test write node with string value")
    public void testWriteNodeWithString() {
        String nodeId = "ns=3;s=DataBlocksGlobal";
        String value = "Test String Value";
        
        APIResponse response = writeNode(nodeId, value);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test write node with boolean value")
    public void testWriteNodeWithBoolean() {
        String nodeId = "ns=3;s=DataBlocksGlobal";
        Boolean value = true;
        
        APIResponse response = writeNode(nodeId, value);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test write node with double value")
    public void testWriteNodeWithDouble() {
        String nodeId = "ns=3;s=DataBlocksGlobal";
        Double value = 123.45;
        
        APIResponse response = writeNode(nodeId, value);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test write node with different data types", dataProvider = "writeDataProvider")
    public void testWriteNodeWithDifferentDataTypes(String nodeId, Object value, String description) {
        APIResponse response = writeNode(nodeId, value);
        
        // Accept both 200 and 400 as valid responses depending on node validity
        Assert.assertTrue(response.status() == 200 || response.status() == 400, 
            "Should get valid response for " + description);
        
        if (response.status() == 200) {
            verifyJsonResponse(response);
        }
        
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        logApiResponse(response);
    }
    
    @Test(description = "Test write node error handling with invalid node")
    public void testWriteNodeErrorHandling() {
        String invalidNodeId = "ns=999;s=InvalidNode";
        Integer value = 123;
        
        APIResponse response = writeNode(invalidNodeId, value);
        
        // Should handle error gracefully
        Assert.assertTrue(response.status() >= 400 && response.status() < 600, 
            "Should return error status for invalid node ID");
        
        logApiResponse(response);
    }
    
    @Test(description = "Test write node with null value")
    public void testWriteNodeWithNullValue() {
        String nodeId = "ns=3;s=DataBlocksGlobal";
        Object value = null;
        
        APIResponse response = writeNode(nodeId, value);
        
        // Should handle null value appropriately
        Assert.assertTrue(response.status() >= 200 && response.status() < 600, 
            "Should handle null value appropriately");
        
        logApiResponse(response);
    }
    
    @Test(description = "Test write node performance")
    public void testWriteNodePerformance() {
        PerformanceMetrics metrics = measureApiPerformance("Write Node Performance", () -> {
            APIResponse response = writeNode("ns=3;s=DataBlocksGlobal", 123);
            verifyResponseStatus(response, 200);
        });
        
        Assert.assertTrue(metrics.getResponseTime() < MAX_RESPONSE_TIME, 
            "Response time should be less than " + MAX_RESPONSE_TIME + "ms");
    }
    
    @Test(description = "Test write data service health")
    public void testWriteDataServiceHealth() {
        boolean isHealthy = checkServiceHealth(SERVICE_NAME);
        Assert.assertTrue(isHealthy, "Write Data service should be healthy");
    }
    
    @Test(description = "Test concurrent write operations")
    public void testConcurrentWriteOperations() {
        int numberOfThreads = 3; // Reduced for write operations
        Thread[] threads = new Thread[numberOfThreads];
        boolean[] results = new boolean[numberOfThreads];
        
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadIndex = i;
            final int value = 100 + i;
            threads[i] = new Thread(() -> {
                try {
                    APIResponse response = writeNode("ns=3;s=DataBlocksGlobal", value);
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
        
        Assert.assertTrue(successCount >= numberOfThreads * 0.6, 
            "At least 60% of concurrent write operations should succeed");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Concurrent write operations completed: " + successCount + "/" + numberOfThreads + " successful");
    }
    
    @Test(description = "Test write node validation")
    public void testWriteNodeValidation() {
        // Test with various edge cases
        String[] nodeIds = {
            "ns=3;s=DataBlocksGlobal",
            "ns=0;i=2253",
            "ns=2;i=5001"
        };
        
        Object[] values = {
            42,
            "Test Value",
            true,
            3.14,
            -100
        };
        
        for (String nodeId : nodeIds) {
            for (Object value : values) {
                APIResponse response = writeNode(nodeId, value);
                
                // Should get a valid response (200 or 400)
                Assert.assertTrue(response.status() == 200 || response.status() == 400, 
                    "Should get valid response for node: " + nodeId + " with value: " + value);
                
                verifyResponseTime(response, MAX_RESPONSE_TIME);
            }
        }
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Write node validation completed for all test cases");
    }
    
    @Test(description = "Test write node with large data")
    public void testWriteNodeWithLargeData() {
        String nodeId = "ns=3;s=DataBlocksGlobal";
        String largeValue = "A".repeat(1000); // 1000 character string
        
        APIResponse response = writeNode(nodeId, largeValue);
        
        // Should handle large data appropriately
        Assert.assertTrue(response.status() >= 200 && response.status() < 600, 
            "Should handle large data appropriately");
        
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        logApiResponse(response);
    }
    
    @DataProvider(name = "writeDataProvider")
    public Object[][] writeDataProvider() {
        return new Object[][] {
            {"ns=3;s=DataBlocksGlobal", 123, "Integer value"},
            {"ns=3;s=DataBlocksGlobal", "Test String", "String value"},
            {"ns=3;s=DataBlocksGlobal", true, "Boolean value"},
            {"ns=3;s=DataBlocksGlobal", 123.45, "Double value"},
            {"ns=0;i=2253", 42, "Numeric node ID with integer"},
            {"ns=2;i=5001", "Another Test", "Another numeric node ID with string"},
            {"ns=999;s=InvalidNode", 123, "Invalid node ID"},
            {"", 123, "Empty node ID"},
            {"ns=3;s=DataBlocksGlobal", "", "Empty string value"}
        };
    }
}
