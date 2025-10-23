package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.pages.components.NavBarComponent;
import com.taf.utils.WaitManager;
import com.taf.utils.dataReader.PropertyReader;
import com.taf.utils.logs.LogsManager;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * BasePage is an abstract class that provides common functionality for all Page Objects.
 * It implements the Page Object Model (POM) design pattern by encapsulating page-level
 * operations and providing reusable methods for all page classes.
 * 
 * Design Patterns Applied:
 * - Page Object Model (POM): Encapsulates page elements and actions
 * - Template Method Pattern: Defines skeleton of page navigation
 * - Fluent Interface: Methods return 'this' or page objects for chaining
 */
public abstract class BasePage<T extends BasePage<T>> {
    
    // Constants for configuration
    protected static final int DEFAULT_RETRY_ATTEMPTS = 3;
    protected static final int RETRY_DELAY_MS = 500;
    
    // Performance optimization: Cache base URL (loaded once)
    private static final String BASE_URL = PropertyReader.getProperty("baseUrlWeb");
    
    @Getter
    protected final GUIWebDriver driver;
    
    private NavBarComponent navigationBar; // Lazy loaded
    
    protected final WaitManager waitManager;
    
    /**
     * Constructor initializing common page components
     * @param driver The GUIWebDriver instance for this page
     */
    protected BasePage(GUIWebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }
        this.driver = driver;
        this.waitManager = new WaitManager(driver.get());
        LogsManager.info("Initialized " + this.getClass().getSimpleName());
    }
    
    /**
     * Get navigation bar component with lazy initialization
     * @return NavBarComponent instance
     */
    public NavBarComponent getNavigationBar() {
        if (navigationBar == null) {
            navigationBar = new NavBarComponent(driver);
        }
        return navigationBar;
    }
    
    /**
     * Abstract method to get the page URL path
     * Each page must define its URL path
     * @return The URL path for this page
     */
    protected abstract String getPageUrl();
    
    /**
     * Navigate to this page using the defined page URL
     * Template Method Pattern: Defines the skeleton for navigation with error handling
     * @return Current page instance for method chaining
     * @throws IllegalStateException if navigation fails
     */
    @SuppressWarnings("unchecked")
    public T navigate() {
        String fullUrl = getBaseUrl() + getPageUrl();
        LogsManager.info("Navigating to: " + fullUrl);
        
        try {
            driver.browser().navigateTo(fullUrl);
            handlePopupsAfterNavigation();
            LogsManager.info("Successfully navigated to: " + fullUrl);
        } catch (Exception e) {
            LogsManager.error("Failed to navigate to: " + fullUrl, e.getMessage());
            throw new IllegalStateException("Navigation failed: " + fullUrl, e);
        }
        
        return (T) this;
    }
    
    /**
     * Handle common popups after navigation
     * Implements common behavior that can be overridden
     */
    protected void handlePopupsAfterNavigation() {
        driver.alert().dismissCommercialsIfPresent().dismissConsentPopupIfPresent();
    }
    
    /**
     * Get the base URL from properties
     * Performance optimized: Returns cached value
     * @return The base URL
     */
    protected String getBaseUrl() {
        return BASE_URL;
    }
    
    /**
     * Refresh the current page
     * @return Current page instance for method chaining
     */
    @SuppressWarnings("unchecked")
    public T refresh() {
        LogsManager.info("Refreshing page: " + this.getClass().getSimpleName());
        driver.browser().refreshPage();
        handlePopupsAfterNavigation();
        return (T) this;
    }
    
    /**
     * Check if element is displayed on the page with error handling
     * @param locator The locator of the element
     * @return true if element is displayed, false otherwise
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            return driver.element().isDisplayed(locator);
        } catch (Exception e) {
            LogsManager.info("Element not displayed: " + locator);
            return false;
        }
    }
    
    /**
     * Check if element is displayed on the page with custom wait time
     * @param locator The locator of the element
     * @param timeoutInSeconds Custom timeout in seconds
     * @return true if element is displayed, false otherwise
     */
    protected boolean isElementDisplayed(By locator, int timeoutInSeconds) {
        return waitManager.waitForVisibility(locator, timeoutInSeconds) != null;
    }
    
    /**
     * Get text from an element with null safety
     * @param locator The locator of the element
     * @return The text content of the element, or empty string if element not found
     */
    protected String getElementText(By locator) {
        try {
            String text = driver.element().getText(locator);
            return text != null ? text : "";
        } catch (Exception e) {
            LogsManager.info("Failed to get text from element: " + locator + " - " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Click on an element with fluent interface
     * @param locator The locator of the element to click
     * @return Current page instance for method chaining
     */
    @SuppressWarnings("unchecked")
    protected T clickElement(By locator) {
        driver.element().click(locator);
        return (T) this;
    }
    
    /**
     * Type text into an element
     * @param locator The locator of the element
     * @param text The text to type
     * @return Current page instance for method chaining
     */
    @SuppressWarnings("unchecked")
    protected T typeText(By locator, String text) {
        LogsManager.info(String.format("Typing text into %s: %s", locator, 
            text.length() > 50 ? text.substring(0, 47) + "..." : text));
        driver.element().type(locator, text);
        return (T) this;
    }
    
    /**
     * Clear and type text into an element
     * @param locator The locator of the element
     * @param text The text to type
     * @return Current page instance for method chaining
     */
    @SuppressWarnings("unchecked")
    protected T clearAndTypeText(By locator, String text) {
        driver.element().type(locator, text);
        return (T) this;
    }
    
    /**
     * Hover over an element
     * @param locator The locator of the element
     * @return Current page instance for method chaining
     */
    @SuppressWarnings("unchecked")
    protected T hoverElement(By locator) {
        driver.element().hover(locator);
        return (T) this;
    }

    /**
     * Select from dropdown by visible text
     * @param locator The locator of the dropdown element
     */
    @SuppressWarnings("unchecked")
    protected T selectFromDropdown(By locator, String visibleText) {
        driver.element().selectFromDropdown(locator, visibleText);
        return (T) this;
    }

    /**
     * Wait for element to be visible
     * @param locator The locator of the element
     * @return WebElement once visible
     */
    protected WebElement waitForElementVisible(By locator) {
        return waitManager.waitForVisibility(locator);
    }

    
    /**
     * Wait for element to be clickable
     * @param locator The locator of the element
     * @return WebElement once clickable
     */
    protected WebElement waitForElementClickable(By locator) {
        return waitManager.waitForClickability(locator);
    }
    
    /**
     * Scroll to element
     * @param locator The locator of the element
     * @return Current page instance for method chaining
     */
    @SuppressWarnings("unchecked")
    protected T scrollToElement(By locator) {
        driver.element().scrollToElementJS(locator);
        return (T) this;
    }
    
    /**
     * Get page title
     * @return The page title
     */
    public String getPageTitle() {
        return driver.browser().getPageTitle();
    }
    
    /**
     * Get current URL
     * @return The current URL
     */
    public String getCurrentUrl() {
        return driver.browser().getCurrentUrl();
    }
    
    /**
     * Verify element is displayed
     * Fluent validation interface
     * @param locator The locator of the element
     * @return Current page instance for method chaining
     */
    @SuppressWarnings("unchecked")
    public T verifyElementDisplayed(By locator) {
        driver.verification().isElementVisible(locator);
        return (T) this;
    }
    
    /**
     * Verify element text equals expected
     * Fluent validation interface
     * @param locator The locator of the element
     * @param expectedText The expected text
     * @return Current page instance for method chaining
     */
    @SuppressWarnings("unchecked")
    public T verifyElementText(By locator, String expectedText) {
        String actualText = getElementText(locator);
        if (!actualText.equals(expectedText)) {
            String errorMsg = String.format(
                "Element text verification failed for %s%nExpected: '%s'%nActual: '%s'",
                locator, expectedText, actualText
            );
            LogsManager.info(errorMsg);
        }
        driver.verification().Equals(actualText, expectedText, 
            "Element text does not match. Expected: " + expectedText + ", Actual: " + actualText);
        return (T) this;
    }
    
    /**
     * Verify element text contains expected substring
     * Fluent validation interface
     * @param locator The locator of the element
     * @param expectedSubstring The expected substring
     * @return Current page instance for method chaining
     */
    @SuppressWarnings("unchecked")
    public T verifyElementTextContains(By locator, String expectedSubstring) {
        String actualText = getElementText(locator);
        driver.verification().Contains(actualText, expectedSubstring,
            "Element text does not contain expected substring. Expected substring: " + expectedSubstring);
        return (T) this;
    }
    
    /**
     * Log page action for better debugging
     * @param action The action being performed
     */
    protected void logAction(String action) {
        LogsManager.info("[" + this.getClass().getSimpleName() + "] " + action);
    }
    
    /**
     * Click element with retry logic for flaky elements
     *
     * @param locator The locator of the element to click
     * @return Current page instance for method chaining
     */
    @SuppressWarnings("unchecked")
    protected T clickElementWithRetry(By locator) {
        int attempts = 0;
        Exception lastException = null;
        
        while (attempts < BasePage.DEFAULT_RETRY_ATTEMPTS) {
            try {
                waitForElementClickable(locator);
                driver.element().click(locator);
                LogsManager.info("Successfully clicked element: " + locator);
                return (T) this;
            } catch (Exception e) {
                lastException = e;
                attempts++;
                LogsManager.warn(String.format("Click attempt %d failed for: %s", attempts, locator));
                
                if (attempts < BasePage.DEFAULT_RETRY_ATTEMPTS) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Interrupted during retry", ie);
                    }
                }
            }
        }

        LogsManager.error("Failed to click element after " + BasePage.DEFAULT_RETRY_ATTEMPTS + " attempts: " + locator);
        throw new RuntimeException("Click failed after retries: " + locator, lastException);
    }

    /**
     * Wait for URL to contain expected string
     * @param urlFragment Expected URL fragment
     * @param timeoutInSeconds Timeout in seconds
     * @return true if URL contains fragment within timeout, false otherwise
     */
    protected boolean waitForUrlContains(String urlFragment, int timeoutInSeconds) {
        try {
            return waitManager.fluentWait(timeoutInSeconds).until(driver ->
                driver.getCurrentUrl().contains(urlFragment)
            );
        } catch (Exception e) {
            LogsManager.warn("URL did not contain expected fragment: " + urlFragment);
            return false;
        }
    }
    
    /**
     * Check if multiple elements are displayed
     * @param locators Array of locators to check
     * @return true if all elements are displayed, false otherwise
     */
    protected boolean areElementsDisplayed(By... locators) {
        for (By locator : locators) {
            if (!isElementDisplayed(locator)) {
                LogsManager.info("Element not displayed: " + locator);
                return false;
            }
        }
        return true;
    }
    
    /**
     * Wait for element to disappear from the page
     * @param locator The locator of the element
     * @param timeoutInSeconds Timeout in seconds
     * @return true if element disappeared, false if still visible after timeout
     */
    protected boolean waitForElementToDisappear(By locator, int timeoutInSeconds) {
        try {
            return waitManager.fluentWait(timeoutInSeconds).until(driver -> {
                try {
                    return !driver.findElement(locator).isDisplayed();
                } catch (Exception e) {
                    return true; // Element not found means it disappeared
                }
            });
        } catch (Exception e) {
            LogsManager.warn("Element did not disappear within timeout: " + locator);
            return false;
        }
    }
}
