package utils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.ExtentReports;




import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentManager {
    private static ExtentReports extentReports;
    private static final String REPORT_PATH = "test-output/Enhanced_Test_Report_" + 
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".html";

    public static synchronized ExtentReports getExtentReports() {
        if (extentReports == null) {
            initializeReport();
        }
        return extentReports;
    }

    public static synchronized void initializeReport() {
        if (extentReports == null) {
            extentReports = new ExtentReports();
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH);
            // Theme configuration removed in ExtentReports 5+
            sparkReporter.config().setDocumentTitle("Microservices API Test Report");
            sparkReporter.config().setReportName("API Test Suite Results");
            sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
            
            extentReports.attachReporter(sparkReporter);
            
            // Add system information
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("Test Framework", "TestNG");
            extentReports.setSystemInfo("Browser", "Playwright");
            
            System.out.println("ExtentReports initialized: " + REPORT_PATH);
        }
    }

    public static synchronized ExtentTest createTest(String testName, String description) {
        ExtentReports reports = getExtentReports();
        return reports.createTest(testName, description);
    }

    public static synchronized ExtentTest createTest(String testName) {
        ExtentReports reports = getExtentReports();
        return reports.createTest(testName);
    }

    public static synchronized void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
            System.out.println("Enhanced ExtentReports flushed to: " + REPORT_PATH);
        }
    }

    public static synchronized void closeReport() {
        if (extentReports != null) {
            extentReports.flush();
            extentReports = null;
            System.out.println("ExtentReports closed");
        }
    }
}

