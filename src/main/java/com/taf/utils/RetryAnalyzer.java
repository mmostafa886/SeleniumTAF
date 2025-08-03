package com.taf.utils;

import com.taf.utils.dataReader.PropertyReader;
import com.taf.utils.logs.LogsManager;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int maxRetryCount = Integer.parseInt(PropertyReader.getProperty("retryCount")); // Retry failed tests up to 2 times

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            LogsManager.info("Retrying test: " + result.getName() + " | Attempt #" + retryCount);
            return true;
        }
        return false;
    }

    public int getRetryCount() {
        return retryCount;
    }
}
