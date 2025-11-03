package com.taf.validations;

import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

public class Validation extends BaseAssertion {

    private static final ThreadLocal<List<Executable>> assertions = ThreadLocal.withInitial(ArrayList::new);
    private static final ThreadLocal<Boolean> used = ThreadLocal.withInitial(() -> false);

    public Validation(WebDriver driver) {
        super(driver);
    }

    public Validation() {
        super();
    }

    /**
     * Soft assertion to validate that the condition is true.
     */
    @Override
    @Step("Validating the condition: {0} is true")
    protected void assertTrue(boolean condition, String message) {
        used.set(true);
        LogsManager.info("Validating condition:", String.valueOf(condition), "is true");
        assertions.get().add(() -> Assertions.assertTrue(condition, message));
    }

    /**
     * Soft assertion to validate that the condition is false.
     */
    @Override
    @Step("Validating the condition: {0} is false")
    protected void assertFalse(boolean condition, String message) {
        used.set(true);
        LogsManager.info("Validating condition:", String.valueOf(condition), "is false");
        assertions.get().add(() -> Assertions.assertFalse(condition, message));
    }

    /**
     * Soft assertion to validate that the actual value equals the expected value.
     */
    @Override
    @Step("Validating that actual value: {0} equals expected value: {1}")
    protected void assertEquals(String actual, String expected, String message) {
        used.set(true);
        LogsManager.info("Validating that actual value:", actual, "equals expected value:", expected);
        assertions.get().add(() -> Assertions.assertEquals(expected, actual, message));
    }

    @Override
    protected void assertContains(String actual, String expected, String message) {
        used.set(true);
        LogsManager.info("Validating that actual value:", actual, "contains expected value:", expected);
        assertions.get().add(() -> Assertions.assertTrue(actual.contains(expected),
                "Expected value: " + expected + " was not found in actual value: " + actual + ". " + message));
    }

    /**
     * Assert all validations collected during the test execution.
     */
    @Step("Assert all validations")
    public static void assertAll() {
        if (!used.get()) {
            LogsManager.info("No validations were performed, skipping assertAll.");
            return;
        }
        try {
            LogsManager.info("Asserting all validations");
            Assertions.assertAll(assertions.get());
        } catch (Exception e) {
            LogsManager.error("Assertion Failed -", e.getMessage());
            throw e;
        } finally {
            LogsManager.info("Resetting assertions for the next test");
            assertions.get().clear();
            used.set(false);
        }
    }

}
