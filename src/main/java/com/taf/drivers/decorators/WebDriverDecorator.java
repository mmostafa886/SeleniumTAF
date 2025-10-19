package com.taf.drivers.decorators;

import org.openqa.selenium.*;

import java.util.List;
import java.util.Set;

/**
 * WebDriverDecorator is the base decorator implementing the Decorator Pattern.
 * It wraps a WebDriver instance and delegates all calls to it, allowing
 * subclasses to add behavior before or after the delegated calls.
 * 
 * Design Patterns Applied:
 * - Decorator Pattern: Adds responsibilities to objects dynamically
 * - Wrapper Pattern: Wraps WebDriver to add functionality
 * - Open/Closed Principle: Open for extension, closed for modification
 */
public abstract class WebDriverDecorator implements WebDriver {
    
    protected final WebDriver driver;
    
    /**
     * Constructor taking the WebDriver to decorate
     * @param driver The WebDriver instance to wrap
     */
    protected WebDriverDecorator(WebDriver driver) {
        this.driver = driver;
    }
    
    /**
     * Get the underlying decorated driver
     * @return The wrapped WebDriver instance
     */
    public WebDriver getDecoratedDriver() {
        if (driver instanceof WebDriverDecorator) {
            return ((WebDriverDecorator) driver).getDecoratedDriver();
        }
        return driver;
    }
    
    @Override
    public void get(String url) {
        driver.get(url);
    }
    
    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    @Override
    public String getTitle() {
        return driver.getTitle();
    }
    
    @Override
    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }
    
    @Override
    public WebElement findElement(By by) {
        return driver.findElement(by);
    }
    
    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }
    
    @Override
    public void close() {
        driver.close();
    }
    
    @Override
    public void quit() {
        driver.quit();
    }
    
    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }
    
    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }
    
    @Override
    public TargetLocator switchTo() {
        return driver.switchTo();
    }
    
    @Override
    public Navigation navigate() {
        return driver.navigate();
    }
    
    @Override
    public Options manage() {
        return driver.manage();
    }
}
