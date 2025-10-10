package base;

import config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import utils.SeleniumUtils;
import utils.ExtentReportManager;
import utils.DriverManager;

import java.time.Duration;


public class BaseTest {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ConfigManager config;
    protected ExtentReportManager extentReport;
    
    @BeforeSuite
    public void setUpSuite() {
        config = ConfigManager.getInstance();
        extentReport = ExtentReportManager.getInstance();
        extentReport.startReport();
        System.out.println("Selenium UI Automation Test Suite Started");
    }
    
    @BeforeMethod
    public void setUp() {
        // Only initialize driver if it's not already initialized
        if (driver == null) {
            initializeDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(config.getIntProperty("browser.explicit.wait", 20)));
            
            if (config.getBooleanProperty("browser.window.maximize", true)) {
                driver.manage().window().maximize();
            }
            
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getIntProperty("browser.implicit.wait", 10)));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getIntProperty("browser.page.load.timeout", 30)));
        } else {
            // Reuse existing driver
            wait = new WebDriverWait(driver, Duration.ofSeconds(config.getIntProperty("browser.explicit.wait", 20)));
        }
    }
    
    @AfterMethod
    public void tearDown() {
        // Check if we should keep browser open
        boolean keepBrowserOpen = config.getBooleanProperty("browser.keep.open", false);
        
        if (keepBrowserOpen) {
            System.out.println("🌐 Browser will remain open for inspection");
            System.out.println("   Set browser.keep.open=false in config to close browser automatically");
        } else {
            if (driver != null) {
                driver.quit();
            }
        }
    }
    
    @AfterSuite
    public void tearDownSuite() {
        extentReport.endReport();
        System.out.println("Selenium UI Automation Test Suite Completed");
    }
    
    /**
     * Initialize WebDriver based on configuration
     */
    private void initializeDriver() {
        String browserName = config.getProperty("browser.name", "chrome").toLowerCase();
        boolean headless = config.getBooleanProperty("browser.headless", false);
        
        // Print browser information
        DriverManager.printBrowserInfo();
        
        switch (browserName) {
            case "chrome":
                driver = DriverManager.setupChromeDriver(headless);
                break;
                
            case "firefox":
                driver = DriverManager.setupFirefoxDriver(headless);
                break;
                
            case "edge":
                driver = DriverManager.setupEdgeDriver(headless);
                break;
                
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
        
        System.out.println("✅ WebDriver initialized successfully for " + browserName.toUpperCase());
    }
    
    /**
     * Navigate to the application URL
     */
    protected void navigateToApp() {
        String url = config.getProperty("app.base.url", "http://localhost:5173/");
        driver.get(url);
        SeleniumUtils.waitForPageLoad(driver);
    }
    
    /**
     * Get the current WebDriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }
    
    /**
     * Get the WebDriverWait instance
     */
    public WebDriverWait getWait() {
        return wait;
    }
    
    /**
     * Get the configuration manager
     */
    public ConfigManager getConfig() {
        return config;
    }
}
