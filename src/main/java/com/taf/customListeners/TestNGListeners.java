package com.taf.customListeners;

import com.taf.utils.reporting.AllureManager;
import org.testng.*;
import com.taf.utils.logs.LogsManager;
import com.taf.utils.dataReader.PropertyReader;

public class TestNGListeners implements IInvokedMethodListener, ITestListener, IExecutionListener {

        public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
            if (method.isTestMethod())
                LogsManager.info("Test Execution Started for:", method.getTestMethod().getMethodName());
        }

        public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
            if (method.isTestMethod())
                LogsManager.info("Test Execution Started for:", method.getTestMethod().getMethodName());
        }

        public void onTestSuccess(ITestResult result) {
            LogsManager.info("Test Passed:" , result.getMethod().getMethodName());
        }

        public void onTestFailure(ITestResult result) {
            LogsManager.error("Test Failed:" , result.getMethod().getMethodName());
        }

        public void onTestSkipped(ITestResult result) {
            LogsManager.warn("Test Skipped:" , result.getMethod().getMethodName());
        }
        public void onExecutionStart() {
            LogsManager.info("Execution started");
            PropertyReader.loadProperties();
            AllureManager.clearAllureResults();
        }

        public void onExecutionFinish() {
            LogsManager.info("Execution finished");
            AllureManager.setAllureEnvironment();
        }

/*        private  int attemps = 0;
        @Override
        public boolean retry(ITestResult iTestResult) {
            if (iTestResult.getStatus() == ITestResult.FAILURE && attemps==0) //1 attempt
            {
                attemps++;
                return true;
            }
            return false;
        }*/
}
