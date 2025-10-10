package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;

/**
 * Utility class for managing WebDriver instances with automatic version detection
 */
public class DriverManager {
    
    /**
     * Setup ChromeDriver with automatic version detection
     */
    public static WebDriver setupChromeDriver(boolean headless) {
        System.out.println("🔧 Setting up ChromeDriver with automatic version detection...");
        
        try {
            // Auto-detect Chrome version and download matching ChromeDriver
            WebDriverManager.chromedriver().setup();
            
            ChromeOptions options = new ChromeOptions();
            
            // Basic options
            if (headless) {
                options.addArguments("--headless");
            }
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-infobars");
            
            // Security and automation options
            options.addArguments("--disable-web-security");
            options.addArguments("--allow-running-insecure-content");
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("--ignore-ssl-errors");
            options.addArguments("--ignore-certificate-errors-spki-list");
            options.addArguments("--disable-blink-features=AutomationControlled");
            
            // Hide automation indicators
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setExperimentalOption("useAutomationExtension", false);
            
            // Performance options
            options.addArguments("--disable-background-timer-throttling");
            options.addArguments("--disable-backgrounding-occluded-windows");
            options.addArguments("--disable-renderer-backgrounding");
            
            WebDriver driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            
            System.out.println("✅ ChromeDriver setup successful");
            return driver;
            
        } catch (Exception e) {
            System.err.println("❌ Failed to setup ChromeDriver: " + e.getMessage());
            throw new RuntimeException("ChromeDriver setup failed", e);
        }
    }
    
    /**
     * Setup FirefoxDriver with automatic version detection
     */
    public static WebDriver setupFirefoxDriver(boolean headless) {
        System.out.println("🔧 Setting up FirefoxDriver with automatic version detection...");
        
        try {
            WebDriverManager.firefoxdriver().setup();
            
            FirefoxOptions options = new FirefoxOptions();
            
            if (headless) {
                options.addArguments("--headless");
            }
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            
            WebDriver driver = new FirefoxDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            
            System.out.println("✅ FirefoxDriver setup successful");
            return driver;
            
        } catch (Exception e) {
            System.err.println("❌ Failed to setup FirefoxDriver: " + e.getMessage());
            throw new RuntimeException("FirefoxDriver setup failed", e);
        }
    }
    
    /**
     * Setup EdgeDriver with automatic version detection
     */
    public static WebDriver setupEdgeDriver(boolean headless) {
        System.out.println("🔧 Setting up EdgeDriver with automatic version detection...");
        
        try {
            WebDriverManager.edgedriver().setup();
            
            EdgeOptions options = new EdgeOptions();
            
            if (headless) {
                options.addArguments("--headless");
            }
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            
            WebDriver driver = new EdgeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            
            System.out.println("✅ EdgeDriver setup successful");
            return driver;
            
        } catch (Exception e) {
            System.err.println("❌ Failed to setup EdgeDriver: " + e.getMessage());
            throw new RuntimeException("EdgeDriver setup failed", e);
        }
    }
    
    /**
     * Get browser version information
     */
    public static void printBrowserInfo() {
        System.out.println("🌐 Browser Information:");
        System.out.println("  - Chrome: " + getChromeVersion());
        System.out.println("  - Firefox: " + getFirefoxVersion());
        System.out.println("  - Edge: " + getEdgeVersion());
        System.out.println();
    }
    
    private static String getChromeVersion() {
        try {
            WebDriverManager chromeManager = WebDriverManager.chromedriver();
            chromeManager.setup();
            return "Configured";
        } catch (Exception e) {
            return "Not available";
        }
    }
    
    private static String getFirefoxVersion() {
        try {
            WebDriverManager firefoxManager = WebDriverManager.firefoxdriver();
            firefoxManager.setup();
            return "Configured";
        } catch (Exception e) {
            return "Not available";
        }
    }
    
    private static String getEdgeVersion() {
        try {
            WebDriverManager edgeManager = WebDriverManager.edgedriver();
            edgeManager.setup();
            return "Configured";
        } catch (Exception e) {
            return "Not available";
        }
    }
}






