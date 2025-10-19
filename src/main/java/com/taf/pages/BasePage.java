package com.taf.pages;

import com.taf.drivers.GUIWebDriver;
import com.taf.pages.components.NavBarComponent;
import com.taf.utils.WaitManager;
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
    
    @Getter
    protected final GUIWebDriver driver;
    
    @Getter
    protected final NavBarComponent navigationBar;
    
    protected final WaitManager waitManager;
    
    /**
     * Constructor initializing common page components
     * @param driver The GUIWebDriver instance for this page
     */
    protected BasePage(GUIWebDriver driver) {
        this.driver = driver;
        this.navigationBar = new NavBarComponent(driver);
        this.waitManager = new WaitManager(driver.get());
        LogsManager.debug("Initialized " + this.getClass().getSimpleName());
    }
    
    /**
     * Abstract method to get the page URL path
     * Each page must define its URL path
     * @return The URL path for this page
     */
    protected abstract String getPageUrl();
    
    /**
     * Navigate to this page using the defined page URL
     * Template Method Pattern: Defines the skeleton for navigation
     * @return Current page instance for method chaining
     */
    @SuppressWarnings("unchecked")
    public T navigate() {
        String fullUrl = getBaseUrl() + getPageUrl();
        LogsManager.info("Navigating to: " + fullUrl);
        driver.browser().navigateTo(fullUrl);
        handlePopupsAfterNavigation();
        waitForPageLoad();
        return (T) this;
    }
    
    /**
     * Wait for the page to load completely
     * Can be overridden by specific pages for custom wait logic
     */
    protected void waitForPageLoad() {
        // Default implementation - can be overridden
        waitManager.pageLoadTimeout();
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
     * @return The base URL
     */
    protected String getBaseUrl() {
        return com.taf.utils.dataReader.PropertyReader.getProperty("baseUrlWeb");
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
     * Check if element is displayed on the page
     * @param locator The locator of the element
     * @return true if element is displayed, false otherwise
     */
    protected boolean isElementDisplayed(By locator) {
        return driver.element().isDisplayed(locator);
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
     * Get text from an element
     * @param locator The locator of the element
     * @return The text content of the element
     */
    protected String getElementText(By locator) {
        return driver.element().getText(locator);
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
        driver.element().clear(locator);
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
        driver.element().scrollToElement(locator);
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
        return driver.browser().getCurrentURL();
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
}
