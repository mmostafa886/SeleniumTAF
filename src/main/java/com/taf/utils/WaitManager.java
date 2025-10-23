package com.taf.utils;

import com.taf.utils.dataReader.PropertyReader;
import com.taf.utils.logs.LogsManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * WaitManager provides centralized wait functionality with performance optimizations.
 * Uses cached values to avoid repeated property reads and object creation.
 */
public class WaitManager {

    private WebDriver driver;
    
    // Performance optimization: Cache default wait timeout (loaded once)
    private static final long DEFAULT_WAIT_TIMEOUT;
    
    // Performance optimization: Cache exception list (created once, reused everywhere)
    private static final List<Class<? extends Exception>> IGNORED_EXCEPTIONS;
    
    static {
        // Initialize cached values once when class is loaded
        DEFAULT_WAIT_TIMEOUT = Long.parseLong(PropertyReader.getProperty("DEFAULT_WAIT"));
        
        // Create immutable exception list once
        List<Class<? extends Exception>> exceptions = new ArrayList<>();
        exceptions.add(NoSuchElementException.class);
        exceptions.add(StaleElementReferenceException.class);
        exceptions.add(ElementNotInteractableException.class);
        exceptions.add(ElementClickInterceptedException.class);
        IGNORED_EXCEPTIONS = Collections.unmodifiableList(exceptions);
    }

    public WaitManager(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Creates a FluentWait instance for the WebDriver.
     * This wait will ignore specific exceptions and poll at regular intervals.
     * @return FluentWait instance configured for the WebDriver
     */
    public FluentWait<WebDriver> fluentWait() {
        return fluentWait(DEFAULT_WAIT_TIMEOUT);
    }

    /**
     * Creates a FluentWait instance for the WebDriver.
     * This wait will ignore specific exceptions and poll at regular intervals.
     * @return FluentWait instance configured for the WebDriver
     * @param timeOutSeconds The timeout in seconds for the wait
     */
    public FluentWait<WebDriver> fluentWait(long timeOutSeconds) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeOutSeconds))
                .pollingEvery(Duration.ofMillis(300))
                .ignoreAll(IGNORED_EXCEPTIONS);
    }

    /**
     * Wait for element to be visible using default timeout
     * @param locator Element locator
     * @return WebElement once visible, or null if timeout occurs
     */
    public WebElement waitForVisibility(By locator) {
        return waitForVisibility(locator, DEFAULT_WAIT_TIMEOUT);
    }

    /**
     * Wait for element to be visible with custom timeout
     * @param locator Element locator
     * @param timeoutInSeconds Custom timeout in seconds
     * @return WebElement once visible, or null if timeout occurs
     */
    public WebElement waitForVisibility(By locator, long timeoutInSeconds) {
        try {
            return fluentWait(timeoutInSeconds).until(driver -> {
                WebElement element = driver.findElement(locator);
                return element.isDisplayed() ? element : null;
            });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Wait for element to be clickable using default timeout
     * @param locator Element locator
     * @return WebElement once clickable, or null if timeout occurs
     */
    public WebElement waitForClickability(By locator) {
        return waitForClickability(locator, DEFAULT_WAIT_TIMEOUT);
    }

    /**
     * Wait for element to be clickable with custom timeout
     * @param locator Element locator
     * @param timeoutInSeconds Custom timeout in seconds
     * @return WebElement once clickable, or null if timeout occurs
     */
    public WebElement waitForClickability(By locator, long timeoutInSeconds) {
        try {
            return fluentWait(timeoutInSeconds).until(driver -> {
                WebElement element = driver.findElement(locator);
                return (element.isDisplayed() && element.isEnabled()) ? element : null;
            });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Wait for page to load completely using document.readyState
     * Uses default timeout from properties
     */
    public void pageLoadTimeout() {
        pageLoadTimeout(DEFAULT_WAIT_TIMEOUT);
    }

    /**
     * Wait for page to load completely with custom timeout
     * Uses JavaScript to check document.readyState
     * @param timeoutInSeconds Custom timeout in seconds
     */
    public void pageLoadTimeout(long timeoutInSeconds) {
        try {
            fluentWait(timeoutInSeconds).until(driver1 ->
                    Objects.equals(((JavascriptExecutor) driver1).executeScript("return document.readyState")
                            , "complete")
            );
            LogsManager.info("Page loaded successfully within " + timeoutInSeconds + " seconds");
        } catch (TimeoutException e) {
            LogsManager.info("Page did not load completely within the timeout of: " + timeoutInSeconds + " seconds");
        }
    }
}
