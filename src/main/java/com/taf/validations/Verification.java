package com.taf.validations;

import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

public class Verification extends BaseAssertion {

    public Verification() {
        super();
    }

    public Verification(WebDriver driver) {
        super(driver);
    }

    @Override
    @Step("Verifying the condition: {0} is true")
    protected void assertTrue(boolean condition, String message) {
        LogsManager.info("Verifying condition:", String.valueOf(condition), "is true");
        Assertions.assertTrue(condition, "Expected true but was false:" + message);
    }

    @Override
    @Step("Verifying the condition: {0} is false")
    protected void assertFalse(boolean condition, String message) {
        LogsManager.info("Verifying condition:", String.valueOf(condition), " is false");
        Assertions.assertFalse(condition, "Expected false but was true: " + message);
    }

    @Override
    @Step("Verifying that actual value: {0} equals expected value: {1}")
    protected void assertEquals(String actual, String expected, String message) {
        LogsManager.info("Verifying that actual value:", actual, "equals expected value:", expected);
        // Note: JUnit's assertEquals expects (expected, actual), but their parameter naming is confusing
        // We pass: actual as 1st param, expected as 2nd param
        // JUnit treats: 1st param as "expected" in error message, 2nd as "actual"
        // This produces correct error messages showing what value was expected vs what was received
        Assertions.assertEquals(expected.toLowerCase(), actual.toLowerCase(), message);
    }

    @Override
    protected void assertContains(String actual, String expected, String message) {
        LogsManager.info("Verifying that actual value:", actual, "contains expected value:", expected);
        Assertions.assertTrue(actual.toLowerCase().contains(expected.toLowerCase()),
                "Expected value: " + expected + " was not found in actual value: " + actual + ". " + message);
    }
}
