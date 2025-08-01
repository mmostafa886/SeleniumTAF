package com.taf.utils.actions;

import com.taf.utils.WaitManager;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;

public class ElementActions {
    private final WebDriver driver;
    private final WaitManager waitManager;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.waitManager = new WaitManager(driver);
    }

    //click method
    @Step("Click on element with locator: {0}")
    public void click(By locator) {
        LogsManager.info("Attempting to click on element with locator:", locator.toString());
        waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                scrollToElementJS(locator);
                // Wait until the element is stable (not moving)
                Point initialLocation = element.getLocation();
                LogsManager.info("initialLocation: " + initialLocation);
                Point finalLocation = element.getLocation();
                LogsManager.info("finalLocation: " + finalLocation);
                if (!initialLocation.equals(finalLocation)) {
                    return false; // still moving, wait longer
                }
                element.click();
                LogsManager.info("Clicked on element: " + locator);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    //type method
    @Step("Type text '{1}' into element with locator: {0}")
    public ElementActions type(By locator, String text) {
        waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                scrollToElementJS(locator);
                element.clear(); // Clear the field before typing
                element.sendKeys(text);
                LogsManager.info("Typed text '", text, "' into element with locator:", locator.toString());
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        return this;
    }

    //hovering
    @Step("Hover over element with locator: {0}")
    public ElementActions hover(By locator) {
        waitManager.fluentWait().until(d ->
                {
                    try {
                        WebElement element = d.findElement(locator);
                        scrollToElementJS(locator);
                        new Actions(d).moveToElement(element).perform();
                        LogsManager.info("Hovered over element: " + locator);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
        );
        return this;
    }

    //getText method
    @Step("Get text from element with locator: {0}")
    public String getText(By locator) {
        String text;
        return waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                scrollToElementJS(locator);
                String msg = element.getText();
                LogsManager.info("Retrieved text from element with locator:", locator.toString(), " - Text:", msg);
                return !msg.isEmpty() ? msg : null;
            } catch (Exception e) {
                LogsManager.info("Retrieved text from element with locator:", locator.toString(), " - Text:", null);
                return null;
            }
        });
    }

    //Upload file method
    @Step("Upload file to element with locator: {0}")
    public void uploadFile(By locator, String filePath) {
        String absoluteFilePath = System.getProperty("user.dir")+ File.separator + filePath;
        waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                scrollToElementJS(locator);
                element.sendKeys(absoluteFilePath);
                LogsManager.info("Uploaded file to element with locator:", locator.toString(), " - File Path:", filePath);
                return true;
            } catch (Exception e) {
                LogsManager.error("Failed to upload file to element with locator:", locator.toString(), " - Error:", e.getMessage());
                return false;
            }
        });
    }

    //A function to scroll to an element using JavaScript
    @Step("Scroll to element with locator: {0}")
    public void scrollToElementJS(By locator) {
        WebElement element = findElement(locator);
        Point intialLocation = element.getLocation();
        int initialX = intialLocation.getX();
        int initialY = intialLocation.getY();
        waitManager.fluentWait().until(d -> {
            ((JavascriptExecutor) driver).executeScript("""
                            arguments[0].scrollIntoView({behavior: "smooth", block: "center",inline: "center"});""", element);
            try {
                // scroll if the element is not completely in view
                if (element.isDisplayed() && element.isEnabled()) {
                    LogsManager.info("Element is already in view:", locator.toString());
                }
                else {
                    LogsManager.info("Element is not in view after scrolling.", "Attempting to scroll again using actions");
                    new Actions(d).scrollToElement(element).perform(); // Attempt to scroll again using Actions
                }
                return true; // Element is already in view
            } catch (Exception e) {
                LogsManager.error("Failed to scroll to element with locator:", locator.toString());
                return false;
            }

        });

    }

    //Find an element using a locator
    @Step("Find element with locator: {0}")
    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    //select from dropdown
    @Step("Select value '{1}' from dropdown with locator: {0}")
    public void selectFromDropdown(By locator, String value) {
        waitManager.fluentWait().until(d ->
                {
                    try {
                        WebElement element = d.findElement(locator);
                        scrollToElementJS(locator);
                        Select select = new Select(element);
                        select.selectByVisibleText(value);
                        LogsManager.info("Selected value '" + value + "' from dropdown: " + locator);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
        );
    }

    @Step("Get the number of Elements with locator: {0}")
    public int getNumberOfElements(By locator) {
        waitManager.fluentWait().until(d -> {
            try {
                int count = d.findElements(locator).size();
                LogsManager.info("Number of elements with locator:", locator.toString(), " - Count:", String.valueOf(count));
                return count > 0 ? count : 0;
            } catch (Exception e) {
                LogsManager.error("Failed to get number of elements with locator:", locator.toString(), " - Error:", e.getMessage());
                return 0;
            }
        });
        return driver.findElements(locator).size();
    }
}