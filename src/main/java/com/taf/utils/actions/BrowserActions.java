package com.taf.utils.actions;

import com.taf.utils.WaitManager;
import com.taf.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;

public class BrowserActions {
    private final WebDriver driver;
    private WaitManager waitManager;

    public BrowserActions(WebDriver driver) {
        this.driver = driver;
        this.waitManager = new WaitManager(driver);
    }

    /**
     * maximize Window method
     */
    public void maximizeWindow() {
        driver.manage().window().maximize();
        LogsManager.info("Browser window maximized.");
    }

    /**
     * get Current URL method
     */
    public String getCurrentUrl() {
        String currentUrl = driver.getCurrentUrl();
        LogsManager.info("Current URL is:", currentUrl);
        return currentUrl;
    }

    /**
     * Navigate to a specific URL
     * @param url the URL to navigate to
     */
    public void navigateTo(String url) {
        driver.get(url);
        LogsManager.info("Navigated to URL:", url);
    }

    /**
     * Refresh the current page
     */
    public void refreshPage() {
        driver.navigate().refresh();
        LogsManager.info("Page refreshed.");
    }

    /**
     * Open a new window
     */
    public void openNewWindow() {
        driver.switchTo().newWindow(WindowType.WINDOW);
        LogsManager.info("New browser window opened.");
    }

    //close extension tab
    public void closeExtensionTab() {
        String currentWindowHandle = driver.getWindowHandle(); //0 1
        try {
            waitManager.fluentWait().until(
                    d ->
                    {
                        return d.getWindowHandles().size() > 1; //wait until extension tab is opened
                    }
            );
            driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString()).close();
            driver.switchTo().window(currentWindowHandle); //switch back to the main window
            LogsManager.info("Extension tab closed");
        } catch (Exception e) {
            LogsManager.warn("No extension tab found to close.");
        }
    }




}
