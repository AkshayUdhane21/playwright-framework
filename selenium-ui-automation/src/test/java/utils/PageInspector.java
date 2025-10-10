package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

/**
 * Utility class for debugging page elements
 */
public class PageInspector {
    
    /**
     * Inspect all buttons on the page
     */
    public static void inspectButtons(WebDriver driver) {
        System.out.println("=== INSPECTING ALL BUTTONS ===");
        
        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        System.out.println("Found " + buttons.size() + " button elements:");
        
        for (int i = 0; i < buttons.size(); i++) {
            WebElement button = buttons.get(i);
            System.out.println("Button " + (i + 1) + ":");
            System.out.println("  - Text: '" + button.getText() + "'");
            System.out.println("  - ID: '" + button.getAttribute("id") + "'");
            System.out.println("  - Class: '" + button.getAttribute("class") + "'");
            System.out.println("  - Visible: " + button.isDisplayed());
            System.out.println("  - Enabled: " + button.isEnabled());
            System.out.println("  - XPath: " + getXPath(driver, button));
            System.out.println();
        }
    }
    
    /**
     * Inspect all elements with specific XPath
     */
    public static void inspectElementsByXPath(WebDriver driver, String xpath) {
        System.out.println("=== INSPECTING ELEMENTS BY XPATH: " + xpath + " ===");
        
        try {
            List<WebElement> elements = driver.findElements(By.xpath(xpath));
            System.out.println("Found " + elements.size() + " elements:");
            
            for (int i = 0; i < elements.size(); i++) {
                WebElement element = elements.get(i);
                System.out.println("Element " + (i + 1) + ":");
                System.out.println("  - Tag: " + element.getTagName());
                System.out.println("  - Text: '" + element.getText() + "'");
                System.out.println("  - ID: '" + element.getAttribute("id") + "'");
                System.out.println("  - Class: '" + element.getAttribute("class") + "'");
                System.out.println("  - Visible: " + element.isDisplayed());
                System.out.println("  - Enabled: " + element.isEnabled());
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Error finding elements: " + e.getMessage());
        }
    }
    
    /**
     * Get XPath of an element
     */
    public static String getXPath(WebDriver driver, WebElement element) {
        return (String) ((JavascriptExecutor) driver).executeScript(
            "function getElementXPath(element) {" +
            "  if (element.id !== '') {" +
            "    return 'id(\"' + element.id + '\")';" +
            "  }" +
            "  if (element === document.body) {" +
            "    return element.tagName;" +
            "  }" +
            "  var ix = 0;" +
            "  var siblings = element.parentNode.childNodes;" +
            "  for (var i = 0; i < siblings.length; i++) {" +
            "    var sibling = siblings[i];" +
            "    if (sibling === element) {" +
            "      return getElementXPath(element.parentNode) + '/' + element.tagName + '[' + (ix + 1) + ']';" +
            "    }" +
            "    if (sibling.nodeType === 1 && sibling.tagName === element.tagName) {" +
            "      ix++;" +
            "    }" +
            "  }" +
            "}" +
            "return getElementXPath(arguments[0]);", element);
    }
    
    /**
     * Wait and inspect page after navigation
     */
    public static void waitAndInspectPage(WebDriver driver, String url) {
        System.out.println("=== NAVIGATING TO: " + url + " ===");
        driver.get(url);
        
        // Wait for page to load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        
        System.out.println("Page loaded successfully");
        System.out.println("Page title: " + driver.getTitle());
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println();
        
        // Inspect buttons
        inspectButtons(driver);
        
        // Inspect specific elements we're looking for
        inspectElementsByXPath(driver, "//button");
        inspectElementsByXPath(driver, "//*[@id='root']//button");
        inspectElementsByXPath(driver, "//*[contains(text(), 'Create') or contains(text(), 'create')]");
    }
}
