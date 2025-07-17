package com.taf.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;
import com.taf.utils.dataReader.PropertyReader;
import com.taf.utils.logs.LogManager;

/**
 * GUIWebDriver is a wrapper class that manages the lifecycle and access to a Selenium WebDriver instance
 * in a thread-safe manner. It initializes the WebDriver based on the configured browser type and ensures
 * that each thread has its own isolated WebDriver instance.
 */
public class GUIWebDriver {

    /**
     * The browser type to be used for WebDriver initialization.
     * This value is read from the configuration properties using the key "browserType".
     * Example values: "chrome", "firefox", "edge".
     */
    private final String browser = PropertyReader.getProperty("browserType");

    /**
     * ThreadLocal container to hold the WebDriver instance for each thread separately.
     * This ensures thread safety by providing each thread its own WebDriver instance,
     * preventing concurrency issues when running tests in parallel.
     */
    private ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Constructor for GUIWebDriver.
     * <p>
     * This constructor performs the following steps:
     * <ul>
     *     <li>Logs the initialization event along with the browser type.</li>
     *     <li>Retrieves the appropriate AbstractDriver implementation based on the browser type.</li>
     *     <li>Creates a new WebDriver instance using the AbstractDriver factory method.</li>
     *     <li>Wraps the WebDriver instance with ThreadGuard to detect illegal cross-thread usage.</li>
     *     <li>Stores the protected WebDriver instance in the ThreadLocal variable for thread-safe access.</li>
     * </ul>
     * <p>
     * This setup allows multiple threads to run tests concurrently without interfering with each other's WebDriver instances.
     */
    public GUIWebDriver() {
        LogManager.info("Initializing GUIWebDriver with browser: ", browser);
        AbstractDriver abstractDriver = Browser.valueOf(browser.toUpperCase()).getDriverFactory();
        WebDriver driver = ThreadGuard.protect(abstractDriver.createDriver());
        driverThreadLocal.set(driver);
    }

    /**
     * Quits the WebDriver instance associated with the current thread.
     * <p>
     * This method performs the following:
     * <ul>
     *     <li>Checks if the current thread has a WebDriver instance.</li>
     *     <li>If present, logs the quitting event with the browser type.</li>
     *     <li>Calls the quit() method on the WebDriver to close the browser and release resources.</li>
     *     <li>Removes the WebDriver instance from the ThreadLocal storage to prevent memory leaks.</li>
     * </ul>
     * <p>
     * It is important to call this method after test execution to properly clean up WebDriver instances.
     */
    public void quitDriver() {
        if (driverThreadLocal.get() != null) {
            LogManager.info("Quitting WebDriver for browser: ", browser);
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
        }
    }

    /**
     * Retrieves the WebDriver instance associated with the current thread.
     * <p>
     * This method returns the WebDriver stored in the ThreadLocal variable, allowing thread-safe access
     * to the browser driver for the current thread.
     *
     * @return the WebDriver instance for the current thread, or null if none has been initialized.
     */
    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }
}
