package com.taf.validations;

import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

public class Validation extends BaseAssertion {

    private static SoftAssert softAssert = new SoftAssert();
    private static  boolean used = false; // Flag to track if softAssert has been used.

    public Validation(WebDriver driver) {
        super(driver);
    }

    /**
     * Soft assertion to validate that the condition is true.
     * @param condition
     * @param message
     */
    @Override
    @Step("Validating the condition: {0} is true")
    protected void assertTrue(boolean condition, String message) {
        used = true; // Mark that softAssert has been used.
        LogsManager.info("Validating condition:", String.valueOf(condition), "is true");
        softAssert.assertTrue(condition, message);
    }

    /**
     * Soft assertion to validate that the condition is false.
     * @param condition
     * @param message
     */
    @Override
    @Step("Validating the condition: {0} is false")
    protected void assertFalse(boolean condition, String message) {
        used = true; // Mark that softAssert has been used.
        LogsManager.info("Validating condition:", String.valueOf(condition), "is false");
        softAssert.assertFalse(condition, message);
    }

    /**
     * Soft assertion to validate that the actual value equals the expected value.
     * @param actual
     * @param expected
     * @param message
     */
    @Override
    @Step("Validating that actual value: {0} equals expected value: {1}")
    protected void assertEquals(String actual, String expected, String message) {
        used = true; // Mark that softAssert has been used.
        LogsManager.info("Validating that actual value:", actual, "equals expected value:", expected);
        softAssert.assertEquals(actual, expected, message);
    }

    /**
     * Soft_Assert all validations collected during the test execution.
     */
    @Step("Assert all validations")
    public static void assertAll() {
        if (!used) {
            LogsManager.info("No validations were performed, skipping assertAll.");
            return; // If no validations were performed, skip assertAll.
        }
        try {
            LogsManager.info("Asserting all validations");
            softAssert.assertAll();
        } catch (Exception e) {
            LogsManager.error("Assertion Failed -", e.getMessage());
        } finally {
            LogsManager.info("Resetting softAssert for the next test");
            softAssert = new SoftAssert(); // Reset softAssert for the next test
        }
    }

}
