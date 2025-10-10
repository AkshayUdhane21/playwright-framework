package utils;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import config.TestConfigManager;

public class EnhancedTestListener implements ITestListener {
    
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Starting test: " + result.getMethod().getMethodName());
        
        // Create ExtentTest if ExtentReports is enabled
        if (TestConfigManager.isExtentReportEnabled()) {
            ExtentTest extentTest = ExtentManager.createTest(
                result.getMethod().getMethodName(),
                "Test: " + result.getMethod().getMethodName()
            );
            result.setAttribute("extentTest", extentTest);
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test passed: " + result.getMethod().getMethodName());
        
        if (TestConfigManager.isExtentReportEnabled()) {
            ExtentTest extentTest = (ExtentTest) result.getAttribute("extentTest");
            if (extentTest != null) {
                extentTest.log(Status.PASS, "Test passed successfully");
            }
        }
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test failed: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            System.err.println("Failure reason: " + result.getThrowable().getMessage());
        }
        
        if (TestConfigManager.isExtentReportEnabled()) {
            ExtentTest extentTest = (ExtentTest) result.getAttribute("extentTest");
            if (extentTest != null) {
                extentTest.log(Status.FAIL, "Test failed: " + 
                    (result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error"));
            }
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test skipped: " + result.getMethod().getMethodName());
        if (result.getSkipCausedBy() != null && result.getSkipCausedBy().size() > 0) {
            System.out.println("Skip reason: " + result.getSkipCausedBy().toString());
        }
        
        if (TestConfigManager.isExtentReportEnabled()) {
            ExtentTest extentTest = (ExtentTest) result.getAttribute("extentTest");
            if (extentTest != null) {
                extentTest.log(Status.SKIP, "Test skipped: " + 
                    (result.getSkipCausedBy() != null ? result.getSkipCausedBy().toString() : "No reason provided"));
            }
        }
    }
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting test suite: " + context.getName());
        
        // Initialize ExtentReports if enabled
        if (TestConfigManager.isExtentReportEnabled()) {
            ExtentManager.initializeReport();
        }
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Finishing test suite: " + context.getName());
        
        // Flush ExtentReports if enabled
        if (TestConfigManager.isExtentReportEnabled()) {
            ExtentManager.flushReport();
        }
    }
}