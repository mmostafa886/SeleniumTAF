package com.taf.utils.reporting;

import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import com.taf.utils.dataReader.PropertyReader;
import com.taf.utils.logs.LogManager;

import java.io.File;
import java.nio.file.Path;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;
import static java.nio.file.Files.newInputStream;

public class AllureManager {

    public static void clearAllureResults() {
        LogManager.info("Clearing Allure results directory...");
        FileUtils.deleteQuietly(
            new File("test-output/allure-results")
        );
    }

    @Step("Attaching screenshot to Allure: {screenshotName}")
    public static void attachScreenshotsToAllure(String screenshotName, String screenshotPath) {
        LogManager.info("Attaching screenshot to Allure: " + screenshotPath);
        try {
            File screenshotFile = new File(screenshotPath);
            Allure.addAttachment(screenshotName, newInputStream(Path.of(screenshotPath)));
        } catch (Exception e) {
            LogManager.error("Failed to attach screenshot to Allure:", e.getMessage());
        }
    }

    public static void setAllureEnvironment() {
        LogManager.info("Setting Allure environment variables");
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Environment", PropertyReader.getProperty("os.name"))
                        .put("Browser", PropertyReader.getProperty("browserType"))
                        .put("JDK Version", System.getProperty("java.version"))
                        .put("WebApp_URL", PropertyReader.getProperty("baseURL"))
                        .build(), System.getProperty("user.dir")
                        + File.separator+ "/test-output/allure-results/" + File.separator);

    }
}
