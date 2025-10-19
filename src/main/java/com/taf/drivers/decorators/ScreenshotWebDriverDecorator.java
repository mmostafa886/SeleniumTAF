package com.taf.drivers.decorators;

import com.taf.utils.logs.LogsManager;
import com.taf.utils.media.ScreenshotsManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URL;

/**
 * ScreenshotWebDriverDecorator automatically captures screenshots during WebDriver operations.
 * It takes screenshots on navigation, before critical actions, and on errors.
 * 
 * Design Patterns Applied:
 * - Decorator Pattern: Adds screenshot behavior dynamically
 * - Strategy Pattern: Different screenshot strategies for different operations
 */
public class ScreenshotWebDriverDecorator extends WebDriverDecorator {
    
    private final ScreenshotsManager screenshotsManager;
    private final boolean screenshotOnNavigation;
    private final boolean screenshotOnError;
    private int screenshotCounter;
    
    /**
     * Constructor with default screenshot settings
     * @param driver The WebDriver instance to wrap
     */
    public ScreenshotWebDriverDecorator(WebDriver driver) {
        this(driver, true, true);
    }
    
    /**
     * Constructor with custom screenshot settings
     * @param driver The WebDriver instance to wrap
     * @param screenshotOnNavigation Whether to take screenshots on navigation
     * @param screenshotOnError Whether to take screenshots on errors
     */
    public ScreenshotWebDriverDecorator(WebDriver driver, boolean screenshotOnNavigation, boolean screenshotOnError) {
        super(driver);
        this.screenshotsManager = new ScreenshotsManager(getDecoratedDriver());
        this.screenshotOnNavigation = screenshotOnNavigation;
        this.screenshotOnError = screenshotOnError;
        this.screenshotCounter = 0;
        LogsManager.debug("ScreenshotWebDriverDecorator initialized");
    }
    
    @Override
    public void get(String url) {
        try {
            super.get(url);
            if (screenshotOnNavigation) {
                takeScreenshot("after_navigation_" + screenshotCounter++);
            }
        } catch (Exception e) {
            if (screenshotOnError) {
                takeScreenshot("error_navigation_" + screenshotCounter++);
            }
            throw e;
        }
    }
    
    @Override
    public WebElement findElement(By by) {
        try {
            return super.findElement(by);
        } catch (Exception e) {
            if (screenshotOnError) {
                takeScreenshot("error_find_element_" + screenshotCounter++);
                LogsManager.error("Screenshot captured on element find error: " + by.toString());
            }
            throw e;
        }
    }
    
    @Override
    public void close() {
        try {
            takeScreenshot("before_close_" + screenshotCounter++);
            super.close();
        } catch (Exception e) {
            if (screenshotOnError) {
                takeScreenshot("error_close_" + screenshotCounter++);
            }
            throw e;
        }
    }
    
    @Override
    public Navigation navigate() {
        return new ScreenshotNavigation(super.navigate());
    }
    
    /**
     * Take a screenshot with the given name
     * @param screenshotName Name for the screenshot
     */
    private void takeScreenshot(String screenshotName) {
        try {
            screenshotsManager.takeScreenshot(screenshotName);
            LogsManager.debug("Screenshot captured: " + screenshotName);
        } catch (Exception e) {
            LogsManager.warn("Failed to capture screenshot: " + e.getMessage());
        }
    }
    
    /**
     * Inner class to add screenshot capability to Navigation operations
     */
    private class ScreenshotNavigation implements Navigation {
        private final Navigation navigation;
        
        public ScreenshotNavigation(Navigation navigation) {
            this.navigation = navigation;
        }
        
        @Override
        public void back() {
            try {
                navigation.back();
                if (screenshotOnNavigation) {
                    takeScreenshot("after_back_" + screenshotCounter++);
                }
            } catch (Exception e) {
                if (screenshotOnError) {
                    takeScreenshot("error_back_" + screenshotCounter++);
                }
                throw e;
            }
        }
        
        @Override
        public void forward() {
            try {
                navigation.forward();
                if (screenshotOnNavigation) {
                    takeScreenshot("after_forward_" + screenshotCounter++);
                }
            } catch (Exception e) {
                if (screenshotOnError) {
                    takeScreenshot("error_forward_" + screenshotCounter++);
                }
                throw e;
            }
        }
        
        @Override
        public void to(String url) {
            try {
                navigation.to(url);
                if (screenshotOnNavigation) {
                    takeScreenshot("after_navigate_to_" + screenshotCounter++);
                }
            } catch (Exception e) {
                if (screenshotOnError) {
                    takeScreenshot("error_navigate_to_" + screenshotCounter++);
                }
                throw e;
            }
        }

        @Override
        public void to(URL url) {
            try {
                navigation.to(url);
                if (screenshotOnNavigation) {
                    takeScreenshot("after_navigate_to_url_" + screenshotCounter++);
                }
            } catch (Exception e) {
                if (screenshotOnError) {
                    takeScreenshot("error_navigate_to_url_" + screenshotCounter++);
                }
                throw e;
            }
        }

        @Override
        public void refresh() {
            try {
                navigation.refresh();
                if (screenshotOnNavigation) {
                    takeScreenshot("after_refresh_" + screenshotCounter++);
                }
            } catch (Exception e) {
                if (screenshotOnError) {
                    takeScreenshot("error_refresh_" + screenshotCounter++);
                }
                throw e;
            }
        }
    }
}
