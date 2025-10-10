package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.MasterProductVariantPage;
import utils.SeleniumUtils;

/**
 * Simple test for Master Module functionality
 * Tests the basic workflow: Navigate -> Click Master -> Click Details -> Create Form -> Fill -> Submit
 */
public class SimpleMasterTest extends BaseTest {
    
    private HomePage homePage;
    private MasterProductVariantPage masterProductVariantPage;
    
    // Test data - exactly as you specified
    private static final String PRODUCT_CODE = "6100100";
    private static final String PRODUCT_NAME = "HL ASSY RH MRE";
    private static final String TROLLEY_TYPE = "NA";
    private static final String STORAGE_CAPACITY = "6";
    
    @Test(description = "Test complete Master Module workflow with 2-second delays")
    public void testMasterModuleWorkflow() throws Exception {
        System.out.println("🚀 Starting Master Module Workflow Test");
        System.out.println("=====================================");
        
        try {
            // Step 1: Navigate to application
            System.out.println("Step 1: Navigating to http://localhost:5173/");
            navigateToApp();
            Thread.sleep(2000); // Wait 2 seconds
            System.out.println("✓ Application loaded successfully");
            
            // Step 2: Click Master button
            System.out.println("\nStep 2: Clicking Master button");
            homePage = new HomePage(driver);
            boolean masterClicked = homePage.clickMasterButton();
            Assert.assertTrue(masterClicked, "Failed to click Master button");
            
            // Step 3: Click Master Product Variant Details
            System.out.println("\nStep 3: Clicking Master Product Variant Details");
            boolean detailsClicked = homePage.clickMasterProductVariantDetails();
            Assert.assertTrue(detailsClicked, "Failed to click Master Product Variant Details");
            
            // Step 4: Click Create button
            System.out.println("\nStep 4: Clicking Create button");
            masterProductVariantPage = new MasterProductVariantPage(driver);
            boolean createClicked = masterProductVariantPage.clickCreateButton();
            Assert.assertTrue(createClicked, "Failed to click Create button");
            
            // Step 5: Fill the form
            System.out.println("\nStep 5: Filling the form");
            System.out.println("  - Product Code: " + PRODUCT_CODE);
            boolean codeEntered = masterProductVariantPage.enterProductVariantCode(PRODUCT_CODE);
            Assert.assertTrue(codeEntered, "Failed to enter product code");
            Thread.sleep(1000); // Small delay between fields
            
            System.out.println("  - Product Name: " + PRODUCT_NAME);
            boolean nameEntered = masterProductVariantPage.enterProductVariantName(PRODUCT_NAME);
            Assert.assertTrue(nameEntered, "Failed to enter product name");
            Thread.sleep(1000);
            
            System.out.println("  - Trolley Type: " + TROLLEY_TYPE);
            boolean typeEntered = masterProductVariantPage.enterTrolleyType(TROLLEY_TYPE);
            Assert.assertTrue(typeEntered, "Failed to enter trolley type");
            Thread.sleep(1000);
            
            System.out.println("  - Storage Capacity: " + STORAGE_CAPACITY);
            boolean capacityEntered = masterProductVariantPage.enterTrolleyStorageCapacity(STORAGE_CAPACITY);
            Assert.assertTrue(capacityEntered, "Failed to enter storage capacity");
            
            // Step 6: Submit the form
            System.out.println("\nStep 6: Submitting the form");
            boolean submitted = masterProductVariantPage.clickSubmitButton();
            Assert.assertTrue(submitted, "Failed to submit form");
            
            // Take final screenshot
            String screenshotPath = SeleniumUtils.takeScreenshot(driver, "master_workflow_complete");
            System.out.println("📸 Screenshot saved: " + screenshotPath);
            
            System.out.println("\n✅ Master Module Workflow Test Completed Successfully!");
            System.out.println("=====================================");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            
            // Take screenshot on failure
            try {
                String screenshotPath = SeleniumUtils.takeScreenshot(driver, "master_workflow_failed");
                System.out.println("📸 Error screenshot saved: " + screenshotPath);
            } catch (Exception screenshotException) {
                System.err.println("Failed to capture error screenshot: " + screenshotException.getMessage());
            }
            
            throw e;
        }
    }
    
    @Test(description = "Test just the navigation and clicking functionality")
    public void testNavigationAndClicking() throws Exception {
        System.out.println("🧭 Testing Navigation and Clicking");
        System.out.println("=================================");
        
        try {
            // Navigate to application
            System.out.println("Step 1: Navigating to application");
            navigateToApp();
            Thread.sleep(2000);
            
            // Test Master button click
            System.out.println("Step 2: Testing Master button click");
            homePage = new HomePage(driver);
            boolean masterClicked = homePage.clickMasterButton();
            System.out.println("Master button click result: " + (masterClicked ? "✓ Success" : "✗ Failed"));
            
            // Test Master Product Variant Details click
            System.out.println("Step 3: Testing Master Product Variant Details click");
            boolean detailsClicked = homePage.clickMasterProductVariantDetails();
            System.out.println("Details click result: " + (detailsClicked ? "✓ Success" : "✗ Failed"));
            
            // Test Create button click
            System.out.println("Step 4: Testing Create button click");
            masterProductVariantPage = new MasterProductVariantPage(driver);
            boolean createClicked = masterProductVariantPage.clickCreateButton();
            System.out.println("Create button click result: " + (createClicked ? "✓ Success" : "✗ Failed"));
            
            // Take screenshot
            String screenshotPath = SeleniumUtils.takeScreenshot(driver, "navigation_test");
            System.out.println("📸 Screenshot saved: " + screenshotPath);
            
            System.out.println("\n✅ Navigation and Clicking Test Completed!");
            
        } catch (Exception e) {
            System.err.println("❌ Navigation test failed: " + e.getMessage());
            
            // Take screenshot on failure
            try {
                String screenshotPath = SeleniumUtils.takeScreenshot(driver, "navigation_test_failed");
                System.out.println("📸 Error screenshot saved: " + screenshotPath);
            } catch (Exception screenshotException) {
                System.err.println("Failed to capture error screenshot: " + screenshotException.getMessage());
            }
            
            throw e;
        }
    }
}






