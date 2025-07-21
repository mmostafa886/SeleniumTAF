package com.taf.validations;

import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class Verification extends BaseAssertion {

    public Verification(WebDriver driver) {
        super(driver);
    }

    @Override
    @Step("Verifying the condition: {0} is true")
    protected void assertTrue(boolean condition, String message) {
        LogsManager.info("Verifying condition:", String.valueOf(condition), "is true");
        Assert.assertTrue(condition, "Expected true but was false:" + message);
    }

    @Override
    @Step("Verifying the condition: {0} is false")
    protected void assertFalse(boolean condition, String message) {
        LogsManager.info("Verifying condition:", String.valueOf(condition), " is false");
        Assert.assertFalse(condition, "Expected false but was true: " + message);
    }

    @Override
    @Step("Verifying that actual value: {0} equals expected value: {1}")
    protected void assertEquals(String actual, String expected, String message) {
        LogsManager.info("Verifying that actual value:", actual, "equals expected value:", expected);
        Assert.assertEquals(actual, expected, message);
    }
}
