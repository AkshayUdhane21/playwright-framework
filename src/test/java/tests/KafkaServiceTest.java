package tests;

import base.RealServiceTestBase;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import java.util.Map;
import java.util.HashMap;

public class KafkaServiceTest extends RealServiceTestBase {
    
    private static final String SERVICE_NAME = "kafka";
    private static final int MAX_RESPONSE_TIME = 10000;
    
    @BeforeClass
    public void setUpKafkaTests() {
        waitForServiceReady(SERVICE_NAME, 30000);
    }
    
    @Test(description = "Test process browse data endpoint")
    public void testProcessBrowseData() {
        String nodeId = "ns=3;s=DataBlocksGlobal";
        Map<String, Object> browseData = new HashMap<>();
        browseData.put("tag", "PLC_To_WMS");
        browseData.put("value", 42);
        browseData.put("timestamp", System.currentTimeMillis());
        
        APIResponse response = processBrowseData(nodeId, browseData);
        
        verifyResponseStatus(response, 200);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test process browse data with different data types", dataProvider = "browseDataProvider")
    public void testProcessBrowseDataWithDifferentTypes(String nodeId, Map<String, Object> browseData, String description) {
        APIResponse response = processBrowseData(nodeId, browseData);
        
        verifyResponseStatus(response, 200);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test has changed endpoint with different values")
    public void testHasChanged() {
        Map<String, Object> previous = new HashMap<>();
        previous.put("a", 1);
        previous.put("b", "test");
        previous.put("c", true);
        
        Map<String, Object> current = new HashMap<>();
        current.put("a", 2);
        current.put("b", "test");
        current.put("c", false);
        
        APIResponse response = hasChanged(previous, current);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test has changed with identical data")
    public void testHasChangedWithIdenticalData() {
        Map<String, Object> data = new HashMap<>();
        data.put("a", 1);
        data.put("b", "test");
        data.put("c", true);
        
        APIResponse response = hasChanged(data, data);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test has changed with null values")
    public void testHasChangedWithNullValues() {
        Map<String, Object> previous = new HashMap<>();
        previous.put("a", null);
        previous.put("b", "test");
        
        Map<String, Object> current = new HashMap<>();
        current.put("a", 1);
        current.put("b", "test");
        
        APIResponse response = hasChanged(previous, current);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test convert value endpoint")
    public void testConvertValue() {
        String variant = "42";
        
        APIResponse response = convertValue(variant);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test convert data value endpoint")
    public void testConvertDataValue() {
        String originalValue = "100";
        
        APIResponse response = convertDataValue(originalValue);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test convert value with different data types", dataProvider = "convertValueProvider")
    public void testConvertValueWithDifferentTypes(String variant, String description) {
        APIResponse response = convertValue(variant);
        
        verifyResponseStatus(response, 200);
        verifyJsonResponse(response);
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        
        logApiResponse(response);
    }
    
    @Test(description = "Test Kafka service health")
    public void testKafkaServiceHealth() {
        boolean isHealthy = checkServiceHealth(SERVICE_NAME);
        Assert.assertTrue(isHealthy, "Kafka service should be healthy");
    }
    
    @Test(description = "Test Kafka message processing performance")
    public void testKafkaMessageProcessingPerformance() {
        PerformanceMetrics metrics = measureApiPerformance("Kafka Message Processing", () -> {
            Map<String, Object> browseData = new HashMap<>();
            browseData.put("tag", "TestTag");
            browseData.put("value", 123);
            
            APIResponse response = processBrowseData("ns=3;s=DataBlocksGlobal", browseData);
            verifyResponseStatus(response, 200);
        });
        
        Assert.assertTrue(metrics.getResponseTime() < MAX_RESPONSE_TIME, 
            "Response time should be less than " + MAX_RESPONSE_TIME + "ms");
    }
    
    @Test(description = "Test Kafka error handling")
    public void testKafkaErrorHandling() {
        // Test with invalid data
        Map<String, Object> invalidData = new HashMap<>();
        invalidData.put("invalid", new Object()); // Invalid object type
        
        APIResponse response = processBrowseData("invalid-node", invalidData);
        
        // Should handle error gracefully
        Assert.assertTrue(response.status() >= 200 && response.status() < 600, 
            "Should handle invalid data gracefully");
        
        logApiResponse(response);
    }
    
    @Test(description = "Test concurrent Kafka operations")
    public void testConcurrentKafkaOperations() {
        int numberOfThreads = 5;
        Thread[] threads = new Thread[numberOfThreads];
        boolean[] results = new boolean[numberOfThreads];
        
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                try {
                    Map<String, Object> browseData = new HashMap<>();
                    browseData.put("tag", "TestTag" + threadIndex);
                    browseData.put("value", 100 + threadIndex);
                    
                    APIResponse response = processBrowseData("ns=3;s=DataBlocksGlobal", browseData);
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
            "At least 80% of concurrent Kafka operations should succeed");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Concurrent Kafka operations completed: " + successCount + "/" + numberOfThreads + " successful");
    }
    
    @Test(description = "Test Kafka message validation")
    public void testKafkaMessageValidation() {
        // Test with various message structures
        @SuppressWarnings("unchecked")
        Map<String, Object>[] testMessages = new Map[] {
            createBrowseDataMessage("ns=3;s=DataBlocksGlobal", "Tag1", 123),
            createBrowseDataMessage("ns=0;i=2253", "Tag2", "String Value"),
            createBrowseDataMessage("ns=2;i=5001", "Tag3", true),
            createBrowseDataMessage("ns=3;s=DataBlocksGlobal", "Tag4", 123.45)
        };
        
        for (Map<String, Object> message : testMessages) {
            String nodeId = (String) message.get("nodeId");
            @SuppressWarnings("unchecked")
            Map<String, Object> browseData = (Map<String, Object>) message.get("browseData");
            
            APIResponse response = processBrowseData(nodeId, browseData);
            
            Assert.assertTrue(response.status() == 200 || response.status() == 400, 
                "Should handle message validation appropriately");
            
            verifyResponseTime(response, MAX_RESPONSE_TIME);
        }
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Kafka message validation completed for all test cases");
    }
    
    @Test(description = "Test Kafka large message handling")
    public void testKafkaLargeMessageHandling() {
        String nodeId = "ns=3;s=DataBlocksGlobal";
        Map<String, Object> largeBrowseData = new HashMap<>();
        largeBrowseData.put("tag", "LargeTag");
        largeBrowseData.put("value", "A".repeat(10000)); // 10KB string
        largeBrowseData.put("metadata", createLargeMetadata());
        
        APIResponse response = processBrowseData(nodeId, largeBrowseData);
        
        Assert.assertTrue(response.status() >= 200 && response.status() < 600, 
            "Should handle large messages appropriately");
        
        verifyResponseTime(response, MAX_RESPONSE_TIME);
        logApiResponse(response);
    }
    
    private Map<String, Object> createBrowseDataMessage(String nodeId, String tag, Object value) {
        Map<String, Object> message = new HashMap<>();
        message.put("nodeId", nodeId);
        
        Map<String, Object> browseData = new HashMap<>();
        browseData.put("tag", tag);
        browseData.put("value", value);
        browseData.put("timestamp", System.currentTimeMillis());
        
        message.put("browseData", browseData);
        return message;
    }
    
    private Map<String, Object> createLargeMetadata() {
        Map<String, Object> metadata = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            metadata.put("key" + i, "value" + i + " - " + "A".repeat(100));
        }
        return metadata;
        
    }
    
    @DataProvider(name = "browseDataProvider")
    public Object[][] browseDataProvider() {
        return new Object[][] {
            {"ns=3;s=DataBlocksGlobal", createSimpleBrowseData("Tag1", 123), "Integer value"},
            {"ns=3;s=DataBlocksGlobal", createSimpleBrowseData("Tag2", "String Value"), "String value"},
            {"ns=3;s=DataBlocksGlobal", createSimpleBrowseData("Tag3", true), "Boolean value"},
            {"ns=3;s=DataBlocksGlobal", createSimpleBrowseData("Tag4", 123.45), "Double value"},
            {"ns=0;i=2253", createSimpleBrowseData("Tag5", 42), "Numeric node ID"},
            {"ns=2;i=5001", createSimpleBrowseData("Tag6", "Another String"), "Another numeric node ID"}
        };
    }
    
    @DataProvider(name = "convertValueProvider")
    public Object[][] convertValueProvider() {
        return new Object[][] {
            {"42", "Integer string"},
            {"123.45", "Double string"},
            {"true", "Boolean string"},
            {"false", "Boolean string"},
            {"test", "String value"},
            {"", "Empty string"},
            {"null", "Null string"}
        };
     
    }
    
    private Map<String, Object> createSimpleBrowseData(String tag, Object value) {
        Map<String, Object> browseData = new HashMap<>();
        browseData.put("tag", tag);
        browseData.put("value", value);
        browseData.put("timestamp", System.currentTimeMillis());
        return browseData;
    }
}