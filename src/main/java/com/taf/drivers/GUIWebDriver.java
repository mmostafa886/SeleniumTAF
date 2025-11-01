package com.taf.drivers;

import com.taf.drivers.decorators.LoggingWebDriverDecorator;
import com.taf.drivers.decorators.ScreenshotWebDriverDecorator;
import com.taf.utils.actions.AlertActions;
import com.taf.utils.actions.BrowserActions;
import com.taf.utils.actions.ElementActions;
import com.taf.utils.actions.FrameActions;
import com.taf.utils.logs.LogsManager;
import com.taf.validations.Validation;
import com.taf.validations.Verification;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;
import com.taf.utils.dataReader.PropertyReader;

/**
 * GUIWebDriver is a wrapper class that manages the lifecycle and access to a Selenium WebDriver instance
 * in a thread-safe manner. It initializes the WebDriver based on the configured browser type and ensures
 * that each thread has its own isolated WebDriver instance.
 * =======================================================================
 * This class delegates WebDriver storage and lifecycle management to ThreadLocalDriverManager,
 * which provides enhanced thread-safe management with metadata tracking and cleanup mechanisms.
 */
public class GUIWebDriver {
    /**
     * The browser type to be used for WebDriver initialization.
     * This value is read from the configuration properties using the key "browser".
     * Example values: "chrome", "firefox", "edge".
     */
    private final String browser = System.getProperty("browser") != null && !System.getProperty("browser").isEmpty()
            ? System.getProperty("browser") : PropertyReader.getProperty("browser");

    /**
     * Constructor for GUIWebDriver.
     * This constructor performs the following steps:
     *     <li>Logs the initialization event along with the browser type.</li>
     *     <li>Retrieves the appropriate AbstractDriver implementation based on the browser type.</li>
     *     <li>Creates a new WebDriver instance using the AbstractDriver factory method.</li>
     *     <li>Applies configured decorators (logging, screenshots) based on properties.</li>
     *     <li>Wraps the WebDriver instance with ThreadGuard to detect illegal cross-thread usage.</li>
     *     <li>Stores the driver in ThreadLocalDriverManager for centralized thread-safe management.</li>
     * This setup allows multiple threads to run tests concurrently without interfering with each other's WebDriver instances.
     */
    public GUIWebDriver() {
        LogsManager.info("Initializing GUIWebDriver with browser: ", browser);
        AbstractDriver abstractDriver = Browser.getBrowserFromString(browser).getDriverFactory();
        WebDriver driver = abstractDriver.createDriver();

        // Apply configured decorators before ThreadGuard
        driver = applyConfiguredDecorators(driver);

        driver = ThreadGuard.protect(driver);

        // Store driver in ThreadLocalDriverManager for centralized management
        ThreadLocalDriverManager.setDriver(driver);
    }

    /**     * Provides access to element-related actions using the current WebDriver instance.
     * This method returns an instance of ElementActions, which contains methods for interacting with web elements
     * such as clicking, typing, and retrieving element properties.
     * @return an instance of ElementActions for the current WebDriver.
     */
    public ElementActions element() {
        return new ElementActions(get());
    }

    /**
     * Provides access to browser-related actions using the current WebDriver instance.
     * This method returns an instance of BrowserActions, which contains methods for performing browser operations
     * such as navigating, refreshing, and managing cookies.
     * @return an instance of BrowserActions for the current WebDriver.
     */
    public BrowserActions browser() {
        return new BrowserActions(get());
    }

    /**
     * Provides access to frame-related actions using the current WebDriver instance.
     * This method returns an instance of FrameActions, which contains methods for switching between frames
     * and handling frame-specific operations.
     * @return an instance of FrameActions for the current WebDriver.
     */
    public FrameActions frame() {
        return new FrameActions(get());
    }

    /**
     * Provides access to alert-related actions using the current WebDriver instance.
     * This method returns an instance of AlertActions, which contains methods for handling JavaScript alerts,
     * confirmations, and prompts.
     * @return an instance of AlertActions for the current WebDriver.
     */
    public AlertActions alert() {
        return new AlertActions(get());
    }

    /**
     * Provides access to validation methods using the current WebDriver instance.
     * This method returns an instance of Validation, which contains methods for performing soft assertions
     * and validating conditions without throwing exceptions immediately.
     * @return an instance of Validation for the current WebDriver.
     */
    public Validation validation() {
        return new Validation(get());
    }

    /**
     * Provides access to verification methods using the current WebDriver instance.
     * This method returns an instance of Verification, which contains methods for performing hard assertions
     * and verifying conditions that will throw exceptions if the conditions are not met.
     * @return an instance of Verification for the current WebDriver.
     */
    public Verification verification() {
        return new Verification(get());
    }

    /**
     * Retrieves the WebDriver instance associated with the current thread.
     * This method delegates to ThreadLocalDriverManager to get the driver for the current thread,
     * allowing thread-safe access to the browser driver.
     * @return the WebDriver instance for the current thread, or null if none has been initialized.
     * @throws IllegalStateException if driver was closed prematurely
     */
    public WebDriver get() {
        WebDriver driver = ThreadLocalDriverManager.getDriver();
        if (driver == null) {
            LogsManager.warn("No WebDriver found for thread: " + Thread.currentThread().threadId() +
                           ". Driver may have been closed prematurely.");
        }
        return driver;
    }

    /**
     * Quits the WebDriver instance associated with the current thread.
     * This method delegates to ThreadLocalDriverManager which performs the following:
     *     <li>Checks if the current thread has a WebDriver instance.</li>
     *     <li>If present, logs the quitting event and quits the browser.</li>
     *     <li>Removes the WebDriver from ThreadLocal storage and active drivers map.</li>
     *     <li>Cleans up metadata to prevent memory leaks.</li>
     * It is important to call this method after test execution to properly clean up WebDriver instances.
     */
    public void quitDriver() {
        if (ThreadLocalDriverManager.hasDriver()) {
            LogsManager.info("Quitting WebDriver for browser: ", browser);
            ThreadLocalDriverManager.removeDriver();
        }
    }

    /**
     * Applies configured decorators to the WebDriver instance based on property settings.
     * Decorators are applied in the following order:
     *     <li>LoggingWebDriverDecorator (if enableDriverLevelLogging=true)</li>
     *     <li>ScreenshotWebDriverDecorator (if enableDriverScreenshots=true)</li>
     * This method reads configuration from properties and applies decorators only when enabled.
     * By default, all decorators are disabled to maintain optimal performance.
     *
     * @param driver the base WebDriver instance to decorate
     * @return the decorated WebDriver instance, or the original if no decorators are enabled
     */
    private WebDriver applyConfiguredDecorators(WebDriver driver) {
        // Apply logging decorator (optional - based on configuration)
        String enableLoggingProperty = PropertyReader.getProperty("enableDriverLevelLogging");
        boolean enableLogging = enableLoggingProperty != null && Boolean.parseBoolean(enableLoggingProperty);
        if (enableLogging) {
            driver = new LoggingWebDriverDecorator(driver);
            LogsManager.info("✓ LoggingWebDriverDecorator applied - All WebDriver operations will be logged with timing");
        }

        // Apply screenshot decorator (optional - based on configuration)
        String enableScreenshotsProperty = PropertyReader.getProperty("enableDriverScreenshots");
        boolean enableScreenshots = enableScreenshotsProperty != null && Boolean.parseBoolean(enableScreenshotsProperty);
        if (enableScreenshots) {
            String screenshotOnNavProperty = PropertyReader.getProperty("screenshotOnNavigation");
            boolean screenshotOnNav = screenshotOnNavProperty != null && Boolean.parseBoolean(screenshotOnNavProperty);

            String screenshotOnErrorProperty = PropertyReader.getProperty("screenshotOnError");
            boolean screenshotOnError = screenshotOnErrorProperty == null || Boolean.parseBoolean(screenshotOnErrorProperty);

            driver = new ScreenshotWebDriverDecorator(driver, screenshotOnNav, screenshotOnError);
            LogsManager.info("✓ ScreenshotWebDriverDecorator applied - Screenshots will be captured based on configuration");
        }

        return driver;
    }
}
