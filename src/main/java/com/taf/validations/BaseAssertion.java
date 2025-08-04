package com.taf.validations;

import com.taf.utils.FileUtils;
import com.taf.utils.WaitManager;
import com.taf.utils.actions.ElementActions;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class BaseAssertion {

    protected WebDriver driver;
    protected WaitManager waitManager;
    protected ElementActions elementActions;

    protected BaseAssertion() {

    }

    protected BaseAssertion(WebDriver driver) {
        this.driver = driver;
        this.waitManager = new WaitManager(driver);
        this.elementActions = new ElementActions(driver);
    }

    protected abstract void assertTrue(boolean condition, String message);

    protected abstract void assertFalse(boolean condition, String message);

    protected abstract void assertEquals(String actual, String expected, String message);

    public BaseAssertion Equals(String actual, String expected, String message) {
        assertEquals(actual, expected, message);
        return this;
    }

    @Step("Asserting the element with Locator: {0} is visible")
    public void isElementVisible(By locator) {
        boolean visibilityFlag = waitManager.fluentWait().until(d -> {
            try {
                d.findElement(locator).isDisplayed();
                LogsManager.info("Element with Locator:", locator.toString(), "is visible.");
                return true;
            } catch (Exception e) {
                LogsManager.error("Element with Locator:", locator.toString(), "is not visible.", e.getMessage());
                return false;
            }
        });
        assertTrue(visibilityFlag,
                "Element with Locator: " + locator.toString() + " is not visible.");
    }

    /**
     * Verify Page URL
     */
    @Step("Asserting that the current URL contains: {0}")
    public void assertPageURL(String expectedURL) {
        String currentURL = driver.getCurrentUrl();
        LogsManager.info("Verifying that the current URL:", currentURL, "contains:", expectedURL);
        assertEquals(currentURL, expectedURL,
                "Expected URL: " + expectedURL + " but was: " + currentURL);
    }

    @Step("Asserting that the current page title is: {0}")
    public void assertPageTitle(String expectedTitle) {
        String actualTitle = driver.getTitle();
        assertEquals(actualTitle, expectedTitle, "Title does not match. Expected: " + expectedTitle + ", Actual: " + actualTitle);
    }

    @Step("Asserting that the file {0} exists")
    public void assertFileExists(String fileName, String message) {
        waitManager.fluentWait().until(
                d -> FileUtils.isFileExists(fileName)
        );
        assertTrue(FileUtils.isFileExists(fileName), message);
    }
}