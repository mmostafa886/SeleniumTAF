package com.taf.utils.actions;

import com.taf.utils.logs.LogManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;

public class BrowserActions {
    private final WebDriver driver;

    public BrowserActions(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * maximize Window method
     */
    public void maximizeWindow() {
        driver.manage().window().maximize();
        LogManager.info("Browser window maximized.");
    }

    /**
     * get Current URL method
     */
    public String getCurrentUrl() {
        String currentUrl = driver.getCurrentUrl();
        LogManager.info("Current URL is: ", currentUrl);
        return currentUrl;
    }

    /**
     * Navigate to a specific URL
     * @param url the URL to navigate to
     */
    public void navigateTo(String url) {
        driver.get(url);
        LogManager.info("Navigated to URL: ", url);
    }

    /**
     * Refresh the current page
     */
    public void refreshPage() {
        driver.navigate().refresh();
        LogManager.info("Page refreshed.");
    }

    /**
     * Open a new window
     */
    public void openNewWindow() {
        driver.switchTo().newWindow(WindowType.WINDOW);
        LogManager.info("New browser window opened.");
    }



}
