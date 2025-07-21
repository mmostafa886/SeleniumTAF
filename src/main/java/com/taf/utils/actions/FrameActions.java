package com.taf.utils.actions;

import com.taf.utils.WaitManager;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FrameActions {
    private final WebDriver driver;
    private final WaitManager waitManager;

    public FrameActions(WebDriver driver) {
        this.driver = driver;
        this.waitManager = new WaitManager(driver);
    }

    /**
     * Switch to a frame by its index.
     *
     * @param index the index of the frame to switch to
     */
    @Step("Switching to frame by index: {index}")
    public void switchToFrameByIndex(int index) {
        LogsManager.info("Attempting to switch to frame by index: ", String.valueOf(index));
        waitManager.fluentWait().until(driver -> {
            try {
                driver.switchTo().frame(index);
                return true; // Successfully switched to frame
            } catch (Exception e) {
                return false; // Frame not found
            }
        });
    }

    /**
     * Switch to a frame by its name or ID.
     *
     * @param nameOrId the name or ID of the frame to switch to
     */
    @Step("Switching to frame by name or ID: {nameOrId}")
    public void switchToFrameByNameOrId(String nameOrId) {
        LogsManager.info("Attempting to switch to frame by name or ID: ", nameOrId);
        waitManager.fluentWait().until(driver -> {
            try {
                driver.switchTo().frame(nameOrId);
                return true; // Successfully switched to frame
            } catch (Exception e) {
                return false; // Frame not found
            }
        });
    }

    /**
     * Switch to a frame by its locator.
     * @param frameLocator the Locator of the frame to switch to
     */
    @Step("Switching to frame by locator: {frameLocator}")
    public void switchToFrameByLocator(By frameLocator) {
        LogsManager.info("Attempting to switch to frame by locator: ", frameLocator.toString());
        waitManager.fluentWait().until(driver -> {
            try {
                driver.switchTo().frame(driver.findElement(frameLocator));
                return true; // Successfully switched to frame
            } catch (Exception e) {
                return false; // Frame not found
            }
        });
    }

    /**
     * Switch back to the default content from a frame.
     */
    @Step("Switching back to default content")
    public void switchToDefaultContent() {
        LogsManager.info("Attempting to switch back to default content");
        waitManager.fluentWait().until(driver -> {
            try {
                driver.switchTo().defaultContent();
                return true; // Successfully switched to default content
            } catch (Exception e) {
                return false; // Default content not found
            }
        });
    }
}
