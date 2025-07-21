package com.taf.media;

import com.taf.utils.TimeManager;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.taf.utils.logs.LogsManager;

import java.io.File;

public class ScreenshotsManager {

    private static final String SCREENSHOT_DIR = "test-outputs/ScreenShots/";

    @Step("Taking screenshot with name: {fileName}")
    public static void takeScreenshot(WebDriver driver, String screenshotName) {
        try {
            // Create a screenshotSource and save it with the specified file name
            File screenshotSource = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Save the screenshotSource to the specified directory
            File screenshotFile = new File(SCREENSHOT_DIR+ screenshotName+ TimeManager.getCompactTimeStamp() + ".png");

            // Copy the screenshotSource to the destination file
            FileUtils.copyFile(screenshotSource, screenshotFile);

           //TODO: Implement AllureManager to attach screenshots
            // Log/Attach the screenshot information and attach it to Allure
            LogsManager.info("Screenshot taken:", screenshotName);
            //AllureManager.attachScreenshotsToAllure(screenshotName, screenshotFile.getPath());
        } catch (Exception e) {
            LogsManager.error("Failed to take screenshot:", e.getMessage());
        }
    }

    /**
     * take Screenshot of a specific element
     */
    @Step("Taking screenshot of element")
    public static void takeElementScreenshot(WebDriver driver, String screenshotName, By elementLocator) {
        try {
            // Create a screenshotSource and save it with the specified file name
            File screenshotSource = driver.findElement(elementLocator).getScreenshotAs(OutputType.FILE);

            String ariaName = driver.findElement(elementLocator).getAccessibleName();

            // Save the screenshotSource to the specified directory
            File screenshotFile = new File(SCREENSHOT_DIR + ariaName + TimeManager.getCompactTimeStamp() + ".png");

            // Copy the screenshotSource to the destination file
            FileUtils.copyFile(screenshotSource, screenshotFile);

            //TODO: Implement AllureManager to attach screenshots
            // Log/Attach the screenshot information and attach it to Allure
            LogsManager.info("Element Screenshot taken:", elementLocator.toString());
            //AllureManager.attachScreenshotsToAllure(screenshotName, screenshotFile.getPath());
        } catch (Exception e) {
            LogsManager.error("Failed to take element screenshot:", e.getMessage());
        }
    }
}
