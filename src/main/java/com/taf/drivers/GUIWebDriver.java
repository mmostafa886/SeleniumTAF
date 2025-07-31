package com.taf.drivers;

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
     * ThreadLocal container to hold the WebDriver instance for each thread separately.
     * This ensures thread safety by providing each thread its own WebDriver instance,
     * preventing concurrency issues when running tests in parallel.
     */
    private ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Constructor for GUIWebDriver.
     * This constructor performs the following steps:
     *     <li>Logs the initialization event along with the browser type.</li>
     *     <li>Retrieves the appropriate AbstractDriver implementation based on the browser type.</li>
     *     <li>Creates a new WebDriver instance using the AbstractDriver factory method.</li>
     *     <li>Wraps the WebDriver instance with ThreadGuard to detect illegal cross-thread usage.</li>
     *     <li>Stores the protected WebDriver instance in the ThreadLocal variable for thread-safe access.</li>
     * This setup allows multiple threads to run tests concurrently without interfering with each other's WebDriver instances.
     */
    public GUIWebDriver() {
        LogsManager.info("Initializing GUIWebDriver with browser: ", browser);
        AbstractDriver abstractDriver = Browser.getBrowserFromString(browser).getDriverFactory();
        WebDriver driver = ThreadGuard.protect(abstractDriver.createDriver());
        driverThreadLocal.set(driver);
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
     * This method returns the WebDriver stored in the ThreadLocal variable, allowing thread-safe access
     * to the browser driver for the current thread.
     * @return the WebDriver instance for the current thread, or null if none has been initialized.
     */
    public WebDriver get() {
        return driverThreadLocal.get();
    }

    /**
     * Quits the WebDriver instance associated with the current thread.
     * This method performs the following:
     *     <li>Checks if the current thread has a WebDriver instance.</li>
     *     <li>If present, logs the quitting event with the browser type.</li>
     *     <li>Calls the quit() method on the WebDriver to close the browser and release resources.</li>
     *     <li>Removes the WebDriver instance from the ThreadLocal storage to prevent memory leaks.</li>
     * It is important to call this method after test execution to properly clean up WebDriver instances.
     */
    public  void quitDriver() {
        if (driverThreadLocal.get() != null) {
            LogsManager.info("Quitting WebDriver for browser: ", browser);
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
        }
    }
}
