package com.taf.utils.actions;

import com.taf.utils.WaitManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.taf.utils.logs.LogManager;

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
        waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                scrollToElementJS(locator);
                element.click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        LogManager.info("Clicked on element with locator:", locator.toString());
    }

    //type method
    @Step("Type text '{1}' into element with locator: {0}")
    public void type(By locator, String text) {
        waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                scrollToElementJS(locator);
                element.clear(); // Clear the field before typing
                element.sendKeys(text);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        LogManager.info("Typed text '", text, "' into element with locator: ", locator.toString());
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
                LogManager.info("Retrieved text from element with locator:", locator.toString(), " - Text:", msg);
                return !msg.isEmpty() ? msg : null;
            } catch (Exception e) {
                LogManager.info("Retrieved text from element with locator:", locator.toString(), " - Text:", null);
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
                LogManager.info("Uploaded file to element with locator:", locator.toString(), " - File Path:", filePath);
                return true;
            } catch (Exception e) {
                LogManager.error("Failed to upload file to element with locator:", locator.toString(), " - Error:", e.getMessage());
                return false;
            }
        });
    }

    //A function to scroll to an element using JavaScript
    @Step("Scroll to element with locator: {0}")
    public void scrollToElementJS(By locator) {
        WebElement element = findElement(locator);
        ((JavascriptExecutor) driver).executeScript("""
                arguments[0].scrollIntoView({behavior: "auto", block: "center",inline: "center"});""", element);

        LogManager.info("Scrolled to element with locator:", locator.toString());
    }

    //Find an element using a locator
    @Step("Find element with locator: {0}")
    public WebElement findElement(By locator) {
       return driver.findElement(locator);
    }
}
