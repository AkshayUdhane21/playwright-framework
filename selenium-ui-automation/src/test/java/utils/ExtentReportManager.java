package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import config.ConfigManager;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Extent Report Manager for generating test reports
 */
public class ExtentReportManager {
    
    private static ExtentReportManager instance;
    private ExtentReports extentReports;
    private ExtentTest extentTest;
    private ConfigManager config;
    
    private ExtentReportManager() {
        config = ConfigManager.getInstance();
    }
    
    public static ExtentReportManager getInstance() {
        if (instance == null) {
            instance = new ExtentReportManager();
        }
        return instance;
    }
    
    public void startReport() {
        String reportPath = config.getProperty("report.path", "test-output/");
        String reportName = config.getProperty("extent.report.name", "UI_Automation_Report");
        String reportTitle = config.getProperty("extent.report.title", "Master Module UI Automation");
        String theme = config.getProperty("extent.report.theme", "standard");
        
        // Create report directory if it doesn't exist
        File reportDir = new File(reportPath);
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }
        
        // Generate timestamp for unique report names
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String reportFileName = reportPath + reportName + "_" + timestamp + ".html";
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFileName);
        
        // Configure report
        sparkReporter.config().setDocumentTitle(reportTitle);
        sparkReporter.config().setReportName(reportName);
        // Theme configuration removed in ExtentReports 5+
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        
        // Set system information
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("Browser", config.getProperty("browser.name", "chrome"));
        extentReports.setSystemInfo("Environment", System.getProperty("env", "local"));
        extentReports.setSystemInfo("Application URL", config.getProperty("app.base.url", "http://localhost:5173/"));
    }
    
    public void endReport() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
    
    public ExtentTest createTest(String testName, String description) {
        extentTest = extentReports.createTest(testName, description);
        return extentTest;
    }
    
    public ExtentTest createTest(String testName) {
        extentTest = extentReports.createTest(testName);
        return extentTest;
    }
    
    public void logInfo(String message) {
        if (extentTest != null) {
            extentTest.log(Status.INFO, message);
        }
    }
    
    public void logPass(String message) {
        if (extentTest != null) {
            extentTest.log(Status.PASS, message);
        }
    }
    
    public void logFail(String message) {
        if (extentTest != null) {
            extentTest.log(Status.FAIL, message);
        }
    }
    
    public void logSkip(String message) {
        if (extentTest != null) {
            extentTest.log(Status.SKIP, message);
        }
    }
    
    public void logWarning(String message) {
        if (extentTest != null) {
            extentTest.log(Status.WARNING, message);
        }
    }
    
    public void addScreenshot(String screenshotPath, String title) {
        if (extentTest != null && screenshotPath != null && !screenshotPath.isEmpty()) {
            try {
                extentTest.addScreenCaptureFromPath(screenshotPath, title);
            } catch (Exception e) {
                System.err.println("Failed to add screenshot to report: " + e.getMessage());
            }
        }
    }
    
    public void addScreenshot(String screenshotPath) {
        addScreenshot(screenshotPath, "Screenshot");
    }
    
    public ExtentTest getCurrentTest() {
        return extentTest;
    }
}

