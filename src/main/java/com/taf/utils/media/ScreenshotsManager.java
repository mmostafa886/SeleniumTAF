package com.taf.utils.media;

import com.taf.utils.TimeManager;
import com.taf.utils.logs.LogsManager;
import com.taf.utils.reporting.AllureAttachmentManager;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;

public class ScreenshotsManager {

    public static final String SCREENSHOTS_PATH = "test-output/ScreenShots/";

    /**
     * Take a full page screenshot and save it with a timestamp.
     * @param driver the WebDriver instance
     * @param screenshotName the name to use for the screenshot file
     */
    //@Step("Taking full page screenshot with name: {screenshotName}")
    public static void takeFullPageScreenshot(WebDriver driver, String screenshotName) {
        try {
            // Capture screenshot using TakesScreenshot
            File screenshotSrc = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Save screenshot to a file if needed
            File screenshotFile = new File(SCREENSHOTS_PATH + screenshotName + "-" + TimeManager.getTimeStamp() + ".png");
            FileUtils.copyFile(screenshotSrc, screenshotFile);


            AllureAttachmentManager.attachScreenshot(screenshotName,screenshotFile.getAbsolutePath());

            LogsManager.info("Capturing Screenshot Succeeded");
        } catch (Exception e) {
            LogsManager.error("Failed to Capture Screenshot " + e.getMessage());
        }
    }

    /**
     * Take a screenshot of a specific element on the page.
     * @param driver the WebDriver instance
     * @param elementSelector the By selector for the element to capture
     */
    @Step("Taking screenshot of element with selector: {elementSelector}")
    public static void takeElementScreenshot(WebDriver driver, By elementSelector) {
        try {
            // Highlight the element if needed (not implemented here)
            // Capture screenshot using TakesScreenshot
            String ariaName = driver.findElement(elementSelector).getAccessibleName();
            File screenshotSrc = driver.findElement(elementSelector).getScreenshotAs(OutputType.FILE);

            // Save screenshot to a file if needed
            File screenshotFile = new File(SCREENSHOTS_PATH + ariaName + "-" + TimeManager.getTimeStamp() + ".png");
            FileUtils.copyFile(screenshotSrc, screenshotFile);
            // TODO: Attach the screenshot to Allure if needed

            LogsManager.info("Capturing Screenshot Succeeded");
        } catch (Exception e) {
            LogsManager.error("Failed to Capture Element Screenshot" , e.getMessage());
        }
    }
}
