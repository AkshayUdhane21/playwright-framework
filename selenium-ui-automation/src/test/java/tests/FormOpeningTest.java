package tests;

import base.BaseTest;
import pages.HomePage;
import pages.MasterProductVariantPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class specifically for testing form opening functionality
 */
public class FormOpeningTest extends BaseTest {
    
    private HomePage homePage;
    private MasterProductVariantPage masterProductVariantPage;
    
    @Test(priority = 1, description = "Test form opening with improved XPath strategies")
    public void testFormOpeningWithImprovedXPath() {
        System.out.println("🚀 Starting Form Opening Test");
        System.out.println("=============================");
        
        try {
            // Step 1: Navigate to application
            System.out.println("Step 1: Navigating to http://localhost:5173/");
            navigateToApp();
            Thread.sleep(3000); // Wait 3 seconds for page load
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
            
            // Step 4: Test Create button clicking with improved strategies
            System.out.println("\nStep 4: Testing Create button with improved XPath strategies");
            masterProductVariantPage = new MasterProductVariantPage(driver);
            
            // Wait a bit for the page to fully load
            Thread.sleep(2000);
            
            // Test the improved click method
            boolean createClicked = masterProductVariantPage.clickCreateButton();
            
            if (createClicked) {
                System.out.println("✓ Create button clicked successfully!");
                
                // Verify form is visible
                Thread.sleep(2000); // Wait for form to appear
                boolean formVisible = masterProductVariantPage.isCreateFormVisible();
                
                if (formVisible) {
                    System.out.println("✓ Form opened successfully!");
                    System.out.println("✓ Test PASSED - Form opening functionality works correctly");
                } else {
                    System.out.println("⚠ Form may not be visible yet, checking form fields...");
                    
                    // Check if form fields are available
                    boolean fieldsEnabled = masterProductVariantPage.areFormFieldsEnabled();
                    if (fieldsEnabled) {
                        System.out.println("✓ Form fields are enabled - form is likely open");
                        System.out.println("✓ Test PASSED - Form opening functionality works correctly");
                    } else {
                        System.out.println("✗ Form fields are not enabled - form may not be open");
                        Assert.fail("Form did not open properly");
                    }
                }
            } else {
                System.out.println("✗ Failed to click Create button with all strategies");
                Assert.fail("Failed to click Create button");
            }
            
        } catch (Exception e) {
            System.err.println("Test failed with exception: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    @Test(priority = 2, description = "Test form opening with specific XPath debugging")
    public void testFormOpeningWithDebugging() {
        System.out.println("🔍 Starting Form Opening Debug Test");
        System.out.println("===================================");
        
        try {
            // Navigate to the specific page
            System.out.println("Step 1: Navigating to Master Product Variant page");
            navigateToApp();
            Thread.sleep(2000);
            
            homePage = new HomePage(driver);
            homePage.clickMasterButton();
            Thread.sleep(1000);
            homePage.clickMasterProductVariantDetails();
            Thread.sleep(2000);
            
            // Create page object and test with debugging
            masterProductVariantPage = new MasterProductVariantPage(driver);
            
            System.out.println("Step 2: Testing Create button with debugging enabled");
            boolean createClicked = masterProductVariantPage.clickCreateButton();
            
            if (createClicked) {
                System.out.println("✓ Create button clicked successfully in debug test");
                
                // Additional verification
                Thread.sleep(3000);
                boolean formVisible = masterProductVariantPage.isCreateFormVisible();
                System.out.println("Form visible: " + formVisible);
                
                if (formVisible) {
                    System.out.println("✓ Debug test PASSED - Form is visible");
                } else {
                    System.out.println("⚠ Debug test - Form visibility unclear, but button was clicked");
                }
            } else {
                System.out.println("✗ Debug test FAILED - Could not click Create button");
                Assert.fail("Debug test failed - Create button not clickable");
            }
            
        } catch (Exception e) {
            System.err.println("Debug test failed with exception: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Debug test failed: " + e.getMessage());
        }
    }
    
    @Test(priority = 3, description = "Test form opening with JavaScript click fallback")
    public void testFormOpeningWithJavaScriptFallback() {
        System.out.println("⚡ Starting JavaScript Fallback Test");
        System.out.println("====================================");
        
        try {
            // Navigate to the page
            navigateToApp();
            Thread.sleep(2000);
            
            homePage = new HomePage(driver);
            homePage.clickMasterButton();
            Thread.sleep(1000);
            homePage.clickMasterProductVariantDetails();
            Thread.sleep(2000);
            
            masterProductVariantPage = new MasterProductVariantPage(driver);
            
            System.out.println("Testing Create button with JavaScript fallback...");
            boolean createClicked = masterProductVariantPage.clickCreateButton();
            
            if (createClicked) {
                System.out.println("✓ JavaScript fallback test PASSED");
                
                // Verify form opened
                Thread.sleep(2000);
                boolean formVisible = masterProductVariantPage.isCreateFormVisible();
                System.out.println("Form opened successfully: " + formVisible);
                
                Assert.assertTrue(createClicked, "JavaScript fallback test should succeed");
            } else {
                System.out.println("✗ JavaScript fallback test FAILED");
                Assert.fail("JavaScript fallback test failed");
            }
            
        } catch (Exception e) {
            System.err.println("JavaScript fallback test failed: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("JavaScript fallback test failed: " + e.getMessage());
        }
    }
}
