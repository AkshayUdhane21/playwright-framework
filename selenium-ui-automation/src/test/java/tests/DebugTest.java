package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import utils.PageInspector;
import utils.SeleniumUtils;

/**
 * Debug test to inspect page elements
 */
public class DebugTest extends BaseTest {
    
    @Test(description = "Debug test to inspect page elements")
    public void debugPageInspection() {
        try {
            // Navigate to the application
            navigateToApp();
            
            // Take a screenshot
            String screenshotPath = SeleniumUtils.takeScreenshot(driver, "debug_page_load");
            System.out.println("Screenshot saved: " + screenshotPath);
            
            // Inspect the page
            PageInspector.waitAndInspectPage(driver, config.getProperty("app.base.url", "http://localhost:5173/"));
            
            // Wait a bit to see the page
            Thread.sleep(5000);
            
        } catch (Exception e) {
            System.err.println("Debug test failed: " + e.getMessage());
            e.printStackTrace();
            
            // Take screenshot on error
            String screenshotPath = SeleniumUtils.takeScreenshot(driver, "debug_error");
            System.out.println("Error screenshot saved: " + screenshotPath);
        }
    }
}







