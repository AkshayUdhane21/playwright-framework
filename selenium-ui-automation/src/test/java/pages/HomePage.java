package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utils.SeleniumUtils;

/**
 * Home page object containing navigation elements
 */
public class HomePage {
    
    private WebDriver driver;
    
    // Locators
    private static final By MASTER_BUTTON = By.xpath("//*[@id=\"root\"]/div/header/nav/div[1]");
    
    // Dropdown menu items
    private static final By MASTER_PRODUCT_VARIANT_DETAILS_LINK = By.xpath("//*[@id=\"root\"]/div/header/nav/div[1]/div/a[1]");
    private static final By MASTER_PRODUCT_DETAILS_LINK = By.xpath("//*[@id=\"root\"]/div/header/nav/div[1]/div/a[2]");
    private static final By MASTER_SHIFTS_LINK = By.xpath("//*[@id=\"root\"]/div/header/nav/div[1]/div/a[3]");
    private static final By MASTER_REASON_DETAILS_LINK = By.xpath("//*[@id=\"root\"]/div/header/nav/div[1]/div/a[4]");
    
    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Click on Master button
     */
    public boolean clickMasterButton() {
        try {
            // Wait 2 seconds before clicking
            Thread.sleep(2000);
            
            if (SeleniumUtils.safeClick(driver, MASTER_BUTTON, 3)) {
                System.out.println("✓ Master button clicked successfully");
                return true;
            }
            
            System.err.println("✗ Failed to click Master button");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Click on Master Product Variant Details link
     */
    public boolean clickMasterProductVariantDetails() {
        try {
            // Wait 2 seconds before clicking
            Thread.sleep(2000);
            
            if (SeleniumUtils.safeClick(driver, MASTER_PRODUCT_VARIANT_DETAILS_LINK, 3)) {
                System.out.println("✓ Master Product Variant Details clicked successfully");
                return true;
            }
            
            System.err.println("✗ Failed to click Master Product Variant Details");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Click on Master Product Details link
     */
    public boolean clickMasterProductDetails() {
        try {
            Thread.sleep(2000);
            
            if (SeleniumUtils.safeClick(driver, MASTER_PRODUCT_DETAILS_LINK, 3)) {
                System.out.println("✓ Master Product Details clicked successfully");
                return true;
            }
            
            System.err.println("✗ Failed to click Master Product Details");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Click on Master Shifts link
     */
    public boolean clickMasterShifts() {
        try {
            Thread.sleep(2000);
            
            if (SeleniumUtils.safeClick(driver, MASTER_SHIFTS_LINK, 3)) {
                System.out.println("✓ Master Shifts clicked successfully");
                return true;
            }
            
            System.err.println("✗ Failed to click Master Shifts");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Click on Master Reason Details link
     */
    public boolean clickMasterReasonDetails() {
        try {
            Thread.sleep(2000);
            
            if (SeleniumUtils.safeClick(driver, MASTER_REASON_DETAILS_LINK, 3)) {
                System.out.println("✓ Master Reason Details clicked successfully");
                return true;
            }
            
            System.err.println("✗ Failed to click Master Reason Details");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Navigate to Master Product Variant Details page with proper flow
     */
    public MasterProductVariantPage navigateToMasterProductVariantDetailsWithFlow() {
        try {
            System.out.println("\n🔄 Starting Navigation Flow to Master Product Variant Details");
            System.out.println("========================================================");
            
            // Step 1: Click Master button to open dropdown
            System.out.println("\n[1/3] Clicking Master button to open dropdown...");
            boolean masterClicked = clickMasterButton();
            if (!masterClicked) {
                throw new RuntimeException("Failed to click Master button");
            }
            System.out.println("    ✅ Master dropdown opened");
            
            // Step 2: Wait for dropdown to be visible
            System.out.println("\n[2/3] Waiting for dropdown menu to be visible...");
            Thread.sleep(2000);
            boolean dropdownVisible = isMasterProductVariantDetailsLinkVisible();
            if (!dropdownVisible) {
                System.err.println("    ⚠️  Dropdown menu not visible, trying alternative approach");
                // Try clicking Master button again
                clickMasterButton();
                Thread.sleep(2000);
            }
            System.out.println("    ✅ Dropdown menu ready");
            
            // Step 3: Click Master Product Variant Details
            System.out.println("\n[3/3] Clicking Master Product Variant Details...");
            boolean detailsClicked = clickMasterProductVariantDetails();
            if (!detailsClicked) {
                throw new RuntimeException("Failed to click Master Product Variant Details");
            }
            System.out.println("    ✅ Navigation to Master Product Variant Details completed");
            
            System.out.println("\n========================================================");
            System.out.println("✅ NAVIGATION FLOW COMPLETED SUCCESSFULLY");
            System.out.println("========================================================");
            
            return new MasterProductVariantPage(driver);
            
        } catch (Exception e) {
            System.err.println("\n❌ NAVIGATION FLOW FAILED: " + e.getMessage());
            throw new RuntimeException("Failed to navigate to Master Product Variant Details page: " + e.getMessage());
        }
    }
    
    /**
     * Navigate to Master Product Variant Details page
     */
    public MasterProductVariantPage navigateToMasterProductVariantDetails() {
        if (clickMasterButton()) {
            if (clickMasterProductVariantDetails()) {
                return new MasterProductVariantPage(driver);
            }
        }
        throw new RuntimeException("Failed to navigate to Master Product Variant Details page");
    }
    
    /**
     * Check if Master button is visible
     */
    public boolean isMasterButtonVisible() {
        return SeleniumUtils.isElementDisplayed(driver, MASTER_BUTTON);
    }
    
    /**
     * Check if Master Product Variant Details link is visible
     */
    public boolean isMasterProductVariantDetailsLinkVisible() {
        return SeleniumUtils.isElementDisplayed(driver, MASTER_PRODUCT_VARIANT_DETAILS_LINK);
    }
    
    /**
     * Check if Master Product Details link is visible
     */
    public boolean isMasterProductDetailsLinkVisible() {
        return SeleniumUtils.isElementDisplayed(driver, MASTER_PRODUCT_DETAILS_LINK);
    }
    
    /**
     * Check if Master Shifts link is visible
     */
    public boolean isMasterShiftsLinkVisible() {
        return SeleniumUtils.isElementDisplayed(driver, MASTER_SHIFTS_LINK);
    }
    
    /**
     * Check if Master Reason Details link is visible
     */
    public boolean isMasterReasonDetailsLinkVisible() {
        return SeleniumUtils.isElementDisplayed(driver, MASTER_REASON_DETAILS_LINK);
    }
    
    /**
     * Check if dropdown menu is open (any of the links is visible)
     */
    public boolean isDropdownMenuOpen() {
        return isMasterProductVariantDetailsLinkVisible() || 
               isMasterProductDetailsLinkVisible() || 
               isMasterShiftsLinkVisible() || 
               isMasterReasonDetailsLinkVisible();
    }
    
    /**
     * Wait for page to load completely
     */
    public void waitForPageLoad() {
        SeleniumUtils.waitForPageLoad(driver);
    }
    
    /**
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
