package utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

/**
 * TestNG listener for handling test events and reporting
 */
public class TestListener implements ITestListener {
    
    private ExtentReportManager extentReport;
    
    @Override
    public void onStart(ITestContext context) {
        extentReport = ExtentReportManager.getInstance();
        System.out.println("Test Suite Started: " + context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test Suite Finished: " + context.getName());
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        
        if (description == null || description.isEmpty()) {
            description = "Test: " + testName;
        }
        
        extentReport.createTest(testName, description);
        System.out.println("Test Started: " + testName);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        extentReport.logPass("Test passed successfully: " + testName);
        System.out.println("Test Passed: " + testName);
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable().getMessage();
        
        extentReport.logFail("Test failed: " + testName + " - " + errorMessage);
        
        // Take screenshot on failure
        try {
            Object testInstance = result.getInstance();
            if (testInstance instanceof base.BaseTest) {
                base.BaseTest baseTest = (base.BaseTest) testInstance;
                String screenshotPath = SeleniumUtils.takeScreenshot(baseTest.getDriver(), testName + "_failed");
                extentReport.addScreenshot(screenshotPath, "Test Failure Screenshot");
            }
        } catch (Exception e) {
            System.err.println("Failed to take screenshot on test failure: " + e.getMessage());
        }
        
        System.out.println("Test Failed: " + testName + " - " + errorMessage);
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String skipReason = result.getSkipCausedBy().toString();
        
        extentReport.logSkip("Test skipped: " + testName + " - " + skipReason);
        System.out.println("Test Skipped: " + testName + " - " + skipReason);
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        extentReport.logWarning("Test failed but within success percentage: " + testName);
        System.out.println("Test Failed but within success percentage: " + testName);
    }
}







