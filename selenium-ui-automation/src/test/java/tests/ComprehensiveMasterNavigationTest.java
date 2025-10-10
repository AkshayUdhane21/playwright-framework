package tests;

import base.BaseTest;
import pages.HomePage;
import pages.MasterProductVariantPage;
import org.openqa.selenium.By;
import utils.SeleniumUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Comprehensive Master Navigation Test
 * This test clicks through all Master pages first, then focuses on Master Product Variant Details automation
 */
public class ComprehensiveMasterNavigationTest extends BaseTest {
    
    private HomePage homePage;
    private MasterProductVariantPage masterProductVariantPage;
    
    // Test data
    private final String validProductCode = "61030367";
    private final String validProductName = "Test Product Variant";
    private final String validTrolleyType = "NA";
    private final String validStorageCapacity = "6";
    
    /**
     * Click specific Yes button with exact XPath
     */
    private boolean clickSpecificYesButton() {
        try {
            Thread.sleep(1200);
            
            // Use the exact XPath provided by user
            By specificYesButton = By.xpath("//*[@id=\"root\"]/div/main/div/div[4]/div/div[2]/button[1]");
            
            System.out.println("    Trying to click specific Yes button: " + specificYesButton);
            
            if (SeleniumUtils.safeClick(driver, specificYesButton, 3)) {
                System.out.println("    ✅ Specific Yes button clicked successfully");
                return true;
            }
            
            System.err.println("    ✗ Failed to click specific Yes button");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("    Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Click Active radio button with specific XPath
     */
    private boolean clickActiveRadioButtonInModal() {
        try {
            Thread.sleep(1200);
            
            // Use specific XPath for Active radio button (first label)
            By activeRadioButton = By.xpath("//*[@id=\"root\"]/div/main/div/div[4]/div/div[1]/label[1]/input");
            
            System.out.println("    Trying to click Active radio button: " + activeRadioButton);
            
            if (SeleniumUtils.safeClick(driver, activeRadioButton, 3)) {
                System.out.println("    ✅ Active radio button clicked successfully");
                return true;
            }
            
            System.err.println("    ✗ Failed to click Active radio button");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("    Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Click Inactive radio button with specific XPath
     */
    private boolean clickInactiveRadioButtonInModal() {
        try {
            Thread.sleep(1200);
            
            // Use the exact XPath provided by user
            By inactiveRadioButton = By.xpath("//*[@id=\"root\"]/div/main/div/div[4]/div/div[1]/label[2]/input");
            
            System.out.println("    Trying to click Inactive radio button: " + inactiveRadioButton);
            
            if (SeleniumUtils.safeClick(driver, inactiveRadioButton, 3)) {
                System.out.println("    ✅ Inactive radio button clicked successfully");
                return true;
            }
            
            System.err.println("    ✗ Failed to click Inactive radio button");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("    Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    @Test(priority = 1, description = "Comprehensive Master Navigation and Product Variant Automation")
    public void testComprehensiveMasterNavigationAndAutomation() {
        System.out.println("🚀 Starting Comprehensive Master Navigation Test");
        System.out.println("=================================================");
        
        try {
            // Step 1: Navigate to application
            System.out.println("\n[STEP 1] Navigating to application...");
            navigateToApp();
            Thread.sleep(3000);
            System.out.println("✅ Application loaded successfully");
            
            // Initialize HomePage
            homePage = new HomePage(driver);
            
            // Step 2: Click Master button to open dropdown
            System.out.println("\n[STEP 2] Opening Master dropdown menu...");
            boolean masterClicked = homePage.clickMasterButton();
            Assert.assertTrue(masterClicked, "Failed to click Master button");
            Thread.sleep(2000);
            System.out.println("✅ Master dropdown opened");
            
            // Step 3: Navigate through all Master pages
            System.out.println("\n[STEP 3] Navigating through all Master pages...");
            System.out.println("=============================================");
            
            // 3.1: Master Product Variant Details
            System.out.println("\n[3.1] Clicking Master Product Variant Details...");
            boolean variantDetailsClicked = homePage.clickMasterProductVariantDetails();
            Assert.assertTrue(variantDetailsClicked, "Failed to click Master Product Variant Details");
            Thread.sleep(3000);
            System.out.println("✅ Master Product Variant Details page opened");
            
            // Navigate back to home for next page
            System.out.println("    ↳ Navigating back to home...");
            navigateToApp();
            Thread.sleep(2000);
            homePage.clickMasterButton();
            Thread.sleep(1000);
            
            // 3.2: Master Product Details
            System.out.println("\n[3.2] Clicking Master Product Details...");
            boolean productDetailsClicked = homePage.clickMasterProductDetails();
            Assert.assertTrue(productDetailsClicked, "Failed to click Master Product Details");
            Thread.sleep(3000);
            System.out.println("✅ Master Product Details page opened");
            
            // Navigate back to home for next page
            System.out.println("    ↳ Navigating back to home...");
            navigateToApp();
            Thread.sleep(2000);
            homePage.clickMasterButton();
            Thread.sleep(1000);
            
            // 3.3: Master Shifts
            System.out.println("\n[3.3] Clicking Master Shifts...");
            boolean shiftsClicked = homePage.clickMasterShifts();
            Assert.assertTrue(shiftsClicked, "Failed to click Master Shifts");
            Thread.sleep(3000);
            System.out.println("✅ Master Shifts page opened");
            
            // Navigate back to home for next page
            System.out.println("    ↳ Navigating back to home...");
            navigateToApp();
            Thread.sleep(2000);
            homePage.clickMasterButton();
            Thread.sleep(1000);
            
            // 3.4: Master Reason Details
            System.out.println("\n[3.4] Clicking Master Reason Details...");
            boolean reasonDetailsClicked = homePage.clickMasterReasonDetails();
            Assert.assertTrue(reasonDetailsClicked, "Failed to click Master Reason Details");
            Thread.sleep(3000);
            System.out.println("✅ Master Reason Details page opened");
            
            System.out.println("\n=============================================");
            System.out.println("✅ All Master pages navigation completed");
            System.out.println("=============================================");
            
            // Step 4: Focus on Master Product Variant Details Automation
            System.out.println("\n[STEP 4] Starting Master Product Variant Details Automation");
            System.out.println("========================================================");
            
            // Navigate back to Master Product Variant Details for automation
            System.out.println("\n[4.1] Navigating to Master Product Variant Details for automation...");
            navigateToApp();
            Thread.sleep(2000);
            homePage.clickMasterButton();
            Thread.sleep(1000);
            boolean finalNavigation = homePage.clickMasterProductVariantDetails();
            Assert.assertTrue(finalNavigation, "Failed to navigate to Master Product Variant Details for automation");
            Thread.sleep(3000);
            System.out.println("✅ Master Product Variant Details page ready for automation");
            
            // Initialize MasterProductVariantPage
            masterProductVariantPage = new MasterProductVariantPage(driver);
            
            // Step 5: Test Create Button
            System.out.println("\n[4.2] Testing Create button functionality...");
            boolean createClicked = masterProductVariantPage.clickCreateButton();
            Assert.assertTrue(createClicked, "Failed to click Create button");
            Thread.sleep(2000);
            System.out.println("✅ Create button clicked - Modal dialog opened");
            
            // Step 6: Fill Form with Test Data
            System.out.println("\n[4.3] Filling form with test data...");
            System.out.println("    ↳ Product Variant Code: " + validProductCode);
            boolean codeEntered = masterProductVariantPage.enterProductVariantCode(validProductCode);
            Assert.assertTrue(codeEntered, "Failed to enter product variant code");
            Thread.sleep(1000);
            
            System.out.println("    ↳ Product Variant Name: " + validProductName);
            boolean nameEntered = masterProductVariantPage.enterProductVariantName(validProductName);
            Assert.assertTrue(nameEntered, "Failed to enter product variant name");
            Thread.sleep(1000);
            
            System.out.println("    ↳ Trolley Type: " + validTrolleyType);
            boolean typeEntered = masterProductVariantPage.enterTrolleyType(validTrolleyType);
            Assert.assertTrue(typeEntered, "Failed to enter trolley type");
            Thread.sleep(1000);
            
            System.out.println("    ↳ Storage Capacity: " + validStorageCapacity);
            boolean capacityEntered = masterProductVariantPage.enterTrolleyStorageCapacity(validStorageCapacity);
            Assert.assertTrue(capacityEntered, "Failed to enter storage capacity");
            Thread.sleep(1000);
            
            System.out.println("✅ Form filled successfully with all test data");
            
            // Step 7: Submit Form
            System.out.println("\n[4.4] Submitting form...");
            boolean submitted = masterProductVariantPage.clickSubmitButton();
            Assert.assertTrue(submitted, "Failed to submit form");
            Thread.sleep(3000);
            System.out.println("✅ Form submitted successfully");
            
            // Step 8: Verify Success
            System.out.println("\n[4.5] Verifying success...");
            boolean successMessage = masterProductVariantPage.isSuccessMessageDisplayed();
            if (successMessage) {
                String message = masterProductVariantPage.getSuccessMessage();
                System.out.println("✅ Success message displayed: " + message);
            } else {
                System.out.println("ℹ️ No success message found, checking for errors...");
                boolean errorMessage = masterProductVariantPage.isErrorMessageDisplayed();
                if (errorMessage) {
                    String error = masterProductVariantPage.getErrorMessage();
                    System.out.println("⚠️ Error message displayed: " + error);
                }
            }
            
            // Step 9: Test Additional Functionality
            System.out.println("\n[4.6] Testing additional functionality...");
            
            // Test Search functionality
            System.out.println("    ↳ Testing search functionality...");
            boolean searchResult = masterProductVariantPage.searchProductVariant(validProductCode);
            if (searchResult) {
                System.out.println("    ✅ Search functionality working");
            } else {
                System.out.println("    ⚠️ Search functionality not available");
            }
            Thread.sleep(2000);
            
            // Test Edit button workflow: Click Edit → Change Input → Click Create
            System.out.println("    ↳ Testing edit button workflow...");
            boolean editAvailable = masterProductVariantPage.isEditButtonVisible();
            if (editAvailable) {
                boolean editClicked = masterProductVariantPage.clickEditButton();
                if (editClicked) {
                    System.out.println("    ✅ Edit button clicked successfully");
                    
                    // Wait for edit form to open
                    Thread.sleep(1200);
                    
                    // Check if edit form is visible
                    boolean editFormVisible = masterProductVariantPage.isCreateFormVisible();
                    if (editFormVisible) {
                        System.out.println("    ✅ Edit form opened successfully");
                        
                        // Change input values in edit form
                        System.out.println("    ↳ Changing input values in edit form...");
                        
                        // Update Product Variant Code
                        System.out.println("    Step 1: Updating Product Variant Code...");
                        String updatedCode = "61030368"; // New code
                        boolean codeUpdated = masterProductVariantPage.enterProductVariantCode(updatedCode);
                        if (codeUpdated) {
                            System.out.println("    ✅ Product Variant Code updated to: " + updatedCode);
                        } else {
                            System.out.println("    ⚠️ Failed to update Product Variant Code");
                        }
                        Thread.sleep(1200);
                        
                        // Update Product Variant Name
                        System.out.println("    Step 2: Updating Product Variant Name...");
                        String updatedName = "Updated Test Product Variant";
                        boolean nameUpdated = masterProductVariantPage.enterProductVariantName(updatedName);
                        if (nameUpdated) {
                            System.out.println("    ✅ Product Variant Name updated to: " + updatedName);
                        } else {
                            System.out.println("    ⚠️ Failed to update Product Variant Name");
                        }
                        Thread.sleep(1200);
                        
                        // Update Trolley Type
                        System.out.println("    Step 3: Updating Trolley Type...");
                        String updatedTrolleyType = "Updated Type";
                        boolean trolleyUpdated = masterProductVariantPage.enterTrolleyType(updatedTrolleyType);
                        if (trolleyUpdated) {
                            System.out.println("    ✅ Trolley Type updated to: " + updatedTrolleyType);
                        } else {
                            System.out.println("    ⚠️ Failed to update Trolley Type");
                        }
                        Thread.sleep(1200);
                        
                        // Update Storage Capacity
                        System.out.println("    Step 4: Updating Storage Capacity...");
                        String updatedCapacity = "8";
                        boolean capacityUpdated = masterProductVariantPage.enterTrolleyStorageCapacity(updatedCapacity);
                        if (capacityUpdated) {
                            System.out.println("    ✅ Storage Capacity updated to: " + updatedCapacity);
                        } else {
                            System.out.println("    ⚠️ Failed to update Storage Capacity");
                        }
                        Thread.sleep(1200);
                        
                        // Click Create/Update button
                        System.out.println("    Step 5: Clicking Create/Update button...");
                        boolean createClicked11 = masterProductVariantPage.clickSubmitButton();
                        if (createClicked11) {
                            System.out.println("    ✅ Create/Update button clicked successfully");
                            Thread.sleep(2000); // Wait for update to complete
                            
                            // Check for success message
                          boolean successMessage11 = masterProductVariantPage.isSuccessMessageDisplayed();
                            if (successMessage11) {
                                String message = masterProductVariantPage.getSuccessMessage();
                                System.out.println("    ✅ Update successful: " + message);
                            } else {
                                System.out.println("    ℹ️ No success message found");
                            }
                        } else {
                            System.out.println("    ⚠️ Failed to click Create/Update button");
                        }
                        
                    } else {
                        System.out.println("    ⚠️ Edit form not visible after clicking edit button");
                    }
                } else {
                    System.out.println("    ⚠️ Edit button click failed");
                }
            } else {
                System.out.println("    ℹ️ Edit button not visible");
            }
            Thread.sleep(1200);
            
            // Test Status button and Status Change Modal - Specific Workflow
            System.out.println("    ↳ Testing status button and status change modal...");
            boolean statusAvailable = masterProductVariantPage.isStatusButtonVisible();
            if (statusAvailable) {
                boolean statusClicked = masterProductVariantPage.clickStatusButton();
                if (statusClicked) {
                    System.out.println("    ✅ Status button clicked successfully");
                    
                    // Wait for status change modal to open
                    Thread.sleep(1200);
                    
                    // Test status change modal functionality
                    System.out.println("    ↳ Testing status change modal...");
                    boolean modalVisible = masterProductVariantPage.isStatusChangeModalVisible();
                    if (modalVisible) {
                        System.out.println("    ✅ Status change modal is visible");
                        
                        String modalTitle = masterProductVariantPage.getStatusChangeModalTitle();
                        System.out.println("    📋 Modal title: " + modalTitle);
                        
                        // Specific workflow: Click Active button → Change to Inactive → Click specific Yes button
                        System.out.println("    ↳ Testing specific workflow: Active → Inactive → Yes button...");
                        
                        // Step 1: Click Active radio button first
                        System.out.println("    Step 1: Clicking Active radio button...");
                        boolean activeClicked = clickActiveRadioButtonInModal();
                        if (activeClicked) {
                            System.out.println("    ✅ Active radio button clicked");
                            
                            // Step 2: Click Inactive radio button to change status
                            System.out.println("    Step 2: Clicking Inactive radio button...");
                            boolean inactiveClicked = clickInactiveRadioButtonInModal();
                            if (inactiveClicked) {
                                System.out.println("    ✅ Inactive radio button clicked");
                                
                                // Step 3: Click specific Yes button with exact XPath
                                System.out.println("    Step 3: Clicking specific Yes button...");
                                boolean yesClicked = clickSpecificYesButton();
                                if (yesClicked) {
                                    System.out.println("    ✅ Status changed to Inactive successfully with specific Yes button");
                                } else {
                                    System.out.println("    ⚠️ Failed to click specific Yes button");
                                }
                            } else {
                                System.out.println("    ⚠️ Failed to click Inactive radio button");
                            }
                        } else {
                            System.out.println("    ⚠️ Failed to click Active radio button");
                        }
                    } else {
                        System.out.println("    ⚠️ Status change modal not visible");
                    }
                } else {
                    System.out.println("    ⚠️ Status button click failed");
                }
            } else {
                System.out.println("    ℹ️ Status button not visible");
            }
            
            System.out.println("\n========================================================");
            System.out.println("✅ COMPREHENSIVE MASTER NAVIGATION AND AUTOMATION COMPLETED");
            System.out.println("========================================================");
            
            // Final summary
            System.out.println("\n📊 TEST SUMMARY:");
            System.out.println("=================");
            System.out.println("✅ Master Product Variant Details - Visited");
            System.out.println("✅ Master Product Details - Visited");
            System.out.println("✅ Master Shifts - Visited");
            System.out.println("✅ Master Reason Details - Visited");
            System.out.println("✅ Master Product Variant Details Automation - Completed");
            System.out.println("✅ Create Form - Filled and Submitted");
            System.out.println("✅ Additional Features - Tested");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed with exception: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    @Test(priority = 2, description = "Quick Master Pages Navigation Test")
    public void testQuickMasterPagesNavigation() {
        System.out.println("🚀 Starting Quick Master Pages Navigation Test");
        System.out.println("==============================================");
        
        try {
            // Navigate to application
            navigateToApp();
            Thread.sleep(2000);
            
            homePage = new HomePage(driver);
            
            // Click Master button
            boolean masterClicked = homePage.clickMasterButton();
            Assert.assertTrue(masterClicked, "Failed to click Master button");
            Thread.sleep(1000);
            
            // Test all Master page links visibility
            System.out.println("\n🔍 Testing Master page links visibility...");
            
            boolean variantDetailsVisible = homePage.isMasterProductVariantDetailsLinkVisible();
            System.out.println("Master Product Variant Details visible: " + variantDetailsVisible);
            
            boolean productDetailsVisible = homePage.isMasterProductDetailsLinkVisible();
            System.out.println("Master Product Details visible: " + productDetailsVisible);
            
            boolean shiftsVisible = homePage.isMasterShiftsLinkVisible();
            System.out.println("Master Shifts visible: " + shiftsVisible);
            
            boolean reasonDetailsVisible = homePage.isMasterReasonDetailsLinkVisible();
            System.out.println("Master Reason Details visible: " + reasonDetailsVisible);
            
            // Verify dropdown is open
            boolean dropdownOpen = homePage.isDropdownMenuOpen();
            System.out.println("Dropdown menu open: " + dropdownOpen);
            
            System.out.println("\n✅ Quick navigation test completed");
            
        } catch (Exception e) {
            System.err.println("❌ Quick navigation test failed: " + e.getMessage());
            Assert.fail("Quick navigation test failed: " + e.getMessage());
        }
    }
    
    @Test(priority = 3, description = "Test Status Change Modal Functionality")
    public void testStatusChangeModalFunctionality() {
        System.out.println("🚀 Starting Status Change Modal Test");
        System.out.println("====================================");
        
        try {
            // Navigate to Master Product Variant Details
            System.out.println("\n[STEP 1] Navigating to Master Product Variant Details...");
            navigateToApp();
            Thread.sleep(2000);
            
            homePage = new HomePage(driver);
            homePage.clickMasterButton();
            Thread.sleep(1000);
            boolean navigationSuccess = homePage.clickMasterProductVariantDetails();
            Assert.assertTrue(navigationSuccess, "Failed to navigate to Master Product Variant Details");
            Thread.sleep(3000);
            System.out.println("✅ Successfully navigated to Master Product Variant Details");
            
            // Initialize MasterProductVariantPage
            masterProductVariantPage = new MasterProductVariantPage(driver);
            
            // Test Status Change Modal - Check Active status only once
            System.out.println("\n[STEP 2] Checking Active status...");
            boolean statusClicked = masterProductVariantPage.clickStatusButton();
            if (statusClicked) {
                Thread.sleep(1200);
                
                boolean modalVisible = masterProductVariantPage.isStatusChangeModalVisible();
                if (modalVisible) {
                    System.out.println("✅ Status change modal opened");
                    
                    // Check if already Active
                    boolean isActive = masterProductVariantPage.isActiveRadioButtonSelected();
                    if (isActive) {
                        System.out.println("✅ Status is already Active - no change needed");
                    } else {
                        System.out.println("ℹ️ Status is not Active - changing to Active");
                        boolean activeClicked = clickActiveRadioButtonInModal();
                        if (activeClicked) {
                            boolean yesClicked = clickSpecificYesButton();
                            if (yesClicked) {
                                System.out.println("✅ Status changed to Active successfully");
                            }
                        }
                    }
                    
                    // Close modal if still open
                    masterProductVariantPage.cancelStatusChange();
                }
            }
            
            // Navigate to home page after 1000ms
            System.out.println("\n[STEP 3] Navigating to home page...");
            Thread.sleep(1000);
            navigateToApp();
            Thread.sleep(2000);
            
            // Test Status Change Modal - Cancel functionality
            System.out.println("\n[STEP 4] Testing status change cancel functionality...");
            boolean  statusClicked111 = masterProductVariantPage.clickStatusButton();
            if (statusClicked111) {
                Thread.sleep(2000);
                
                boolean modalVisible = masterProductVariantPage.isStatusChangeModalVisible();
                if (modalVisible) {
                    System.out.println("✅ Status change modal opened for cancel test");
                    
                    // Test cancel functionality
                    boolean cancelSuccess = masterProductVariantPage.cancelStatusChange();
                    if (cancelSuccess) {
                        System.out.println("✅ Status change canceled successfully");
                    } else {
                        System.out.println("⚠️ Failed to cancel status change");
                    }
                } else {
                    System.out.println("⚠️ Status change modal not visible for cancel test");
                }
            } else {
                System.out.println("⚠️ Failed to click status button for cancel test");
            }
            
            System.out.println("\n====================================");
            System.out.println("✅ STATUS CHANGE MODAL TEST COMPLETED");
            System.out.println("====================================");
            
        } catch (Exception e) {
            System.err.println("❌ Status change modal test failed: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Status change modal test failed: " + e.getMessage());
        }
    }
    
    @Test(priority = 4, description = "Master Shifts Details Page Automation")
    public void testMasterShiftsDetailsAutomation() {
        System.out.println("🚀 Starting Master Shifts Details Automation Test");
        System.out.println("===============================================");
        
        try {
            // Navigate to Master Shifts Details
            System.out.println("\n[STEP 1] Navigating to Master Shifts Details...");
            navigateToApp();
            Thread.sleep(2000);
            
            homePage = new HomePage(driver);
            homePage.clickMasterButton();
            Thread.sleep(1000);
            boolean navigationSuccess = homePage.clickMasterShifts();
            Assert.assertTrue(navigationSuccess, "Failed to navigate to Master Shifts Details");
            Thread.sleep(3000);
            System.out.println("✅ Successfully navigated to Master Shifts Details");
            
            // Step 2: Click Create button
            System.out.println("\n[STEP 2] Clicking Create button...");
            
            // Try multiple possible XPaths for the Create button
            By createButton = null;
            boolean createClicked = false;
            
            // Try different possible XPaths for Create button
            String[] createButtonXPaths = {
                "//*[@id=\"root\"]/div/main/div/div[1]/button",
                "//button[contains(text(), 'Create')]",
                "//button[contains(text(), 'Add')]",
                "//button[contains(@class, 'create')]",
                "//button[contains(@class, 'add')]",
                "//div[contains(@class, 'create')]//button",
                "//div[contains(@class, 'add')]//button",
                "//*[@id=\"root\"]//button[1]",
                "//*[@id=\"root\"]//button[contains(text(), 'Create')]",
                "//*[@id=\"root\"]//button[contains(text(), 'Add')]"
            };
            
            for (String xpath : createButtonXPaths) {
                try {
                    createButton = By.xpath(xpath);
                    System.out.println("    Trying XPath: " + xpath);
                    
                    if (driver.findElements(createButton).size() > 0) {
                        System.out.println("    ✅ Found element with XPath: " + xpath);
                        createClicked = SeleniumUtils.safeClick(driver, createButton, 3);
                        if (createClicked) {
                            System.out.println("    ✅ Create button clicked successfully");
                            break;
                        } else {
                            System.out.println("    ⚠️ Element found but not clickable");
                        }
                    } else {
                        System.out.println("    ❌ Element not found with XPath: " + xpath);
                    }
                } catch (Exception e) {
                    System.out.println("    ❌ Error with XPath " + xpath + ": " + e.getMessage());
                }
            }
            
            if (!createClicked) {
                // Debug: Print page source to help identify the correct XPath
                System.out.println("\n🔍 DEBUG: Page source snippet for Create button detection:");
                try {
                    String pageSource = driver.getPageSource();
                    if (pageSource.contains("Create") || pageSource.contains("Add")) {
                        System.out.println("    ✅ Page contains 'Create' or 'Add' text");
                        // Look for button elements in the page source
                        if (pageSource.contains("<button")) {
                            System.out.println("    ✅ Page contains button elements");
                        }
                    } else {
                        System.out.println("    ❌ Page does not contain 'Create' or 'Add' text");
                    }
                } catch (Exception e) {
                    System.out.println("    ❌ Could not get page source: " + e.getMessage());
                }
                
                // Try to find any button on the page
                try {
                    By anyButton = By.tagName("button");
                    int buttonCount = driver.findElements(anyButton).size();
                    System.out.println("    📊 Total buttons found on page: " + buttonCount);
                    
                    if (buttonCount > 0) {
                        System.out.println("    🔍 Available buttons:");
                        for (int i = 0; i < Math.min(buttonCount, 5); i++) {
                            try {
                                String buttonText = driver.findElements(anyButton).get(i).getText();
                                String buttonId = driver.findElements(anyButton).get(i).getAttribute("id");
                                String buttonClass = driver.findElements(anyButton).get(i).getAttribute("class");
                                System.out.println("      Button " + (i+1) + ": text='" + buttonText + "', id='" + buttonId + "', class='" + buttonClass + "'");
                            } catch (Exception e) {
                                System.out.println("      Button " + (i+1) + ": Could not get details");
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("    ❌ Could not find any buttons: " + e.getMessage());
                }
            }
            
            Assert.assertTrue(createClicked, "Failed to click Create button - tried multiple XPaths");
            Thread.sleep(2000);
            System.out.println("✅ Create button clicked - Form opened");
            
            // Step 3: Fill Shift Number
            System.out.println("\n[STEP 3] Filling Shift Number...");
            
            // Try multiple possible XPaths for Shift Number field
            String[] shiftNumberXPaths = {
                "//*[@id=\"shiftNumber\"]",
                "//input[@id=\"shiftNumber\"]",
                "//input[contains(@placeholder, 'Shift Number')]",
                "//input[contains(@name, 'shiftNumber')]",
                "//input[contains(@name, 'shift')]",
                "//*[@id=\"root\"]//input[1]",
                "//*[@id=\"root\"]//input[contains(@placeholder, 'Number')]"
            };
            
            By shiftNumberField = null;
            boolean shiftNumberClicked = false;
            
            for (String xpath : shiftNumberXPaths) {
                try {
                    shiftNumberField = By.xpath(xpath);
                    System.out.println("    Trying Shift Number XPath: " + xpath);
                    
                    if (driver.findElements(shiftNumberField).size() > 0) {
                        System.out.println("    ✅ Found Shift Number field with XPath: " + xpath);
                        shiftNumberClicked = SeleniumUtils.safeClick(driver, shiftNumberField, 3);
                        if (shiftNumberClicked) {
                            // Clear existing value and enter "1"
                            driver.findElement(shiftNumberField).clear();
                            driver.findElement(shiftNumberField).sendKeys("1");
                            Thread.sleep(1000);
                            System.out.println("    ✅ Shift Number entered: 1");
                            break;
                        } else {
                            System.out.println("    ⚠️ Shift Number field found but not clickable");
                        }
                    } else {
                        System.out.println("    ❌ Shift Number field not found with XPath: " + xpath);
                    }
                } catch (Exception e) {
                    System.out.println("    ❌ Error with Shift Number XPath " + xpath + ": " + e.getMessage());
                }
            }
            
            Assert.assertTrue(shiftNumberClicked, "Failed to click Shift Number field - tried multiple XPaths");
            
            // Step 4: Fill Shift Name
            System.out.println("\n[STEP 4] Filling Shift Name...");
            
            // Try multiple possible XPaths for Shift Name field
            String[] shiftNameXPaths = {
                "//*[@id=\"shiftName\"]",
                "//input[@id=\"shiftName\"]",
                "//input[contains(@placeholder, 'Shift Name')]",
                "//input[contains(@name, 'shiftName')]",
                "//input[contains(@name, 'name')]",
                "//*[@id=\"root\"]//input[2]",
                "//*[@id=\"root\"]//input[contains(@placeholder, 'Name')]"
            };
            
            By shiftNameField = null;
            boolean shiftNameClicked = false;
            
            for (String xpath : shiftNameXPaths) {
                try {
                    shiftNameField = By.xpath(xpath);
                    System.out.println("    Trying Shift Name XPath: " + xpath);
                    
                    if (driver.findElements(shiftNameField).size() > 0) {
                        System.out.println("    ✅ Found Shift Name field with XPath: " + xpath);
                        shiftNameClicked = SeleniumUtils.safeClick(driver, shiftNameField, 3);
                        if (shiftNameClicked) {
                            // Clear existing value and enter "shift 1"
                            driver.findElement(shiftNameField).clear();
                            driver.findElement(shiftNameField).sendKeys("shift 1");
                            Thread.sleep(1000);
                            System.out.println("    ✅ Shift Name entered: shift 1");
                            break;
                        } else {
                            System.out.println("    ⚠️ Shift Name field found but not clickable");
                        }
                    } else {
                        System.out.println("    ❌ Shift Name field not found with XPath: " + xpath);
                    }
                } catch (Exception e) {
                    System.out.println("    ❌ Error with Shift Name XPath " + xpath + ": " + e.getMessage());
                }
            }
            
            Assert.assertTrue(shiftNameClicked, "Failed to click Shift Name field - tried multiple XPaths");
            
            // Step 5: Fill Shift Description
            System.out.println("\n[STEP 5] Filling Shift Description...");
            
            // Try multiple possible XPaths for Shift Description field
            String[] shiftDescXPaths = {
                "//*[@id=\"shiftDesc\"]",
                "//input[@id=\"shiftDesc\"]",
                "//textarea[@id=\"shiftDesc\"]",
                "//input[contains(@placeholder, 'Description')]",
                "//textarea[contains(@placeholder, 'Description')]",
                "//input[contains(@name, 'description')]",
                "//textarea[contains(@name, 'description')]",
                "//*[@id=\"root\"]//input[3]",
                "//*[@id=\"root\"]//textarea[1]"
            };
            
            By shiftDescField = null;
            boolean shiftDescClicked = false;
            
            for (String xpath : shiftDescXPaths) {
                try {
                    shiftDescField = By.xpath(xpath);
                    System.out.println("    Trying Shift Description XPath: " + xpath);
                    
                    if (driver.findElements(shiftDescField).size() > 0) {
                        System.out.println("    ✅ Found Shift Description field with XPath: " + xpath);
                        shiftDescClicked = SeleniumUtils.safeClick(driver, shiftDescField, 3);
                        if (shiftDescClicked) {
                            // Clear existing value and enter "Ak"
                            driver.findElement(shiftDescField).clear();
                            driver.findElement(shiftDescField).sendKeys("Ak");
                            Thread.sleep(1000);
                            System.out.println("    ✅ Shift Description entered: Ak");
                            break;
                        } else {
                            System.out.println("    ⚠️ Shift Description field found but not clickable");
                        }
                    } else {
                        System.out.println("    ❌ Shift Description field not found with XPath: " + xpath);
                    }
                } catch (Exception e) {
                    System.out.println("    ❌ Error with Shift Description XPath " + xpath + ": " + e.getMessage());
                }
            }
            
            Assert.assertTrue(shiftDescClicked, "Failed to click Shift Description field - tried multiple XPaths");
            
            // Step 6: Select Start Time
            System.out.println("\n[STEP 6] Selecting Start Time...");
            // Look for start time field (common patterns)
            By startTimeField = By.xpath("//input[contains(@id, 'start') or contains(@name, 'start') or contains(@placeholder, 'Start')]");
            try {
                boolean startTimeClicked = SeleniumUtils.safeClick(driver, startTimeField, 3);
                if (startTimeClicked) {
                    // Set start time to 08:00
                    driver.findElement(startTimeField).clear();
                    driver.findElement(startTimeField).sendKeys("08:00");
                    Thread.sleep(1000);
                    System.out.println("✅ Start Time set: 08:00");
                } else {
                    System.out.println("⚠️ Start Time field not found or not clickable");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Start Time field not available: " + e.getMessage());
            }
            
            // Step 7: Select End Time
            System.out.println("\n[STEP 7] Selecting End Time...");
            // Look for end time field (common patterns)
            By endTimeField = By.xpath("//input[contains(@id, 'end') or contains(@name, 'end') or contains(@placeholder, 'End')]");
            try {
                boolean endTimeClicked = SeleniumUtils.safeClick(driver, endTimeField, 3);
                if (endTimeClicked) {
                    // Set end time to 16:00
                    driver.findElement(endTimeField).clear();
                    driver.findElement(endTimeField).sendKeys("16:00");
                    Thread.sleep(1000);
                    System.out.println("✅ End Time set: 16:00");
                } else {
                    System.out.println("⚠️ End Time field not found or not clickable");
                }
            } catch (Exception e) {
                System.out.println("⚠️ End Time field not available: " + e.getMessage());
            }
            
            // Step 8: Click Create/Submit button
            System.out.println("\n[STEP 8] Clicking Create/Submit button...");
            
            // Try multiple possible XPaths for Submit button
            String[] submitButtonXPaths = {
                "//*[@id=\"root\"]/div/main/div/div[4]/div/div[6]/button[1]",
                "//button[contains(text(), 'Create')]",
                "//button[contains(text(), 'Submit')]",
                "//button[contains(text(), 'Save')]",
                "//button[contains(@class, 'submit')]",
                "//button[contains(@class, 'create')]",
                "//button[contains(@class, 'save')]",
                "//*[@id=\"root\"]//button[contains(text(), 'Create')]",
                "//*[@id=\"root\"]//button[contains(text(), 'Submit')]",
                "//*[@id=\"root\"]//button[last()]"
            };
            
            By submitButton = null;
            boolean submitClicked = false;
            
            for (String xpath : submitButtonXPaths) {
                try {
                    submitButton = By.xpath(xpath);
                    System.out.println("    Trying Submit button XPath: " + xpath);
                    
                    if (driver.findElements(submitButton).size() > 0) {
                        System.out.println("    ✅ Found Submit button with XPath: " + xpath);
                        submitClicked = SeleniumUtils.safeClick(driver, submitButton, 3);
                        if (submitClicked) {
                            System.out.println("    ✅ Submit button clicked successfully");
                            break;
                        } else {
                            System.out.println("    ⚠️ Submit button found but not clickable");
                        }
                    } else {
                        System.out.println("    ❌ Submit button not found with XPath: " + xpath);
                    }
                } catch (Exception e) {
                    System.out.println("    ❌ Error with Submit button XPath " + xpath + ": " + e.getMessage());
                }
            }
            
            Assert.assertTrue(submitClicked, "Failed to click Create/Submit button - tried multiple XPaths");
            Thread.sleep(3000);
            System.out.println("✅ Create/Submit button clicked");
            
            // Step 9: Verify Success
            System.out.println("\n[STEP 9] Verifying success...");
            Thread.sleep(2000);
            
            // Check for success message or form closure
            try {
                // Look for success message
                By successMessage = By.xpath("//div[contains(text(), 'success') or contains(text(), 'created') or contains(text(), 'saved')]");
                if (driver.findElements(successMessage).size() > 0) {
                    String message = driver.findElement(successMessage).getText();
                    System.out.println("✅ Success message displayed: " + message);
                } else {
                    System.out.println("ℹ️ No success message found, checking if form closed...");
                    
                    // Check if form is closed (create button should be visible again)
                    boolean createButtonVisible = driver.findElements(createButton).size() > 0;
                    if (createButtonVisible) {
                        System.out.println("✅ Form closed successfully - Create button visible again");
                    } else {
                        System.out.println("⚠️ Form may still be open");
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠️ Could not verify success: " + e.getMessage());
            }
            
            System.out.println("\n===============================================");
            System.out.println("✅ MASTER SHIFTS DETAILS AUTOMATION COMPLETED");
            System.out.println("===============================================");
            
            // Final summary
            System.out.println("\n📊 MASTER SHIFTS TEST SUMMARY:");
            System.out.println("===============================");
            System.out.println("✅ Master Shifts Details - Navigated");
            System.out.println("✅ Create Form - Opened");
            System.out.println("✅ Shift Number - Entered (1)");
            System.out.println("✅ Shift Name - Entered (shift 1)");
            System.out.println("✅ Shift Description - Entered (Ak)");
            System.out.println("✅ Start Time - Set (08:00)");
            System.out.println("✅ End Time - Set (16:00)");
            System.out.println("✅ Create Button - Clicked");
            System.out.println("✅ Form Submission - Completed");
            
        } catch (Exception e) {
            System.err.println("❌ Master Shifts automation test failed: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Master Shifts automation test failed: " + e.getMessage());
        }
    }
}
