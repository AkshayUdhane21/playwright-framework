package tests;

import base.BaseTest;
import pages.HomePage;
import pages.MasterProductVariantPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import java.util.List;

/**
 * Simplified test specifically for debugging Create button clicking
 */
public class SimpleCreateButtonTest extends BaseTest {
    
    private HomePage homePage;
    private MasterProductVariantPage masterProductVariantPage;
    
    @Test(priority = 1, description = "Simple Create button test with detailed debugging")
    public void testCreateButtonSimple() {
        System.out.println("🔍 Starting Simple Create Button Test");
        System.out.println("====================================");
        
        try {
            // Step 1: Navigate to application
            System.out.println("Step 1: Navigating to http://localhost:5173/");
            navigateToApp();
            Thread.sleep(3000);
            System.out.println("✓ Application loaded successfully");
            
            // Step 2: Click Master button
            System.out.println("\nStep 2: Clicking Master button");
            homePage = new HomePage(driver);
            boolean masterClicked = homePage.clickMasterButton();
            Assert.assertTrue(masterClicked, "Failed to click Master button");
            System.out.println("✓ Master button clicked successfully");
            
            // Step 3: Click Master Product Variant Details
            System.out.println("\nStep 3: Clicking Master Product Variant Details");
            boolean detailsClicked = homePage.clickMasterProductVariantDetails();
            Assert.assertTrue(detailsClicked, "Failed to click Master Product Variant Details");
            System.out.println("✓ Master Product Variant Details clicked successfully");
            
            // Step 4: Wait and find the Create button
            System.out.println("\nStep 4: Finding Create button");
            Thread.sleep(2000);
            
            // Try to find the button by class name
            By createButtonByClass = By.cssSelector("button.add-product-button");
            List<WebElement> createButtons = driver.findElements(createButtonByClass);
            
            System.out.println("Found " + createButtons.size() + " buttons with class 'add-product-button'");
            
            if (createButtons.size() > 0) {
                WebElement createButton = createButtons.get(0);
                System.out.println("Create button found:");
                System.out.println("  - Displayed: " + createButton.isDisplayed());
                System.out.println("  - Enabled: " + createButton.isEnabled());
                System.out.println("  - Text: '" + createButton.getText() + "'");
                System.out.println("  - Class: '" + createButton.getAttribute("class") + "'");
                
                // Try different clicking methods
                System.out.println("\nStep 5: Attempting to click Create button");
                
                try {
                    // Method 1: Regular click
                    System.out.println("Trying regular click...");
                    createButton.click();
                    System.out.println("✓ Regular click successful");
                    Thread.sleep(2000);
                    
                    // Check if form appeared
                    boolean formVisible = checkIfFormIsVisible();
                    if (formVisible) {
                        System.out.println("✓ Form is visible - Test PASSED!");
                    } else {
                        System.out.println("⚠ Form not visible, trying JavaScript click...");
                        
                        // Method 2: JavaScript click
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", createButton);
                        System.out.println("✓ JavaScript click executed");
                        Thread.sleep(2000);
                        
                        formVisible = checkIfFormIsVisible();
                        if (formVisible) {
                            System.out.println("✓ Form is visible after JavaScript click - Test PASSED!");
                        } else {
                            System.out.println("✗ Form still not visible after both click methods");
                        }
                    }
                    
                } catch (Exception e) {
                    System.err.println("✗ Click failed: " + e.getMessage());
                    e.printStackTrace();
                    
                    // Try JavaScript click as fallback
                    try {
                        System.out.println("Trying JavaScript click as fallback...");
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", createButton);
                        System.out.println("✓ JavaScript click executed");
                        Thread.sleep(2000);
                        
                        boolean formVisible = checkIfFormIsVisible();
                        if (formVisible) {
                            System.out.println("✓ Form is visible after JavaScript click - Test PASSED!");
                        } else {
                            System.out.println("✗ Form still not visible after JavaScript click");
                        }
                    } catch (Exception e2) {
                        System.err.println("✗ JavaScript click also failed: " + e2.getMessage());
                    }
                }
                
            } else {
                System.err.println("✗ No Create button found with class 'add-product-button'");
                
                // List all buttons for debugging
                List<WebElement> allButtons = driver.findElements(By.tagName("button"));
                System.out.println("All buttons on page:");
                for (int i = 0; i < allButtons.size(); i++) {
                    WebElement btn = allButtons.get(i);
                    System.out.println("Button " + (i+1) + ": " + btn.getAttribute("class") + " - " + btn.getText());
                }
            }
            
        } catch (Exception e) {
            System.err.println("Test failed with exception: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * Check if the create form is visible
     */
    private boolean checkIfFormIsVisible() {
        try {
            // Look for form elements that would indicate the form is open
            List<WebElement> formInputs = driver.findElements(By.xpath("//input[@id='productVariantCode' or @id='productVariantName' or @id='trolleyType' or @id='trolleyStorageCapacity']"));
            boolean hasFormInputs = formInputs.size() > 0;
            
            List<WebElement> formTitles = driver.findElements(By.xpath("//h1[contains(text(), 'Create') or contains(text(), 'Product') or contains(text(), 'Variant')]"));
            boolean hasFormTitle = formTitles.size() > 0;
            
            List<WebElement> submitButtons = driver.findElements(By.xpath("//button[contains(text(), 'Submit') or contains(text(), 'Save') or contains(text(), 'Create')]"));
            boolean hasSubmitButton = submitButtons.size() > 0;
            
            System.out.println("Form visibility check:");
            System.out.println("  - Form inputs found: " + hasFormInputs + " (" + formInputs.size() + ")");
            System.out.println("  - Form title found: " + hasFormTitle + " (" + formTitles.size() + ")");
            System.out.println("  - Submit button found: " + hasSubmitButton + " (" + submitButtons.size() + ")");
            
            return hasFormInputs || hasFormTitle || hasSubmitButton;
            
        } catch (Exception e) {
            System.err.println("Error checking form visibility: " + e.getMessage());
            return false;
        }
    }
}






