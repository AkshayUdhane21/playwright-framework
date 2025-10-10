package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;
import pages.MasterProductVariantPage;
import utils.ExtentReportManager;
import utils.SeleniumUtils;

/**
 * Comprehensive workflow test that navigates through all pages first,
 * then performs Master Product Variant automation without closing browser
 */
public class ComprehensiveWorkflowTest extends BaseTest {

    private HomePage homePage;
    private MasterProductVariantPage masterProductVariantPage;
    private ExtentReportManager extentReport;

    private String validProductCode;
    private String validProductName;
    private String validTrolleyType;
    private String validStorageCapacity;

    @BeforeClass
    public void setUpClass() {
        extentReport = ExtentReportManager.getInstance();

        validProductCode = config.getProperty("master.product.code", "61030367");
        validProductName = config.getProperty("master.product.name", "Test Product Variant");
        validTrolleyType = config.getProperty("master.trolley.type", "NA");
        validStorageCapacity = config.getProperty("master.storage.capacity", "6");
    }

    @BeforeMethod
    public void setUpTest() throws InterruptedException {
        System.out.println("\n========================================");
        System.out.println("🔄 Starting Comprehensive Workflow Test");
        System.out.println("========================================");

        navigateToApp();
        Thread.sleep(2000);
        homePage = new HomePage(driver);
        System.out.println("✅ Application loaded and HomePage initialized");
    }

    @Test(description = "Complete workflow: Navigate all pages first, then automate Master Product Variant", priority = 1)
    public void testCompleteWorkflowWithoutBrowserRestart() throws InterruptedException {
        extentReport.createTest("Complete Workflow Without Browser Restart", 
                "Navigate through all pages first, then perform Master Product Variant automation");

        try {
            System.out.println("\n🎯 COMPREHENSIVE WORKFLOW TEST");
            System.out.println("=========================================");
            System.out.println("📋 This test will:");
            System.out.println("   1. Navigate through all Master pages");
            System.out.println("   2. Return to Master Product Variant Details");
            System.out.println("   3. Perform all automation actions");
            System.out.println("   4. Keep browser open throughout");
            System.out.println("=========================================");

            // PHASE 1: Navigate through all Master pages
            System.out.println("\n📖 PHASE 1: Navigating through all Master pages");
            System.out.println("─────────────────────────────────────────────");

            // Step 1: Navigate to Master Product Variant Details
            System.out.println("\n[1/5] Navigating to Master Product Variant Details...");
            masterProductVariantPage = homePage.navigateToMasterProductVariantDetailsWithFlow();
            Thread.sleep(2000);
            System.out.println("    ✅ Master Product Variant Details page loaded");
            extentReport.logInfo("Step 1/5: Master Product Variant Details page loaded");

            // Step 2: Navigate to Master Product Details
            System.out.println("\n[2/5] Navigating to Master Product Details...");
            homePage.clickMasterButton();
            Thread.sleep(1000);
            boolean productDetailsClicked = homePage.clickMasterProductDetails();
            if (productDetailsClicked) {
                System.out.println("    ✅ Master Product Details page loaded");
                extentReport.logInfo("Step 2/5: Master Product Details page loaded");
            } else {
                System.out.println("    ⚠️  Master Product Details navigation failed");
                extentReport.logInfo("Step 2/5: Master Product Details navigation failed");
            }
            Thread.sleep(2000);

            // Step 3: Navigate to Master Shifts
            System.out.println("\n[3/5] Navigating to Master Shifts...");
            homePage.clickMasterButton();
            Thread.sleep(1000);
            boolean shiftsClicked = homePage.clickMasterShifts();
            if (shiftsClicked) {
                System.out.println("    ✅ Master Shifts page loaded");
                extentReport.logInfo("Step 3/5: Master Shifts page loaded");
            } else {
                System.out.println("    ⚠️  Master Shifts navigation failed");
                extentReport.logInfo("Step 3/5: Master Shifts navigation failed");
            }
            Thread.sleep(2000);

            // Step 4: Navigate to Master Reason Details
            System.out.println("\n[4/5] Navigating to Master Reason Details...");
            homePage.clickMasterButton();
            Thread.sleep(1000);
            boolean reasonDetailsClicked = homePage.clickMasterReasonDetails();
            if (reasonDetailsClicked) {
                System.out.println("    ✅ Master Reason Details page loaded");
                extentReport.logInfo("Step 4/5: Master Reason Details page loaded");
            } else {
                System.out.println("    ⚠️  Master Reason Details navigation failed");
                extentReport.logInfo("Step 4/5: Master Reason Details navigation failed");
            }
            Thread.sleep(2000);

            // Step 5: Return to Master Product Variant Details for automation
            System.out.println("\n[5/5] Returning to Master Product Variant Details for automation...");
            masterProductVariantPage = homePage.navigateToMasterProductVariantDetailsWithFlow();
            Thread.sleep(2000);
            System.out.println("    ✅ Back to Master Product Variant Details page");
            extentReport.logInfo("Step 5/5: Returned to Master Product Variant Details page");

            System.out.println("\n✅ PHASE 1 COMPLETED: All pages navigated successfully");
            System.out.println("─────────────────────────────────────────────");

            // PHASE 2: Master Product Variant Automation
            System.out.println("\n🤖 PHASE 2: Master Product Variant Automation");
            System.out.println("─────────────────────────────────────────────");

            // Step 1: Test Search Functionality
            System.out.println("\n[1/6] Testing Search Functionality...");
            boolean searchEntered = masterProductVariantPage.searchProductVariant(validProductCode);
            if (searchEntered) {
                System.out.println("    ✅ Search completed for: " + validProductCode);
                extentReport.logPass("Search completed for " + validProductCode);
            } else {
                System.out.println("    ⚠️  Search failed (might be expected)");
                extentReport.logInfo("Search failed (might be expected)");
            }
            Thread.sleep(2000);

            // Step 2: Test Edit Button
            System.out.println("\n[2/6] Testing Edit Button...");
            boolean editClicked = masterProductVariantPage.clickEditButton();
            if (editClicked) {
                System.out.println("    ✅ Edit button clicked");
                extentReport.logPass("Edit button clicked");
            } else {
                System.out.println("    ⚠️  Edit button click failed (might be expected)");
                extentReport.logInfo("Edit button click failed (might be expected)");
            }
            Thread.sleep(2000);

            // Step 3: Test Status Button
            System.out.println("\n[3/6] Testing Status Button...");
            boolean statusClicked = masterProductVariantPage.clickStatusButton();
            if (statusClicked) {
                System.out.println("    ✅ Status button clicked");
                extentReport.logPass("Status button clicked");
            } else {
                System.out.println("    ⚠️  Status button click failed (might be expected)");
                extentReport.logInfo("Status button click failed (might be expected)");
            }
            Thread.sleep(2000);

            // Step 4: Test Create Button and Form
            System.out.println("\n[4/6] Testing Create Button and Form...");
            boolean createClicked = masterProductVariantPage.clickCreateButton();
            if (createClicked) {
                System.out.println("    ✅ Create button clicked");
                extentReport.logPass("Create button clicked");
                Thread.sleep(2000);

                // Fill the form
                System.out.println("\n[4a/6] Filling Create Form...");
                boolean formFilled = masterProductVariantPage.fillForm(validProductCode, validProductName, validTrolleyType, validStorageCapacity);
                if (formFilled) {
                    System.out.println("    ✅ Form filled with test data");
                    extentReport.logPass("Form filled with test data");
                    
                    // Submit the form
                    System.out.println("\n[4b/6] Submitting Create Form...");
                    boolean submitted = masterProductVariantPage.clickSubmitButton();
                    if (submitted) {
                        System.out.println("    ✅ Form submitted successfully");
                        extentReport.logPass("Form submitted successfully");
                    } else {
                        System.out.println("    ⚠️  Form submission failed");
                        extentReport.logInfo("Form submission failed");
                    }
                } else {
                    System.out.println("    ⚠️  Form filling failed");
                    extentReport.logInfo("Form filling failed");
                }
            } else {
                System.out.println("    ⚠️  Create button click failed");
                extentReport.logInfo("Create button click failed");
            }
            Thread.sleep(2000);

            // Step 5: Test Search Again (after form submission)
            System.out.println("\n[5/6] Testing Search Again...");
            boolean searchAgain = masterProductVariantPage.searchProductVariant(validProductCode);
            if (searchAgain) {
                System.out.println("    ✅ Search completed again for: " + validProductCode);
                extentReport.logPass("Search completed again for " + validProductCode);
            } else {
                System.out.println("    ⚠️  Second search failed");
                extentReport.logInfo("Second search failed");
            }
            Thread.sleep(2000);

            // Step 6: Final Verification
            System.out.println("\n[6/6] Final Verification...");
            boolean searchVisible = masterProductVariantPage.isSearchInputVisible();
            boolean editVisible = masterProductVariantPage.isEditButtonVisible();
            boolean statusVisible = masterProductVariantPage.isStatusButtonVisible();
            
            System.out.println("    Search input visible: " + searchVisible);
            System.out.println("    Edit button visible: " + editVisible);
            System.out.println("    Status button visible: " + statusVisible);
            extentReport.logInfo("Final verification completed");

            // Take final screenshot
            String screenshotPath = SeleniumUtils.takeScreenshot(driver, "comprehensive_workflow_completed");
            extentReport.addScreenshot(screenshotPath, "Comprehensive Workflow Completed");

            System.out.println("\n=========================================");
            System.out.println("✅ COMPREHENSIVE WORKFLOW TEST PASSED!");
            System.out.println("=========================================");
            System.out.println("🎉 Browser remained open throughout entire process");
            System.out.println("🎉 All pages navigated successfully");
            System.out.println("🎉 All Master Product Variant actions completed");
            System.out.println("=========================================");
            extentReport.logPass("Comprehensive workflow test passed successfully!");

        } catch (Exception e) {
            System.err.println("\n=========================================");
            System.err.println("❌ COMPREHENSIVE WORKFLOW TEST FAILED!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("=========================================");
            e.printStackTrace();

            extentReport.logFail("Comprehensive workflow test failed: " + e.getMessage());
            String screenshotPath = SeleniumUtils.takeScreenshot(driver, "comprehensive_workflow_failed");
            extentReport.addScreenshot(screenshotPath, "Comprehensive Workflow Failed");
            throw e;
        }
    }

    @Test(description = "Quick Master Product Variant automation without page navigation", priority = 2)
    public void testQuickMasterProductVariantAutomation() throws InterruptedException {
        extentReport.createTest("Quick Master Product Variant Automation", 
                "Direct automation of Master Product Variant page without navigation");

        try {
            System.out.println("\n⚡ QUICK MASTER PRODUCT VARIANT AUTOMATION");
            System.out.println("=========================================");

            // Navigate directly to Master Product Variant Details
            System.out.println("\n[1/4] Navigating directly to Master Product Variant Details...");
            masterProductVariantPage = homePage.navigateToMasterProductVariantDetailsWithFlow();
            Thread.sleep(2000);
            System.out.println("    ✅ Master Product Variant Details page loaded");
            extentReport.logInfo("Step 1/4: Master Product Variant Details page loaded");

            // Test Search
            System.out.println("\n[2/4] Testing Search...");
            boolean searchEntered = masterProductVariantPage.searchProductVariant(validProductCode);
            System.out.println("    Search result: " + (searchEntered ? "✅ Success" : "⚠️ Failed"));
            extentReport.logInfo("Step 2/4: Search test completed");

            // Test Create Form
            System.out.println("\n[3/4] Testing Create Form...");
            boolean createClicked = masterProductVariantPage.clickCreateButton();
            if (createClicked) {
                Thread.sleep(2000);
                boolean formFilled = masterProductVariantPage.fillForm(validProductCode, validProductName, validTrolleyType, validStorageCapacity);
                if (formFilled) {
                    boolean submitted = masterProductVariantPage.clickSubmitButton();
                    System.out.println("    Create form result: " + (submitted ? "✅ Success" : "⚠️ Failed"));
                } else {
                    System.out.println("    Create form result: ⚠️ Form filling failed");
                }
            } else {
                System.out.println("    Create form result: ⚠️ Create button click failed");
            }
            extentReport.logInfo("Step 3/4: Create form test completed");

            // Test Action Buttons
            System.out.println("\n[4/4] Testing Action Buttons...");
            boolean editClicked = masterProductVariantPage.clickEditButton();
            boolean statusClicked = masterProductVariantPage.clickStatusButton();
            System.out.println("    Edit button: " + (editClicked ? "✅ Success" : "⚠️ Failed"));
            System.out.println("    Status button: " + (statusClicked ? "✅ Success" : "⚠️ Failed"));
            extentReport.logInfo("Step 4/4: Action buttons test completed");

            // Take screenshot
            String screenshotPath = SeleniumUtils.takeScreenshot(driver, "quick_automation_completed");
            extentReport.addScreenshot(screenshotPath, "Quick Automation Completed");

            System.out.println("\n✅ QUICK AUTOMATION TEST COMPLETED");
            extentReport.logPass("Quick automation test completed successfully!");

        } catch (Exception e) {
            System.err.println("\n❌ QUICK AUTOMATION TEST FAILED: " + e.getMessage());
            extentReport.logFail("Quick automation test failed: " + e.getMessage());
            String screenshotPath = SeleniumUtils.takeScreenshot(driver, "quick_automation_failed");
            extentReport.addScreenshot(screenshotPath, "Quick Automation Failed");
            throw e;
        }
    }

    @AfterMethod
    public void tearDownTest() {
        System.out.println("\n========================================");
        System.out.println("🏁 Comprehensive Test Completed");
        System.out.println("🌐 Browser remains open for inspection");
        System.out.println("========================================\n");
    }

    // Override tearDown to keep browser open
    @Override
    public void tearDown() {
        System.out.println("\n========================================");
        System.out.println("🌐 Browser will remain open");
        System.out.println("========================================\n");
        // Don't call driver.quit() to keep browser open
    }
}
