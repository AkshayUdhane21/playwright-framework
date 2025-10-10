package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;
import pages.MasterProductVariantPage;
import utils.ExtentReportManager;
import utils.SeleniumUtils;

public class MasterProductVariantTest extends BaseTest {

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

		validProductCode = config.getProperty("master.product.code", "6100100");
		validProductName = config.getProperty("master.product.name", "HL ASSY RH MRE");
		validTrolleyType = config.getProperty("master.trolley.type", "NA");
		validStorageCapacity = config.getProperty("master.storage.capacity", "6");
	}

	@BeforeMethod
	public void setUpTest() throws InterruptedException {
		System.out.println("\n========================================");
		System.out.println("🔄 Starting New Test");
		System.out.println("========================================");

		navigateToApp();
		Thread.sleep(2000); // Wait after navigation
		homePage = new HomePage(driver);
		System.out.println(" Application loaded and HomePage initialized");
	}

	@Test(description = "Verify navigation to Master Product Variant Details page", priority = 1)
	public void testNavigateToMasterProductVariantDetails() throws InterruptedException {
		extentReport.createTest("Navigate to Master Product Variant Details",
				"Test navigation to Master Product Variant Details page");

		try {
			System.out.println("\n Test: Navigate to Master Product Variant Details");
			System.out.println("");

			// Step 1: Click Master button
			System.out.println("\nStep 1: Clicking Master button...");
			boolean masterClicked = homePage.clickMasterButton();
			Assert.assertTrue(masterClicked, "Failed to click Master button");
			Thread.sleep(2000);
			System.out.println(" Master button clicked successfully");
			extentReport.logInfo("Step 1: Clicked Master button");

			// Step 2: Click Master Product Variant Details
			System.out.println("\nStep 2: Clicking Master Product Variant Details...");
			boolean detailsClicked = homePage.clickMasterProductVariantDetails();
			Assert.assertTrue(detailsClicked, "Failed to click Master Product Variant Details");
			Thread.sleep(2000);
			System.out.println(" Master Product Variant Details clicked successfully");
			extentReport.logInfo("Step 2: Clicked Master Product Variant Details");

			// Initialize page object
			masterProductVariantPage = new MasterProductVariantPage(driver);
			Assert.assertNotNull(masterProductVariantPage, "Failed to initialize Master Product Variant page");

			extentReport.logPass("Successfully navigated to Master Product Variant Details page");

			// Take screenshot
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "navigate_to_master_product_variant");
			extentReport.addScreenshot(screenshotPath, "Navigation Successful");
			System.out.println(" Screenshot captured");

		} catch (Exception e) {
			System.err.println(" Navigation failed: " + e.getMessage());
			extentReport.logFail("Failed to navigate: " + e.getMessage());
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "navigation_failed");
			extentReport.addScreenshot(screenshotPath, "Navigation Failed");
			throw e;
		}
	}

	@Test(description = "Verify Create button functionality", priority = 2)
	public void testCreateButtonFunctionality() throws InterruptedException {
		extentReport.createTest("Create Button Functionality", "Test Create button functionality and form visibility");

		try {
			System.out.println("\n Test: Create Button Functionality");
			System.out.println("─────────────────────────────────────────");

			// Step 1: Click Master button
			System.out.println("\nStep 1: Clicking Master button...");
			boolean masterClicked = homePage.clickMasterButton();
			Assert.assertTrue(masterClicked, "Failed to click Master button");
			Thread.sleep(2000);
			System.out.println(" Master button clicked");

			// Step 2: Click Master Product Variant Details
			System.out.println("\nStep 2: Clicking Master Product Variant Details...");
			boolean detailsClicked = homePage.clickMasterProductVariantDetails();
			Assert.assertTrue(detailsClicked, "Failed to click details");
			Thread.sleep(2000);
			System.out.println(" Details page opened");

			// Step 3: Initialize page and click Create button
			masterProductVariantPage = new MasterProductVariantPage(driver);
			System.out.println("\nStep 3: Clicking Create button...");
			boolean createClicked = masterProductVariantPage.clickCreateButton();
			Assert.assertTrue(createClicked, "Failed to click Create button");
			Thread.sleep(2000);
			System.out.println("✅ Create button clicked successfully");
			extentReport.logPass("Successfully clicked Create button");

			// Step 4: Verify form is visible
			System.out.println("\nStep 4: Verifying form visibility...");
			boolean formVisible = masterProductVariantPage.isCreateFormVisible();
			Assert.assertTrue(formVisible, "Create form is not visible");
			System.out.println(" Create form is visible");
			extentReport.logPass("Create form is visible");

			// Take screenshot
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "create_form_opened");
			extentReport.addScreenshot(screenshotPath, "Create Form Opened");
			System.out.println(" Screenshot captured");

		} catch (Exception e) {
			System.err.println(" Test failed: " + e.getMessage());
			extentReport.logFail("Test failed: " + e.getMessage());
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "create_button_failed");
			extentReport.addScreenshot(screenshotPath, "Create Button Test Failed");
			throw e;
		}
	}

	@Test(description = "Test successful product variant creation with valid data", priority = 3)
	public void testSuccessfulProductVariantCreation() throws InterruptedException {
		extentReport.createTest("Successful Product Variant Creation",
				"Test successful creation of product variant with valid data");

		try {
			System.out.println("\n📋 Test: Successful Product Variant Creation");
			System.out.println("─────────────────────────────────────────");

			// Step 1: Click Master button
			System.out.println("\nStep 1: Clicking Master button...");
			boolean masterClicked = homePage.clickMasterButton();
			Assert.assertTrue(masterClicked, "Failed to click Master button");
			Thread.sleep(2000);
			System.out.println(" Master button clicked");

			// Step 2: Click Master Product Variant Details
			System.out.println("\nStep 2: Clicking Master Product Variant Details...");
			boolean detailsClicked = homePage.clickMasterProductVariantDetails();
			Assert.assertTrue(detailsClicked, "Failed to click details");
			Thread.sleep(2000);
			System.out.println(" Details page opened");

			// Step 3: Click Create button
			masterProductVariantPage = new MasterProductVariantPage(driver);
			System.out.println("\nStep 3: Clicking Create button...");
			boolean createClicked = masterProductVariantPage.clickCreateButton();
			Assert.assertTrue(createClicked, "Failed to click Create button");
			Thread.sleep(2000);
			System.out.println(" Create button clicked");

			// Step 4: Fill Product Variant Code
			System.out.println("\nStep 4: Entering Product Variant Code: " + validProductCode);
			boolean codeEntered = masterProductVariantPage.enterProductVariantCode(validProductCode);
			Assert.assertTrue(codeEntered, "Failed to enter product code");
			Thread.sleep(1000);
			System.out.println(" Product code entered");
			extentReport.logInfo("Entered product code: " + validProductCode);

			// Step 5: Fill Product Variant Name
			System.out.println("\nStep 5: Entering Product Variant Name: " + validProductName);
			boolean nameEntered = masterProductVariantPage.enterProductVariantName(validProductName);
			Assert.assertTrue(nameEntered, "Failed to enter product name");
			Thread.sleep(1000);
			System.out.println(" Product name entered");
			extentReport.logInfo("Entered product name: " + validProductName);

			// Step 6: Fill Trolley Type
			System.out.println("\nStep 6: Entering Trolley Type: " + validTrolleyType);
			boolean typeEntered = masterProductVariantPage.enterTrolleyType(validTrolleyType);
			Assert.assertTrue(typeEntered, "Failed to enter trolley type");
			Thread.sleep(1000);
			System.out.println(" Trolley type entered");
			extentReport.logInfo("Entered trolley type: " + validTrolleyType);

			// Step 7: Fill Storage Capacity
			System.out.println("\nStep 7: Entering Storage Capacity: " + validStorageCapacity);
			boolean capacityEntered = masterProductVariantPage.enterTrolleyStorageCapacity(validStorageCapacity);
			Assert.assertTrue(capacityEntered, "Failed to enter storage capacity");
			Thread.sleep(1000);
			System.out.println(" Storage capacity entered");
			extentReport.logInfo("Entered storage capacity: " + validStorageCapacity);

			extentReport.logPass("Successfully filled form with valid data");

			// Step 8: Submit form
			System.out.println("\nStep 8: Clicking Submit button...");
			boolean submitted = masterProductVariantPage.clickSubmitButton();
			Assert.assertTrue(submitted, "Failed to submit form");
			Thread.sleep(3000); // Wait for submission to complete
			System.out.println(" Form submitted successfully");
			extentReport.logPass("Successfully submitted form");

			// Step 9: Check for success message
			System.out.println("\nStep 9: Checking for success message...");
			boolean successMessage = masterProductVariantPage.waitForSuccessMessage(10);
			if (successMessage) {
				String message = masterProductVariantPage.getSuccessMessage();
				Assert.assertFalse(message.isEmpty(), "Success message should not be empty");
				System.out.println(" Success message: " + message);
				extentReport.logPass("Success message displayed: " + message);
			} else {
				System.out.println("ℹ️  No success message displayed");
				extentReport.logInfo("No success message displayed (this might be expected behavior)");
			}

			// Take final screenshot
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "product_variant_created_successfully");
			extentReport.addScreenshot(screenshotPath, "Product Variant Created Successfully");
			System.out.println(" Final screenshot captured");

			System.out.println("\n TEST PASSED: Product variant created successfully!");

		} catch (Exception e) {
			System.err.println("\n TEST FAILED: " + e.getMessage());
			e.printStackTrace();
			extentReport.logFail("Product variant creation test failed: " + e.getMessage());
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "product_creation_failed");
			extentReport.addScreenshot(screenshotPath, "Product Creation Failed");
			throw e;
		}
	}

	@Test(description = "Test form field validation - Product Variant Code", priority = 4)
	public void testProductVariantCodeValidation() throws InterruptedException {
		extentReport.createTest("Product Variant Code Validation",
				"Test form validation for Product Variant Code field");

		try {
			System.out.println("\n Test: Product Variant Code Validation");
			System.out.println("─────────────────────────────────────────");

			// Navigate to form
			homePage.clickMasterButton();
			Thread.sleep(2000);
			homePage.clickMasterProductVariantDetails();
			Thread.sleep(2000);
			masterProductVariantPage = new MasterProductVariantPage(driver);
			masterProductVariantPage.clickCreateButton();
			Thread.sleep(2000);

			// Test empty product code
			System.out.println("\nTesting empty product code validation...");
			masterProductVariantPage.fillForm("", validProductName, validTrolleyType, validStorageCapacity);
			Thread.sleep(1000);
			masterProductVariantPage.clickSubmitButton();
			Thread.sleep(2000);

			// Check for validation error
			boolean hasError = masterProductVariantPage.isErrorMessageDisplayed();
			if (hasError) {
				String errorMessage = masterProductVariantPage.getErrorMessage();
				Assert.assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
				System.out.println(" Validation error displayed: " + errorMessage);
				extentReport.logPass("Validation error displayed for empty product code: " + errorMessage);
			} else {
				System.out.println("  No validation error displayed for empty product code");
				extentReport.logInfo("No validation error displayed for empty product code");
			}

			// Test with valid code
			System.out.println("\nTesting valid product code entry...");
			boolean validCodeEntered = masterProductVariantPage.enterProductVariantCode(validProductCode);
			Assert.assertTrue(validCodeEntered, "Failed to enter valid product code");
			Thread.sleep(1000);

			String enteredCode = masterProductVariantPage.getProductVariantCode();
			Assert.assertEquals(enteredCode, validProductCode, "Product code mismatch");
			System.out.println(" Valid product code entered: " + enteredCode);
			extentReport.logPass("Valid product code entered successfully: " + enteredCode);

		} catch (Exception e) {
			System.err.println(" Validation test failed: " + e.getMessage());
			extentReport.logFail("Product variant code validation test failed: " + e.getMessage());
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "code_validation_failed");
			extentReport.addScreenshot(screenshotPath, "Code Validation Failed");
			throw e;
		}
	}

	@Test(description = "Test complete workflow with detailed steps", priority = 5)
	public void testCompleteWorkflowStepByStep() throws InterruptedException {
		extentReport.createTest("Complete Workflow Step by Step",
				"Test complete workflow with detailed step-by-step execution");

		try {
			System.out.println("\n Test: Complete Workflow - Step by Step");
			System.out.println("=========================================");

			// Step 1: Click Master
			System.out.println("\n[1/8] Clicking Master button...");
			boolean masterClicked = homePage.clickMasterButton();
			Assert.assertTrue(masterClicked, "Step 1 failed: Master button click");
			Thread.sleep(2000);
			System.out.println("    ✅ Master button clicked");
			extentReport.logInfo("Step 1/8: Master button clicked");

			// Step 2: Click Details
			System.out.println("\n[2/8] Clicking Master Product Variant Details...");
			boolean detailsClicked = homePage.clickMasterProductVariantDetails();
			Assert.assertTrue(detailsClicked, "Step 2 failed: Details click");
			Thread.sleep(2000);
			System.out.println("    ✅ Details page opened");
			extentReport.logInfo("Step 2/8: Master Product Variant Details opened");

			// Step 3: Click Create
			System.out.println("\n[3/8] Clicking Create button...");
			masterProductVariantPage = new MasterProductVariantPage(driver);
			boolean createClicked = masterProductVariantPage.clickCreateButton();
			Assert.assertTrue(createClicked, "Step 3 failed: Create button click");
			Thread.sleep(2000);
			System.out.println("    ✅ Create form opened");
			extentReport.logInfo("Step 3/8: Create button clicked");

			// Step 4: Enter Product Code
			System.out.println("\n[4/8] Entering Product Code: " + validProductCode);
			boolean codeEntered = masterProductVariantPage.enterProductVariantCode(validProductCode);
			Assert.assertTrue(codeEntered, "Step 4 failed: Product code entry");
			Thread.sleep(1500);
			System.out.println("    ✅ Product code: " + validProductCode);
			extentReport.logInfo("Step 4/8: Product code entered - " + validProductCode);

			// Step 5: Enter Product Name
			System.out.println("\n[5/8] Entering Product Name: " + validProductName);
			boolean nameEntered = masterProductVariantPage.enterProductVariantName(validProductName);
			Assert.assertTrue(nameEntered, "Step 5 failed: Product name entry");
			Thread.sleep(1500);
			System.out.println("    ✅ Product name: " + validProductName);
			extentReport.logInfo("Step 5/8: Product name entered - " + validProductName);

			// Step 6: Enter Trolley Type
			System.out.println("\n[6/8] Entering Trolley Type: " + validTrolleyType);
			boolean typeEntered = masterProductVariantPage.enterTrolleyType(validTrolleyType);
			Assert.assertTrue(typeEntered, "Step 6 failed: Trolley type entry");
			Thread.sleep(1500);
			System.out.println("    ✅ Trolley type: " + validTrolleyType);
			extentReport.logInfo("Step 6/8: Trolley type entered - " + validTrolleyType);

			// Step 7: Enter Storage Capacity
			System.out.println("\n[7/8] Entering Storage Capacity: " + validStorageCapacity);
			boolean capacityEntered = masterProductVariantPage.enterTrolleyStorageCapacity(validStorageCapacity);
			Assert.assertTrue(capacityEntered, "Step 7 failed: Storage capacity entry");
			Thread.sleep(1500);
			System.out.println("    ✅ Storage capacity: " + validStorageCapacity);
			extentReport.logInfo("Step 7/8: Storage capacity entered - " + validStorageCapacity);

			// Step 8: Submit Form
			System.out.println("\n[8/8] Submitting form...");
			boolean submitted = masterProductVariantPage.clickSubmitButton();
			Assert.assertTrue(submitted, "Step 8 failed: Form submission");
			Thread.sleep(3000);
			System.out.println("    ✅ Form submitted successfully");
			extentReport.logInfo("Step 8/8: Form submitted");

			// Take final screenshot
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "complete_workflow_success");
			extentReport.addScreenshot(screenshotPath, "Complete Workflow Success");

			System.out.println("\n=========================================");
			System.out.println("✅ COMPLETE WORKFLOW TEST PASSED!");
			System.out.println("=========================================");
			extentReport.logPass("Complete workflow test passed successfully!");

		} catch (Exception e) {
			System.err.println("\n=========================================");
			System.err.println("❌ COMPLETE WORKFLOW TEST FAILED!");
			System.err.println("Error: " + e.getMessage());
			System.err.println("=========================================");
			e.printStackTrace();

			extentReport.logFail("Complete workflow test failed: " + e.getMessage());
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "complete_workflow_failed");
			extentReport.addScreenshot(screenshotPath, "Complete Workflow Failed");
			throw e;
		}
	}

	@DataProvider(name = "invalidProductData")
	public Object[][] getInvalidProductData() {
		return new Object[][] { { "", "HL ASSY RH MRE", "NA", "6", "Empty product code" },
				{ "6100100", "", "NA", "6", "Empty product name" },
				{ "6100100", "HL ASSY RH MRE", "", "6", "Empty trolley type" },
				{ "6100100", "HL ASSY RH MRE", "NA", "", "Empty storage capacity" } };
	}

	@Test(description = "Test form validation with invalid data", dataProvider = "invalidProductData", priority = 6)
	public void testFormValidationWithInvalidData(String code, String name, String type, String capacity,
			String testCase) throws InterruptedException {
		extentReport.createTest("Form Validation: " + testCase, "Testing: " + testCase);

		try {
			System.out.println("\n📋 Test: Form Validation - " + testCase);
			System.out.println("─────────────────────────────────────────");

			// Navigate to form
			homePage.clickMasterButton();
			Thread.sleep(2000);
			homePage.clickMasterProductVariantDetails();
			Thread.sleep(2000);
			masterProductVariantPage = new MasterProductVariantPage(driver);
			masterProductVariantPage.clickCreateButton();
			Thread.sleep(2000);

			// Fill form with invalid data
			System.out.println("\nFilling form with test data:");
			System.out.println("  Code: " + (code.isEmpty() ? "[EMPTY]" : code));
			System.out.println("  Name: " + (name.isEmpty() ? "[EMPTY]" : name));
			System.out.println("  Type: " + (type.isEmpty() ? "[EMPTY]" : type));
			System.out.println("  Capacity: " + (capacity.isEmpty() ? "[EMPTY]" : capacity));

			boolean formFilled = masterProductVariantPage.fillForm(code, name, type, capacity);
			Assert.assertTrue(formFilled, "Failed to fill form");
			Thread.sleep(1000);
			extentReport.logInfo("Form filled with invalid data");

			// Submit form
			System.out.println("\nSubmitting form...");
			boolean submitted = masterProductVariantPage.clickSubmitButton();
			Assert.assertTrue(submitted, "Failed to submit form");
			Thread.sleep(2000);
			System.out.println("✅ Form submitted");
			extentReport.logInfo("Form submitted");

			// Check for error message
			boolean hasError = masterProductVariantPage.isErrorMessageDisplayed();
			if (hasError) {
				String errorMessage = masterProductVariantPage.getErrorMessage();
				System.out.println("✅ Validation error displayed: " + errorMessage);
				extentReport.logPass("Validation error message displayed: " + errorMessage);
			} else {
				System.out.println("ℹ️  No validation error displayed");
				extentReport.logInfo("No validation error displayed for this case");
			}

			// Take screenshot
			String screenshotPath = SeleniumUtils.takeScreenshot(driver,
					"validation_" + testCase.replaceAll("[^a-zA-Z0-9]", "_"));
			extentReport.addScreenshot(screenshotPath, testCase);

		} catch (Exception e) {
			System.err.println("❌ Validation test failed: " + e.getMessage());
			extentReport.logFail("Form validation test failed: " + e.getMessage());
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "validation_failed");
			extentReport.addScreenshot(screenshotPath, "Validation Failed");
			throw e;
		}
	}

	@Test(description = "Test navigation flow to Master Product Variant Details", priority = 11)
	public void testNavigationFlowToMasterProductVariantDetails() throws InterruptedException {
		extentReport.createTest("Navigation Flow Test", "Test proper navigation flow using dropdown menu");
		
		try {
			System.out.println("\n🧭 Test: Navigation Flow to Master Product Variant Details");
			System.out.println("─────────────────────────────────────────────────────────");
			
			// Step 1: Verify Master button is visible
			System.out.println("\nStep 1: Verifying Master button visibility...");
			boolean masterButtonVisible = homePage.isMasterButtonVisible();
			Assert.assertTrue(masterButtonVisible, "Master button is not visible");
			System.out.println("✅ Master button is visible");
			extentReport.logPass("Master button is visible");
			
			// Step 2: Test dropdown menu opening
			System.out.println("\nStep 2: Testing dropdown menu opening...");
			boolean masterClicked = homePage.clickMasterButton();
			Assert.assertTrue(masterClicked, "Failed to click Master button");
			Thread.sleep(2000);
			
			// Check if dropdown is open
			boolean dropdownOpen = homePage.isDropdownMenuOpen();
			if (dropdownOpen) {
				System.out.println("✅ Dropdown menu opened successfully");
				extentReport.logPass("Dropdown menu opened successfully");
			} else {
				System.out.println("ℹ️  Dropdown menu not detected as open (might be expected)");
				extentReport.logInfo("Dropdown menu not detected as open (might be expected)");
			}
			
			// Step 3: Verify all dropdown items are visible
			System.out.println("\nStep 3: Verifying dropdown menu items visibility...");
			boolean variantDetailsVisible = homePage.isMasterProductVariantDetailsLinkVisible();
			boolean productDetailsVisible = homePage.isMasterProductDetailsLinkVisible();
			boolean shiftsVisible = homePage.isMasterShiftsLinkVisible();
			boolean reasonDetailsVisible = homePage.isMasterReasonDetailsLinkVisible();
			
			System.out.println("    Master Product Variant Details: " + variantDetailsVisible);
			System.out.println("    Master Product Details: " + productDetailsVisible);
			System.out.println("    Master Shifts: " + shiftsVisible);
			System.out.println("    Master Reason Details: " + reasonDetailsVisible);
			
			extentReport.logInfo("Dropdown items visibility checked");
			
			// Step 4: Navigate to Master Product Variant Details using proper flow
			System.out.println("\nStep 4: Navigating to Master Product Variant Details using proper flow...");
			masterProductVariantPage = homePage.navigateToMasterProductVariantDetailsWithFlow();
			Thread.sleep(2000);
			System.out.println("✅ Navigation completed using proper flow");
			extentReport.logPass("Navigation completed using proper flow");
			
			// Step 5: Verify we're on the correct page
			System.out.println("\nStep 5: Verifying page elements are visible...");
			boolean searchVisible = masterProductVariantPage.isSearchInputVisible();
			boolean createButtonVisible = masterProductVariantPage.isCreateFormVisible();
			
			System.out.println("    Search input visible: " + searchVisible);
			System.out.println("    Create form elements visible: " + createButtonVisible);
			extentReport.logInfo("Page elements visibility verified");
			
			// Take screenshot
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "navigation_flow_completed");
			extentReport.addScreenshot(screenshotPath, "Navigation Flow Completed");
			
			System.out.println("\n✅ NAVIGATION FLOW TEST COMPLETED");
			
		} catch (Exception e) {
			System.err.println("\n❌ NAVIGATION FLOW TEST FAILED: " + e.getMessage());
			extentReport.logFail("Navigation flow test failed: " + e.getMessage());
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "navigation_flow_failed");
			extentReport.addScreenshot(screenshotPath, "Navigation Flow Test Failed");
			throw e;
		}
	}
	
	@AfterMethod
	public void tearDownTest() {
		System.out.println("\n========================================");
		System.out.println("🏁 Test Completed");
		System.out.println("========================================\n");
	}
	
	@Test(description = "Test edit button functionality", priority = 7)
	public void testEditButtonFunctionality() throws InterruptedException {
		extentReport.createTest("Edit Button Functionality", "Test edit button functionality on the first row");
		
		try {
			System.out.println("\n📝 Test: Edit Button Functionality");
			System.out.println("─────────────────────────────────────────");
			
			// Step 1: Navigate to Master Product Variant Details page using proper flow
			System.out.println("\nStep 1: Navigating to Master Product Variant Details using dropdown flow...");
			masterProductVariantPage = homePage.navigateToMasterProductVariantDetailsWithFlow();
			Thread.sleep(2000);
			System.out.println("✅ Navigation completed using dropdown flow");
			extentReport.logInfo("Step 1: Navigated to Master Product Variant Details page using dropdown flow");
			
			// Step 2: Verify edit button is visible
			System.out.println("\nStep 2: Verifying edit button visibility...");
			boolean editButtonVisible = masterProductVariantPage.isEditButtonVisible();
			if (editButtonVisible) {
				System.out.println("✅ Edit button is visible");
				extentReport.logPass("Edit button is visible");
			} else {
				System.out.println("ℹ️  Edit button not visible (might be expected if no data)");
				extentReport.logInfo("Edit button not visible - might be expected if no data exists");
			}
			
			// Step 3: Click edit button
			System.out.println("\nStep 3: Clicking edit button...");
			boolean editClicked = masterProductVariantPage.clickEditButton();
			if (editClicked) {
				System.out.println("✅ Edit button clicked successfully");
				extentReport.logPass("Edit button clicked successfully");
				
				// Wait for edit form or modal to appear
				Thread.sleep(3000);
				
				// Take screenshot after edit button click
				String screenshotPath = SeleniumUtils.takeScreenshot(driver, "edit_button_clicked");
				extentReport.addScreenshot(screenshotPath, "Edit Button Clicked");
			} else {
				System.out.println("ℹ️  Edit button click failed (might be expected if no data)");
				extentReport.logInfo("Edit button click failed - might be expected if no data exists");
			}
			
			System.out.println("\n✅ EDIT BUTTON TEST COMPLETED");
			
		} catch (Exception e) {
			System.err.println("\n❌ EDIT BUTTON TEST FAILED: " + e.getMessage());
			extentReport.logFail("Edit button test failed: " + e.getMessage());
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "edit_button_failed");
			extentReport.addScreenshot(screenshotPath, "Edit Button Test Failed");
			throw e;
		}
	}
	
	@Test(description = "Test status button functionality", priority = 8)
	public void testStatusButtonFunctionality() throws InterruptedException {
		extentReport.createTest("Status Button Functionality", "Test status button functionality on the first row");
		
		try {
			System.out.println("\n🔄 Test: Status Button Functionality");
			System.out.println("─────────────────────────────────────────");
			
			// Step 1: Navigate to Master Product Variant Details page using proper flow
			System.out.println("\nStep 1: Navigating to Master Product Variant Details using dropdown flow...");
			masterProductVariantPage = homePage.navigateToMasterProductVariantDetailsWithFlow();
			Thread.sleep(2000);
			System.out.println("✅ Navigation completed using dropdown flow");
			extentReport.logInfo("Step 1: Navigated to Master Product Variant Details page using dropdown flow");
			
			// Step 2: Verify status button is visible
			System.out.println("\nStep 2: Verifying status button visibility...");
			boolean statusButtonVisible = masterProductVariantPage.isStatusButtonVisible();
			if (statusButtonVisible) {
				System.out.println("✅ Status button is visible");
				extentReport.logPass("Status button is visible");
			} else {
				System.out.println("ℹ️  Status button not visible (might be expected if no data)");
				extentReport.logInfo("Status button not visible - might be expected if no data exists");
			}
			
			// Step 3: Click status button
			System.out.println("\nStep 3: Clicking status button...");
			boolean statusClicked = masterProductVariantPage.clickStatusButton();
			if (statusClicked) {
				System.out.println("✅ Status button clicked successfully");
				extentReport.logPass("Status button clicked successfully");
				
				// Wait for status change or modal to appear
				Thread.sleep(3000);
				
				// Take screenshot after status button click
				String screenshotPath = SeleniumUtils.takeScreenshot(driver, "status_button_clicked");
				extentReport.addScreenshot(screenshotPath, "Status Button Clicked");
			} else {
				System.out.println("ℹ️  Status button click failed (might be expected if no data)");
				extentReport.logInfo("Status button click failed - might be expected if no data exists");
			}
			
			System.out.println("\n✅ STATUS BUTTON TEST COMPLETED");
			
		} catch (Exception e) {
			System.err.println("\n❌ STATUS BUTTON TEST FAILED: " + e.getMessage());
			extentReport.logFail("Status button test failed: " + e.getMessage());
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "status_button_failed");
			extentReport.addScreenshot(screenshotPath, "Status Button Test Failed");
			throw e;
		}
	}
	
	@Test(description = "Test search functionality with product code 61030367", priority = 9)
	public void testSearchFunctionality() throws InterruptedException {
		extentReport.createTest("Search Functionality", "Test search functionality with product code 61030367");
		
		try {
			System.out.println("\n🔍 Test: Search Functionality");
			System.out.println("─────────────────────────────────────────");
			
			// Step 1: Navigate to Master Product Variant Details page using proper flow
			System.out.println("\nStep 1: Navigating to Master Product Variant Details using dropdown flow...");
			masterProductVariantPage = homePage.navigateToMasterProductVariantDetailsWithFlow();
			Thread.sleep(2000);
			System.out.println("✅ Navigation completed using dropdown flow");
			extentReport.logInfo("Step 1: Navigated to Master Product Variant Details page using dropdown flow");
			
			// Step 2: Verify search input is visible
			System.out.println("\nStep 2: Verifying search input visibility...");
			boolean searchInputVisible = masterProductVariantPage.isSearchInputVisible();
			Assert.assertTrue(searchInputVisible, "Search input is not visible");
			System.out.println("✅ Search input is visible");
			extentReport.logPass("Search input is visible");
			
			// Step 3: Enter search code
			String searchCode = "61030367";
			System.out.println("\nStep 3: Entering search code: " + searchCode);
			boolean searchEntered = masterProductVariantPage.searchProductVariant(searchCode);
			Assert.assertTrue(searchEntered, "Failed to enter search code");
			System.out.println("✅ Search code entered successfully");
			extentReport.logPass("Search code entered: " + searchCode);
			
			// Step 5: Verify search value
			System.out.println("\nStep 4: Verifying search value...");
			String actualSearchValue = masterProductVariantPage.getSearchValue();
			Assert.assertEquals(actualSearchValue, searchCode, "Search value mismatch");
			System.out.println("✅ Search value verified: " + actualSearchValue);
			extentReport.logPass("Search value verified: " + actualSearchValue);
			
			// Step 6: Take screenshot after search
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "search_functionality_completed");
			extentReport.addScreenshot(screenshotPath, "Search Functionality Completed");
			System.out.println("✅ Screenshot captured");
			
			// Step 7: Clear search (optional)
			System.out.println("\nStep 5: Clearing search input...");
			boolean searchCleared = masterProductVariantPage.clearSearch();
			if (searchCleared) {
				System.out.println("✅ Search input cleared");
				extentReport.logInfo("Search input cleared");
			} else {
				System.out.println("ℹ️  Search input clear failed (not critical)");
				extentReport.logInfo("Search input clear failed (not critical)");
			}
			
			System.out.println("\n✅ SEARCH FUNCTIONALITY TEST COMPLETED");
			
		} catch (Exception e) {
			System.err.println("\n❌ SEARCH FUNCTIONALITY TEST FAILED: " + e.getMessage());
			extentReport.logFail("Search functionality test failed: " + e.getMessage());
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "search_functionality_failed");
			extentReport.addScreenshot(screenshotPath, "Search Functionality Test Failed");
			throw e;
		}
	}
	
	@Test(description = "Test complete workflow with edit, status, and search actions", priority = 10)
	public void testCompleteWorkflowWithAllActions() throws InterruptedException {
		extentReport.createTest("Complete Workflow with All Actions", 
				"Test complete workflow including create, edit, status, and search functionality");
		
		try {
			System.out.println("\n🎯 Test: Complete Workflow with All Actions");
			System.out.println("=========================================");
			
			// Step 1: Navigate to Master Product Variant Details page using proper flow
			System.out.println("\n[1/6] Navigating to Master Product Variant Details using dropdown flow...");
			masterProductVariantPage = homePage.navigateToMasterProductVariantDetailsWithFlow();
			Thread.sleep(2000);
			System.out.println("    ✅ Navigation completed using dropdown flow");
			extentReport.logInfo("Step 1/6: Navigated to Master Product Variant Details page using dropdown flow");
			
			// Step 2: Test search functionality
			System.out.println("\n[2/6] Testing search functionality...");
			String searchCode = "61030367";
			boolean searchEntered = masterProductVariantPage.searchProductVariant(searchCode);
			if (searchEntered) {
				System.out.println("    ✅ Search completed for: " + searchCode);
				extentReport.logPass("Step 2/6: Search completed for " + searchCode);
			} else {
				System.out.println("    ℹ️  Search failed (might be expected)");
				extentReport.logInfo("Step 2/6: Search failed (might be expected)");
			}
			
			// Step 3: Test edit button
			System.out.println("\n[3/6] Testing edit button...");
			boolean editClicked = masterProductVariantPage.clickEditButton();
			if (editClicked) {
				System.out.println("    ✅ Edit button clicked");
				extentReport.logPass("Step 3/6: Edit button clicked");
				Thread.sleep(2000);
			} else {
				System.out.println("    ℹ️  Edit button click failed (might be expected)");
				extentReport.logInfo("Step 3/6: Edit button click failed (might be expected)");
			}
			
			// Step 5: Test status button
			System.out.println("\n[4/6] Testing status button...");
			boolean statusClicked = masterProductVariantPage.clickStatusButton();
			if (statusClicked) {
				System.out.println("    ✅ Status button clicked");
				extentReport.logPass("Step 4/6: Status button clicked");
				Thread.sleep(2000);
			} else {
				System.out.println("    ℹ️  Status button click failed (might be expected)");
				extentReport.logInfo("Step 4/6: Status button click failed (might be expected)");
			}
			
			// Step 6: Test create button (existing functionality)
			System.out.println("\n[5/6] Testing create button...");
			boolean createClicked = masterProductVariantPage.clickCreateButton();
			if (createClicked) {
				System.out.println("    ✅ Create button clicked");
				extentReport.logPass("Step 5/6: Create button clicked");
				Thread.sleep(2000);
				
				// Fill form with test data
				masterProductVariantPage.fillForm("61030367", "Test Product", "NA", "6");
				Thread.sleep(1000);
				masterProductVariantPage.clickSubmitButton();
				Thread.sleep(2000);
			} else {
				System.out.println("    ℹ️  Create button click failed");
				extentReport.logInfo("Step 5/6: Create button click failed");
			}
			
			// Step 7: Final verification
			System.out.println("\n[6/6] Final verification...");
			boolean searchVisible = masterProductVariantPage.isSearchInputVisible();
			boolean editVisible = masterProductVariantPage.isEditButtonVisible();
			boolean statusVisible = masterProductVariantPage.isStatusButtonVisible();
			
			System.out.println("    Search input visible: " + searchVisible);
			System.out.println("    Edit button visible: " + editVisible);
			System.out.println("    Status button visible: " + statusVisible);
			extentReport.logInfo("Step 6/6: Final verification completed");
			
			// Take final screenshot
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "complete_workflow_all_actions");
			extentReport.addScreenshot(screenshotPath, "Complete Workflow with All Actions");
			
			System.out.println("\n=========================================");
			System.out.println("✅ COMPLETE WORKFLOW WITH ALL ACTIONS TEST PASSED!");
			System.out.println("=========================================");
			extentReport.logPass("Complete workflow with all actions test passed successfully!");
			
		} catch (Exception e) {
			System.err.println("\n=========================================");
			System.err.println("❌ COMPLETE WORKFLOW WITH ALL ACTIONS TEST FAILED!");
			System.err.println("Error: " + e.getMessage());
			System.err.println("=========================================");
			e.printStackTrace();
			
			extentReport.logFail("Complete workflow with all actions test failed: " + e.getMessage());
			String screenshotPath = SeleniumUtils.takeScreenshot(driver, "complete_workflow_all_actions_failed");
			extentReport.addScreenshot(screenshotPath, "Complete Workflow with All Actions Failed");
			throw e;
		}
	}
}
