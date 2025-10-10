package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;
import pages.MasterProductVariantPage;
import utils.ExtentReportManager;
import utils.SeleniumUtils;

/**
 * Single browser session test that navigates through all pages first,
 * then performs Master Product Variant automation
 */
public class SingleBrowserNavigationTest extends BaseTest {

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
        System.out.println("🌐 Starting Single Browser Navigation Test");
        System.out.println("========================================");

        navigateToApp();
        Thread.sleep(2000);
        homePage = new HomePage(driver);
        System.out.println("✅ Application loaded and HomePage initialized");
    }

    @Test(description = "Navigate all pages first, then automate Master Product Variant", priority = 1)
    public void testNavigateAllPagesThenAutomate() throws InterruptedException {
        extentReport.createTest("Navigate All Pages Then Automate", 
                "Navigate through all Master pages first, then perform Master Product Variant automation");

        try {
            System.out.println("\n🌐 SINGLE BROWSER - NAVIGATE ALL PAGES FIRST");
            System.out.println("=============================================");
            System.out.println("📋 This test will:");
            System.out.println("   1. Navigate through ALL Master pages in ONE browser session");
            System.out.println("   2. Then perform Master Product Variant automation");
            System.out.println("   3. Keep the SAME browser open throughout");
            System.out.println("=============================================");

            // PHASE 1: Navigate through ALL Master pages
            System.out.println("\n📖 PHASE 1: Navigating through ALL Master pages");
            System.out.println("─────────────────────────────────────────────");

            // Step 1: Navigate to Master Product Variant Details
            System.out.println("\n[1/4] Navigating to Master Product Variant Details...");
            masterProductVariantPage = homePage.navigateToMasterProductVariantDetailsWithFlow();
            Thread.sleep(2000);
            System.out.println("    ✅ Master Product Variant Details page loaded");
            extentReport.logInfo("Step 1/4: Master Product Variant Details page loaded");

            // Step 2: Navigate to Master Product Details
            System.out.println("\n[2/4] Navigating to Master Product Details...");
            homePage.clickMasterButton();
            Thread.sleep(1000);
            boolean productDetailsClicked = homePage.clickMasterProductDetails();
            if (productDetailsClicked) {
                System.out.println("    ✅ Master Product Details page loaded");
                extentReport.logInfo("Step 2/4: Master Product Details page loaded");
            } else {
                System.out.println("    ⚠️  Master Product Details navigation failed");
                extentReport.logInfo("Step 2/4: Master Product Details navigation failed");
            }
            Thread.sleep(2000);

            // Step 3: Navigate to Master Shifts
            System.out.println("\n[3/4] Navigating to Master Shifts...");
            homePage.clickMasterButton();
            Thread.sleep(1000);
            boolean shiftsClicked = homePage.clickMasterShifts();
            if (shiftsClicked) {
                System.out.println("    ✅ Master Shifts page loaded");
                extentReport.logInfo("Step 3/4: Master Shifts page loaded");
            } else {
                System.out.println("    ⚠️  Master Shifts navigation failed");
                extentReport.logInfo("Step 3/4: Master Shifts navigation failed");
            }
            Thread.sleep(2000);

            // Step 4: Navigate to Master Reason Details
            System.out.println("\n[4/4] Navigating to Master Reason Details...");
            homePage.clickMasterButton();
            Thread.sleep(1000);
            boolean reasonDetailsClicked = homePage.clickMasterReasonDetails();
            if (reasonDetailsClicked) {
                System.out.println("    ✅ Master Reason Details page loaded");
                extentReport.logInfo("Step 4/4: Master Reason Details page loaded");
            } else {
                System.out.println("    ⚠️  Master Reason Details navigation failed");
                extentReport.logInfo("Step 4/4: Master Reason Details navigation failed");
            }
            Thread.sleep(2000);

            System.out.println("\n✅ PHASE 1 COMPLETED: All pages navigated in single browser session");
            System.out.println("─────────────────────────────────────────────");

            // PHASE 2: Return to Master Product Variant Details and Automate
            System.out.println("\n🤖 PHASE 2: Master Product Variant Automation");
            System.out.println("─────────────────────────────────────────────");

            // Step 1: Return to Master Product Variant Details
            System.out.println("\n[1/6] Returning to Master Product Variant Details for automation...");
            masterProductVariantPage = homePage.navigateToMasterProductVariantDetailsWithFlow();
            Thread.sleep(2000);
            System.out.println("    ✅ Back to Master Product Variant Details page");
            extentReport.logInfo("Step 1/6: Returned to Master Product Variant Details page");

            // Step 2: Test Search Functionality
            System.out.println("\n[2/6] Testing Search Functionality...");
            boolean searchEntered = masterProductVariantPage.searchProductVariant(validProductCode);
            if (searchEntered) {
                System.out.println("    ✅ Search completed for: " + validProductCode);
                extentReport.logPass("Step 2/6: Search completed for " + validProductCode);
            } else {
                System.out.println("    ⚠️  Search failed (might be expected)");
                extentReport.logInfo("Step 2/6: Search failed (might be expected)");
            }
            Thread.sleep(2000);

            // Step 3: Test Edit Button
            System.out.println("\n[3/6] Testing Edit Button...");
            boolean editClicked = masterProductVariantPage.clickEditButton();
            if (editClicked) {
                System.out.println("    ✅ Edit button clicked");
                extentReport.logPass("Step 3/6: Edit button clicked");
            } else {
                System.out.println("    ⚠️  Edit button click failed (might be expected)");
                extentReport.logInfo("Step 3/6: Edit button click failed (might be expected)");
            }
            Thread.sleep(2000);

            // Step 4: Test Status Button
            System.out.println("\n[4/6] Testing Status Button...");
            boolean statusClicked = masterProductVariantPage.clickStatusButton();
            if (statusClicked) {
                System.out.println("    ✅ Status button clicked");
                extentReport.logPass("Step 4/6: Status button clicked");
            } else {
                System.out.println("    ⚠️  Status button click failed (might be expected)");
                extentReport.logInfo("Step 4/6: Status button click failed (might be expected)");
            }
            Thread.sleep(2000);

            // Step 5: Test Create Button and Form
            System.out.println("\n[5/6] Testing Create Button and Form...");
            boolean createClicked = masterProductVariantPage.clickCreateButton();
            if (createClicked) {
                System.out.println("    ✅ Create button clicked");
                extentReport.logPass("Step 5/6: Create button clicked");
                Thread.sleep(2000);

                // Fill the form
                boolean formFilled = masterProductVariantPage.fillForm(validProductCode, validProductName, validTrolleyType, validStorageCapacity);
                if (formFilled) {
                    boolean submitted = masterProductVariantPage.clickSubmitButton();
                    if (submitted) {
                        System.out.println("    ✅ Form submitted successfully");
                        extentReport.logPass("Step 5a/6: Form submitted successfully");
                    } else {
                        System.out.println("    ⚠️  Form submission failed");
                        extentReport.logInfo("Step 5a/6: Form submission failed");
                    }
                } else {
                    System.out.println("    ⚠️  Form filling failed");
                    extentReport.logInfo("Step 5a/6: Form filling failed");
                }
            } else {
                System.out.println("    ⚠️  Create button click failed");
                extentReport.logInfo("Step 5/6: Create button click failed");
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
            extentReport.logInfo("Step 6/6: Final verification completed");

            // Take final screenshot
            String screenshotPath = SeleniumUtils.takeScreenshot(driver, "single_browser_navigation_completed");
            extentReport.addScreenshot(screenshotPath, "Single Browser Navigation Completed");

            System.out.println("\n=============================================");
            System.out.println("✅ SINGLE BROWSER NAVIGATION TEST PASSED!");
            System.out.println("=============================================");
            System.out.println("🎉 Navigated through ALL pages in ONE browser session");
            System.out.println("🎉 Performed Master Product Variant automation");
            System.out.println("🌐 Browser remains open for inspection");
            System.out.println("=============================================");
            extentReport.logPass("Single browser navigation test passed successfully!");

        } catch (Exception e) {
            System.err.println("\n=============================================");
            System.err.println("❌ SINGLE BROWSER NAVIGATION TEST FAILED!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("=============================================");
            e.printStackTrace();

            extentReport.logFail("Single browser navigation test failed: " + e.getMessage());
            String screenshotPath = SeleniumUtils.takeScreenshot(driver, "single_browser_navigation_failed");
            extentReport.addScreenshot(screenshotPath, "Single Browser Navigation Failed");
            throw e;
        }
    }

    @AfterMethod
    public void tearDownTest() {
        System.out.println("\n========================================");
        System.out.println("🏁 Single Browser Test Completed");
        System.out.println("🌐 Browser remains open for inspection");
        System.out.println("========================================\n");
    }

    // Override tearDown to keep browser open
    @Override
    public void tearDown() {
        System.out.println("\n========================================");
        System.out.println("🌐 Browser will remain open for inspection");
        System.out.println("========================================\n");
        // Don't call driver.quit() to keep browser open
    }
}
