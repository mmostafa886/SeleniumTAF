package com.taf.utils.actions;

import com.taf.utils.WaitManager;
import com.taf.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AlertActions {

    private final WebDriver driver;
    private final WaitManager waitManager;

    private final By consentButton = By.xpath("//button[.='Consent']");

    public AlertActions(WebDriver driver) {
        this.driver = driver;
        this.waitManager = new WaitManager(driver);
    }

    /**
     * Accepts the alert if present.
     */
    @Step("Accepting alert if present")
    public void acceptAlert() {
        LogsManager.info("Attempting to accept alert if present.");
        waitManager.fluentWait().until(driver -> {
            try {
                driver.switchTo().alert().accept();
                return true; // Alert is present
            } catch (Exception e) {
                LogsManager.error("Failed to accept alert", e.getMessage());
                return false; // No alert present
            }
        });
    }

    /**
     * Dismisses the alert if present.
     */
    @Step("Dismissing alert if present")
    public void dismissAlert() {
        LogsManager.info("Attempting to dismiss alert if present.");
        waitManager.fluentWait().until(driver -> {
            try {
                driver.switchTo().alert().dismiss();
                return true; // Alert is present
            } catch (Exception e) {
                LogsManager.error("Failed to dismiss alert", e.getMessage());
                return false; // No alert present
            }
        });
    }

    /**
     * Retrieves the text from the alert if present.
     *
     * @return The alert text or null if no alert is present.
     */
    @Step("Retrieving alert text if present")
    public String getAlertText() {
        return waitManager.fluentWait().until(driver -> {
            LogsManager.info("Attempting to retrieve alert text if present.");
            try {
                String alertText = driver.switchTo().alert().getText();
                return !alertText.isEmpty() ? alertText : null; // Alert is present
            } catch (Exception e) {
                LogsManager.error("Failed to retrieve alert text", e.getMessage());
                return null; // No alert present
            }
        });
    }

    /**
     * Sends text to the alert if present.
     *
     * @param text The text to send to the alert.
     */
    @Step("Set text to alert if present: {text}")
    public void setAlertText(String text) {
        LogsManager.info("Attempting to send text to alert if present: ", text);
        waitManager.fluentWait().until(driver -> {
            try {
                driver.switchTo().alert().sendKeys(text);
                return true; // Alert is present
            } catch (Exception e) {
                LogsManager.error("Failed to send text to alert", e.getMessage());
                return false; // No alert present
            }
        });
    }

    @Step("Dismiss consent popup if present")
    public void dismissConsentPopupIfPresent() {
        waitManager.fluentWait(3).until(d -> {
            try {
                new ElementActions(driver).click(consentButton);
                LogsManager.info("Consent popup dismissed successfully.");
                return true; // Consent popup is dismissed.
            } catch (Exception e) {
                LogsManager.warn("Consent popup not found or not dismissed. Continue ");
                return true;
            }
        });
    }

}
