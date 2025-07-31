package com.taf.utils;

import com.taf.utils.dataReader.PropertyReader;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
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
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(Long.parseLong(PropertyReader.getProperty("DEFAULT_WAIT"))))
                .pollingEvery(Duration.ofMillis(300))
                .ignoreAll(getExceptions());
    }

    /**
     * Creates a FluentWait instance for the WebDriver.
     * This wait will ignore specific exceptions and poll at regular intervals.
     * @return FluentWait instance configured for the WebDriver
     * @param timeOutSeconds The timeout in seconds for the wait
     */
    public FluentWait<WebDriver> fluentWait(int timeOutSeconds) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeOutSeconds))
                .pollingEvery(Duration.ofMillis(100))
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
}
