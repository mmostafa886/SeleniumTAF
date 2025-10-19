package com.taf.utils;

import com.taf.utils.dataReader.PropertyReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class WaitManager {

    private WebDriver driver;

    public WaitManager(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Creates a FluentWait instance for the WebDriver.
     * This wait will ignore specific exceptions and poll at regular intervals.
     * @return FluentWait instance configured for the WebDriver
     * The timeout is set to 10 seconds by default and polling every 100 milliseconds.
     */
    public FluentWait<WebDriver> fluentWait() {
        return fluentWait(Long.parseLong(PropertyReader.getProperty("DEFAULT_WAIT")));
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
                .ignoreAll(getExceptions());
    }

    /**
     * Returns a list of exceptions to ignore during the wait.
     * This includes common exceptions that may occur when elements are not immediately available.
     * @return ArrayList of exception classes to ignore
     */
    private ArrayList<Class<? extends Exception>> getExceptions() {
        ArrayList<Class<? extends Exception>> exceptions = new ArrayList<>();
        exceptions.add(NoSuchElementException.class);
        exceptions.add(StaleElementReferenceException.class);
        exceptions.add(ElementNotInteractableException.class);
        exceptions.add(ElementClickInterceptedException.class);
        return exceptions;
    }

    /**
     * Wait for element to be visible using default timeout
     * @param locator Element locator
     * @return WebElement once visible, or null if timeout occurs
     */
    public WebElement waitForVisibility(By locator) {
        return waitForVisibility(locator, Long.parseLong(PropertyReader.getProperty("DEFAULT_WAIT")));
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
        return waitForClickability(locator, Long.parseLong(PropertyReader.getProperty("DEFAULT_WAIT")));
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
        pageLoadTimeout(Long.parseLong(PropertyReader.getProperty("DEFAULT_WAIT")));
    }

    /**
     * Wait for page to load completely with custom timeout
     * Uses JavaScript to check document.readyState
     * @param timeoutInSeconds Custom timeout in seconds
     */
    public void pageLoadTimeout(long timeoutInSeconds) {
        try {
            fluentWait(timeoutInSeconds).until(driver ->
                ((JavascriptExecutor) driver).executeScript("return document.readyState")
                    .equals("complete")
            );
        } catch (TimeoutException e) {
            // Page didn't load in time, log but don't fail
        }
    }
}
