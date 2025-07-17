package com.taf.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.taf.utils.logs.LogManager;
import com.taf.utils.reporting.AllureManager;

import java.io.File;

public class ScreenshotUtils {
    public static void takeScreenshot(WebDriver driver, String fileName) {
        try {
            // Create a screenshot and save it with the specified file name
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File("screenshots/" + fileName + ".png");
            FileUtils.copyFile(screenshot, destFile);
            LogManager.info("Screenshot taken:", fileName);
            AllureManager.attachScreenshotsToAllure(fileName, destFile.getPath());
        } catch (Exception e) {
            LogManager.error("Failed to take screenshot:", e.getMessage());
        }
    }
}
