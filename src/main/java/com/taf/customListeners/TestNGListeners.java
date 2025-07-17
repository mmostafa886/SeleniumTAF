package com.taf.customListeners;

import org.testng.*;
import com.taf.utils.logs.LogManager;
import com.taf.utils.dataReader.PropertyReader;

public class TestNGListeners implements IInvokedMethodListener, ITestListener, IExecutionListener {

        public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
            if (method.isTestMethod())
                LogManager.info("Test Execution Started for:", method.getTestMethod().getMethodName());
        }

        public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
            if (method.isTestMethod())
                LogManager.info("Test Execution Started for:", method.getTestMethod().getMethodName());
        }

        public void onTestSuccess(ITestResult result) {
            LogManager.info("Test Passed:" , result.getMethod().getMethodName());
        }

        public void onTestFailure(ITestResult result) {
            LogManager.error("Test Failed:" , result.getMethod().getMethodName());
        }

        public void onTestSkipped(ITestResult result) {
            LogManager.warn("Test Skipped:" , result.getMethod().getMethodName());
        }
        public void onExecutionStart() {
            LogManager.info("Execution started");
            PropertyReader.loadProperties();
        }

        public void onExecutionFinish() {
            LogManager.info("Execution finished");
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
