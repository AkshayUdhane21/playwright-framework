package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class EnhancedExtentManager {
    private static ExtentReports extentReports;
    private static Map<String, ExtentTest> testMap = new HashMap<>();
    private static String reportPath;
    
    public static void initializeReport() {
        if (extentReports == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            reportPath = "test-output/Enhanced_Test_Report_" + timestamp + ".html";
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath)
                    .viewConfigurer()
                    .viewOrder()
                    .as(new ViewName[]{
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.CATEGORY,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION
                    })
                    .apply();
            
            // Configure the reporter
            sparkReporter.config().setDocumentTitle("Microservices API Test Suite Report");
            sparkReporter.config().setReportName("Enhanced API Test Report");
            // Theme configuration removed in ExtentReports 5+
            sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
            sparkReporter.config().setCss(".nav-wrapper { background-color: #1f4e79; }");
            sparkReporter.config().setCss(".brand-logo { color: white; }");
            
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            
            // Add system information
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("User", System.getProperty("user.name"));
            extentReports.setSystemInfo("Test Environment", "Real Microservices");
            extentReports.setSystemInfo("Test Framework", "Playwright + TestNG");
            extentReports.setSystemInfo("Report Generated", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            System.out.println("Enhanced ExtentReports initialized: " + reportPath);
        }
    }
    
    public static ExtentTest createTest(String testName, String description) {
        if (extentReports == null) {
            initializeReport();
        }
        
        ExtentTest test = extentReports.createTest(testName, description);
        testMap.put(testName, test);
        return test;
    }
    
    public static ExtentTest createTest(String testName, String description, String category) {
        if (extentReports == null) {
            initializeReport();
        }
        
        ExtentTest test = extentReports.createTest(testName, description)
                .assignCategory(category);
        testMap.put(testName, test);
        return test;
    }
    
    public static ExtentTest createTest(String testName, String description, String category, String author) {
        if (extentReports == null) {
            initializeReport();
        }
        
        ExtentTest test = extentReports.createTest(testName, description)
                .assignCategory(category)
                .assignAuthor(author);
        testMap.put(testName, test);
        return test;
    }
    
    public static ExtentTest getTest(String testName) {
        return testMap.get(testName);
    }
    
    public static void logInfo(String testName, String message) {
        ExtentTest test = getTest(testName);
        if (test != null) {
            test.log(Status.INFO, message);
        }
    }
    
    public static void logPass(String testName, String message) {
        ExtentTest test = getTest(testName);
        if (test != null) {
            test.log(Status.PASS, MarkupHelper.createLabel(message, ExtentColor.GREEN));
        }
    }
    
    public static void logFail(String testName, String message) {
        ExtentTest test = getTest(testName);
        if (test != null) {
            test.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED));
        }
    }
    
    public static void logSkip(String testName, String message) {
        ExtentTest test = getTest(testName);
        if (test != null) {
            test.log(Status.SKIP, MarkupHelper.createLabel(message, ExtentColor.ORANGE));
        }
    }
    
    public static void logWarning(String testName, String message) {
        ExtentTest test = getTest(testName);
        if (test != null) {
            test.log(Status.WARNING, MarkupHelper.createLabel(message, ExtentColor.YELLOW));
        }
    }
    
    public static void addScreenshot(String testName, String screenshotPath, String description) {
        ExtentTest test = getTest(testName);
        if (test != null) {
            test.addScreenCaptureFromPath(screenshotPath, description);
        }
    }
    
    public static void addApiResponse(String testName, String response, String description) {
        ExtentTest test = getTest(testName);
        if (test != null) {
            test.log(Status.INFO, description + ": " + response);
        }
    }
    
    public static void addPerformanceMetrics(String testName, String operation, long responseTime, long memoryUsed) {
        ExtentTest test = getTest(testName);
        if (test != null) {
            String metrics = String.format("Operation: %s, Response Time: %dms, Memory Used: %d bytes", 
                operation, responseTime, memoryUsed);
            test.log(Status.INFO, "Performance Metrics: " + metrics);
        }
    }
    
    public static void addServiceHealthStatus(String testName, String serviceName, boolean isHealthy) {
        ExtentTest test = getTest(testName);
        if (test != null) {
            String status = isHealthy ? "UP" : "DOWN";
            Status logStatus = isHealthy ? Status.PASS : Status.FAIL;
            test.log(logStatus, "Service " + serviceName + " status: " + status);
        }
    }
    
    public static void addTestData(String testName, String dataType, Object data) {
        ExtentTest test = getTest(testName);
        if (test != null) {
            test.log(Status.INFO, dataType + ": " + data.toString());
        }
    }
    
    public static void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
            System.out.println("Enhanced ExtentReports flushed to: " + reportPath);
        }
    }
    
    public static ExtentReports getExtentReports() {
        return extentReports;
    }
    
    public static String getReportPath() {
        return reportPath;
    }
    
    // Method to create a summary report
    public static void createSummaryReport() {
        if (extentReports != null) {
            // Add summary information
            extentReports.setSystemInfo("Total Tests", String.valueOf(testMap.size()));
            
            long passedTests = testMap.values().stream()
                .mapToLong(test -> test.getModel().getStatus() == Status.PASS ? 1 : 0)
                .sum();
            
            long failedTests = testMap.values().stream()
                .mapToLong(test -> test.getModel().getStatus() == Status.FAIL ? 1 : 0)
                .sum();
            
            long skippedTests = testMap.values().stream()
                .mapToLong(test -> test.getModel().getStatus() == Status.SKIP ? 1 : 0)
                .sum();
            
            extentReports.setSystemInfo("Passed Tests", String.valueOf(passedTests));
            extentReports.setSystemInfo("Failed Tests", String.valueOf(failedTests));
            extentReports.setSystemInfo("Skipped Tests", String.valueOf(skippedTests));
            extentReports.setSystemInfo("Success Rate", String.format("%.2f%%", (passedTests * 100.0 / testMap.size())));
        }
    }
}
