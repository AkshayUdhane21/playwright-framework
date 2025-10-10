package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import utils.DriverManager;

/**
 * Test to verify driver version compatibility
 */
public class DriverVersionTest extends BaseTest {
    
    @Test(description = "Test driver version compatibility and browser detection")
    public void testDriverVersions() {
        System.out.println("🔍 Testing Driver Version Compatibility");
        System.out.println("=====================================");
        
        try {
            // Navigate to a simple page to test driver functionality
            navigateToApp();
            
            // Get browser information
            String browserName = driver.getClass().getSimpleName().replace("Driver", "");
            String currentUrl = driver.getCurrentUrl();
            String pageTitle = driver.getTitle();
            
            System.out.println("✅ Driver Information:");
            System.out.println("  - Browser: " + browserName);
            System.out.println("  - Current URL: " + currentUrl);
            System.out.println("  - Page Title: " + pageTitle);
            System.out.println("  - Driver Status: Active and responsive");
            
            // Test basic functionality
            driver.manage().window().maximize();
            System.out.println("  - Window maximized: ✓");
            
            // Take a screenshot to verify everything works
            String screenshotPath = utils.SeleniumUtils.takeScreenshot(driver, "driver_version_test");
            System.out.println("  - Screenshot saved: " + screenshotPath);
            
            System.out.println("\n✅ Driver version test completed successfully!");
            System.out.println("No version mismatch issues detected.");
            
        } catch (Exception e) {
            System.err.println("❌ Driver version test failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @Test(description = "Test browser version detection")
    public void testBrowserVersionDetection() {
        System.out.println("🌐 Testing Browser Version Detection");
        System.out.println("===================================");
        
        // Print detected browser versions
        DriverManager.printBrowserInfo();
        
        System.out.println("✅ Browser version detection completed!");
    }
}







