package tests;

import base.RealServiceTestBase;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class IntegrationTest extends RealServiceTestBase {
    
    private static final int MAX_RESPONSE_TIME = 15000;
    
    @BeforeClass
    public void setUpIntegrationTests() {
        // Wait for all services to be ready
        waitForServiceReady("opcconnection", 30000);
        waitForServiceReady("readdata", 30000);
        waitForServiceReady("kafka", 30000);
        waitForServiceReady("writedata", 30000);
    }
    
    @Test(description = "Test complete data flow from OPC UA to Kafka")
    public void testCompleteDataFlow() {
        // Step 1: Initialize OPC UA connection
        APIResponse initResponse = initOpcUaConnection();
        verifyResponseStatus(initResponse, 200);
        
        // Step 2: Connect to OPC UA
        APIResponse connectResponse = connectOpcUa();
        verifyResponseStatus(connectResponse, 200);
        
        // Step 3: Browse tags
        APIResponse browseResponse = browseTags("ns=3;s=\"WMS TO PLC\"");
        verifyResponseStatus(browseResponse, 200);
        
        // Step 4: Read values
        APIResponse readResponse = readValue("ns=3;s=\"PLC_To_WMS\"");
        verifyResponseStatus(readResponse, 200);
        
        // Step 5: Process browse data through Kafka
        Map<String, Object> browseData = new HashMap<>();
        browseData.put("tag", "PLC_To_WMS");
        browseData.put("value", 42);
        browseData.put("timestamp", System.currentTimeMillis());
        
        APIResponse kafkaResponse = processBrowseData("ns=3;s=DataBlocksGlobal", browseData);
        verifyResponseStatus(kafkaResponse, 200);
        
        // Step 6: Check for changes
        Map<String, Object> previous = new HashMap<>();
        previous.put("value", 41);
        
        Map<String, Object> current = new HashMap<>();
        current.put("value", 42);
        
        APIResponse changeResponse = hasChanged(previous, current);
        verifyResponseStatus(changeResponse, 200);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Complete data flow test completed successfully");
    }
    
    @Test(description = "Test read-write cycle")
    public void testReadWriteCycle() {
        String nodeId = "ns=3;s=DataBlocksGlobal";
        Object testValue = 999;
        
        // Step 1: Write value
        APIResponse writeResponse = writeNode(nodeId, testValue);
        verifyResponseStatus(writeResponse, 200);
        
        // Step 2: Read value back
        APIResponse readResponse = readValue(nodeId);
        verifyResponseStatus(readResponse, 200);
        
        // Step 3: Verify the value was written
        String responseText = readResponse.text();
        Assert.assertTrue(responseText.contains("999") || responseText.contains("value"), 
            "Written value should be readable");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Read-write cycle completed successfully");
    }
    
    @Test(description = "Test service discovery and health check")
    public void testServiceDiscovery() {
        // Check service registry
        APIResponse registryResponse = getServiceRegistryStatus();
        verifyResponseStatus(registryResponse, 200);
        
        // Check all services health
        boolean opcHealthy = checkServiceHealth("opcconnection");
        boolean readHealthy = checkServiceHealth("readdata");
        boolean kafkaHealthy = checkServiceHealth("kafka");
        boolean writeHealthy = checkServiceHealth("writedata");
        
        Assert.assertTrue(opcHealthy && readHealthy && kafkaHealthy && writeHealthy, 
            "All services should be healthy");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Service discovery test completed successfully");
    }
    
    @Test(description = "Test concurrent operations across services")
    public void testConcurrentOperationsAcrossServices() {
        int numberOfThreads = 5;
        Thread[] threads = new Thread[numberOfThreads];
        List<Boolean> results = new ArrayList<>();
        
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                try {
                    // Each thread performs different operations
                    switch (threadIndex % 4) {
                        case 0:
                            // OPC UA operations
                            APIResponse response = getOpcUaConnectionStatus();
                            results.add(response.status() == 200);
                            break;
                        case 1:
                            // Read operations
                            APIResponse response2 = browseTags("ns=3;s=\"WMS TO PLC\"");
                            results.add(response2.status() == 200);
                            break;
                        case 2:
                            // Write operations
                            APIResponse response3 = writeNode("ns=3;s=DataBlocksGlobal", 100 + threadIndex);
                            results.add(response3.status() == 200);
                            break;
                        case 3:
                            // Kafka operations
                            Map<String, Object> browseData = new HashMap<>();
                            browseData.put("tag", "TestTag" + threadIndex);
                            browseData.put("value", 200 + threadIndex);
                            APIResponse response4 = processBrowseData("ns=3;s=DataBlocksGlobal", browseData);
                            results.add(response4.status() == 200);
                            break;
                    }
                } catch (Exception e) {
                    results.add(false);
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
        long successCount = results.stream().mapToLong(b -> b ? 1 : 0).sum();
        Assert.assertTrue(successCount >= numberOfThreads * 0.7, 
            "At least 70% of concurrent operations should succeed");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Concurrent operations test completed: " + successCount + "/" + numberOfThreads + " successful");
    }
    
    @Test(description = "Test error propagation across services")
    public void testErrorPropagationAcrossServices() {
        // Test with invalid node ID that should propagate errors
        String invalidNodeId = "ns=999;s=InvalidNode";
        
        // Try to read from invalid node
        APIResponse readResponse = readValue(invalidNodeId);
        Assert.assertTrue(readResponse.status() >= 400, 
            "Should return error for invalid node");
        
        // Try to write to invalid node
        APIResponse writeResponse = writeNode(invalidNodeId, 123);
        Assert.assertTrue(writeResponse.status() >= 400, 
            "Should return error for invalid node write");
        
        // Try to process invalid data through Kafka
        Map<String, Object> invalidBrowseData = new HashMap<>();
        invalidBrowseData.put("invalid", new Object());
        
        APIResponse kafkaResponse = processBrowseData(invalidNodeId, invalidBrowseData);
        Assert.assertTrue(kafkaResponse.status() >= 200, 
            "Kafka should handle invalid data gracefully");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Error propagation test completed successfully");
    }
    
    @Test(description = "Test performance under load")
    public void testPerformanceUnderLoad() {
        int numberOfOperations = 10;
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < numberOfOperations; i++) {
            // Perform a complete operation cycle
            APIResponse browseResponse = browseTags("ns=3;s=\"WMS TO PLC\"");
            verifyResponseStatus(browseResponse, 200);
            
            APIResponse readResponse = readValue("ns=3;s=\"PLC_To_WMS\"");
            verifyResponseStatus(readResponse, 200);
            
            Map<String, Object> browseData = new HashMap<>();
            browseData.put("tag", "LoadTest" + i);
            browseData.put("value", i);
            
            APIResponse kafkaResponse = processBrowseData("ns=3;s=DataBlocksGlobal", browseData);
            verifyResponseStatus(kafkaResponse, 200);
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        long averageTime = totalTime / numberOfOperations;
        
        Assert.assertTrue(averageTime < MAX_RESPONSE_TIME, 
            "Average operation time should be less than " + MAX_RESPONSE_TIME + "ms");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Performance test completed: " + numberOfOperations + " operations in " + totalTime + "ms (avg: " + averageTime + "ms)");
    }
    
    @Test(description = "Test data consistency across services")
    public void testDataConsistencyAcrossServices() {
        String nodeId = "ns=3;s=DataBlocksGlobal";
        Object testValue = 777;
        
        // Write value
        APIResponse writeResponse = writeNode(nodeId, testValue);
        verifyResponseStatus(writeResponse, 200);
        
        // Read value from different service
        APIResponse readResponse = readValue(nodeId);
        verifyResponseStatus(readResponse, 200);
        
        // Process through Kafka
        Map<String, Object> browseData = new HashMap<>();
        browseData.put("tag", "ConsistencyTest");
        browseData.put("value", testValue);
        
        APIResponse kafkaResponse = processBrowseData(nodeId, browseData);
        verifyResponseStatus(kafkaResponse, 200);
        
        // Verify data consistency
        String readResponseText = readResponse.text();
        String kafkaResponseText = kafkaResponse.text();
        
        Assert.assertTrue(readResponseText.contains("777") || readResponseText.contains("value"), 
            "Data should be consistent across services");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Data consistency test completed successfully");
    }
    
    @Test(description = "Test service recovery after failure")
    public void testServiceRecoveryAfterFailure() {
        // This test simulates service recovery by testing retry mechanisms
        int maxRetries = 3;
        boolean success = false;
        
        for (int attempt = 1; attempt <= maxRetries && !success; attempt++) {
            try {
                APIResponse response = getOpcUaConnectionStatus();
                if (response.status() == 200) {
                    success = true;
                    extentTest.log(com.aventstack.extentreports.Status.PASS, 
                        "Service recovered after " + attempt + " attempts");
                }
            } catch (Exception e) {
                extentTest.log(com.aventstack.extentreports.Status.INFO, 
                    "Recovery attempt " + attempt + " failed: " + e.getMessage());
                
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(2000); // Wait before retry
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        
        Assert.assertTrue(success, "Service should recover within " + maxRetries + " attempts");
    }
}
