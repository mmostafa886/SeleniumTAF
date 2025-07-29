package com.taf.customListeners;

import com.taf.drivers.WebDriverProvider;
import com.taf.utils.logs.LogsManager;
import com.taf.utils.media.ScreenRecordManager;
import com.taf.utils.media.ScreenshotsManager;
import com.taf.utils.FileUtils;
import com.taf.utils.reporting.AllureAttachmentManager;
import com.taf.utils.reporting.AllureEnvironmentManager;
import com.taf.utils.reporting.AllureReportGenerator;
import com.taf.validations.Validation;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import com.taf.utils.dataReader.PropertyReader;

import java.io.File;

public class TestNGListeners implements IInvokedMethodListener, ITestListener, IExecutionListener {

        public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
            if (method.isTestMethod()) {
                ScreenRecordManager.startRecording();
                LogsManager.info("Test Case " + testResult.getName() + " started");
            }
        }

        public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
            WebDriver driver = null;
            if (method.isTestMethod())
            {
                ScreenRecordManager.stopRecording(testResult.getName());
                Validation.assertAll();
                if (testResult.getInstance() instanceof WebDriverProvider provider)
                    driver = provider.getWebDriver(); //initialize driver from WebDriverProvider
                switch (testResult.getStatus()){
                    case ITestResult.SUCCESS -> ScreenshotsManager.takeFullPageScreenshot(driver,"passed-" + testResult.getName());
                    case ITestResult.FAILURE -> ScreenshotsManager.takeFullPageScreenshot(driver,"failed-" + testResult.getName());
                    case ITestResult.SKIP -> ScreenshotsManager.takeFullPageScreenshot(driver,"skipped-" + testResult.getName());
                }
                AllureAttachmentManager.attachLogs();
                AllureAttachmentManager.attachRecords(testResult.getName());
            }
        }

        public void onTestSuccess(ITestResult result) {
            LogsManager.info("Test" , result.getMethod().getMethodName(), "Passed");
        }

        public void onTestFailure(ITestResult result) {
            LogsManager.error("Test" , result.getMethod().getMethodName(), "Failed");
        }

        public void onTestSkipped(ITestResult result) {
            LogsManager.warn("Test" , result.getMethod().getMethodName(), "Skipped");
        }
        public void onExecutionStart() {
            LogsManager.info("Test Execution started");
            PropertyReader.loadProperties();
            LogsManager.info("Properties loaded");
            cleanTestOutputDirectories();
            LogsManager.info("Directories cleaned");
            createTestOutputDirectories();
            LogsManager.info("Directories created");
            AllureEnvironmentManager.setEnvironmentVariables();
            LogsManager.info("Allure environment set");
        }

        public void onExecutionFinish() {
            AllureReportGenerator.generateReports(false);
            AllureReportGenerator.copyHistory();
            AllureReportGenerator.generateReports(true);
            AllureReportGenerator.openReport(AllureReportGenerator.renameReport());
            LogsManager.info("Test Execution Finished");
        }


    // cleaning and creating dirs (logs, screenshots, recordings,allure-results)
    private void cleanTestOutputDirectories() {
        // Implement logic to clean test output directories
       // FileUtils.cleanDirectory(AllureConstants.RESULTS_FOLDER.toFile());
        FileUtils.cleanDirectory(new File(ScreenshotsManager.SCREENSHOTS_PATH));
        FileUtils.cleanDirectory(new File(ScreenRecordManager.RECORDINGS_PATH));
        FileUtils.cleanDirectory(new File(LogsManager.LOGS_PATH));
    }

    // Implement logic to create test output directories
    private void createTestOutputDirectories() {
        FileUtils.createDirectory(ScreenshotsManager.SCREENSHOTS_PATH);
        FileUtils.createDirectory(ScreenRecordManager.RECORDINGS_PATH);
    }
}
