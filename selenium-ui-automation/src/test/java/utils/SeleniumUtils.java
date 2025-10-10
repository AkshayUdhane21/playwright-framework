package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;


public class SeleniumUtils {
    
    /**
     * Wait for page to load completely
     */
    public static void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(webDriver -> 
            ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }
    
    /**
     * Wait for element to be visible and clickable
     */
    public static WebElement waitForElementToBeClickable(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for element to be visible
     */
    public static WebElement waitForElementToBeVisible(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be present in DOM
     */
    public static WebElement waitForElementToBePresent(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Safe click with retry mechanism
     */
    public static boolean safeClick(WebDriver driver, By locator, int maxRetries) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                WebElement element = waitForElementToBeClickable(driver, locator, 10);
                scrollToElement(driver, element);
                element.click();
                return true;
            } catch (Exception e) {
                if (i == maxRetries - 1) {
                    System.err.println("Failed to click element after " + maxRetries + " attempts: " + locator);
                    return false;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false;
    }
    
    /**
     * Safe send keys with retry mechanism
     */
    public static boolean safeSendKeys(WebDriver driver, By locator, String text, int maxRetries) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                WebElement element = waitForElementToBeVisible(driver, locator, 10);
                scrollToElement(driver, element);
                element.clear();
                element.sendKeys(text);
                return true;
            } catch (Exception e) {
                if (i == maxRetries - 1) {
                    System.err.println("Failed to send keys to element after " + maxRetries + " attempts: " + locator);
                    return false;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false;
    }
    
    /**
     * Scroll to element
     */
    public static void scrollToElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
    
    /**
     * Scroll to element by locator
     */
    public static void scrollToElement(WebDriver driver, By locator) {
        WebElement element = driver.findElement(locator);
        scrollToElement(driver, element);
    }
    
    /**
     * Check if element is displayed
     */
    public static boolean isElementDisplayed(WebDriver driver, By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    /**
     * Check if element is enabled
     */
    public static boolean isElementEnabled(WebDriver driver, By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    /**
     * Get element text safely
     */
    public static String getElementText(WebDriver driver, By locator) {
        try {
            WebElement element = waitForElementToBeVisible(driver, locator, 10);
            return element.getText();
        } catch (Exception e) {
            System.err.println("Failed to get text from element: " + locator);
            return "";
        }
    }
    
    /**
     * Get element attribute value safely
     */
    public static String getElementAttribute(WebDriver driver, By locator, String attributeName) {
        try {
            WebElement element = waitForElementToBePresent(driver, locator, 10);
            return element.getAttribute(attributeName);
        } catch (Exception e) {
            System.err.println("Failed to get attribute " + attributeName + " from element: " + locator);
            return "";
        }
    }
    
    /**
     * Wait for text to be present in element
     */
    public static boolean waitForTextToBePresent(WebDriver driver, By locator, String text, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Take screenshot using AShot
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        try {
            Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(1000))
                .takeScreenshot(driver);
            
            String screenshotPath = "screenshots/" + testName + "_" + System.currentTimeMillis() + ".png";
            File screenshotFile = new File(screenshotPath);
            screenshotFile.getParentFile().mkdirs();
            
            ImageIO.write(screenshot.getImage(), "PNG", screenshotFile);
            return screenshotPath;
        } catch (IOException e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Take element screenshot
     */
    public static String takeElementScreenshot(WebDriver driver, WebElement element, String testName) {
        try {
            Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(1000))
                .takeScreenshot(driver, element);
            
            String screenshotPath = "screenshots/" + testName + "_element_" + System.currentTimeMillis() + ".png";
            File screenshotFile = new File(screenshotPath);
            screenshotFile.getParentFile().mkdirs();
            
            ImageIO.write(screenshot.getImage(), "PNG", screenshotFile);
            return screenshotPath;
        } catch (IOException e) {
            System.err.println("Failed to take element screenshot: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Wait for alert and accept it
     */
    public static void acceptAlert(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (Exception e) {
            System.err.println("No alert present or failed to accept alert: " + e.getMessage());
        }
    }
    
    /**
     * Wait for alert and dismiss it
     */
    public static void dismissAlert(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.dismiss();
        } catch (Exception e) {
            System.err.println("No alert present or failed to dismiss alert: " + e.getMessage());
        }
    }
    
    /**
     * Double click on element
     */
    public static void doubleClick(WebDriver driver, By locator) {
        try {
            WebElement element = waitForElementToBeClickable(driver, locator, 10);
            Actions actions = new Actions(driver);
            actions.doubleClick(element).perform();
        } catch (Exception e) {
            System.err.println("Failed to double click element: " + locator);
        }
    }
    
    /**
     * Right click on element
     */
    public static void rightClick(WebDriver driver, By locator) {
        try {
            WebElement element = waitForElementToBeClickable(driver, locator, 10);
            Actions actions = new Actions(driver);
            actions.contextClick(element).perform();
        } catch (Exception e) {
            System.err.println("Failed to right click element: " + locator);
        }
    }
    
    /**
     * Hover over element
     */
    public static void hoverOverElement(WebDriver driver, By locator) {
        try {
            WebElement element = waitForElementToBeVisible(driver, locator, 10);
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
        } catch (Exception e) {
            System.err.println("Failed to hover over element: " + locator);
        }
    }
    
    /**
     * Switch to frame by index
     */
    public static void switchToFrame(WebDriver driver, int frameIndex) {
        try {
            driver.switchTo().frame(frameIndex);
        } catch (Exception e) {
            System.err.println("Failed to switch to frame: " + frameIndex);
        }
    }
    
    /**
     * Switch to frame by name or id
     */
    public static void switchToFrame(WebDriver driver, String frameNameOrId) {
        try {
            driver.switchTo().frame(frameNameOrId);
        } catch (Exception e) {
            System.err.println("Failed to switch to frame: " + frameNameOrId);
        }
    }
    
    /**
     * Switch to default content
     */
    public static void switchToDefaultContent(WebDriver driver) {
        try {
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            System.err.println("Failed to switch to default content");
        }
    }
    
    /**
     * Wait for element to be invisible
     */
    public static boolean waitForElementToBeInvisible(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for element to be selected
     */
    public static boolean waitForElementToBeSelected(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.elementToBeSelected(locator));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Press Enter key on element
     */
    public static boolean pressEnterKey(WebDriver driver, By locator) {
        try {
            WebElement element = waitForElementToBeVisible(driver, locator, 10);
            element.sendKeys(Keys.RETURN);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to press Enter key on element: " + locator);
            return false;
        }
    }
    
    /**
     * Press Tab key on element
     */
    public static boolean pressTabKey(WebDriver driver, By locator) {
        try {
            WebElement element = waitForElementToBeVisible(driver, locator, 10);
            element.sendKeys(Keys.TAB);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to press Tab key on element: " + locator);
            return false;
        }
    }
    
    /**
     * Press Escape key on element
     */
    public static boolean pressEscapeKey(WebDriver driver, By locator) {
        try {
            WebElement element = waitForElementToBeVisible(driver, locator, 10);
            element.sendKeys(Keys.ESCAPE);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to press Escape key on element: " + locator);
            return false;
        }
    }
}


