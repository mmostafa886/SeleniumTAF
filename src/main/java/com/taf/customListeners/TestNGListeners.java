package com.taf.customListeners;

import com.taf.drivers.UITest;
import com.taf.drivers.WebDriverProvider;
import com.taf.utils.RetryAnalyzer;
import com.taf.utils.logs.LogsManager;
import com.taf.utils.media.ScreenRecordManager;
import com.taf.utils.media.ScreenshotsManager;
import com.taf.utils.FileUtils;
import com.taf.utils.reporting.AllureAttachmentManager;
import com.taf.utils.reporting.AllureConstants;
import com.taf.utils.reporting.AllureEnvironmentManager;
import com.taf.utils.reporting.AllureReportGenerator;
import com.taf.validations.Validation;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import com.taf.utils.dataReader.PropertyReader;
import org.testng.annotations.ITestAnnotation;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestNGListeners implements
        IInvokedMethodListener, ITestListener, IExecutionListener, ISuiteListener, IAnnotationTransformer {

    @Override
    public void transform(
            ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // Automatically set RetryAnalyzer for all @Test methods
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }

    public void onStart(ISuite suite) {
        suite.getXmlSuite().setName("Automation Exercise");
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
        AllureReportGenerator.copyHistory();
        AllureReportGenerator.generateReports(false);
        AllureReportGenerator.generateReports(true);
        AllureReportGenerator.openReport(AllureReportGenerator.renameReport());
        LogsManager.info("Test Execution Finished");
    }

    // Synchronization lock for log file operations in parallel execution
    private static final Object LOG_FILE_LOCK = new Object();

    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        String testMethodName = testResult.getName();
        String testDescription = testResult.getMethod().getDescription();
        if (method.isTestMethod()) {
            // Synchronize log file clearing to prevent race conditions in parallel execution
            synchronized (LOG_FILE_LOCK) {
                try {
                    FileUtils.clearFileContents(LogsManager.LOGS_PATH + "logs.log");
                    LogsManager.info("Logs cleared for test:", testMethodName + "/" + testDescription);
                } catch (Exception e) {
                    LogsManager.warn("Could not clear log file (may be locked by another thread):", e.getMessage());
                }
            }
            if (testResult.getInstance() instanceof UITest) {
                try {
                    ScreenRecordManager.startRecording();
                } catch (Exception e) {
                    LogsManager.warn("Could not start screen recording:", e.getMessage());
                }
            }
            LogsManager.info("Test Case:", testMethodName + "/" + testDescription, "starting ....");
        }
    }

    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        String testMethodName = testResult.getName();
        String testDescription = testResult.getMethod().getDescription();
        String fullTestName = testMethodName + "_" + testDescription;
        WebDriver driver = null;
        if (method.isTestMethod()) {
            if (testResult.getInstance() instanceof UITest) {
                try {
                    ScreenRecordManager.stopRecording(testResult.getName());
                } catch (Exception e) {
                    LogsManager.warn("Could not stop screen recording:", e.getMessage());
                }
            }

            try {
                if (testResult.getInstance() instanceof WebDriverProvider provider) {
                    driver = provider.getWebDriver();//initialize driver from WebDriverProvider
                    // Check if driver is still valid before taking screenshot
                    if (driver != null) {
                        try {
                            // Verify driver session is still active
                            driver.getTitle(); // Will throw exception if session is closed
                            switch (testResult.getStatus()) {
/*                              case ITestResult.SUCCESS ->
                                        ScreenshotsManager.takeFullPageScreenshot(driver, "passed-" + fullTestName);*/
                                case ITestResult.FAILURE ->
                                        ScreenshotsManager.takeFullPageScreenshot(driver, "failed-" + fullTestName);
/*                              case ITestResult.SKIP ->
                                        ScreenshotsManager.takeFullPageScreenshot(driver, "skipped-" + fullTestName);*/
                            }
                        } catch (org.openqa.selenium.NoSuchSessionException e) {
                            LogsManager.warn("WebDriver session already closed for thread " +
                                           Thread.currentThread().threadId() + ", skipping screenshot");
                        }
                    } else {
                        LogsManager.debug("Driver is null, skipping screenshot");
                    }
                }
            } catch (Exception e) {
                LogsManager.error("Error taking screenshot:", e.getMessage());
                LogsManager.debug("Driver not initialized or the Test is an API test.");
            }

            try {
                AllureAttachmentManager.attachRecords(testResult.getName());
            } catch (Exception e) {
                LogsManager.warn("Could not attach records:", e.getMessage());
            }
        }

        Validation.assertAll(testResult);
        LogsManager.info("Test Case:", fullTestName, "finished.");

        //attach logs to allure report
        synchronized (LOG_FILE_LOCK) {
            try {
                File logFile = new File(LogsManager.LOGS_PATH + "logs.log");
                if (logFile.exists()) {
                    AllureAttachmentManager.attachLogs();
                }
            } catch (Exception e) {
                LogsManager.error("Error attaching logs", e.getMessage());
            }
        }
    }


    public void onTestSuccess(ITestResult result) {
        IRetryAnalyzer retry = result.getMethod().getRetryAnalyzer(result);

        if (retry != null && retry instanceof RetryAnalyzer) {
            int retryCount = ((RetryAnalyzer) retry).getRetryCount();
            if (retryCount > 0) {
                LogsManager.info("Test passed after " + retryCount + " retries: " + result.getName());
            }
        }

        LogsManager.info("Test", result.getMethod().getMethodName(), "Passed");
    }

    public void onTestFailure(ITestResult result) {
        IRetryAnalyzer retry = result.getMethod().getRetryAnalyzer(result);
        if (retry != null && retry instanceof RetryAnalyzer) {
            int retryCount = ((RetryAnalyzer) retry).getRetryCount();
            if (retryCount > 0) {
                LogsManager.warn("Test failed, retry #" + retryCount + " for test: " + result.getName());
            }
        }
        LogsManager.error("Test", result.getMethod().getMethodName(), "Failed");
    }

    public void onTestSkipped(ITestResult result) {
        LogsManager.warn("Test", result.getMethod().getMethodName(), "Skipped");
    }


    // cleaning and creating dirs (logs, screenshots, recordings,allure-results)
    private void cleanTestOutputDirectories() {
        // Implement logic to clean test output directories
        FileUtils.cleanDirectory(AllureConstants.RESULTS_FOLDER.toFile());
        FileUtils.cleanDirectory(new File(ScreenshotsManager.SCREENSHOTS_PATH));
        FileUtils.cleanDirectory(new File(ScreenRecordManager.RECORDINGS_PATH));
        FileUtils.cleanDirectory(new File("src/test/resources/downloads/"));
    }

    // Implement logic to create test output directories
    private void createTestOutputDirectories() {
        FileUtils.createDirectory(ScreenshotsManager.SCREENSHOTS_PATH);
        FileUtils.createDirectory(ScreenRecordManager.RECORDINGS_PATH);
        FileUtils.createDirectory("src/test/resources/downloads/");
    }
}
