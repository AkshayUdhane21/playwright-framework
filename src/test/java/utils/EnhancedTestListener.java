package utils;

import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class EnhancedTestListener implements ITestListener {
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting test suite: " + context.getName());
        EnhancedExtentManager.initializeReport();
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Finishing test suite: " + context.getName());
        EnhancedExtentManager.createSummaryReport();
        EnhancedExtentManager.flushReport();
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        String className = result.getTestClass().getName();
        
        System.out.println("Starting test: " + testName);
        
        // Create ExtentTest with category based on class name
        String category = extractCategoryFromClassName(className);
        EnhancedExtentManager.createTest(testName, description, category, "API Test Framework");
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();
        
        System.out.println("Test passed: " + testName + " (Duration: " + duration + "ms)");
        EnhancedExtentManager.logPass(testName, "Test passed in " + duration + "ms");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable().getMessage();
        long duration = result.getEndMillis() - result.getStartMillis();
        
        System.out.println("Test failed: " + testName + " (Duration: " + duration + "ms)");
        System.out.println("Error: " + errorMessage);
        
        EnhancedExtentManager.logFail(testName, "Test failed in " + duration + "ms: " + errorMessage);
        
        // Add stack trace if available
        if (result.getThrowable() != null) {
            EnhancedExtentManager.logFail(testName, "Stack trace: " + 
                java.util.Arrays.toString(result.getThrowable().getStackTrace()));
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String skipReason = result.getSkipCausedBy().toString();
        
        System.out.println("Test skipped: " + testName + " - Reason: " + skipReason);
        EnhancedExtentManager.logSkip(testName, "Test skipped: " + skipReason);
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("Test failed but within success percentage: " + testName);
        EnhancedExtentManager.logWarning(testName, "Test failed but within success percentage");
    }
    
    private String extractCategoryFromClassName(String className) {
        if (className.contains("OpUaConnectionServiceTest")) {
            return "OPC UA Connection";
        } else if (className.contains("ReadDataServiceTest")) {
            return "Read Data Service";
        } else if (className.contains("WriteDataServiceTest")) {
            return "Write Data Service";
        } else if (className.contains("KafkaServiceTest")) {
            return "Kafka Service";
        } else if (className.contains("IntegrationTest")) {
            return "Integration Testing";
        } else if (className.contains("ApiTestExecutor")) {
            return "Legacy API Tests";
        } else {
            return "General";
        }
    }
}
