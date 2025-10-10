package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utils.SeleniumUtils;
import java.util.List;

/**
 * Master Product Variant page object containing form elements and actions
 */
public class MasterProductVariantPage {
    
    private WebDriver driver;
    
    // Locators - Multiple strategies for Create button
    private static final By CREATE_BUTTON_SVG = By.xpath("//*[@id=\"root\"]/div[1]/main[1]/div[1]/div[1]/div[1]/button[1]/svg");
    private static final By CREATE_BUTTON_SVG_PATH = By.xpath("//*[@id=\"root\"]/div[1]/main[1]/div[1]/div[1]/div[1]/button[1]/svg/path");
    private static final By CREATE_BUTTON = By.xpath("//*[@id=\"root\"]/div[1]/main[1]/div[1]/div[1]/div[1]/button[1]");
    private static final By CREATE_BUTTON_BY_CLASS = By.xpath("//button[contains(@class, 'add-product-button')]");
    private static final By CREATE_BUTTON_BY_CSS = By.cssSelector("button.add-product-button");
    private static final By CREATE_BUTTON_BY_TEXT = By.xpath("//button[contains(text(), 'Create') or contains(text(), 'create') or contains(text(), 'Add') or contains(text(), 'New')]");
    private static final By CREATE_BUTTON_BY_ARIA = By.xpath("//button[@aria-label='Create' or @aria-label='Add' or @title='Create' or @title='Add']");
    private static final By PRODUCT_VARIANT_CODE_INPUT = By.id("productVariantCode");
    private static final By PRODUCT_VARIANT_NAME_INPUT = By.id("productVariantName");
    private static final By TROLLEY_TYPE_INPUT = By.id("trolleyType");
    private static final By TROLLEY_STORAGE_CAPACITY_INPUT = By.id("trolleyStorageCapacity");
    private static final By SUBMIT_BUTTON = By.xpath("//*[@id=\"root\"]/div/main/div/div[4]/div/div[6]/button[1]");
    private static final By SUBMIT_BUTTON_ALTERNATIVE = By.xpath("//button[contains(text(), 'Submit') or contains(text(), 'Save') or contains(text(), 'Create')]");
    
    // Table action buttons locators
    private static final By EDIT_BUTTON = By.xpath("//*[@id=\"root\"]/div/main/div/div[3]/table/tbody/tr[1]/td[6]/button[1]");
    private static final By STATUS_BUTTON = By.xpath("//*[@id=\"root\"]/div/main/div/div[3]/table/tbody/tr[1]/td[6]/button[3]");
    private static final By SEARCH_INPUT = By.xpath("//*[@id=\"root\"]/div/main/div/div[2]/div[2]/input");
    
    // Form validation locators
    private static final By FORM_TITLE = By.xpath("//h1[contains(text(), 'Create New Product Variant')]");
    private static final By SUCCESS_MESSAGE = By.xpath("//div[contains(@class, 'success') or contains(@class, 'alert-success')]");
    private static final By ERROR_MESSAGE = By.xpath("//div[contains(@class, 'error') or contains(@class, 'alert-danger')]");
    
    // Status Change Modal locators
    private static final By STATUS_MODAL_TITLE = By.xpath("//h2[contains(text(), 'Change Status Of Part:')]");
    private static final By ACTIVE_RADIO_BUTTON = By.xpath("//input[@type='radio' and @value='Active' or contains(@id, 'active')]");
    private static final By INACTIVE_RADIO_BUTTON = By.xpath("//input[@type='radio' and @value='Inactive' or contains(@id, 'inactive')]");
    private static final By STATUS_MODAL_YES_BUTTON = By.xpath("//button[contains(text(), 'Yes') or contains(@class, 'yes') or contains(@id, 'yes')]");
    private static final By STATUS_MODAL_NO_BUTTON = By.xpath("//button[contains(text(), 'No') or contains(@class, 'no') or contains(@id, 'no')]");
    private static final By STATUS_MODAL_CONFIRM_BUTTON = By.xpath("//button[contains(text(), 'Confirm') or contains(text(), 'OK') or contains(text(), 'Save')]");
    private static final By STATUS_MODAL_CANCEL_BUTTON = By.xpath("//button[contains(text(), 'Cancel') or contains(text(), 'Close')]");
    
    public MasterProductVariantPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Click on Create button to open the form
     */
    public boolean clickCreateButton() {
        try {
            // Wait 2 seconds before clicking
            Thread.sleep(2000);
            
            // Debug: Print available buttons
            debugAvailableButtons();
            
            // Try multiple strategies in order of preference
            By[] locators = {
                CREATE_BUTTON_BY_CSS,    // By CSS class (most reliable based on debug output)
                CREATE_BUTTON_BY_CLASS,  // By XPath class
                CREATE_BUTTON,           // Button element (exact XPath)
                CREATE_BUTTON_SVG_PATH,  // Your specific XPath
                CREATE_BUTTON_SVG,       // SVG element
                CREATE_BUTTON_BY_TEXT,   // By text content
                CREATE_BUTTON_BY_ARIA    // By ARIA attributes
            };
            
            for (int i = 0; i < locators.length; i++) {
                System.out.println("Trying locator " + (i + 1) + ": " + locators[i]);
                
                // Try regular click first
                if (SeleniumUtils.safeClick(driver, locators[i], 3)) {
                    System.out.println("✓ Create button clicked successfully with locator " + (i + 1));
                    return true;
                }
                
                // Try JavaScript click as fallback
                if (clickWithJavaScript(locators[i])) {
                    System.out.println("✓ Create button clicked successfully with JavaScript (locator " + (i + 1) + ")");
                    return true;
                }
            }
            
            System.err.println("✗ Failed to click Create button with all strategies");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Enter product variant code
     */
    public boolean enterProductVariantCode(String code) {
        return SeleniumUtils.safeSendKeys(driver, PRODUCT_VARIANT_CODE_INPUT, code, 3);
    }
    
    /**
     * Enter product variant name
     */
    public boolean enterProductVariantName(String name) {
        return SeleniumUtils.safeSendKeys(driver, PRODUCT_VARIANT_NAME_INPUT, name, 3);
    }
    
    /**
     * Enter trolley type
     */
    public boolean enterTrolleyType(String type) {
        return SeleniumUtils.safeSendKeys(driver, TROLLEY_TYPE_INPUT, type, 3);
    }
    
    /**
     * Enter trolley storage capacity
     */
    public boolean enterTrolleyStorageCapacity(String capacity) {
        return SeleniumUtils.safeSendKeys(driver, TROLLEY_STORAGE_CAPACITY_INPUT, capacity, 3);
    }
    
    /**
     * Click submit button
     */
    public boolean clickSubmitButton() {
        try {
            // Wait 2 seconds before clicking
            Thread.sleep(2000);
            
            // Try primary locator first
            if (SeleniumUtils.safeClick(driver, SUBMIT_BUTTON, 3)) {
                System.out.println("✓ Submit button clicked successfully");
                return true;
            }
            
            // Try alternative locator
            if (SeleniumUtils.safeClick(driver, SUBMIT_BUTTON_ALTERNATIVE, 3)) {
                System.out.println("✓ Submit button clicked successfully (alternative)");
                return true;
            }
            
            System.err.println("✗ Failed to click Submit button");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Fill the complete form with provided data
     */
    public boolean fillForm(String code, String name, String trolleyType, String capacity) {
        boolean success = true;
        
        success &= enterProductVariantCode(code);
        success &= enterProductVariantName(name);
        success &= enterTrolleyType(trolleyType);
        success &= enterTrolleyStorageCapacity(capacity);
        
        return success;
    }
    
    /**
     * Create a new product variant with the specified data
     */
    public boolean createProductVariant(String code, String name, String trolleyType, String capacity) {
        if (clickCreateButton()) {
            if (fillForm(code, name, trolleyType, capacity)) {
                return clickSubmitButton();
            }
        }
        return false;
    }
    
    /**
     * Check if create form is visible
     */
    public boolean isCreateFormVisible() {
        return SeleniumUtils.isElementDisplayed(driver, FORM_TITLE);
    }
    
    /**
     * Check if success message is displayed
     */
    public boolean isSuccessMessageDisplayed() {
        return SeleniumUtils.isElementDisplayed(driver, SUCCESS_MESSAGE);
    }
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return SeleniumUtils.isElementDisplayed(driver, ERROR_MESSAGE);
    }
    
    /**
     * Get success message text
     */
    public String getSuccessMessage() {
        return SeleniumUtils.getElementText(driver, SUCCESS_MESSAGE);
    }
    
    /**
     * Get error message text
     */
    public String getErrorMessage() {
        return SeleniumUtils.getElementText(driver, ERROR_MESSAGE);
    }
    
    /**
     * Get current value of product variant code field
     */
    public String getProductVariantCode() {
        return SeleniumUtils.getElementAttribute(driver, PRODUCT_VARIANT_CODE_INPUT, "value");
    }
    
    /**
     * Get current value of product variant name field
     */
    public String getProductVariantName() {
        return SeleniumUtils.getElementAttribute(driver, PRODUCT_VARIANT_NAME_INPUT, "value");
    }
    
    /**
     * Get current value of trolley type field
     */
    public String getTrolleyType() {
        return SeleniumUtils.getElementAttribute(driver, TROLLEY_TYPE_INPUT, "value");
    }
    
    /**
     * Get current value of trolley storage capacity field
     */
    public String getTrolleyStorageCapacity() {
        return SeleniumUtils.getElementAttribute(driver, TROLLEY_STORAGE_CAPACITY_INPUT, "value");
    }
    
    /**
     * Clear all form fields
     */
    public void clearForm() {
        try {
            WebElement codeField = driver.findElement(PRODUCT_VARIANT_CODE_INPUT);
            WebElement nameField = driver.findElement(PRODUCT_VARIANT_NAME_INPUT);
            WebElement typeField = driver.findElement(TROLLEY_TYPE_INPUT);
            WebElement capacityField = driver.findElement(TROLLEY_STORAGE_CAPACITY_INPUT);
            
            codeField.clear();
            nameField.clear();
            typeField.clear();
            capacityField.clear();
        } catch (Exception e) {
            System.err.println("Failed to clear form fields: " + e.getMessage());
        }
    }
    
    /**
     * Validate form fields are enabled
     */
    public boolean areFormFieldsEnabled() {
        boolean codeEnabled = SeleniumUtils.isElementEnabled(driver, PRODUCT_VARIANT_CODE_INPUT);
        boolean nameEnabled = SeleniumUtils.isElementEnabled(driver, PRODUCT_VARIANT_NAME_INPUT);
        boolean typeEnabled = SeleniumUtils.isElementEnabled(driver, TROLLEY_TYPE_INPUT);
        boolean capacityEnabled = SeleniumUtils.isElementEnabled(driver, TROLLEY_STORAGE_CAPACITY_INPUT);
        
        return codeEnabled && nameEnabled && typeEnabled && capacityEnabled;
    }
    
    /**
     * Wait for form to be visible
     */
    public void waitForFormToBeVisible() {
        SeleniumUtils.waitForElementToBeVisible(driver, FORM_TITLE, 10);
    }
    
    /**
     * Wait for success message
     */
    public boolean waitForSuccessMessage(int timeoutSeconds) {
        return SeleniumUtils.waitForTextToBePresent(driver, SUCCESS_MESSAGE, "success", timeoutSeconds);
    }
    
    /**
     * Wait for error message
     */
    public boolean waitForErrorMessage(int timeoutSeconds) {
        return SeleniumUtils.waitForTextToBePresent(driver, ERROR_MESSAGE, "error", timeoutSeconds);
    }
    
    /**
     * Click edit button for the first row
     */
    public boolean clickEditButton() {
        try {
            System.out.println("Attempting to click edit button...");
            
            // Wait for the table to be visible
            Thread.sleep(2000);
            
            // Try multiple strategies for edit button
            By[] editLocators = {
                EDIT_BUTTON,
                By.xpath("//table//tr[1]//button[1]"), // First button in first row
                By.xpath("//button[contains(@class, 'edit') or contains(@title, 'Edit')]"),
                By.xpath("//button[contains(text(), 'Edit')]")
            };
            
            for (int i = 0; i < editLocators.length; i++) {
                System.out.println("Trying edit button locator " + (i + 1) + ": " + editLocators[i]);
                
                if (SeleniumUtils.safeClick(driver, editLocators[i], 3)) {
                    System.out.println("✓ Edit button clicked successfully with locator " + (i + 1));
                    return true;
                }
                
                // Try JavaScript click as fallback
                if (clickWithJavaScript(editLocators[i])) {
                    System.out.println("✓ Edit button clicked successfully with JavaScript (locator " + (i + 1) + ")");
                    return true;
                }
            }
            
            System.err.println("✗ Failed to click edit button with all strategies");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Click status button for the first row
     */
    public boolean clickStatusButton() {
        try {
            System.out.println("Attempting to click status button...");
            
            // Wait for the table to be visible
            Thread.sleep(2000);
            
            // Try multiple strategies for status button
            By[] statusLocators = {
                STATUS_BUTTON,
                By.xpath("//table//tr[1]//button[3]"), // Third button in first row
                By.xpath("//button[contains(@class, 'status') or contains(@title, 'Status')]"),
                By.xpath("//button[contains(text(), 'Status') or contains(text(), 'Active') or contains(text(), 'Inactive')]")
            };
            
            for (int i = 0; i < statusLocators.length; i++) {
                System.out.println("Trying status button locator " + (i + 1) + ": " + statusLocators[i]);
                
                if (SeleniumUtils.safeClick(driver, statusLocators[i], 3)) {
                    System.out.println("✓ Status button clicked successfully with locator " + (i + 1));
                    return true;
                }
                
                // Try JavaScript click as fallback
                if (clickWithJavaScript(statusLocators[i])) {
                    System.out.println("✓ Status button clicked successfully with JavaScript (locator " + (i + 1) + ")");
                    return true;
                }
            }
            
            System.err.println("✗ Failed to click status button with all strategies");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Search for a product variant by code
     */
    public boolean searchProductVariant(String searchCode) {
        try {
            System.out.println("Attempting to search for product variant: " + searchCode);
            
            // Wait for search input to be visible
            Thread.sleep(2000);
            
            // Try multiple strategies for search input
            By[] searchLocators = {
                SEARCH_INPUT,
                By.xpath("//input[@placeholder='Search' or @placeholder='search']"),
                By.xpath("//input[contains(@class, 'search')]"),
                By.xpath("//div[2]/div[2]/input"), // Alternative path
                By.xpath("//input[@type='text']")
            };
            
            for (int i = 0; i < searchLocators.length; i++) {
                System.out.println("Trying search input locator " + (i + 1) + ": " + searchLocators[i]);
                
                if (SeleniumUtils.safeSendKeys(driver, searchLocators[i], searchCode, 3)) {
                    System.out.println("✓ Search code entered successfully with locator " + (i + 1));
                    
                    // Press Enter to trigger search
                    Thread.sleep(1000);
                    SeleniumUtils.pressEnterKey(driver, searchLocators[i]);
                    Thread.sleep(2000); // Wait for search results
                    
                    return true;
                }
            }
            
            System.err.println("✗ Failed to enter search code with all strategies");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Clear search input
     */
    public boolean clearSearch() {
        try {
            System.out.println("Clearing search input...");
            
            By[] searchLocators = {
                SEARCH_INPUT,
                By.xpath("//input[@placeholder='Search' or @placeholder='search']"),
                By.xpath("//input[contains(@class, 'search')]")
            };
            
            for (By locator : searchLocators) {
                try {
                    WebElement searchElement = driver.findElement(locator);
                    searchElement.clear();
                    System.out.println("✓ Search input cleared");
                    return true;
                } catch (Exception e) {
                    // Continue to next locator
                }
            }
            
            System.err.println("✗ Failed to clear search input");
            return false;
            
        } catch (Exception e) {
            System.err.println("Error clearing search: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if edit button is visible
     */
    public boolean isEditButtonVisible() {
        return SeleniumUtils.isElementDisplayed(driver, EDIT_BUTTON);
    }
    
    /**
     * Check if status button is visible
     */
    public boolean isStatusButtonVisible() {
        return SeleniumUtils.isElementDisplayed(driver, STATUS_BUTTON);
    }
    
    /**
     * Check if search input is visible
     */
    public boolean isSearchInputVisible() {
        return SeleniumUtils.isElementDisplayed(driver, SEARCH_INPUT);
    }
    
    /**
     * Get the current search input value
     */
    public String getSearchValue() {
        return SeleniumUtils.getElementAttribute(driver, SEARCH_INPUT, "value");
    }
    
    /**
     * Click element using JavaScript
     */
    private boolean clickWithJavaScript(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            return true;
        } catch (Exception e) {
            System.err.println("JavaScript click failed for locator " + locator + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Debug method to print available buttons on the page
     */
    private void debugAvailableButtons() {
        try {
            System.out.println("=== DEBUG: Available buttons on page ===");
            
            // Find all buttons
            List<WebElement> buttons = driver.findElements(By.tagName("button"));
            System.out.println("Total buttons found: " + buttons.size());
            
            for (int i = 0; i < buttons.size(); i++) {
                WebElement button = buttons.get(i);
                try {
                    String text = button.getText();
                    String id = button.getAttribute("id");
                    String className = button.getAttribute("class");
                    String ariaLabel = button.getAttribute("aria-label");
                    String title = button.getAttribute("title");
                    boolean isDisplayed = button.isDisplayed();
                    boolean isEnabled = button.isEnabled();
                    
                    System.out.println("Button " + (i + 1) + ":");
                    System.out.println("  Text: '" + text + "'");
                    System.out.println("  ID: '" + id + "'");
                    System.out.println("  Class: '" + className + "'");
                    System.out.println("  Aria-label: '" + ariaLabel + "'");
                    System.out.println("  Title: '" + title + "'");
                    System.out.println("  Displayed: " + isDisplayed);
                    System.out.println("  Enabled: " + isEnabled);
                    System.out.println("  XPath: " + getElementXPath(button));
                    System.out.println();
                } catch (Exception e) {
                    System.out.println("Button " + (i + 1) + ": Error getting details - " + e.getMessage());
                }
            }
            
            // Check for SVG elements inside buttons
            List<WebElement> svgElements = driver.findElements(By.xpath("//button//svg"));
            System.out.println("SVG elements inside buttons: " + svgElements.size());
            
            for (int i = 0; i < svgElements.size(); i++) {
                WebElement svg = svgElements.get(i);
                try {
                    String parentButtonText = svg.findElement(By.xpath("..")).getText();
                    System.out.println("SVG " + (i + 1) + " (parent button text: '" + parentButtonText + "')");
                } catch (Exception e) {
                    System.out.println("SVG " + (i + 1) + ": Error getting parent details - " + e.getMessage());
                }
            }
            
            System.out.println("=== END DEBUG ===");
        } catch (Exception e) {
            System.err.println("Debug failed: " + e.getMessage());
        }
    }
    
    /**
     * Get XPath for an element
     */
    private String getElementXPath(WebElement element) {
        try {
            return (String) ((JavascriptExecutor) driver).executeScript(
                "function getElementXPath(element) {" +
                "  if (element.id !== '') {" +
                "    return '//*[@id=\"' + element.id + '\"]';" +
                "  }" +
                "  if (element === document.body) {" +
                "    return '/html/body';" +
                "  }" +
                "  var ix = 0;" +
                "  var siblings = element.parentNode.childNodes;" +
                "  for (var i = 0; i < siblings.length; i++) {" +
                "    var sibling = siblings[i];" +
                "    if (sibling === element) {" +
                "      return getElementXPath(element.parentNode) + '/' + element.tagName.toLowerCase() + '[' + (ix + 1) + ']';" +
                "    }" +
                "    if (sibling.nodeType === 1 && sibling.tagName === element.tagName) {" +
                "      ix++;" +
                "    }" +
                "  }" +
                "};" +
                "return getElementXPath(arguments[0]);", element);
        } catch (Exception e) {
            return "XPath generation failed";
        }
    }
    
    // ==================== STATUS CHANGE MODAL METHODS ====================
    
    /**
     * Check if status change modal is visible
     */
    public boolean isStatusChangeModalVisible() {
        return SeleniumUtils.isElementDisplayed(driver, STATUS_MODAL_TITLE);
    }
    
    /**
     * Get status change modal title text
     */
    public String getStatusChangeModalTitle() {
        return SeleniumUtils.getElementText(driver, STATUS_MODAL_TITLE);
    }
    
    /**
     * Click Active radio button in status change modal
     */
    public boolean clickActiveRadioButton() {
        try {
            Thread.sleep(1000);
            if (SeleniumUtils.safeClick(driver, ACTIVE_RADIO_BUTTON, 3)) {
                System.out.println("✓ Active radio button clicked successfully");
                return true;
            }
            System.err.println("✗ Failed to click Active radio button");
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Click Inactive radio button in status change modal
     */
    public boolean clickInactiveRadioButton() {
        try {
            Thread.sleep(1000);
            if (SeleniumUtils.safeClick(driver, INACTIVE_RADIO_BUTTON, 3)) {
                System.out.println("✓ Inactive radio button clicked successfully");
                return true;
            }
            System.err.println("✗ Failed to click Inactive radio button");
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Click Yes button in status change modal
     */
    public boolean clickStatusModalYesButton() {
        try {
            Thread.sleep(1000);
            
            // Try multiple strategies for Yes button
            By[] yesLocators = {
                STATUS_MODAL_YES_BUTTON,
                STATUS_MODAL_CONFIRM_BUTTON,
                By.xpath("//button[contains(text(), 'Yes')]"),
                By.xpath("//button[contains(text(), 'Confirm')]"),
                By.xpath("//button[contains(text(), 'OK')]")
            };
            
            for (int i = 0; i < yesLocators.length; i++) {
                System.out.println("Trying Yes button locator " + (i + 1) + ": " + yesLocators[i]);
                
                if (SeleniumUtils.safeClick(driver, yesLocators[i], 3)) {
                    System.out.println("✓ Yes button clicked successfully with locator " + (i + 1));
                    return true;
                }
            }
            
            System.err.println("✗ Failed to click Yes button with all strategies");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Click No button in status change modal
     */
    public boolean clickStatusModalNoButton() {
        try {
            Thread.sleep(1000);
            
            // Try multiple strategies for No button
            By[] noLocators = {
                STATUS_MODAL_NO_BUTTON,
                STATUS_MODAL_CANCEL_BUTTON,
                By.xpath("//button[contains(text(), 'No')]"),
                By.xpath("//button[contains(text(), 'Cancel')]"),
                By.xpath("//button[contains(text(), 'Close')]")
            };
            
            for (int i = 0; i < noLocators.length; i++) {
                System.out.println("Trying No button locator " + (i + 1) + ": " + noLocators[i]);
                
                if (SeleniumUtils.safeClick(driver, noLocators[i], 3)) {
                    System.out.println("✓ No button clicked successfully with locator " + (i + 1));
                    return true;
                }
            }
            
            System.err.println("✗ Failed to click No button with all strategies");
            return false;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if Active radio button is selected
     */
    public boolean isActiveRadioButtonSelected() {
        try {
            WebElement activeRadio = driver.findElement(ACTIVE_RADIO_BUTTON);
            return activeRadio.isSelected();
        } catch (Exception e) {
            System.err.println("Error checking Active radio button selection: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if Inactive radio button is selected
     */
    public boolean isInactiveRadioButtonSelected() {
        try {
            WebElement inactiveRadio = driver.findElement(INACTIVE_RADIO_BUTTON);
            return inactiveRadio.isSelected();
        } catch (Exception e) {
            System.err.println("Error checking Inactive radio button selection: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Change status to Active and confirm
     */
    public boolean changeStatusToActive() {
        try {
            System.out.println("🔄 Changing status to Active...");
            
            // Click Active radio button
            if (!clickActiveRadioButton()) {
                return false;
            }
            
            Thread.sleep(1000);
            
            // Click Yes button to confirm
            if (!clickStatusModalYesButton()) {
                return false;
            }
            
            Thread.sleep(2000); // Wait for modal to close
            
            System.out.println("✅ Status changed to Active successfully");
            return true;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Change status to Inactive and confirm
     */
    public boolean changeStatusToInactive() {
        try {
            System.out.println("🔄 Changing status to Inactive...");
            
            // Click Inactive radio button
            if (!clickInactiveRadioButton()) {
                return false;
            }
            
            Thread.sleep(1000);
            
            // Click Yes button to confirm
            if (!clickStatusModalYesButton()) {
                return false;
            }
            
            Thread.sleep(2000); // Wait for modal to close
            
            System.out.println("✅ Status changed to Inactive successfully");
            return true;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cancel status change modal
     */
    public boolean cancelStatusChange() {
        try {
            System.out.println("❌ Canceling status change...");
            
            // Click No button to cancel
            if (!clickStatusModalNoButton()) {
                return false;
            }
            
            Thread.sleep(2000); // Wait for modal to close
            
            System.out.println("✅ Status change canceled successfully");
            return true;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Complete status change workflow: Click status button -> Change status -> Confirm
     */
    public boolean completeStatusChangeWorkflow(String newStatus) {
        try {
            System.out.println("🔄 Starting complete status change workflow to: " + newStatus);
            
            // Step 1: Click status button to open modal
            System.out.println("Step 1: Clicking status button...");
            if (!clickStatusButton()) {
                System.err.println("Failed to click status button");
                return false;
            }
            
            Thread.sleep(2000); // Wait for modal to open
            
            // Step 2: Verify modal is open
            System.out.println("Step 2: Verifying status change modal is open...");
            if (!isStatusChangeModalVisible()) {
                System.err.println("Status change modal is not visible");
                return false;
            }
            
            String modalTitle = getStatusChangeModalTitle();
            System.out.println("Modal title: " + modalTitle);
            
            // Step 3: Select new status
            System.out.println("Step 3: Selecting status: " + newStatus);
            boolean statusSelected = false;
            if ("Active".equalsIgnoreCase(newStatus)) {
                statusSelected = clickActiveRadioButton();
            } else if ("Inactive".equalsIgnoreCase(newStatus)) {
                statusSelected = clickInactiveRadioButton();
            } else {
                System.err.println("Invalid status: " + newStatus + ". Use 'Active' or 'Inactive'");
                return false;
            }
            
            if (!statusSelected) {
                System.err.println("Failed to select status: " + newStatus);
                return false;
            }
            
            Thread.sleep(1000);
            
            // Step 4: Confirm the change
            System.out.println("Step 4: Confirming status change...");
            if (!clickStatusModalYesButton()) {
                System.err.println("Failed to confirm status change");
                return false;
            }
            
            Thread.sleep(3000); // Wait for modal to close and page to update
            
            System.out.println("✅ Complete status change workflow completed successfully");
            return true;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }
    }
}
