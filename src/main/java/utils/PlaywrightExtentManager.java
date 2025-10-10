package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class PlaywrightExtentManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    private static void createInstance() {
        extent = new ExtentReports();
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
        
        // Configure the reporter
        // Theme configuration removed in ExtentReports 5+
        sparkReporter.config().setDocumentTitle("Playwright Test Report");
        sparkReporter.config().setReportName("Playwright Automation Framework");
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        
        extent.attachReporter(sparkReporter);
        
        // Set system info
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User Name", System.getProperty("user.name"));
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }

    public static void removeTest() {
        test.remove();
    }

    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static void initializeReport() {
        getInstance();
    }

    public static ExtentReports getExtentReports() {
        return extent;
    }

    public static ExtentTest createTest(String testName, String description) {
        return getInstance().createTest(testName, description);
    }
}
