package com.taf.drivers.decorators;

import com.taf.utils.logs.LogsManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * LoggingWebDriverDecorator adds logging capabilities to WebDriver operations.
 * It logs all WebDriver actions including navigation, element finding, and browser operations.
 * 
 * Design Patterns Applied:
 * - Decorator Pattern: Adds logging behavior dynamically
 * - Aspect-Oriented Programming: Cross-cutting logging concern
 */
public class LoggingWebDriverDecorator extends WebDriverDecorator {
    
    /**
     * Constructor taking the WebDriver to decorate with logging
     * @param driver The WebDriver instance to wrap
     */
    public LoggingWebDriverDecorator(WebDriver driver) {
        super(driver);
        LogsManager.debug("LoggingWebDriverDecorator initialized");
    }
    
    @Override
    public void get(String url) {
        LogsManager.info("Navigating to URL: " + url);
        long startTime = System.currentTimeMillis();
        try {
            super.get(url);
            long duration = System.currentTimeMillis() - startTime;
            LogsManager.debug("Navigation completed in " + duration + "ms");
        } catch (Exception e) {
            LogsManager.error("Navigation failed to: " + url + " - " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public String getCurrentUrl() {
        LogsManager.debug("Getting current URL");
        String url = super.getCurrentUrl();
        LogsManager.debug("Current URL: " + url);
        return url;
    }
    
    @Override
    public String getTitle() {
        LogsManager.debug("Getting page title");
        String title = super.getTitle();
        LogsManager.debug("Page title: " + title);
        return title;
    }
    
    @Override
    public WebElement findElement(By by) {
        LogsManager.debug("Finding element: " + by.toString());
        long startTime = System.currentTimeMillis();
        try {
            WebElement element = super.findElement(by);
            long duration = System.currentTimeMillis() - startTime;
            LogsManager.debug("Element found in " + duration + "ms: " + by.toString());
            return element;
        } catch (Exception e) {
            LogsManager.error("Element not found: " + by.toString() + " - " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public List<WebElement> findElements(By by) {
        LogsManager.debug("Finding elements: " + by.toString());
        long startTime = System.currentTimeMillis();
        try {
            List<WebElement> elements = super.findElements(by);
            long duration = System.currentTimeMillis() - startTime;
            LogsManager.debug("Found " + elements.size() + " elements in " + duration + "ms: " + by.toString());
            return elements;
        } catch (Exception e) {
            LogsManager.error("Error finding elements: " + by.toString() + " - " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public void close() {
        LogsManager.info("Closing current browser window");
        try {
            super.close();
            LogsManager.debug("Browser window closed successfully");
        } catch (Exception e) {
            LogsManager.error("Error closing browser window: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public void quit() {
        LogsManager.info("Quitting WebDriver and closing all windows");
        try {
            super.quit();
            LogsManager.debug("WebDriver quit successfully");
        } catch (Exception e) {
            LogsManager.error("Error quitting WebDriver: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public String getWindowHandle() {
        LogsManager.debug("Getting current window handle");
        String handle = super.getWindowHandle();
        LogsManager.debug("Current window handle: " + handle);
        return handle;
    }
    
    @Override
    public Navigation navigate() {
        LogsManager.debug("Getting Navigation interface");
        return new LoggingNavigation(super.navigate());
    }
    
    /**
     * Inner class to add logging to Navigation operations
     */
    private static class LoggingNavigation implements Navigation {
        private final Navigation navigation;
        
        public LoggingNavigation(Navigation navigation) {
            this.navigation = navigation;
        }
        
        @Override
        public void back() {
            LogsManager.info("Navigating back in browser history");
            navigation.back();
            LogsManager.debug("Navigated back successfully");
        }
        
        @Override
        public void forward() {
            LogsManager.info("Navigating forward in browser history");
            navigation.forward();
            LogsManager.debug("Navigated forward successfully");
        }
        
        @Override
        public void to(String url) {
            LogsManager.info("Navigating to URL via Navigation: " + url);
            long startTime = System.currentTimeMillis();
            navigation.to(url);
            long duration = System.currentTimeMillis() - startTime;
            LogsManager.debug("Navigation completed in " + duration + "ms");
        }
        
        @Override
        public void refresh() {
            LogsManager.info("Refreshing current page");
            long startTime = System.currentTimeMillis();
            navigation.refresh();
            long duration = System.currentTimeMillis() - startTime;
            LogsManager.debug("Page refreshed in " + duration + "ms");
        }
    }
}
